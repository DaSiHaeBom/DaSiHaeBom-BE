package com.project.dasihaebom.domain.resume.service.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dasihaebom.domain.introduction.entity.Introduction;
import com.project.dasihaebom.domain.introduction.exception.IntroductionErrorCode;
import com.project.dasihaebom.domain.introduction.exception.IntroductionException;
import com.project.dasihaebom.domain.introduction.repository.IntroductionRepository;
import com.project.dasihaebom.domain.license.entity.License;
import com.project.dasihaebom.domain.resume.entity.Resume;
import com.project.dasihaebom.domain.resume.exception.ResumeErrorCode;
import com.project.dasihaebom.domain.resume.exception.ResumeException;
import com.project.dasihaebom.domain.resume.repository.ResumeRepository;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import com.project.dasihaebom.domain.user.worker.exception.WorkerErrorCode;
import com.project.dasihaebom.domain.user.worker.exception.WorkerException;
import com.project.dasihaebom.domain.user.worker.repository.WorkerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ResumeCommandServiceImpl implements ResumeCommandService {

    private final WorkerRepository workerRepository;
    private final IntroductionRepository introductionRepository;
    private final ResumeRepository resumeRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void syncResume(Long workerId) {
        // 원본 데이터 조회
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new WorkerException(WorkerErrorCode.WORKER_NOT_FOUND)); // Worker 예외
        Introduction introduction = introductionRepository.findTopByWorkerIdOrderByIdDesc(workerId)
                .orElseThrow(() -> new IntroductionException(IntroductionErrorCode.INTRODUCTION_NOT_FOUND)); // Introduction 예외

        List<License> licenseList = worker.getLicenseList();

        // 라이선스 정보를 JSON 문자열로 변환
        String licensesJson;
        try {
            licensesJson = objectMapper.writeValueAsString(licenseList);
        } catch (JsonProcessingException e) {
            log.error("자격증 정보 JSON 변환 실패: workerId={}", workerId, e);
            throw new ResumeException(ResumeErrorCode.JSON_PROCESSING_ERROR); // ⬅️ Resume 예외
        }

        // 이력서 조회 또는 생성
        Resume resume = resumeRepository.findByWorker(worker)
                .orElse(Resume.builder().worker(worker).build());

        // 데이터 동기화 및 저장
        resume.syncData(worker, introduction, licensesJson);
        resumeRepository.save(resume);
    }
}
