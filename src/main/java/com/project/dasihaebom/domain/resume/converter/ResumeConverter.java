package com.project.dasihaebom.domain.resume.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dasihaebom.domain.license.entity.License;
import com.project.dasihaebom.domain.resume.dto.response.ResumeResDto;
import com.project.dasihaebom.domain.resume.entity.Resume;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResumeConverter {


    private static final ObjectMapper objectMapper = new ObjectMapper();


    //상세조회 컨버터
    public static ResumeResDto.ResumeDetailDTO toResumeDetailDTO(Resume resume) {

        // 1. licenses (JSON 문자열)를 List<License> 객체로 변환
        List<License> licenseList;
        try {
            licenseList = objectMapper.readValue(resume.getLicenses(), new TypeReference<>() {});
        } catch (Exception e) {
            log.error("자격증 정보 JSON 파싱 실패: resumeId={}", resume.getId(), e);
            licenseList = Collections.emptyList(); // 파싱 실패 시 빈 리스트 반환
        }

        // 2. List<License>를 List<LicenseDTO>로 변환
        List<ResumeResDto.LicenseDTO> licenseDTOs = licenseList.stream()
                .map(license -> ResumeResDto.LicenseDTO.builder()
                        .name(license.getName())
                        .issuedAt(license.getIssuedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                        .issuer(license.getIssuer())
                        .build())
                .collect(Collectors.toList());

        // 3. 최종 ResumeDetailDTO 생성
        return ResumeResDto.ResumeDetailDTO.builder()
                .resumeId(resume.getId())
                .username(resume.getUsername())
                .birthDate(resume.getBirthDate())
                .gender(resume.getGender().toString())
                .address(resume.getAddress())
                .phoneNumber(resume.getPhoneNumber())
                .introductionFullText(resume.getIntroductionFullText())
                .licenses(licenseDTOs)
                .build();
    }


    //생년월일을 만나이로 변환하는 매서드
    private static Integer calculateAge(String birthDate) {

        if (birthDate == null || birthDate.length() != 6) {
            return null;
        }

        // ⬇️ 2자리 연도를 1900년대로 해석하도록 포맷터를 설정합니다.
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendValueReduced(ChronoField.YEAR, 2, 2, 1930) // 기준 연도를 1930년으로 설정
                .appendPattern("MMdd")
                .toFormatter();

        LocalDate birth = LocalDate.parse(birthDate, formatter);
        LocalDate today = LocalDate.now();

        return Period.between(birth, today).getYears();
    }
}
