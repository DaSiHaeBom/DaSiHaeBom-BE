package com.project.dasihaebom.domain.resume.service.query;

import com.project.dasihaebom.domain.resume.entity.Resume;
import com.project.dasihaebom.domain.resume.exception.ResumeErrorCode;
import com.project.dasihaebom.domain.resume.exception.ResumeException;
import com.project.dasihaebom.domain.resume.repository.ResumeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ResumeQueryServiceImpl implements ResumeQueryService {

    private final ResumeRepository resumeRepository;

    @Override
    public Resume getMyResume(Long workerId) {
        return resumeRepository.findByWorkerId(workerId)
                .orElseThrow(() -> new ResumeException(ResumeErrorCode.RESUME_NOT_FOUND));
    }

    @Override
    public Resume getResumeByWorkerId(Long workerId) {
        return resumeRepository.findByWorkerId(workerId)
                .orElseThrow(() -> new ResumeException(ResumeErrorCode.RESUME_NOT_FOUND));
    }
}
