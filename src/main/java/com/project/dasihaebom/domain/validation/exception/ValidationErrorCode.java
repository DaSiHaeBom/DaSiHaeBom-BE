package com.project.dasihaebom.domain.validation.exception;

import com.project.dasihaebom.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ValidationErrorCode implements BaseErrorCode {
    // ErrorCode
    MESSAGE_SEND_ERROR(HttpStatus.BAD_GATEWAY, "MSG502", "문자 발송 API 오류"),
    CODE_COOL_DOWN(HttpStatus.BAD_GATEWAY, "MSG502_2", "잠시 후 다시 시도해주세요. 문자는 1분에 한 번만 보낼 수 있습니다."),
    WRONG_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "MSG400", "해당 핸드폰과 관련된 인증이 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
