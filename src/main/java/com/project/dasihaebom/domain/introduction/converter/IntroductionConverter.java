package com.project.dasihaebom.domain.introduction.converter;

import com.project.dasihaebom.domain.introduction.dto.request.AnswerReqDto;
import com.project.dasihaebom.domain.introduction.dto.response.AnswerResDto;
import com.project.dasihaebom.domain.introduction.entity.Answer;
import com.project.dasihaebom.domain.introduction.entity.Question;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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

    public static Answer toAnswer(AnswerReqDto.CreateAnswerReqDto request, Worker worker, Question question) {
        return Answer.builder()
                .content(request.content())
                .worker(worker)
                .question(question)
                .build();
    }

    public static List<AnswerResDto.AnswerDetailDTO> toAnswerDetailDTOList(List<Answer> answerList) {
        return answerList.stream()
                .map(IntroductionConverter::toAnswerDetailDTO)
                .collect(Collectors.toList());
    } // 조회 리스트 컨트롤러 컨버터
}
