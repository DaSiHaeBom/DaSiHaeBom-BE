package com.project.dasihaebom.domain.introduction.exception;

import com.project.dasihaebom.global.apiPayload.exception.CustomException;

public class IntroductionException extends CustomException {
    public IntroductionException(IntroductionErrorCode errorCode) {
        super(errorCode);
    }
}
