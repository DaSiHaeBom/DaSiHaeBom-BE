package com.project.dasihaebom.domain.introduction.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

public class AnswerResDto {

    @Schema(description = "답변 조회 dto")
    @Builder
    public record AnswerDetailDTO(

            Long answerId,
            Long questionId,
            String questionContent,
            String answerContent,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}

    @Schema(description = "생성된 자기소개서 응답 DTO")
    @Builder
    public record GeneratedIntroductionDTO(
            Long introductionId,
            String fullText,
            String summary,
            LocalDateTime createdAt
    ) {}
}
