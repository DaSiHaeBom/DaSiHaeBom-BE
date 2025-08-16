package com.project.dasihaebom.domain.resume.service.command;

import com.project.dasihaebom.domain.resume.entity.Resume;

public interface ResumeCommandService {


    void syncResume(Long workerId);
}
