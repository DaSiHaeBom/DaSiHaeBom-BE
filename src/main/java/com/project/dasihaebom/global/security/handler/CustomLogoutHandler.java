package com.project.dasihaebom.global.security.handler;

import com.project.dasihaebom.global.security.utils.JwtUtil;
import com.project.dasihaebom.global.util.RedisUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.project.dasihaebom.global.constant.common.CommonConstants.ACCESS_COOKIE_NAME;
import static com.project.dasihaebom.global.constant.common.CommonConstants.REFRESH_COOKIE_NAME;
import static com.project.dasihaebom.global.constant.redis.RedisConstants.*;
import static com.project.dasihaebom.global.util.CookieUtils.createJwtCookies;
import static com.project.dasihaebom.global.util.CookieUtils.getTokenFromCookies;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final JwtUtil jwtUtil;
    private final RedisUtils<String> redisUtils;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        String accessToken = getTokenFromCookies(request, ACCESS_COOKIE_NAME);
        String refreshToken = getTokenFromCookies(request, REFRESH_COOKIE_NAME);
        String loginId = jwtUtil.getEmail(refreshToken);

        jwtUtil.saveBlackListToken(loginId, accessToken, refreshToken);

        createJwtCookies(response, ACCESS_COOKIE_NAME, null, 0);
        createJwtCookies(response, REFRESH_COOKIE_NAME, null, 0);
        log.info("[ CustomLogoutHandler ] 쿠키 삭제 완료");
    }
}
