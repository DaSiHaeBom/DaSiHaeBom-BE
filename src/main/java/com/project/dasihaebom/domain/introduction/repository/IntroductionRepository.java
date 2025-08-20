package com.project.dasihaebom.domain.introduction.repository;

import com.project.dasihaebom.domain.introduction.entity.Introduction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IntroductionRepository extends JpaRepository<Introduction,Long> {
    void deleteAllByWorkerId(Long workerId);

    Optional<Introduction> findByWorkerId(Long workerId);

    Optional<Introduction> findTopByWorkerIdOrderByIdDesc(Long workerId);

    //자소서가 존재한다 = 이력서가 존재한다
    boolean existsByWorker_Id(Long workerId);
}
