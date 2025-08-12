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

    @Operation(summary = "로그인", description = "현재 쿠키 구현은 되어 있지만 스웨거에서 사용하기 위해 헤더 토큰 방식만 적용 <br> 쿠키는 Postman으로 해야 함")
    @PostMapping("/login")
    public CustomResponse<?> Login(
            @RequestBody AuthReqDto.AuthLoginDto authLoginDto
    ) {
        return null;
    }
}
