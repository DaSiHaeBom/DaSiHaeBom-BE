package com.project.dasihaebom.domain.resume.repository;

import com.project.dasihaebom.domain.resume.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
}
