package com.project.dasihaebom.domain.resume.service.query;

import com.project.dasihaebom.domain.resume.dto.request.ResumeSearchCondition;
import com.project.dasihaebom.domain.resume.dto.response.ResumeResDto;
import com.project.dasihaebom.domain.resume.entity.Resume;

public interface ResumeQueryService {
    ResumeResDto.ResumeCursorResponse searchResumes(ResumeSearchCondition condition, Long userCorpId);

    Resume getMyResume(Long workerId);

    Resume getResumeByWorkerId(Long workerId);

    boolean checkResumeExistence(Long workerId); //이력서(자소서 유무 판정)
}
