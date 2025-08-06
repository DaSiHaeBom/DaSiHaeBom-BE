package com.project.dasihaebom.domain.user.worker.exception;

import com.project.dasihaebom.global.apiPayload.exception.CustomException;

public class WorkerException extends CustomException {
    public WorkerException(WorkerErrorCode errorCode) {
        super(errorCode);
    }
}
