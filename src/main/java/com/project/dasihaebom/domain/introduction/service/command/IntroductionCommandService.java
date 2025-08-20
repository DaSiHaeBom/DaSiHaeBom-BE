package com.project.dasihaebom.domain.introduction.service.command;

import com.project.dasihaebom.domain.introduction.dto.request.AnswerReqDto;
import com.project.dasihaebom.domain.introduction.entity.Answer;
import com.project.dasihaebom.domain.introduction.entity.Introduction;

public interface IntroductionCommandService {


    Answer upsertAnswer(Long workerId, Long questionId, AnswerReqDto.SaveAnswerReqDto request);

    Introduction generateIntroduction(Long workerId);

    Introduction updateIntroductionSummary(Long workerId, AnswerReqDto.UpdateIntroductionSummaryReqDto request);

    Introduction updateIntroductionFullText(Long workerId, AnswerReqDto.UpdateIntroductionFullTextReqDto request);
}
