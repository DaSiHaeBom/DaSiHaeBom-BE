package com.project.dasihaebom.global.client.exception;

import com.project.dasihaebom.global.apiPayload.exception.CustomException;

public class ClientException extends CustomException {
    public ClientException(ClientErrorCode errorCode) {
        super(errorCode);
    }
}
