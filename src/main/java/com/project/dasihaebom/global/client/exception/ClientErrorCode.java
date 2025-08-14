package com.project.dasihaebom.global.client.exception;

import com.project.dasihaebom.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ClientErrorCode implements BaseErrorCode {
    // ErrorCode
    WRONG_ADDRESS(HttpStatus.BAD_REQUEST, "COORD_400", "카카오 API에서 좌표를 찾을 수 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}