package com.project.dasihaebom.domain.introduction.service.query;

import com.project.dasihaebom.domain.introduction.entity.Answer;
import com.project.dasihaebom.domain.introduction.entity.Introduction;

import java.util.List;
import java.util.Optional;

public interface IntroductionQueryService {

    List<Answer> getMyAnswers(Long workerId);

    Answer getMyAnswer(Long workerId, Long questionId);

    Introduction getMyIntroduction(Long workerId);
}
