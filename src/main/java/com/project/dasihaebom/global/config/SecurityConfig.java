package com.project.dasihaebom.global.config;


import com.project.dasihaebom.global.security.exception.JwtAuthenticationEntryPoint;
import com.project.dasihaebom.global.security.filter.CustomLoginFilter;
import com.project.dasihaebom.global.security.exception.JwtAccessDeniedHandler;
import com.project.dasihaebom.global.security.filter.JwtAuthorizationFilter;
import com.project.dasihaebom.global.security.handler.CustomLogoutHandler;
import com.project.dasihaebom.global.security.handler.CustomLogoutSuccessHandler;
import com.project.dasihaebom.global.security.utils.JwtUtil;
import com.project.dasihaebom.global.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // 빈 등록
@EnableWebSecurity // 필터 체인 관리 시작 어노테이션
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final RedisUtils<String> redisUtils;
    private final CustomLogoutHandler jwtLogoutHandler;
    private final CustomLogoutSuccessHandler jwtLogoutSuccessHandler;


    //인증이 필요하지 않은 url
    private final String[] allowUrl = {
            "/api/v1/auth/login",   //로그인
            "/api/v1/users/workers",    // 개인 회원 가입
            "/api/v1/users/corps",      // 기업 회원 가입
            "/api/v1/users/corps/business-validation",  // 사업자 번호 조회
            "/api/v1/security/reissue-cookie",
            "swagger-resources/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        // 로그인 필터 객체 생성
        CustomLoginFilter loginFilter = new CustomLoginFilter(authenticationManager(authenticationConfiguration), jwtUtil);
        // 로그인 앤드 포인트
        loginFilter.setFilterProcessesUrl("/api/v1/auth/login");

        http
                // CORS CONFIG
                .cors(cors -> cors.configurationSource(CorsConfig.apiConfigurationSource()))

                // 요쳥별 접근 권한 설정
                .authorizeHttpRequests(request -> request
                        // 허용할 Url은 인증 없이 접근 허용
                        .requestMatchers(allowUrl).permitAll()
                        // 그 외 모든 요청에 대해서 인증
                        .anyRequest().authenticated())

                // JWT 인증 필터 등록
                .addFilterBefore(new JwtAuthorizationFilter(jwtUtil, redisUtils), UsernamePasswordAuthenticationFilter.class)

                // 커스텀 로그인 필터 등록 -> 기본 폼 로그인 대신 JWT 로직 실행
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)

                // SPRING SECURITY 기본 로그인 폼 비활성화 -> REST API 기반 JWT 사용
                .formLogin(AbstractHttpConfigurer::disable)

                // HTTP BASIC 인증 비활성화 -> JWT은 사용하지 않음
                .httpBasic(HttpBasicConfigurer::disable)

                // TODO : CSRF 토큰 + SameSite + Origin 검증
                // CSRF 설정 (JWT를 헤더에 넣으면 필요 없는데 쿠키는 해야함)
                .csrf(AbstractHttpConfigurer::disable)

                // logout
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(jwtLogoutHandler)
                        .logoutSuccessHandler(jwtLogoutSuccessHandler)
                )

                // 예외 처리 핸들러 설정
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        // 인증은 되었지만 권한이 없을 때 (403)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        // 인증 자체가 안 된 경우 (401)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
        ;

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}

//    @Bean
//    public JwtAuthorizationFilter jwtAuthorizationFilter(JwtUtil jwtUtil, RedisUtils<String> redisUtils, List<RequestMatcher> publicMatchers) {
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
//                        // 요청 경로를 PathContainer로 감싸고, 패턴과 매칭여부를 반환 -> true : public URL
//                        return pathPattern.matches(PathContainer.parsePath(path));
//                    };
//                })
//                // 불변 리스트로 모아 publicMatchers에 저장
//                .toList();
//        return new JwtAuthorizationFilter(jwtUtil, redisUtils, publicMatchers);
//    }
}