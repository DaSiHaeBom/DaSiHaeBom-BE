package com.project.dasihaebom.domain.user.corp.repository;

import com.project.dasihaebom.domain.user.corp.entity.Corp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CorpRepository extends JpaRepository<Corp, Long> {

    Optional<Corp> findByLoginId(String loginId);
}
