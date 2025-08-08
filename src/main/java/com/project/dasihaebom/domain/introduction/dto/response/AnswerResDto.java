package com.project.dasihaebom.domain.introduction.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class AnswerResDto {

    @Schema(description = "답변 조회 dto")
    public record AnswerDetailDTO(

            Long answerId,
            Long questionId,
            String questionContent,
            String answerContent,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}

    @Schema(description = "ai자기소개서 응답 dto")
    public record IntroductionResponseDTO(
            String content
    ) {}
}
