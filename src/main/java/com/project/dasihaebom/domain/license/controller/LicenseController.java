package com.project.dasihaebom.domain.license.controller;

import com.project.dasihaebom.domain.license.dto.request.LicenseReqDto;
import com.project.dasihaebom.domain.license.dto.response.LicenseResDto;
import com.project.dasihaebom.domain.license.service.command.LicenseCommandService;
import com.project.dasihaebom.global.apiPayload.CustomResponse;
import com.project.dasihaebom.global.security.userdetails.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/licenses")
@Tag(name = "License", description = "자격증 관련 API")
public class LicenseController {

    private final LicenseCommandService licenseCommandService;

    @Operation(summary = "자격증 생성")
    @PostMapping()
    public CustomResponse<LicenseResDto.LicenseCreateResDto> createLicense(
            @RequestBody LicenseReqDto.LicenseCreateReqDto licenseCreateReqDto,
            @AuthenticationPrincipal CurrentUser currentUser
    ) {
        return CustomResponse.onSuccess(HttpStatus.CREATED, licenseCommandService.createLicense(licenseCreateReqDto, currentUser.getId()));
    }

    @Operation(summary = "자격증 수정")
    @PatchMapping("/{licenseId}")
    public CustomResponse<String> updateLicense(
            @PathVariable long licenseId,
            @RequestBody LicenseReqDto.LicenseUpdateReqDto licenseUpdateReqDto,
            @AuthenticationPrincipal CurrentUser currentUser
    ) {
        licenseCommandService.updateLicense(licenseId, licenseUpdateReqDto, currentUser.getId());
        return CustomResponse.onSuccess("자격증 수정 완료");
    }
}
