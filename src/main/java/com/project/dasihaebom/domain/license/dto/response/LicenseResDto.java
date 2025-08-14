package com.project.dasihaebom.domain.license.dto.response;

import lombok.Builder;

public class LicenseResDto {

    @Builder
    public record LicenseCreateResDto(
            long licenseId
    ) {
    }
}
