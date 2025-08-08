package com.project.dasihaebom.domain.user.corp.controller;

import com.project.dasihaebom.domain.user.corp.dto.request.CorpReqDto;
import com.project.dasihaebom.domain.user.corp.repository.CorpRepository;
import com.project.dasihaebom.domain.user.corp.service.command.CorpCommandService;
import com.project.dasihaebom.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Corp", description = "기업 유저 관련 API")
public class CorpController {

    private final CorpCommandService corpCommandService;

    @Operation(summary = "기업 회원 가입")
    @PostMapping("/corps")
    public CustomResponse<String> createCorp(
            @RequestBody CorpReqDto.CorpCreateReqDto corpCreateReqDto
    ) {
        corpCommandService.createCorp(corpCreateReqDto);
        return CustomResponse.onSuccess("기업 회원 가입 완료");
    }
}
