package com.project.dasihaebom.global.security.controller;

import com.project.dasihaebom.domain.user.worker.entity.Worker;
import com.project.dasihaebom.domain.user.worker.repository.WorkerRepository;
import com.project.dasihaebom.global.apiPayload.CustomResponse;
import com.project.dasihaebom.global.security.service.SecurityService;
import com.project.dasihaebom.global.security.userdetails.CustomUserDetails;
import com.project.dasihaebom.global.security.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.project.dasihaebom.global.util.CookieUtils.createJwtCookies;
import static com.project.dasihaebom.global.util.CookieUtils.getTokenFromCookies;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/security")
public class SecurityController {

    private final SecurityService securityService;
    private final JwtUtil jwtUtil;
    private final WorkerRepository workerRepository;

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

    @Operation(summary = "스웨거 로컬 테스트용 액세스 토큰 발급 api")
    @GetMapping("/test/token")
    public String getTestToken() {
        // DB에서 1번 유저를 찾아옵니다.
        Worker worker = workerRepository.findById(1L).orElseThrow();

        // UserDetails 객체를 만듭니다.
        CustomUserDetails userDetails = new CustomUserDetails(
                worker.getId(),
                worker.getUsername(),
                null,
                worker.getRole()
        );

        // 토큰을 생성하여 바로 반환합니다.
        return jwtUtil.createJwtAccessToken(userDetails);
    }
}
