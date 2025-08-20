package com.project.dasihaebom.domain.resume.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

public class ResumeResDto {


    @Builder
    public record ResumeCursorResponse(
            List<ResumeSummaryDTO> resumes,
            Long nextCursorId,
            Double nextCursorDistance, // 거리순 정렬
            boolean hasNext //다음 페이지 존재 유무
    ) {}

    //목록 조회용 요약 dto
    @Builder
    public record ResumeSummaryDTO(
            Long resumeId,
            Long workerId,
            Integer age,
            String address,
            String introductionSummary,
            List<String> licenseNames,
            @JsonInclude(JsonInclude.Include.NON_NULL) // 거리순 정렬일 때만 값이 포함됨
            Double distance
    ) {}


    //상세조회용 dto
    @Builder
    public record ResumeDetailDTO(
            Long resumeId,
            String username,
            String birthDate,
            String gender,
            String address,
            String phoneNumber,
            String introductionFullText, // 자기소개서 본문
            List<LicenseDTO> licenses   // 자격증 상세 정보 목록
    ) {}

    @Builder
    public record LicenseDTO(
            String name,
            String issuedAt, // 취득일
            String issuer    // 발행처
    ) {}

    @Builder
    public record ResumeExistenceDTO(
            Boolean exists
    ) {}
}
