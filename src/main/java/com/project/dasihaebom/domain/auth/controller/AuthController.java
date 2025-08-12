package com.project.dasihaebom.domain.auth.controller;

import com.project.dasihaebom.domain.auth.dto.request.AuthReqDto;
import com.project.dasihaebom.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Auth 관련 API")
public class AuthController {

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
}
