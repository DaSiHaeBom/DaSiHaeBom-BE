package com.project.dasihaebom.domain.resume.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dasihaebom.domain.license.entity.License;
import com.project.dasihaebom.domain.resume.dto.response.ResumeResDto;
import com.project.dasihaebom.domain.resume.entity.Resume;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
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
    public static ResumeResDto.ResumeDetailDTO toResumeDetailDTO(Resume resume, ObjectMapper objectMapper) {

        List<License> licenseList;
        try {
            // 파라미터로 받은 objectMapper 사용
            licenseList = objectMapper.readValue(resume.getLicenses(), new TypeReference<>() {});
        } catch (Exception e) {
            log.error("자격증 정보 JSON 파싱 실패: resumeId={}", resume.getId(), e);
            licenseList = Collections.emptyList();
        }

        // 2. List<License>를 List<LicenseDTO>로 변환
        List<ResumeResDto.LicenseDTO> licenseDTOs = licenseList.stream()
                .map(license -> ResumeResDto.LicenseDTO.builder()
                        .name(license.getName())
                        .issuedAt(license.getIssuedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                        .issuer(license.getIssuer())
                        .build())
                .collect(Collectors.toList());

        // 3.  ResumeDetailDTO 생성
        return ResumeResDto.ResumeDetailDTO.builder()
                .resumeId(resume.getId())
                .username(resume.getUsername())
                .birthDate(resume.getBirthDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .gender(resume.getGender().toString())
                .address(resume.getAddress())
                .phoneNumber(resume.getPhoneNumber())
                .introductionFullText(resume.getIntroductionFullText())
                .licenses(licenseDTOs)
                .build();
    }

    public static ResumeResDto.ResumeSummaryDTO toResumeSummaryDTO(Resume resume, Double distance) {
        if (resume == null) {
            return null;
        }

        Worker worker = resume.getWorker();


        return ResumeResDto.ResumeSummaryDTO.builder()
                .resumeId(resume.getId())
                .workerId(worker.getId())
                .age(calculateAge(resume.getBirthDate())) // LocalDate를 나이로 계산
                .address(resume.getAddress())
                .introductionSummary(resume.getIntroductionSummary())
                .licenseNames(extractLicenseNames(worker)) // Worker의 자격증 목록에서 이름만 추출
                .distance(distance)
                .build();
    }

    public static ResumeResDto.ResumeCursorResponse toResumeCursorResponse(
            List<ResumeResDto.ResumeSummaryDTO> resumes,
            boolean hasNext,
            Long nextCursorId,
            Double nextCursorDistance
    ) {
        return ResumeResDto.ResumeCursorResponse.builder()
                .resumes(resumes)
                .hasNext(hasNext)
                .nextCursorId(nextCursorId)
                .nextCursorDistance(nextCursorDistance)
                .build();
    }


    //생년월일을 만나이로 변환하는 매서드
    private static Integer calculateAge(LocalDate birthDate) {

        if (birthDate == null) {
            return null;
        }

        LocalDate today = LocalDate.now();

        return Period.between(birthDate, today).getYears();
    }

    private static List<String> extractLicenseNames(Worker worker) {
        if (worker.getLicense() == null) {
            return Collections.emptyList();
        }
        return worker.getLicense().stream()
                .map(License::getName)
                .collect(Collectors.toList());
    }

    public static ResumeResDto.ResumeExistenceDTO toResumeExistenceDTO(boolean exists) {
        return new ResumeResDto.ResumeExistenceDTO(exists);
    }
}
