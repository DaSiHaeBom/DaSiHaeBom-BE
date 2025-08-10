package com.project.dasihaebom.domain.introduction.service.query;

import com.project.dasihaebom.domain.introduction.entity.Answer;

import java.util.List;

public interface IntroductionQueryService {

    List<Answer> getMyAnswers(Long workerId);

    Answer getMyAnswer(Long workerId, Long questionId);
}
