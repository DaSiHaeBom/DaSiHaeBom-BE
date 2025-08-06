package com.project.dasihaebom.domain.auth.repository;

import com.project.dasihaebom.domain.auth.entity.WorkerAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerAuthRepository extends JpaRepository<WorkerAuth, Long> {
}
