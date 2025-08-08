package com.project.dasihaebom.domain.user.worker.controller;

import com.project.dasihaebom.domain.user.worker.dto.request.WorkerReqDto;
import com.project.dasihaebom.domain.user.worker.repository.WorkerRepository;
import com.project.dasihaebom.domain.user.worker.service.command.WorkerCommandService;
import com.project.dasihaebom.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "Worker", description = "개인 사용자 관련 API")
public class WorkerController {

    private final WorkerCommandService workerCommandService;
    private final WorkerRepository workerRepository;

    @Operation(summary = "개인 회원 가입")
    @PostMapping("/workers")
    public CustomResponse<String> createWorker(
            @RequestBody WorkerReqDto.WorkerCreateReqDto workerCreateReqDto
    ) {
        workerCommandService.createWorker(workerCreateReqDto);
        return CustomResponse.onSuccess("개인 회원 가입 완료");
    }
}
