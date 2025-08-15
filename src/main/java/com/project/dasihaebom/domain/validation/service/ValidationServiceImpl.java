package com.project.dasihaebom.domain.validation.service;

import com.project.dasihaebom.domain.auth.repository.AuthRepository;
import com.project.dasihaebom.domain.validation.dto.request.ValidationReqDto;
import com.project.dasihaebom.domain.validation.exception.ValidationErrorCode;
import com.project.dasihaebom.domain.validation.exception.ValidationException;
import com.project.dasihaebom.global.client.phonenumber.PhoneNumberClient;
import com.project.dasihaebom.global.util.RedisUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.project.dasihaebom.global.constant.redis.RedisConstants.*;
import static com.project.dasihaebom.global.constant.scope.ScopeConstants.SCOPE_CHANGE_PHONE_NUMBER;
import static com.project.dasihaebom.global.constant.scope.ScopeConstants.SCOPE_SIGNUP;
import static com.project.dasihaebom.global.constant.valid.MessageConstants.CODE_CONFIRMATION_IS_FAILURE;
import static com.project.dasihaebom.global.constant.valid.MessageConstants.CODE_CONFIRMATION_IS_SUCCESS;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ValidationServiceImpl implements ValidationService {

    private final PhoneNumberClient phoneNumberClient;
    private final RedisUtils<String> redisUtils;
    private final AuthRepository authRepository;

    @Override
    public String sendCode(ValidationReqDto.PhoneNumberCodeReqDto phoneNumberCodeReqDto, String scope) {
        // 휴대폰 번호를 가져옴
        final String phoneNumber = phoneNumberCodeReqDto.phoneNumber();

        boolean isSignUpScope = Objects.equals(scope, SCOPE_SIGNUP);
        boolean isChangePhoneNumberScope = Objects.equals(scope, SCOPE_CHANGE_PHONE_NUMBER);
        boolean phoneNumberExists = authRepository.findByPhoneNumber(phoneNumber).isPresent();

        // 만약 회원 가입 또는 프로필 전화번호 변경 인증인 경우 가입된 핸드폰 번호라면 인증을 막아 가입을 막는다
        if ((isSignUpScope || isChangePhoneNumberScope) && phoneNumberExists) {
            throw new ValidationException(ValidationErrorCode.ALREADY_USED_PHONE_NUMBER);
        }

        // 해당 휴대폰 번호로 인증 번호를 보낸 적이 있다면 1분 대기
        if (redisUtils.hasKey(phoneNumber + KEY_COOLDOWN_SUFFIX)) {
            throw new ValidationException(ValidationErrorCode.CODE_COOL_DOWN);
        }
        // 6자리 난수 코드를 받음
        final String code = createVerificationCode();
        // 전송 String 생성
        final String msg = createMessageWithCode(code);

//         문자 전송 실패시 예외 처리
//        try {
//            phoneNumberClient.sendMessage(msg, phoneNumber);
//        } catch (Exception e) {
//            throw new ValidationException(ValidationErrorCode.MESSAGE_SEND_ERROR);
//        }

        // 성공 시
        // 레디스에 인증 정보 저장
        redisUtils.save(phoneNumber + KEY_CODE_SUFFIX, code + ":" + scope, CODE_EXP_TIME, TimeUnit.MILLISECONDS);
        // 쿨다운 키 저장 (연속 인증 방지)
        redisUtils.save(phoneNumber + KEY_COOLDOWN_SUFFIX, VALUE_COOLDOWN, COOLDOWN_EXP_TIME, TimeUnit.MILLISECONDS);
        return code;
    }

    @Override
    public String verifyCode(ValidationReqDto.PhoneNumberValidationReqDto phoneNumberValidationReqDto) {
        // 핸드폰 번호
        final String phoneNumber = phoneNumberValidationReqDto.phoneNumber();
        // 사욪자가 입력한 인증 코드
        final String code = phoneNumberValidationReqDto.code();

        // 레디스에 저장된 인증 코드 + 사용 스코프 ( :으로 구별되어 있음 )
        final String value = redisUtils.get(phoneNumber + KEY_CODE_SUFFIX);
        // 없으면 핸드폰 입력이 잘못됨
        if (value == null) {
            throw new ValidationException(ValidationErrorCode.WRONG_PHONE_NUMBER);
        }

        String[] values = value.split(":");
        final String storedCode = values[0];
        final String scope = values[1];

        // 비교
        if (!Objects.equals(code, storedCode)) {
            return CODE_CONFIRMATION_IS_FAILURE;
        }
        // 성공시 회원 가입을 위해 삭제
        redisUtils.delete(phoneNumber + KEY_CODE_SUFFIX);

        redisUtils.delete(phoneNumber + KEY_COOLDOWN_SUFFIX);

        // 해당 스코프에서 사용할 인증이 완료되었음을 레디스에 저장
        redisUtils.save(phoneNumber + KEY_SCOPE_SUFFIX, scope, SCOPE_EXP_TIME, TimeUnit.MILLISECONDS);

        return CODE_CONFIRMATION_IS_SUCCESS + code;
    }

    // 6자리 난수 생성기
    private String createVerificationCode() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1000000));
    }

    private String createMessageWithCode(String code) {
        return "[다시해봄]\n" +
                "본인확인 인증번호\n\n" +
                "[" + code + "]\n" +
                "인증번호는 3분간 유효합니다.";
    }
}
