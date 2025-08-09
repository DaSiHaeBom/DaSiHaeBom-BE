package com.project.dasihaebom.domain.user.corp.service.command;

import com.project.dasihaebom.domain.auth.service.command.AuthCommandService;
import com.project.dasihaebom.domain.user.corp.converter.CorpConverter;
import com.project.dasihaebom.domain.user.corp.dto.request.CorpReqDto;
import com.project.dasihaebom.domain.user.corp.dto.response.CorpResDto;
import com.project.dasihaebom.domain.user.corp.entity.Corp;
import com.project.dasihaebom.domain.user.corp.exception.CorpErrorCode;
import com.project.dasihaebom.domain.user.corp.exception.CorpException;
import com.project.dasihaebom.domain.user.corp.repository.CorpRepository;
import com.project.dasihaebom.global.client.corpNumber.CorpNumberClient;
import com.project.dasihaebom.global.client.corpNumber.dto.NtsCorpInfoResDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.project.dasihaebom.global.constant.valid.MessageConstants.CORP_NUMBER_IS_NOT_REGISTERED;
import static com.project.dasihaebom.global.util.UpdateUtils.updateIfChanged;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CorpCommandServiceImpl implements CorpCommandService {

    // Repo
    private final CorpRepository corpRepository;
//    private final BCryptPasswordEncoder passwordEncoder;

    // Service
    private final AuthCommandService authCommandService;

    // API Client
    private final CorpNumberClient corpNumberClient;


    @Override
    public void createCorp(CorpReqDto.CorpCreateReqDto corpCreateReqDto) {
        Corp corp = CorpConverter.toCorp(corpCreateReqDto);
        corpRepository.save(corp);

        authCommandService.savePassword(corp, encodePassword(corpCreateReqDto.password()));
    }

    @Override
    public void updateCorp(CorpReqDto.CorpUpdateReqDto corpUpdateReqDto) {
        Corp corp = corpRepository.findById(1L)
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

        return CorpConverter.toCorpNumberValidResDto(corpNumberValidReqDto, isValid);
    }

    // TODO : 일단 미구현
    private String encodePassword(String rawPassword) {
//        return passwordEncoder.encode(rawPassword);
        return rawPassword;
    }
}
