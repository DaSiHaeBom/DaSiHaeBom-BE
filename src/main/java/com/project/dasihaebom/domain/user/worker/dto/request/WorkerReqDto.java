package com.project.dasihaebom.domain.user.worker.dto.request;

import com.project.dasihaebom.domain.user.worker.entity.Gender;
import com.project.dasihaebom.global.constant.valid.PatternConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import static com.project.dasihaebom.global.constant.valid.MessageConstants.*;
import static com.project.dasihaebom.global.constant.valid.PatternConstants.*;

public class WorkerReqDto {

    public record WorkerCreateReqDto(
            @NotBlank(message = USER_BLANK_PHONE_NUMBER)
            @Pattern(regexp = USER_PHONE_NUMBER_PATTERN, message = USER_WRONG_PHONE_NUMBER)
            String phoneNumber,

            @NotBlank(message = USER_BLANK_PASSWORD)
            @Pattern(
                    regexp = PatternConstants.USER_PASSWORD_PATTERN,
                    message = USER_WRONG_PASSWORD)
            String password,

            @NotBlank(message = USER_BLANK_USERNAME)
            @Pattern(regexp = PatternConstants.USER_USERNAME_PATTERN, message = USER_WRONG_USERNAME)
            String username,

            @NotNull(message = USER_BLANK_BIRTHDATE)
            @Pattern(regexp = PatternConstants.USER_BIRTHDATE_PATTERN, message = USER_WRONG_BIRTHDATE)
            String birthDate,

            @NotNull(message = USER_BLANK_GENDER)
            Gender gender,

            @NotBlank(message = USER_BLANK_ADDRESS)
            String address
    ) {
    }

    public record WorkerUpdateReqDto(
            @NotBlank(message = USER_BLANK_PHONE_NUMBER)
            @Pattern(regexp = USER_PHONE_NUMBER_PATTERN, message = USER_WRONG_PHONE_NUMBER)
            String phoneNumber,

            @NotBlank(message = USER_BLANK_USERNAME)
            @Pattern(regexp = USER_USERNAME_PATTERN, message = USER_WRONG_USERNAME)
            String username,

            @NotBlank(message = USER_BLANK_BIRTHDATE)
            @Pattern(regexp = USER_BIRTHDATE_PATTERN, message = USER_WRONG_BIRTHDATE)
            String birthDate,

            @NotBlank(message = USER_BLANK_ADDRESS)
            String address
    ) {
    }
}
