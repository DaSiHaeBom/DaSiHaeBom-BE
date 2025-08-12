package com.project.dasihaebom.domain.auth.repository;

import com.project.dasihaebom.domain.auth.entity.Auth;
import com.project.dasihaebom.domain.user.corp.entity.Corp;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {

    Optional<Auth> findByCorp(Corp corp);

    Optional<Auth> findByWorker(Worker worker);

    Optional<Auth> findByLoginId(String loginId);

    @Query("SELECT a " +
            "FROM Auth a " +
            "JOIN Worker w " +
            "ON a.worker.id = w.id " +
            "WHERE w.phoneNumber = :phoneNumber " +
            "UNION ALL " +
            "SELECT a " +
            "FROM Auth a " +
            "JOIN Corp c " +
            "ON a.corp.id = c.id " +
            "WHERE c.phoneNumber = :phoneNumber ")
    Optional<Auth> findByPhoneNumber(
            @Param("phoneNumber") String phoneNumber
    );
}
