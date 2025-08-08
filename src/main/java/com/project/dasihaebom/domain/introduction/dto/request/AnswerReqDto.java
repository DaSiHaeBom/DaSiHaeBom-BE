package com.project.dasihaebom.domain.introduction.dto.request;

import lombok.Builder;

public class AnswerReqDto {
    @Builder
    public record CreateAnswerReqDto(
            String content // 질문에 대한 답변 내용
    ){}

    @Builder
    public record UpdateAnswerReqDto(
            String content // 질문에 대한 답변 내용
    ){}
}
