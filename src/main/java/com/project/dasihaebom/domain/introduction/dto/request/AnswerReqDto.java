package com.project.dasihaebom.domain.introduction.dto.request;

import lombok.Builder;

public class AnswerReqDto {


    @Builder
    public record SaveAnswerReqDto(
            String content // 질문에 대한 답변 내용
    ){}

    @Builder
    public record UpdateIntroductionSummaryReqDto(
            String summary
    ) {}

    @Builder
    public record UpdateIntroductionFullTextReqDto(
            String fullText
    ) {}
}
