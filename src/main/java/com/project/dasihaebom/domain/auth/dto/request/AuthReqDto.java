package com.project.dasihaebom.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import static com.project.dasihaebom.global.constant.valid.MessageConstants.*;
import static com.project.dasihaebom.global.constant.valid.PatternConstants.USER_PASSWORD_PATTERN;

public class AuthReqDto {

    public record AuthLoginDto(
            String loginId,
            String password
    ) {
    }

    public record AuthPasswordChangeReqDto(
            @NotBlank(message = USER_BLANK_PASSWORD)
            @Pattern(regexp = USER_PASSWORD_PATTERN, message = USER_WRONG_PASSWORD)
            String currentPassword,
            @NotBlank(message = USER_BLANK_PASSWORD)
            @Pattern(regexp = USER_PASSWORD_PATTERN, message = USER_WRONG_PASSWORD)
            String newPassword,
            @NotBlank(message = USER_BLANK_PASSWORD)
            @Pattern(regexp = USER_PASSWORD_PATTERN, message = USER_WRONG_PASSWORD)
            String newPasswordConfirmation
    ) {
    }

    public record AuthTempPasswordReqDto(
            String phoneNumber
    ) {
    }
}
