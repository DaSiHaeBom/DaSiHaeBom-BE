package com.project.dasihaebom.domain.validation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import static com.project.dasihaebom.global.constant.valid.MessageConstants.*;
import static com.project.dasihaebom.global.constant.valid.PatternConstants.USER_PHONE_NUMBER_PATTERN;

public class ValidationReqDto {

    public record PhoneNumberValidationReqDto(
            @NotBlank(message = USER_BLANK_PHONE_NUMBER)
            @Pattern(regexp = USER_PHONE_NUMBER_PATTERN, message = USER_WRONG_PHONE_NUMBER)
            String phoneNumber
    ) {
    }
}
