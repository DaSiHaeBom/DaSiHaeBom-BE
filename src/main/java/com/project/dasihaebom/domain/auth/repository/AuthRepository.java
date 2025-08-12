package com.project.dasihaebom.domain.auth.repository;

import com.project.dasihaebom.domain.auth.entity.Auth;
import com.project.dasihaebom.domain.user.corp.entity.Corp;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {

    Optional<Auth> findByCorp(Corp corp);

    Optional<Auth> findByWorker(Worker worker);

    Optional<Auth> findByLoginId(String loginId);
}
