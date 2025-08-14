package com.project.dasihaebom.global.security.filter;

import com.project.dasihaebom.domain.user.Role;
import com.project.dasihaebom.global.security.userdetails.CustomUserDetails;
import com.project.dasihaebom.global.security.utils.JwtUtil;
import com.project.dasihaebom.global.util.RedisUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

import static com.project.dasihaebom.global.constant.redis.RedisConstants.KEY_ACCESS_TOKEN_SUFFIX;
import static com.project.dasihaebom.global.util.CookieUtils.getTokenFromCookies;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    // JWT 관련 유틸리티 클래스 주입
    private final JwtUtil jwtUtil;

    // redis 주입
    private final RedisUtils<String> redisUtils;

//    서버 자동 엑세스 토큰 갱신 실패!
//
//    @Override
//    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
//        log.info("shouldNotFilter : {}", request.getRequestURI());
////        if (publicMatchers == null || publicMatchers.isEmpty()) return false; // NPE 방지
//        return publicMatchers.stream().anyMatch(m -> m.matches(request));
//    }

//    private List<RequestMatcher> publicMatchers;
//
//    // 스프링이 의존성 주입 완료 직후 딱 한 번 실행, 생성자보다 늦고, 빈 사용 전 초기화
//    @PostConstruct
//    // SecurityConfig의 allowUrl 패턴들로 RequestMatcher 리스트를 만들어 shouldNotFilter()에 사용하게 함
//    public void initMatchers() {
//        // /** /* {var} 같은 패턴 문자열을 파싱해 쓸 수 있게 해주는 파서 생성
//        PathPatternParser parser = new PathPatternParser();
////        // 요청에서 컨텍스트 경로를 제외한 실제 경로를 뽑아내기 위한 헬퍼
//        UrlPathHelper urlPathHelper = new UrlPathHelper();
//
//        // SecurityConfig에 선언한 퍼블릭 URL 패턴 배열을 스트림으로 변환
//        this.publicMatchers = Arrays.stream(SecurityConfig.allowUrl)
//                // 각 패턴 문자열마다 Matcher 하나씩 만든다
//                .map(pattern -> {
//                    // 문자열 패턴을 PathPattern 객체로 변환
//                    PathPattern pathPattern = parser.parse(pattern);
//                    // RequestMatcher 구현체 생성
//                    return (RequestMatcher) request -> {
//                        // 현재 요청의 애플리케이션 기준 경로 (localhost:8080/myapp/api/v1... -> api/v1...)
//                        String path = urlPathHelper.getPathWithinApplication(request);
////                        String path = request.getRequestURI();
//                        log.info("path: {}", path);
//                        // 요청 경로를 PathContainer로 감싸고, 패턴과 매칭여부를 반환 -> true : public URL
//                        return pathPattern.matches(PathContainer.parsePath(path));
//                    };
//                })
//                // 불변 리스트로 모아 publicMatchers에 저장
//                .toList();
//    }
//
//    @Override
//    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
//        return publicMatchers.stream().anyMatch(matcher -> matcher.matches(request));
//    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("[ JwtAuthorizationFilter ] 인가 필터 작동");

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            log.info("검색된 쿠키 : ");
            for (Cookie cookie : cookies) {
                log.info("쿠키명 : {}, 값 : {}", cookie.getName(), cookie.getValue().substring(0, 15));
            }
        } else {
            log.warn("현재 쿠키 자체가 없습니다");
        }

        try {
            // 1. Cookie 에서 Access Token 추출
            log.info("access token 쿠키 검색");
            String accessToken = getTokenFromCookies(request, "access-token");

            // 스웨거 전용 헤더에 토큰 넣기
//            log.warn("\u001B[31m스\u001B[33m웨\u001B[32m거\u001B[36m \u001B[34m사\u001B[35m용\u001B[31m으\u001B[33m로\u001B[32m \u001B[36m쿠\u001B[34m키\u001B[35m \u001B[31m로\u001B[33m그\u001B[32m인\u001B[36m \u001B[34m방\u001B[35m식\u001B[31m을\u001B[33m \u001B[32m이\u001B[36m용\u001B[34m하\u001B[35m고\u001B[31m \u001B[33m있\u001B[32m지\u001B[36m \u001B[34m않\u001B[35m습\u001B[31m니\u001B[33m다\u001B[32m.\u001B[36m \u001B[34m서\u001B[35m버\u001B[31m \u001B[33m배\u001B[32m포\u001B[36m시\u001B[34m \u001B[35m제\u001B[31m거\u001B[0m");
//            log.warn("\u001B[31m스\u001B[33m웨\u001B[32m거\u001B[36m \u001B[34m사\u001B[35m용\u001B[31m으\u001B[33m로\u001B[32m \u001B[36m쿠\u001B[34m키\u001B[35m \u001B[31m로\u001B[33m그\u001B[32m인\u001B[36m \u001B[34m방\u001B[35m식\u001B[31m을\u001B[33m \u001B[32m이\u001B[36m용\u001B[34m하\u001B[35m고\u001B[31m \u001B[33m있\u001B[32m지\u001B[36m \u001B[34m않\u001B[35m습\u001B[31m니\u001B[33m다\u001B[32m.\u001B[36m \u001B[34m서\u001B[35m버\u001B[31m \u001B[33m배\u001B[32m포\u001B[36m시\u001B[34m \u001B[35m제\u001B[31m거\u001B[0m");
//            log.warn("\u001B[31m스\u001B[33m웨\u001B[32m거\u001B[36m \u001B[34m사\u001B[35m용\u001B[31m으\u001B[33m로\u001B[32m \u001B[36m쿠\u001B[34m키\u001B[35m \u001B[31m로\u001B[33m그\u001B[32m인\u001B[36m \u001B[34m방\u001B[35m식\u001B[31m을\u001B[33m \u001B[32m이\u001B[36m용\u001B[34m하\u001B[35m고\u001B[31m \u001B[33m있\u001B[32m지\u001B[36m \u001B[34m않\u001B[35m습\u001B[31m니\u001B[33m다\u001B[32m.\u001B[36m \u001B[34m서\u001B[35m버\u001B[31m \u001B[33m배\u001B[32m포\u001B[36m시\u001B[34m \u001B[35m제\u001B[31m거\u001B[0m");
//            if (accessToken == null) {
//                accessToken = jwtUtil.resolveAccessToken(request);
//            }


            // 2. Access Token이 없으면 다음 필터로 바로 진행
            if (accessToken == null) {
                log.info("[ JwtAuthorizationFilter ] Access Token 없음, 다음 필터로 진행");
                filterChain.doFilter(request, response);
                return;
            }

            log.info("[ JwtAuthorizationFilter ] 로그아웃 여부 확인");
            if (Objects.equals(accessToken, redisUtils.get(jwtUtil.getEmail(accessToken) + KEY_ACCESS_TOKEN_SUFFIX))) {
                log.info("[ JwtAuthorizationFilter ] 블랙리스트 토큰. 인증 생략하고 다음 필터로 진행");
                filterChain.doFilter(request, response);
                return;
            }

            // 3. Access Token을 이용한 인증 처리
            authenticateAccessToken(accessToken);
            log.info("[ JwtAuthorizationFilter ] 종료. 다음 필터로 넘어갑니다.");

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("Access Token 이 만료되었습니다.");
        }
    }

    // Access Token을 바탕으로 인증 객체 생성 및 SecurityContext에 저장
    private void authenticateAccessToken(String accessToken) {
        log.info("[ JwtAuthorizationFilter ] 토큰으로 인가 과정을 시작합니다. ");

        // 1. Access Token의 유효성 검증
        jwtUtil.validateToken(accessToken);
        log.info("[ JwtAuthorizationFilter ] Access Token 유효성 검증 성공. ");

        // 2. Access Token에서 사용자 정보 추출 후 CustomUserDetails 생성
        Long userId = jwtUtil.getId(accessToken);
        String loginId = jwtUtil.getEmail(accessToken);
        Role role = jwtUtil.getRoles(accessToken);
        log.info("[ JwtAuthorizationFilter ] userId = {}, loginId = {}, role = {}", userId, loginId, role);

        CustomUserDetails userDetails = new CustomUserDetails(userId, loginId, null, role);

        log.info("[ JwtAuthorizationFilter ] UserDetails 객체 생성 성공");

        // 3. 인증 객체 생성 및 SecurityContextHolder에 저장
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        log.info("[ JwtAuthorizationFilter ] 인증 객체 생성 완료");

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.info("[ JwtAuthorizationFilter ] 인증 객체 저장 완료");
    }

}
