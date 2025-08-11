package com.project.dasihaebom.domain.introduction.repository;

import com.project.dasihaebom.domain.introduction.entity.Introduction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntroductionRepository extends JpaRepository<Introduction,Long> {
    void deleteAllByWorkerId(Long workerId);
}
