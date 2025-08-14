package com.project.dasihaebom.domain.license.controller;

import com.project.dasihaebom.domain.license.dto.request.LicenseReqDto;
import com.project.dasihaebom.domain.license.dto.response.LicenseResDto;
import com.project.dasihaebom.domain.license.service.command.LicenseCommandService;
import com.project.dasihaebom.domain.license.service.query.LicenseQueryService;
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
    private final LicenseQueryService licenseQueryService;

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

    @Operation(summary = "자격증 삭제")
    @DeleteMapping("/{licenseId}")
    public CustomResponse<String> deleteLicense(
            @PathVariable long licenseId,
            @AuthenticationPrincipal CurrentUser currentUser
    ) {
        licenseCommandService.deleteLicense(licenseId, currentUser.getId());
        return CustomResponse.onSuccess(HttpStatus.NO_CONTENT, "자격증 삭제 완료");
    }

    @Operation(summary = "자격증 단일 조회")
    @GetMapping("/{licenseId}")
    public CustomResponse<LicenseResDto.LicenseDetailResDto> getLicenseDetail(
            @PathVariable long licenseId,
            @AuthenticationPrincipal CurrentUser currentUser
    ) {
        return CustomResponse.onSuccess(licenseQueryService.getLicenseDetail(licenseId, currentUser.getId()));
    }

    @Operation(summary = "내 자격증 조회")
    @GetMapping()
    public CustomResponse<LicenseResDto.LicenseListResDto> getMyLicensesList(
            @AuthenticationPrincipal CurrentUser currentUser
    ) {
        return CustomResponse.onSuccess(licenseQueryService.getMyLicensesList(currentUser.getId()));
    }
}
