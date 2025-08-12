package com.project.dasihaebom.global.security.service;

import com.project.dasihaebom.global.apiPayload.exception.CustomException;
import com.project.dasihaebom.global.security.exception.SecurityErrorCode;
import com.project.dasihaebom.global.security.utils.JwtUtil;
import com.project.dasihaebom.global.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.project.dasihaebom.global.util.CookieUtils.createJwtCookies;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityService {

    private final JwtUtil jwtUtil;
    private final RedisUtils<String> redisUtils;

    public String reissueCookie(String refreshToken) {
        // 4. 토큰 만료 시 refreshToken으로 AccessToken 재발급

        if (refreshToken == null) {
            // 리프레시 토큰 조차 만료 -> 재 로그인 안내
            throw new CustomException(SecurityErrorCode.REQUIRED_RE_LOGIN);
        }

        // refresh token의 유효성 검사
        log.info("[ JwtAuthorizationFilter ] refresh token의 유효성을 검사합니다.");
        jwtUtil.validateToken(refreshToken);

        // redis 에 해당 refresh token이 존재하는지 검사
        if (!Objects.equals(redisUtils.get(jwtUtil.getEmail(refreshToken) + ":refresh"), refreshToken)) {
            // 서버에 리프레시 토큰이 없음 -> 재 로그인 안내
            throw new CustomException(SecurityErrorCode.REQUIRED_RE_LOGIN);
        }

        // access token 재발급
        log.info("[ JwtAuthorizationFilter ] refresh token 으로 access token 을 생성합니다.");
        return jwtUtil.reissueToken(refreshToken);
    }
}
