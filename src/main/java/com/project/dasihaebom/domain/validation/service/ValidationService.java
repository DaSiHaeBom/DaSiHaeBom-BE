package com.project.dasihaebom.domain.validation.service;

import com.project.dasihaebom.domain.validation.dto.request.ValidationReqDto;

public interface ValidationService {

    void sendCode(ValidationReqDto.PhoneNumberCodeReqDto phoneNumberCodeReqDto, String scope);

    String verifyCode(ValidationReqDto.PhoneNumberValidationReqDto phoneNumberValidationReqDto);
}
