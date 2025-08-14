package com.project.dasihaebom.domain.license.exception;

import com.project.dasihaebom.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum LicenseErrorCode implements BaseErrorCode {
    // ErrorCode
    LICENSE_NOT_FOUND(HttpStatus.NOT_FOUND, "LICENSE404", "자격증 정보가 업습니다."),
    LICENSE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "LICENSE403", "해당 자격증에 접근 권한이 없습니다"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
