package com.project.dasihaebom.domain.resume.dto.response;

import lombok.Builder;

import java.util.List;

public class ResumeResDto {


    //목록 조회용 요약 dto
    @Builder
    public record ResumeSummaryDTO(
            Long resumeId,
            String username,
            Integer age,
            String address,
            String introductionSummary,
            List<String> licenseNames
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
}
