package com.project.dasihaebom.domain.location.exception;

import com.project.dasihaebom.global.apiPayload.exception.CustomException;

public class LocationException extends CustomException {
    public LocationException(LocationErrorCode errorCode) {
        super(errorCode);
    }
}
