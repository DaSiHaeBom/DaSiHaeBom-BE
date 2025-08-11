package com.project.dasihaebom.domain.introduction.service.command;

import com.project.dasihaebom.domain.introduction.dto.request.AnswerReqDto;
import com.project.dasihaebom.domain.introduction.entity.Answer;
import com.project.dasihaebom.domain.introduction.entity.Introduction;

public interface IntroductionCommandService {

    Answer createAnswer(Long workerId, Long questionId, AnswerReqDto.CreateAnswerReqDto request);

    Answer updateAnswer(Long workerId, Long questionId, AnswerReqDto.UpdateAnswerReqDto request);

    Introduction generateIntroduction(Long workerId);
}
