package com.project.dasihaebom.domain.validation.service;

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
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ValidationServiceImpl implements ValidationService {

    private final PhoneNumberClient phoneNumberClient;
    private final RedisUtils<String> redisUtils;

    @Override
    public void sendCode(ValidationReqDto.PhoneNumberValidationReqDto phoneNumberValidationReqDto) {

        // 휴대폰 번호를 가져옴
        final String phoneNumber = phoneNumberValidationReqDto.phoneNumber();
        // 해당 휴대폰 번호로 인증 번호를 보낸 적이 있다면 1분 대기
        if (redisUtils.hasKey(phoneNumber + ":cooldown")) {
            throw new ValidationException(ValidationErrorCode.CODE_COOL_DOWN);
        }
        // 6자리 난수 코드를 받음
        final String code = createVerificationCode();
        // 전송 String 생성
        final String msg = createMessageWithCode(code);

        // 문자 전송 실패시 예외 처리
        try {
            phoneNumberClient.sendSms(msg, phoneNumber);
        } catch (Exception e) {
            throw new ValidationException(ValidationErrorCode.CODE_SEND_ERROR);
        }

        // 성공 시
        // 레디스에 인증 정보 저장
        redisUtils.save(phoneNumber + ":code", code, 300000L, TimeUnit.MILLISECONDS);
        // 쿨다운 키 저장 (연속 인증 방지)
        redisUtils.save(phoneNumber + ":cooldown", "cooldown...", 60000L, TimeUnit.MILLISECONDS);

    }

    // 6자리 난수 생성기
    private String createVerificationCode() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1000000));
    }

    private String createMessageWithCode(String code) {

        return "[다시해봄]\n" +
                "본인확인 인증번호\n" +
                "\n" +
                "[" + code + "]를 화면에 입력해주세요.\n" +
                "인증번호는 3분간 유효합니다.";
    }
}
