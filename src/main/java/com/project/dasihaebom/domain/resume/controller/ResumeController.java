package com.project.dasihaebom.domain.resume.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dasihaebom.domain.resume.converter.ResumeConverter;
import com.project.dasihaebom.domain.resume.dto.request.ResumeSearchCondition;
import com.project.dasihaebom.domain.resume.dto.response.ResumeResDto;
import com.project.dasihaebom.domain.resume.entity.Resume;
import com.project.dasihaebom.domain.resume.repository.ResumeRepository;
import com.project.dasihaebom.domain.resume.service.command.PdfGenerateService;
import com.project.dasihaebom.domain.resume.service.query.ResumeQueryService;
import com.project.dasihaebom.domain.user.corp.entity.Corp;
import com.project.dasihaebom.domain.user.corp.exception.CorpErrorCode;
import com.project.dasihaebom.domain.user.corp.repository.CorpRepository;
import com.project.dasihaebom.global.apiPayload.CustomResponse;
import com.project.dasihaebom.global.apiPayload.exception.CustomException;
import com.project.dasihaebom.global.security.userdetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "resume", description = "이력서 관련 API")
public class ResumeController {

    private final ResumeRepository resumeRepository;
    private final ResumeQueryService resumeQueryService;
    private final CorpRepository corpRepository;
    private final PdfGenerateService pdfGenerateService;
    private final ObjectMapper objectMapper;

    @PostMapping("resume/search")
    @Operation(summary = "이력서 목록 조회", description = "조회지만 검색 필터가 많아 post로 reqBody를 받습니다.")
    @PreAuthorize("hasAuthority('CORP') or hasAuthority('ADMIN')")
    public CustomResponse<ResumeResDto.ResumeCursorResponse> searchResumes(
            @RequestBody ResumeSearchCondition condition,  // @ModelAttribute → @RequestBody 변경
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        // 디버깅을 위한 로그
        System.out.println("Received condition: " + condition);
        System.out.println("Licenses: " + condition.getLicenses());
        System.out.println("License count: " + (condition.getLicenses() != null ? condition.getLicenses().size() : "null"));

        // 1. userDetails에서 로그인한 기업의 ID를 가져옵니다.
        Long currentCorpId = userDetails.getId();

        // 2. 검색 조건에 위도/경도 값이 없는 경우에만 기업 위치를 설정합니다.
        if (condition.getLatitude() == null || condition.getLongitude() == null) {
            // ID를 사용하여 DB에서 Corp 엔티티를 조회합니다.
            Corp corp = corpRepository.findById(currentCorpId)
                    .orElseThrow(() -> new CustomException(CorpErrorCode.CORP_NOT_FOUND));

            // Corp 엔티티에서 위도와 경도 정보를 가져와 condition에 설정합니다.
            condition.setLatitude(corp.getLatitude());
            condition.setLongitude(corp.getLongitude());
        }

        // 3. 업데이트된 condition 객체를 서비스에 전달합니다.
        ResumeResDto.ResumeCursorResponse responseDTO = resumeQueryService.searchResumes(condition, currentCorpId);

        return CustomResponse.onSuccess(responseDTO);
    }

    @GetMapping("resume/my")
    @Operation(summary = "내 이력서 상세 조회 API", description = "로그인한 사용자의 이력서 정보를 조회합니다.")
    public CustomResponse<ResumeResDto.ResumeDetailDTO> getMyResume(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long workerId = userDetails.getId();
        Resume resume = resumeQueryService.getMyResume(workerId);
        ResumeResDto.ResumeDetailDTO responseDTO = ResumeConverter.toResumeDetailDTO(resume, objectMapper);
        return CustomResponse.onSuccess(responseDTO);
    }

    @GetMapping("resume/{workerId}")
    @Operation(summary = "특정 사용자 이력서 상세 조회 API (기업용)", description = "기업 회원이 특정 worker ID를 가진 사용자의 이력서를 조회합니다.")
    @PreAuthorize("hasAuthority('CORP') or hasAuthority('ADMIN')")
    public CustomResponse<ResumeResDto.ResumeDetailDTO> getResumeByWorkerId(
            @PathVariable Long workerId
    ) {
        Resume resume = resumeQueryService.getResumeByWorkerId(workerId);
        ResumeResDto.ResumeDetailDTO responseDTO = ResumeConverter.toResumeDetailDTO(resume, objectMapper);
        return CustomResponse.onSuccess(responseDTO);
    }

    @GetMapping("/{resumeId}/download")
    @Operation(summary = "PDF 다운로드 api입니다.", description = "기본적인 html밖에 몰라서 디자인은 아쉬울 수도 있습니당")
    public ResponseEntity<byte[]> downloadResumePdf(@PathVariable Long resumeId) throws IOException {

        byte[] pdfBytes = pdfGenerateService.generateResumePdf(resumeId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // "attachment"는 파일을 다운로드하라는 의미, filename은 다운로드될 파일 이름
        headers.setContentDispositionFormData("attachment", "resume_" + resumeId + ".pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK); // 외부 api를 사용하기 때문에 json형식을 맞추기 위해 custom은 사용안함
    }
}
