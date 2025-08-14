package com.project.dasihaebom.domain.license.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LicenseResDto {

    @Builder
    public record LicenseCreateResDto(
            long licenseId
    ) {
    }

    @Builder
    public record LicenseDetailResDto(
        long licenseId,
        String name,
        LocalDate issuedAt,
        String issuer,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
    }
}
