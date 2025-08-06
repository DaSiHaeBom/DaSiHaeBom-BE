package com.project.dasihaebom.domain.user.corp.exception;

import com.project.dasihaebom.global.apiPayload.exception.CustomException;

public class CorpException extends CustomException {
    public CorpException(CorpErrorCode errorCode) {
        super(errorCode);
    }
}
