package com.project.dasihaebom.domain.validation.exception;

import com.project.dasihaebom.global.apiPayload.exception.CustomException;

public class ValidationException extends CustomException {
    public ValidationException(ValidationErrorCode errorCode) {
        super(errorCode);
    }
}
