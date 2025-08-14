package com.project.dasihaebom.domain.license.converter;

import com.project.dasihaebom.domain.license.dto.request.LicenseReqDto;
import com.project.dasihaebom.domain.license.dto.response.LicenseResDto;
import com.project.dasihaebom.domain.license.entity.License;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LicenseConverter {

    public static License toLicense(LicenseReqDto.LicenseCreateReqDto licenseCreateReqDto, Worker worker) {
        return License.builder()
                .name(licenseCreateReqDto.name())
                .issuedAt(licenseCreateReqDto.issuedAt())
                .issuer(licenseCreateReqDto.issuer())
                .worker(worker)
                .build();
    }

    public static LicenseResDto.LicenseCreateResDto toLicenseCreateResDto(License license) {
        return LicenseResDto.LicenseCreateResDto.builder()
                .licenseId(license.getId())
                .build();
    }
}
