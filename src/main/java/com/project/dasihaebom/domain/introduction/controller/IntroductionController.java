package com.project.dasihaebom.domain.introduction.controller;

import com.project.dasihaebom.domain.introduction.converter.IntroductionConverter;
import com.project.dasihaebom.domain.introduction.dto.request.AnswerReqDto;
import com.project.dasihaebom.domain.introduction.dto.response.AnswerResDto;
import com.project.dasihaebom.domain.introduction.entity.Answer;
import com.project.dasihaebom.domain.introduction.entity.Introduction;
import com.project.dasihaebom.domain.introduction.service.command.IntroductionCommandService;
import com.project.dasihaebom.domain.introduction.service.query.IntroductionQueryService;
import com.project.dasihaebom.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "introduction", description = "질문 응답 관련 API")
public class IntroductionController {

    private final IntroductionCommandService introductionCommandService;
    private final IntroductionQueryService introductionQueryService;

    @PostMapping("/answer/{questionId}")
    @Operation(summary = "답변 생성 API")
    public CustomResponse<AnswerResDto.AnswerDetailDTO> createAnswer(
            // TODO: @AuthenticationPrincipal 적용
            @PathVariable Long questionId,
            @RequestBody AnswerReqDto.CreateAnswerReqDto request
    ) {
        long tempWorkerId = 1L; // 테스트용 임시 아이디
        Answer answer = introductionCommandService.createAnswer(tempWorkerId, questionId, request);
        AnswerResDto.AnswerDetailDTO responseDTO = IntroductionConverter.toAnswerDetailDTO(answer);

        return CustomResponse.onSuccess(responseDTO);
    }

    @PostMapping("/answer/generate")
    @Operation(summary = "AI 자기소개서 생성 API", description = "저장된 모든 답변을 기반으로 자기소개서 본문과 요약을 생성하고 저장")
    public CustomResponse<AnswerResDto.GeneratedIntroductionDTO> generateIntroduction(
            // TODO: @AuthenticationPrincipal 적용
    ) {
        long tempWorkerId = 1L;

        // 1. Service를 호출하여 AI 생성 및 저장을 요청
        Introduction introduction = introductionCommandService.generateIntroduction(tempWorkerId);

        // 2. Converter를 사용해 응답 DTO로 변환
        AnswerResDto.GeneratedIntroductionDTO responseDTO = IntroductionConverter.toGeneratedIntroductionDTO(introduction);

        // 3. 성공 응답 반환
        return CustomResponse.onSuccess(responseDTO);
    }

    @GetMapping("/answer/{questionId}")
    @Operation(summary = "내 답변 상세 조회 API")
    public CustomResponse<AnswerResDto.AnswerDetailDTO> getMyAnswer(
            // TODO: @AuthenticationPrincipal 적용
            @PathVariable Long questionId
    ) {
        long tempWorkerId = 1L; // 임시  아이디
        Answer answer = introductionQueryService.getMyAnswer(tempWorkerId, questionId);
        AnswerResDto.AnswerDetailDTO responseDTO = IntroductionConverter.toAnswerDetailDTO(answer);
        return CustomResponse.onSuccess(responseDTO);
    }

    @GetMapping("/answer/my")
    @Operation(summary = "내 답변 목록 조회 API")
    public CustomResponse<List<AnswerResDto.AnswerDetailDTO>> getMyAnswers(
            // TODO: @AuthenticationPrincipal 적용
    ) {
        long tempWorkerId = 1L;
        List<Answer> answerList = introductionQueryService.getMyAnswers(tempWorkerId);
        List<AnswerResDto.AnswerDetailDTO> responseDTOList = IntroductionConverter.toAnswerDetailDTOList(answerList);
        return CustomResponse.onSuccess(responseDTOList);
    }

    @GetMapping("/answer/introduction")
    @Operation(summary = "내 자기소개서 본문 & 요약 조회 API")
    public CustomResponse<AnswerResDto.GeneratedIntroductionDTO> getMyIntroductions(
            // TODO: @AuthenticationPrincipal 적용
    ){
        long tempWorkerId = 1L;
        Introduction introduction = introductionQueryService.getMyIntroduction(tempWorkerId);

        AnswerResDto.GeneratedIntroductionDTO responseDTO = IntroductionConverter.toGeneratedIntroductionDTO(introduction);
        return CustomResponse.onSuccess(responseDTO);
    }


    @PatchMapping("/answer/{questionId}")
    @Operation(summary = "답변 수정 API")
    public CustomResponse<AnswerResDto.AnswerDetailDTO> updateAnswer(
            // TODO: @AuthenticationPrincipal 적용
            @PathVariable Long questionId,
            @RequestBody AnswerReqDto.UpdateAnswerReqDto request
    ) {
        long tempWorkerId = 1L;
        Answer answer = introductionCommandService.updateAnswer(tempWorkerId, questionId, request);
        AnswerResDto.AnswerDetailDTO responseDTO = IntroductionConverter.toAnswerDetailDTO(answer);
        return CustomResponse.onSuccess(responseDTO);
    }



}
