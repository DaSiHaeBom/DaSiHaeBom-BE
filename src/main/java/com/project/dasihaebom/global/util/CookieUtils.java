package com.project.dasihaebom.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CookieUtils {
    // 쿠키를 생성하는 메서드
    public static void createJwtCookies(HttpServletResponse response, String name, String token, long tokenExpMs) {
        // jwtDto 에서 access token을 꺼내서 accessToken이라는 이름의 쿠키 생성
        Cookie jwtCookie = new Cookie(name, token);
        // JS 에서 쿠키 읽기 불가능 XSS 방지
        jwtCookie.setHttpOnly(true);
        // HTTPS 연결에서만 쿠키 전송
        jwtCookie.setSecure(true);
        // '/' 경로 이하 모든 API 요청에 쿠키가 포함되도록
        jwtCookie.setPath("/");
        // 쿠키 만료 시간 환경변수로 받아옴 (MS -> Sec로 변환 하려고 /1000)
        jwtCookie.setMaxAge((int) (tokenExpMs / 1000));
        // CSRF 설정 -> 개발 중에는 None
        jwtCookie.setAttribute("SameSite", "None");
        // 쿠키 추가
        response.addCookie(jwtCookie);
    }

    public static String getTokenFromCookies(HttpServletRequest request, String cookieName) {
        log.info("쿠키에서 토큰을 추출하기 위해 쿠키를 검색합니다.");
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(cookieName)) {
                    log.info("accessToken 쿠키가 존재합니다.");
                    return cookie.getValue();
                }
            }

            log.warn("사용 가능한 쿠키가 존재하지 않습니다.");
            log.warn("현재 쿠키 목록");
            for (Cookie cookie : request.getCookies()) {
                log.warn(" - {}", cookie.getName());
            }
            log.warn("accessToken 쿠키가 존재하지 않습니다.");
        }
        return null;
    }
}
