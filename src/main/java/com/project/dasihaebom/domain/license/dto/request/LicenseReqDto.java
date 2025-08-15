package com.project.dasihaebom.domain.license.dto.request;

import java.time.LocalDate;

public class LicenseReqDto {
    public record LicenseCreateReqDto(
            String name,
            LocalDate issuedAt,
            String issuer
    ) {
    }

    public record LicenseUpdateReqDto(
            String name,
            LocalDate issuedAt,
            String issuer
    ) {
    }
}
