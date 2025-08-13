package com.project.dasihaebom.domain.user.worker.controller;

import com.project.dasihaebom.domain.user.worker.dto.request.WorkerReqDto;
import com.project.dasihaebom.domain.user.worker.repository.WorkerRepository;
import com.project.dasihaebom.domain.user.worker.service.command.WorkerCommandService;
import com.project.dasihaebom.global.apiPayload.CustomResponse;
import com.project.dasihaebom.global.security.userdetails.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/workers")
@Tag(name = "Worker", description = "개인 사용자 관련 API")
public class WorkerController {

    private final WorkerCommandService workerCommandService;

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
}
