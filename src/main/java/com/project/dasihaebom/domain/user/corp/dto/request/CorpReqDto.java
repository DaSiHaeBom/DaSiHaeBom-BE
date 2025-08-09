package com.project.dasihaebom.domain.user.corp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import static com.project.dasihaebom.global.constant.valid.MessageConstants.*;
import static com.project.dasihaebom.global.constant.valid.PatternConstants.*;

public class CorpReqDto {

    public record CorpCreateReqDto(
            @NotBlank(message = USER_BLANK_LOGIN_ID)
            @Pattern(regexp = USER_LOGIN_ID_PATTERN, message = USER_WRONG_LOGIN_ID)
            String loginId,

            @NotBlank(message = USER_BLANK_PASSWORD)
            @Pattern(regexp = USER_PASSWORD_PATTERN, message = USER_WRONG_PASSWORD)
            String password,

            @NotBlank(message = USER_BLANK_CEO_NAME)
            @Pattern(regexp = USER_USERNAME_PATTERN, message = USER_WRONG_USERNAME)
            String ceoName,

            @NotBlank(message = USER_BLANK_PHONE_NUMBER)
            @Pattern(regexp = USER_PHONE_NUMBER_PATTERN, message = USER_WRONG_PHONE_NUMBER)
            String phoneNumber,

            @NotBlank(message = USER_BLANK_CORP_NUMBER)
            @Pattern(regexp = USER_CORP_NUMBER_PATTERN, message = USER_WRONG_CORP_NUMBER)
            String corpNumber,

            @NotBlank(message = USER_BLANK_CORP_NAME)
            String corpName,

            @NotBlank(message = USER_BLANK_ADDRESS)
            String corpAddress
    ) {
    }

    public record CorpUpdateReqDto(
            @NotBlank(message = USER_BLANK_CEO_NAME)
            @Pattern(regexp = USER_USERNAME_PATTERN, message = USER_WRONG_USERNAME)
            String ceoName,

            @NotBlank(message = USER_BLANK_PHONE_NUMBER)
            @Pattern(regexp = USER_PHONE_NUMBER_PATTERN, message = USER_WRONG_PHONE_NUMBER)
            String phoneNumber,

            @NotBlank(message = USER_BLANK_CORP_NUMBER)
            @Pattern(regexp = USER_CORP_NUMBER_PATTERN, message = USER_WRONG_CORP_NUMBER)
            String corpNumber,

            @NotBlank(message = USER_BLANK_CORP_NAME)
            String corpName,

            @NotBlank(message = USER_BLANK_ADDRESS)
            String corpAddress
    ) {
    }

    public record CorpNumberValidReqDto(
            String corpNumber
    ) {
    }
}
