package com.project.dasihaebom.domain.introduction.controller;

import com.project.dasihaebom.domain.introduction.converter.IntroductionConverter;
import com.project.dasihaebom.domain.introduction.dto.request.AnswerReqDto;
import com.project.dasihaebom.domain.introduction.dto.response.AnswerResDto;
import com.project.dasihaebom.domain.introduction.entity.Answer;
import com.project.dasihaebom.domain.introduction.entity.Introduction;
import com.project.dasihaebom.domain.introduction.service.command.IntroductionCommandService;
import com.project.dasihaebom.domain.introduction.service.query.IntroductionQueryService;
import com.project.dasihaebom.global.apiPayload.CustomResponse;
import com.project.dasihaebom.global.security.userdetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PutMapping("/answer/{questionId}")
    @Operation(summary = "답변 저장(생성/수정) API")
    public CustomResponse<AnswerResDto.AnswerDetailDTO> saveAnswer(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long questionId,
            @RequestBody AnswerReqDto.SaveAnswerReqDto request
    ) {
        Long workerId = userDetails.getId();
        Answer answer = introductionCommandService.upsertAnswer(workerId, questionId, request);
        AnswerResDto.AnswerDetailDTO responseDTO = IntroductionConverter.toAnswerDetailDTO(answer);
        return CustomResponse.onSuccess(responseDTO);
    }

    @PostMapping("/answer/generate")
    @Operation(summary = "AI 자기소개서 생성 API", description = "저장된 모든 답변을 기반으로 자기소개서 본문과 요약을 생성하고 저장")
    public CustomResponse<AnswerResDto.GeneratedIntroductionDTO> generateIntroduction(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long workerId = userDetails.getId();
        Introduction introduction = introductionCommandService.generateIntroduction(workerId);
        AnswerResDto.GeneratedIntroductionDTO responseDTO = IntroductionConverter.toGeneratedIntroductionDTO(introduction);
        return CustomResponse.onSuccess(responseDTO);
    }

    @GetMapping("/answer/{questionId}")
    @Operation(summary = "내 답변 상세 조회 API")
    public CustomResponse<AnswerResDto.AnswerDetailDTO> getMyAnswer(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long questionId
    ) {
        Long workerId = userDetails.getId();
        Answer answer = introductionQueryService.getMyAnswer(workerId, questionId);
        AnswerResDto.AnswerDetailDTO responseDTO = IntroductionConverter.toAnswerDetailDTO(answer);
        return CustomResponse.onSuccess(responseDTO);
    }

    @GetMapping("/answer/my")
    @Operation(summary = "내 답변 목록 조회 API")
    public CustomResponse<List<AnswerResDto.AnswerDetailDTO>> getMyAnswers(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long workerId = userDetails.getId();
        List<Answer> answerList = introductionQueryService.getMyAnswers(workerId);
        List<AnswerResDto.AnswerDetailDTO> responseDTOList = IntroductionConverter.toAnswerDetailDTOList(answerList);
        return CustomResponse.onSuccess(responseDTOList);
    }

    @GetMapping("/answer/introduction")
    @Operation(summary = "내 자기소개서 본문 & 요약 조회 API")
    public CustomResponse<AnswerResDto.GeneratedIntroductionDTO> getMyIntroductions(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        Long workerId = userDetails.getId();
        Introduction introduction = introductionQueryService.getMyIntroduction(workerId);
        AnswerResDto.GeneratedIntroductionDTO responseDTO = IntroductionConverter.toGeneratedIntroductionDTO(introduction);
        return CustomResponse.onSuccess(responseDTO);
    }

    @PatchMapping("/summary")
    @Operation(summary = "자기소개서 요약 수정", description = "자기소개서의 한 줄 요약 내용을 수정합니다.")
    @PreAuthorize("hasAuthority('WORKER')")
    public CustomResponse<String> updateSummary(
            @RequestBody AnswerReqDto.UpdateIntroductionSummaryReqDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long workerId = userDetails.getId();
        introductionCommandService.updateIntroductionSummary(workerId, request);

        return CustomResponse.onSuccess(null);
    }

    @PatchMapping("/full-text")
    @Operation(summary = "자기소개서 본문 수정", description = "자기소개서의 전체 본문 내용을 수정합니다.")
    @PreAuthorize("hasAuthority('WORKER')")
    public CustomResponse<String> updateFullText(
            @RequestBody AnswerReqDto.UpdateIntroductionFullTextReqDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long workerId = userDetails.getId();
        introductionCommandService.updateIntroductionFullText(workerId, request);

        return CustomResponse.onSuccess(null);
    }
}
