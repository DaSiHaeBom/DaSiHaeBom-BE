package com.project.dasihaebom.global.security.controller;

import com.project.dasihaebom.domain.user.corp.entity.Corp;
import com.project.dasihaebom.domain.user.corp.repository.CorpRepository;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import com.project.dasihaebom.domain.user.worker.repository.WorkerRepository;
import com.project.dasihaebom.global.apiPayload.CustomResponse;
import com.project.dasihaebom.global.security.service.SecurityService;
import com.project.dasihaebom.global.security.userdetails.CustomUserDetails;
import com.project.dasihaebom.global.security.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import static com.project.dasihaebom.global.constant.common.CommonConstants.ACCESS_COOKIE_NAME;
import static com.project.dasihaebom.global.constant.common.CommonConstants.REFRESH_COOKIE_NAME;
import static com.project.dasihaebom.global.util.CookieUtils.createJwtCookies;
import static com.project.dasihaebom.global.util.CookieUtils.getTokenFromCookies;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/security")
@Tag(name = "Security", description = "시큐리티 관련 API")
public class SecurityController {

    private final SecurityService securityService;
    private final JwtUtil jwtUtil;
    private final WorkerRepository workerRepository;
    private final CorpRepository corpRepository;

    @Operation(summary = "엑세스 쿠키 재발급", description = "엑세스 쿠키가 만료되어 없어졌고, 리프레시 쿠키가 있다면 엑세스 쿠키를 만들어준다.")
    @PostMapping("/reissue-cookie")
    public CustomResponse<String> reissueCookie(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String refreshToken = getTokenFromCookies(request, REFRESH_COOKIE_NAME);
        String accessToken = securityService.reissueCookie(refreshToken);

        // 쿠키 재발급
        log.info("[ JwtAuthorizationFilter ] 쿠키를 재생성 합니다.");
        createJwtCookies(response, ACCESS_COOKIE_NAME, accessToken, jwtUtil.getAccessExpMs());

        return CustomResponse.onSuccess("엑세스 쿠키가 재발급 되었습니다.");
    }

    @Operation(summary = "CSRF 토큰 발급", description = "CSRF 토큰을 쿠키로 발급합니다")
    @GetMapping("/csrf")
    public CustomResponse<String> csrf(
            HttpServletRequest request
    ) {
        // lazy 토큰을 실제로 생성해서 쿠키에 담기도록 트리거
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (token != null) {
            token.getToken(); // 호출 시 쿠키가 Set-Cookie 로 내려감
        }
        return CustomResponse.onSuccess("CSRF 토큰이 쿠키로 발급되었습니다.");
    }
}
