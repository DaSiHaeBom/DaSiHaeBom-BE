package com.project.dasihaebom.domain.validation.controller;

import com.project.dasihaebom.domain.validation.dto.request.ValidationReqDto;
import com.project.dasihaebom.domain.validation.service.ValidationService;
import com.project.dasihaebom.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.project.dasihaebom.global.constant.scope.ScopeConstants.SCOPE_TEMP_PASSWORD;
import static com.project.dasihaebom.global.constant.scope.ScopeConstants.SCOPE_SIGNUP;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/validations")
@Tag(name = "Validation", description = "인증 관련 API")
public class ValidationController {

    private final ValidationService validationService;

    @Operation(summary = "회원 가입 휴대폰 문자 인증 번호 발송", description = "회원 가입시에만 사용 <br> 메시지 구현 완료 했으나, 일단 resBody로 제공")
    @PostMapping("/phone/code/sign-up")
    public CustomResponse<String> sendSignUpCode(
            @RequestBody @Valid ValidationReqDto.PhoneNumberCodeReqDto phoneNumberCodeReqDto
    ) {                             // 회원 가입을 위한 인증은 회원 가입에서만 소모되어야 한다. 인증 정보 탈취 방지 스코프 정의
        validationService.sendCode(phoneNumberCodeReqDto, SCOPE_SIGNUP);
        return CustomResponse.onSuccess("인증 번호 발송 성공!");
    }

    @Operation(summary = "임시 비밀번호 휴대폰 문자 인증 번호 발송", description = "임시 비밀번호 발급에만 사용 <br> 메시지 구현 완료 했으나, 일단 resBody로 제공")
    @PostMapping("/phone/code/reset-password")
    public CustomResponse<String> sendResetPasswordCode(
            @RequestBody @Valid ValidationReqDto.PhoneNumberCodeReqDto phoneNumberCodeReqDto
    ) {
                                    // 비밀번호를 위한 인증은 비밀번호에서만 소모되어야 한다. 인증 정보 탈취 방지 스코프 정의
        validationService.sendCode(phoneNumberCodeReqDto, SCOPE_TEMP_PASSWORD);
        return CustomResponse.onSuccess("인증 번호 발송 성공!");
    }

    @Operation(summary = "휴대폰 문자 인증 번호 검증")
    @PostMapping("/phone/code/confirmation")
    public CustomResponse<String> verifyCode(
            @RequestBody @Valid ValidationReqDto.PhoneNumberValidationReqDto phoneNumberValidationReqDto
    ) {
        return CustomResponse.onSuccess(validationService.verifyCode(phoneNumberValidationReqDto));
    }
}
