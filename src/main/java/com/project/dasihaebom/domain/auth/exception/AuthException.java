package com.project.dasihaebom.domain.auth.exception;

import com.project.dasihaebom.global.apiPayload.exception.CustomException;

public class AuthException extends CustomException {
    public AuthException(AuthErrorCode errorCode) {
        super(errorCode);
    }
}