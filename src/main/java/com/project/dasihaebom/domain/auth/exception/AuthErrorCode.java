package com.project.dasihaebom.domain.auth.exception;

import com.project.dasihaebom.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {
    // ErrorCode
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH404", "회원 정보를 찾을 수 없습니다."),
    NEW_PASSWORD_DOES_NOT_MATCH(HttpStatus.BAD_REQUEST, "PASS400_1", "새 비밀번호와 비밀번호 재입력이 일치하지 않습니다."),
    CURRENT_PASSWORD_DOES_NOT_MATCH(HttpStatus.BAD_REQUEST, "PASS400_2", "현재 비밀번호가 일치하지 않습니다."),
    NEW_PASSWORD_IS_CURRENT_PASSWORD(HttpStatus.BAD_REQUEST, "PASS400_3", "현재 비빌번호와 새 비밀번호가 일치합니다."),
    PHONE_VALIDATION_IS_NOT_EXIST(HttpStatus.UNAUTHORIZED, "AUTH401", "휴대폰 인증이 변조되었거나 만료되어 존재하지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
