package com.project.dasihaebom.domain.user.worker.controller;

import com.project.dasihaebom.domain.user.worker.dto.request.WorkerReqDto;
import com.project.dasihaebom.domain.user.worker.service.command.WorkerCommandService;
import com.project.dasihaebom.global.apiPayload.CustomResponse;
import com.project.dasihaebom.global.security.userdetails.CurrentUser;
import com.project.dasihaebom.global.security.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

import static com.project.dasihaebom.global.util.CookieUtils.createJwtCookies;
import static com.project.dasihaebom.global.util.CookieUtils.getTokenFromCookies;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/workers")
@Tag(name = "Worker", description = "개인 사용자 관련 API")
public class WorkerController {

    private final WorkerCommandService workerCommandService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "개인 회원 가입", description = "전화번호가 겹치면 가입이 안됨")
    @PostMapping("")
    public CustomResponse<String> createWorker(
            @RequestBody @Valid WorkerReqDto.WorkerCreateReqDto workerCreateReqDto
    ) {
        workerCommandService.createWorker(workerCreateReqDto);
        return CustomResponse.onSuccess("개인 회원 가입 완료");
    }

    @Operation(summary = "개인 회원 정보 수정")
    @PatchMapping("/me")
    public CustomResponse<String> updateWorker(
            @RequestBody @Valid WorkerReqDto.WorkerUpdateReqDto workerUpdateReqDto,
            @AuthenticationPrincipal CurrentUser currentUser
    ) {
        workerCommandService.updateWorker(workerUpdateReqDto, currentUser.getId());
        return CustomResponse.onSuccess("개인 회원 정보 수정 완료");
    }

    @Operation(summary = "회원 탈퇴", description = "worker에 있긴 하지만 기업 회원도 탈퇴 가능합니다")
    @DeleteMapping("/withdrawal")
    public CustomResponse<String> deleteUser(
            @AuthenticationPrincipal CurrentUser currentUser,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        final String accessToken = getTokenFromCookies(request, "access-token");
        final String refreshToken = getTokenFromCookies(request, "refresh-token");

        workerCommandService.deleteUser(currentUser.getId(), currentUser.getRole(), accessToken, refreshToken);

        createJwtCookies(response, "access-token", null, 0);
        createJwtCookies(response, "refresh-token", null, 0);

        return CustomResponse.onSuccess(HttpStatus.NO_CONTENT, "회원 탈퇴 성공");
    }
}
