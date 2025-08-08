package com.project.dasihaebom.domain.user.corp.exception;

import com.project.dasihaebom.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CorpErrorCode implements BaseErrorCode {
    // ErrorCode
    CORP_NOT_FOUND(HttpStatus.NOT_FOUND, "CORP404", "회원을 찾을 수 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
