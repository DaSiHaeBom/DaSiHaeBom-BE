package com.project.dasihaebom.global.security.controller;

import com.project.dasihaebom.global.apiPayload.CustomResponse;
import com.project.dasihaebom.global.security.service.SecurityService;
import com.project.dasihaebom.global.security.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.project.dasihaebom.global.util.CookieUtils.createJwtCookies;
import static com.project.dasihaebom.global.util.CookieUtils.getTokenFromCookies;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/security")
public class SecurityController {

    private final SecurityService securityService;
    private final JwtUtil jwtUtil;


    @Operation(summary = "엑세스 쿠키 재발급", description = "엑세스 쿠키가 만료되어 없어졌고, 리프레시 쿠키가 있다면 엑세스 쿠키를 만들어준다.")
    @PostMapping("/reissue-cookie")
    public CustomResponse<String> reissueCookie(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String refreshToken = getTokenFromCookies(request, "refresh-token");
        String accessToken = securityService.reissueCookie(refreshToken);

        // 쿠키 재발급
        log.info("[ JwtAuthorizationFilter ] 쿠키를 재생성 합니다.");
        createJwtCookies(response, "access-token", accessToken, jwtUtil.getAccessExpMs());

        return CustomResponse.onSuccess("엑세스 쿠키가 재발급 되었습니다.");
    }
}
