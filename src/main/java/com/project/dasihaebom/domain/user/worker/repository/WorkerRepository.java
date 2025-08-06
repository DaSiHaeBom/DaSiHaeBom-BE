package com.project.dasihaebom.domain.user.worker.repository;

import com.project.dasihaebom.domain.user.worker.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
}
