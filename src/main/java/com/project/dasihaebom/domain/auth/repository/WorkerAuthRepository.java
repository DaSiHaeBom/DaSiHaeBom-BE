package com.project.dasihaebom.domain.auth.repository;

import com.project.dasihaebom.domain.auth.entity.WorkerAuth;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkerAuthRepository extends JpaRepository<WorkerAuth, Long> {

    Optional<WorkerAuth> findByWorker(Worker worker);
}
