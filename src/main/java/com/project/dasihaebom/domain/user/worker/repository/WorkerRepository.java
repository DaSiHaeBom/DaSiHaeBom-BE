package com.project.dasihaebom.domain.user.worker.repository;

import com.project.dasihaebom.domain.user.worker.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WorkerRepository extends JpaRepository<Worker, Long> {

    Optional<Worker> findByPhoneNumber(String phoneNUmber);
}
