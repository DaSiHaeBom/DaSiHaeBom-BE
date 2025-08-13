package com.project.dasihaebom.domain.user.worker.exception;

import com.project.dasihaebom.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum WorkerErrorCode implements BaseErrorCode {
    // ErrorCode
    WORKER_NOT_FOUND(HttpStatus.NOT_FOUND, "WORKER404", "회원을 찾을 수 없습니다."),
    WORKER_DUPLICATED(HttpStatus.CONFLICT, "WORKER409", "이미 가입된 사용자 입니다."),
    PHONE_VALIDATION_DOES_NOT_EXIST(HttpStatus.UNAUTHORIZED, "WORK401", "회원 가입 휴대폰 인증이 변조되었거나 만료되어 존재하지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
