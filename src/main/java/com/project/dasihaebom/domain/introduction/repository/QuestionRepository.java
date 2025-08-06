package com.project.dasihaebom.domain.introduction.repository;

import com.project.dasihaebom.domain.introduction.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question,Long> {
}
