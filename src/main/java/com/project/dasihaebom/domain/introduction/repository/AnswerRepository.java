package com.project.dasihaebom.domain.introduction.repository;

import com.project.dasihaebom.domain.introduction.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    boolean existsByWorkerIdAndQuestionId(Long workerId, Long questionId);

    Optional<Answer> findByWorkerIdAndQuestionId(Long workerId, Long questionId);

    List<Answer> findAllByWorkerId(Long workerId);

}
