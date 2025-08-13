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
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
