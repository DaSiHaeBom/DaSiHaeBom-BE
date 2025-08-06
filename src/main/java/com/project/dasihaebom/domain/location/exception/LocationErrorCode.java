package com.project.dasihaebom.domain.location.exception;

import com.project.dasihaebom.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum LocationErrorCode implements BaseErrorCode {
    // ErrorCode

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
