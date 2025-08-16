package com.project.dasihaebom.domain.resume.repository;

import com.project.dasihaebom.domain.resume.entity.Resume;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Optional<Resume> findByWorker(Worker worker);

    Optional<Resume> findByWorkerId(Long workerId);

}
