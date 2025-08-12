package com.project.dasihaebom.domain.auth.repository;

import com.project.dasihaebom.domain.auth.entity.CorpAuth;
import com.project.dasihaebom.domain.user.corp.entity.Corp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CorpAuthRepository extends JpaRepository<CorpAuth, Long> {

    Optional<CorpAuth> findByCorp(Corp corp);
}
