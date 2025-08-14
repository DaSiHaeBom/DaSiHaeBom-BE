package com.project.dasihaebom.domain.user.corp.service.command;

import com.project.dasihaebom.domain.auth.service.command.AuthCommandService;
import com.project.dasihaebom.domain.location.converter.LocationConverter;
import com.project.dasihaebom.domain.user.corp.converter.CorpConverter;
import com.project.dasihaebom.domain.user.corp.dto.request.CorpReqDto;
import com.project.dasihaebom.domain.user.corp.dto.response.CorpResDto;
import com.project.dasihaebom.domain.user.corp.entity.Corp;
import com.project.dasihaebom.domain.user.corp.exception.CorpErrorCode;
import com.project.dasihaebom.domain.user.corp.exception.CorpException;
import com.project.dasihaebom.domain.user.corp.repository.CorpRepository;
import com.project.dasihaebom.global.client.corpNumber.CorpNumberClient;
import com.project.dasihaebom.global.client.corpNumber.dto.NtsCorpInfoResDto;
import com.project.dasihaebom.global.client.location.coordinate.CoordinateClient;
import com.project.dasihaebom.global.util.RedisUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.project.dasihaebom.global.constant.redis.RedisConstants.*;
import static com.project.dasihaebom.global.constant.scope.ScopeConstants.SCOPE_CORP_NUMBER;
import static com.project.dasihaebom.global.constant.scope.ScopeConstants.SCOPE_SIGNUP;
import static com.project.dasihaebom.global.constant.valid.MessageConstants.CORP_NUMBER_IS_NOT_REGISTERED;
import static com.project.dasihaebom.global.util.UpdateUtils.updateIfChanged;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CorpCommandServiceImpl implements CorpCommandService {

    // Repo
    private final CorpRepository corpRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Service
    private final AuthCommandService authCommandService;

    // API Client
    private final CorpNumberClient corpNumberClient;
    private final CoordinateClient coordinateClient;

    private final RedisUtils<String> redisUtils;


    @Override
    public void createCorp(CorpReqDto.CorpCreateReqDto corpCreateReqDto) {

        // 휴대폰 인증이 있는지 확인
        final String phoneNumber = corpCreateReqDto.phoneNumber();
        // 해당 인증이 회원 가입을 위한 것인지 확인
        if (!Objects.equals(redisUtils.get(phoneNumber + KEY_SCOPE_SUFFIX), SCOPE_SIGNUP)) {
            throw new CorpException(CorpErrorCode.PHONE_VALIDATION_DOES_NOT_EXIST);
        }

        // 사업자 인증이 있는지 확인
        final String corpNumber = corpCreateReqDto.corpNumber();
        if (!Objects.equals(redisUtils.get(corpNumber + KEY_SCOPE_SUFFIX), SCOPE_CORP_NUMBER)) {
            throw new CorpException(CorpErrorCode.CORP_VALIDATION_FAILURE);
        }

        final String address = corpCreateReqDto.corpAddress();
        List<Double> corpCoordinates = LocationConverter.toCoordinateList(coordinateClient.getKakaoCoordinateInfo(address));

        Corp corp = CorpConverter.toCorp(corpCreateReqDto, corpCoordinates);
        try {
            corpRepository.save(corp);
            authCommandService.savePassword(corp, encodePassword(corpCreateReqDto.password()));
        } catch (DataIntegrityViolationException e) {
            throw new CorpException(CorpErrorCode.CORP_DUPLICATED);
        }

        // 인증 정보 삭제
        redisUtils.delete(phoneNumber + KEY_SCOPE_SUFFIX);
        redisUtils.delete(corpNumber + KEY_SCOPE_SUFFIX);
    }

    @Override
    public void updateCorp(CorpReqDto.CorpUpdateReqDto corpUpdateReqDto, long corpId) {
        Corp corp = corpRepository.findById(corpId)
                .orElseThrow(() -> new CorpException(CorpErrorCode.CORP_NOT_FOUND));

        updateIfChanged(corpUpdateReqDto.ceoName(), corp.getCorpName(), corp::changeCeoName);
        updateIfChanged(corpUpdateReqDto.phoneNumber(), corp.getPhoneNumber(), corp::changePhoneNumber);
        updateIfChanged(corpUpdateReqDto.corpNumber(), corp.getCorpNumber(), corp::changeCorpNumber);
        updateIfChanged(corpUpdateReqDto.corpName(), corp.getCorpName(), corp::changeCorpName);
        updateIfChanged(corpUpdateReqDto.corpAddress(), corp.getCorpAddress(), corp::changeCorpAddress);
    }

    @Override
    public CorpResDto.CorpNumberValidResDto validCorpNumber(CorpReqDto.CorpNumberValidReqDto corpNumberValidReqDto) {

        NtsCorpInfoResDto.CorpInfo corpInfoDto = corpNumberClient.getCorpInfo(corpNumberValidReqDto.corpNumber());
        String corpTaxType = corpInfoDto.data().stream()
                // 사업자 등록 번호는 하나만 입력하므로 가장 처음 요소
                .findFirst()
                // tax_type 찾아서 반환
                .map(NtsCorpInfoResDto.CorpInfo.InfoItem::tax_type)
                .orElseThrow(() -> new CorpException(CorpErrorCode.CORP_NUMBER_API_ERROR));

        // 인증 성공 여부
        boolean isValid = !Objects.equals(corpTaxType, CORP_NUMBER_IS_NOT_REGISTERED);

        // 성공 했다면 성공 정보를 redis에 저장
        if (isValid) {
            final String corpNumber =  corpNumberValidReqDto.corpNumber();
            redisUtils.save(corpNumber + KEY_SCOPE_SUFFIX, SCOPE_CORP_NUMBER, SCOPE_EXP_TIME, TimeUnit.MILLISECONDS);
        }

        return CorpConverter.toCorpNumberValidResDto(corpNumberValidReqDto, isValid);
    }

    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
