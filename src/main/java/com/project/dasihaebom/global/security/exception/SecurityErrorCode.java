package com.project.dasihaebom.global.security.exception;

import com.project.dasihaebom.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum SecurityErrorCode implements BaseErrorCode {
    // ErrorCode
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "접근이 금지되었습니다"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404", "요청한 자원을 찾을 수 없습니다"),

    REQUIRED_RE_LOGIN(HttpStatus.UNAUTHORIZED, "TOKEN401", "모든 토큰이 만료되었습니다. 다시 로그인 하세요"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
