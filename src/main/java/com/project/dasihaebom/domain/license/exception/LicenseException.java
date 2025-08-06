package com.project.dasihaebom.domain.license.exception;

import com.project.dasihaebom.global.apiPayload.exception.CustomException;

public class LicenseException extends CustomException {
    public LicenseException(LicenseErrorCode errorCode) {
        super(errorCode);
    }
}
