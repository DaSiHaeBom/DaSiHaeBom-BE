package com.project.dasihaebom.domain.auth.exception;

import com.project.dasihaebom.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {
    // ErrorCode
    AUTH_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH404", "회원 정보를 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
