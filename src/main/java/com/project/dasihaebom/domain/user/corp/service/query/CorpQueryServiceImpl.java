package com.project.dasihaebom.domain.user.corp.service.query;

import com.project.dasihaebom.domain.user.Role;
import com.project.dasihaebom.domain.user.corp.converter.CorpConverter;
import com.project.dasihaebom.domain.user.corp.dto.request.CorpReqDto;
import com.project.dasihaebom.domain.user.corp.dto.response.CorpResDto;
import com.project.dasihaebom.domain.user.corp.entity.Corp;
import com.project.dasihaebom.domain.user.corp.exception.CorpErrorCode;
import com.project.dasihaebom.domain.user.corp.exception.CorpException;
import com.project.dasihaebom.domain.user.corp.repository.CorpRepository;
import com.project.dasihaebom.global.util.RedisUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static com.project.dasihaebom.global.constant.redis.RedisConstants.KEY_SCOPE_SUFFIX;
import static com.project.dasihaebom.global.constant.scope.ScopeConstants.SCOPE_FIND_LOGIN_ID;
import static com.project.dasihaebom.global.constant.scope.ScopeConstants.SCOPE_SIGNUP;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CorpQueryServiceImpl implements CorpQueryService {

    private final CorpRepository corpRepository;
    private final RedisUtils<String> redisUtils;

    @Override
    public CorpResDto.CorpProfileResDto getCorpProfile(long corpId, Role role) {
        if (role.equals(Role.WORKER)) {
            throw new CorpException(CorpErrorCode.ROLE_IS_NOT_CORP);
        }

        Corp corp = corpRepository.findById(corpId)
                .orElseThrow(() -> new CorpException(CorpErrorCode.CORP_NOT_FOUND));

        return CorpConverter.toCorpProfileResDto(corp);
    }

    @Override
    public CorpResDto.CorpLoginIdResDto getCorpLoginId(CorpReqDto.CorpLoginIdReqDto corpLoginIdReqDto) {
        // 휴대폰 인증이 있는지 확인
        final String phoneNumber = corpLoginIdReqDto.phoneNumber();
        // 해당 인증이 회원 가입을 위한 것인지 확인
        if (!Objects.equals(redisUtils.get(phoneNumber + KEY_SCOPE_SUFFIX), SCOPE_FIND_LOGIN_ID)) {
            throw new CorpException(CorpErrorCode.SIGN_UP_PHONE_VALIDATION_DOES_NOT_EXIST);
        }

        Corp corp = corpRepository.findByPhoneNumber(corpLoginIdReqDto.phoneNumber())
                .orElseThrow(() -> new CorpException(CorpErrorCode.CORP_NOT_FOUND));

        redisUtils.delete(phoneNumber + KEY_SCOPE_SUFFIX);

        return CorpConverter.toCorpLoginIdResDto(corp.getLoginId());
    }
}
