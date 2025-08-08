package com.project.dasihaebom.domain.introduction.repository;

import com.project.dasihaebom.domain.introduction.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    boolean existsByWorkerIdAndQuestionId(Long workerId, Long questionId);
}
