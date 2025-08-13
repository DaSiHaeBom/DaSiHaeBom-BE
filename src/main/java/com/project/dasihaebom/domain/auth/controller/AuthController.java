package com.project.dasihaebom.domain.auth.controller;

import com.project.dasihaebom.domain.auth.dto.request.AuthReqDto;
import com.project.dasihaebom.domain.auth.service.command.AuthCommandService;
import com.project.dasihaebom.global.apiPayload.CustomResponse;
import com.project.dasihaebom.global.security.userdetails.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Auth 관련 API")
public class AuthController {

    private final AuthCommandService authCommandService;

    @Operation(summary = "로그인", description = "아이디와 비밀번호를 입력하면 access / refresh 쿠키 생성")
    @PostMapping("/login")
    public CustomResponse<?> Login(
            @RequestBody AuthReqDto.AuthLoginDto authLoginDto
    ) {
        return null;
    }

    @Operation(summary = "로그아웃", description = "access / refresh 쿠키를 삭제합니다.")
    @PostMapping("logout")
    public CustomResponse<?> logout() {
        return null;
    }

    @Operation(summary = "비밀번호 변경", description = "현재 비밀번호와 바꿀 비밀번호를 입력해 검증 후 비밀번호를 변경한다")
    @PostMapping("/me/password")
    public CustomResponse<String> changePassword(
            @RequestBody @Valid AuthReqDto.AuthPasswordChangeReqDto authPasswordChangeReqDto,
            @AuthenticationPrincipal CurrentUser currentUser
    ) {
        authCommandService.changePassword(authPasswordChangeReqDto, currentUser.getLoginId());
        return CustomResponse.onSuccess("비밀번호 변경이 완료되었습니다.");
    }

    @Operation(summary = "임시 비밀번호 발급", description = "비밀번호 변경 휴대폰 인증 완료 후 실행 <br> 메시지 전송 구현 완료했지만, 비밀번호는 일단 ResBody로 제공")
    @PostMapping("/temp-password")
    public CustomResponse<String> resetPassword(
            @RequestBody @Valid AuthReqDto.AuthTempPasswordReqDto authTempPasswordReqDto
    ) {
        String password = authCommandService.tempPassword(authTempPasswordReqDto);
        return CustomResponse.onSuccess("임시 비빌번호가 전화번호로 발송되었습니다. " + password);
    }
}
