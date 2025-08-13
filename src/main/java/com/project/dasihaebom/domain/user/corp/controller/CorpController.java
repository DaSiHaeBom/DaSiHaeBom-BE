package com.project.dasihaebom.domain.user.corp.controller;

import com.project.dasihaebom.domain.user.corp.dto.request.CorpReqDto;
import com.project.dasihaebom.domain.user.corp.dto.response.CorpResDto;
import com.project.dasihaebom.domain.user.corp.repository.CorpRepository;
import com.project.dasihaebom.domain.user.corp.service.command.CorpCommandService;
import com.project.dasihaebom.global.apiPayload.CustomResponse;
import com.project.dasihaebom.global.security.userdetails.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/corps")
@Tag(name = "Corp", description = "기업 유저 관련 API")
public class CorpController {

    private final CorpCommandService corpCommandService;

    @Operation(summary = "기업 회원 가입", description = "아이디, 전화번호, 사업자 번호가 겹치면 가입 안됨")
    @PostMapping()
    public CustomResponse<String> createCorp(
            @RequestBody @Valid CorpReqDto.CorpCreateReqDto corpCreateReqDto
    ) {
        corpCommandService.createCorp(corpCreateReqDto);
        return CustomResponse.onSuccess("기업 회원 가입 완료");
    }

    @Operation(summary = "기업 회원 정보 수정")
    @PatchMapping("/me")
    public CustomResponse<String> updateCorp(
            @RequestBody @Valid CorpReqDto.CorpUpdateReqDto corpUpdateReqDto,
            @AuthenticationPrincipal CurrentUser currentUser
            ) {
        corpCommandService.updateCorp(corpUpdateReqDto, currentUser.getId());
        return CustomResponse.onSuccess("기업 회원 정보 수정 완료");
    }

    @Operation(summary = "사업자 번호 유효성 검사", description = "사업자 번호 인증 성공 -> true / 인증 실패 or API 오류 -> false")
    @PostMapping("/business-validation")
    public CustomResponse<CorpResDto.CorpNumberValidResDto> validCorpNumber(
            @RequestBody @Valid CorpReqDto.CorpNumberValidReqDto corpNumberValidReqDto
    ) {
        return CustomResponse.onSuccess(corpCommandService.validCorpNumber(corpNumberValidReqDto));
    }
}
