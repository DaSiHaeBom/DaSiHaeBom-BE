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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/validations")
@Tag(name = "Validation", description = "인증 관련 API")
public class ValidationController {

    private final ValidationService validationService;

    @Operation(summary = "휴대폰 문자 인증 번호 발송")
    @PostMapping("/phone/code")
    public CustomResponse<String> sendCode(
            @RequestBody @Valid ValidationReqDto.PhoneNumberValidationReqDto phoneNumberValidationReqDto
    ) {
        validationService.sendCode(phoneNumberValidationReqDto);
        return CustomResponse.onSuccess("인증 번호 발송 성공!");
    }
}
