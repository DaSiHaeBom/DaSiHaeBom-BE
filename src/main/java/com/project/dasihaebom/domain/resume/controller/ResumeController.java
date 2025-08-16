package com.project.dasihaebom.domain.resume.controller;

import com.project.dasihaebom.domain.resume.converter.ResumeConverter;
import com.project.dasihaebom.domain.resume.dto.response.ResumeResDto;
import com.project.dasihaebom.domain.resume.entity.Resume;
import com.project.dasihaebom.domain.resume.repository.ResumeRepository;
import com.project.dasihaebom.domain.resume.service.query.ResumeQueryService;
import com.project.dasihaebom.global.apiPayload.CustomResponse;
import com.project.dasihaebom.global.security.userdetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "resume", description = "이력서 관련 API")
public class ResumeController {

    private final ResumeRepository resumeRepository;
    private final ResumeQueryService resumeQueryService;

    @GetMapping("/my")
    @Operation(summary = "내 이력서 상세 조회 API", description = "로그인한 사용자의 이력서 정보를 조회합니다.")
    public CustomResponse<ResumeResDto.ResumeDetailDTO> getMyResume(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long workerId = userDetails.getId();
        Resume resume = resumeQueryService.getMyResume(workerId);
        ResumeResDto.ResumeDetailDTO responseDTO = ResumeConverter.toResumeDetailDTO(resume);
        return CustomResponse.onSuccess(responseDTO);
    }

    @GetMapping("/{workerId}")
    @Operation(summary = "특정 사용자 이력서 상세 조회 API (기업용)", description = "기업 회원이 특정 worker ID를 가진 사용자의 이력서를 조회합니다.")
    @PreAuthorize("hasAuthority('CORP')")
    public CustomResponse<ResumeResDto.ResumeDetailDTO> getResumeByWorkerId(
            @PathVariable Long workerId
    ) {
        Resume resume = resumeQueryService.getResumeByWorkerId(workerId);
        ResumeResDto.ResumeDetailDTO responseDTO = ResumeConverter.toResumeDetailDTO(resume);
        return CustomResponse.onSuccess(responseDTO);
    }
}
