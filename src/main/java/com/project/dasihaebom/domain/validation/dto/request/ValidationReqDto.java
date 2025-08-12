package com.project.dasihaebom.domain.validation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import static com.project.dasihaebom.global.constant.valid.MessageConstants.*;
import static com.project.dasihaebom.global.constant.valid.PatternConstants.*;

public class ValidationReqDto {

    public record PhoneNumberCodeReqDto(
            @NotBlank(message = USER_BLANK_PHONE_NUMBER)
            @Pattern(regexp = USER_PHONE_NUMBER_PATTERN, message = USER_WRONG_PHONE_NUMBER)
            String phoneNumber
    ) {
    }

    public record PhoneNumberValidationReqDto(
            @NotBlank(message = USER_BLANK_CODE)
            @Pattern(regexp = USER_CODE_PATTERN, message = USER_WRONG_CODE)
            String code,

            @NotBlank(message = USER_BLANK_PHONE_NUMBER)
            @Pattern(regexp = USER_PHONE_NUMBER_PATTERN, message = USER_WRONG_PHONE_NUMBER)
            String phoneNumber
    ) {
    }
}
