package com.project.dasihaebom.domain.license.dto.response;

import com.project.dasihaebom.domain.license.entity.LicenseType;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @Builder
    public record LicenseListResDto(
            List<LicenseDetailResDto> licenses
    ) {
    }

    @Builder
    public record LicenseTypeSearchResDto(
            List<LicenseTypeDetailResDto> licenseTypes
    ) {
        @Builder
        public record LicenseTypeDetailResDto(
                long id,
                String name,
                String issuer
        ) {
        }
    }
}
