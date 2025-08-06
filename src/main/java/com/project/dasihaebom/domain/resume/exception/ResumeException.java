package com.project.dasihaebom.domain.resume.exception;

import com.project.dasihaebom.global.apiPayload.exception.CustomException;

public class ResumeException extends CustomException {
    public ResumeException(ResumeErrorCode errorCode) {
        super(errorCode);
    }
}
