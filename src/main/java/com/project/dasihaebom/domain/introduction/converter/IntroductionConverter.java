package com.project.dasihaebom.domain.introduction.converter;

import com.project.dasihaebom.domain.introduction.dto.response.AnswerResDto;
import com.project.dasihaebom.domain.introduction.entity.Answer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IntroductionConverter {

    public static AnswerResDto.AnswerDetailDTO toAnswerDetailDTO(Answer answer) {
        return AnswerResDto.AnswerDetailDTO.builder()
                .answerId(answer.getId())
                .questionId(answer.getQuestion().getId())
                .questionContent(answer.getQuestion().getQuestion())
                .answerContent(answer.getContent())
                .createdAt(answer.getCreatedAt())
                .updatedAt(answer.getUpdatedAt())
                .build();
    } // 조회 응답 컨버터
}
