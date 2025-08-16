package com.project.dasihaebom.domain.resume.service.query;

import com.project.dasihaebom.domain.resume.entity.Resume;

public interface ResumeQueryService {
    Resume getMyResume(Long workerId);

    Resume getResumeByWorkerId(Long workerId);
}
