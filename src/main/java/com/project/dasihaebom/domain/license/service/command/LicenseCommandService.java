package com.project.dasihaebom.domain.license.service.command;

import com.project.dasihaebom.domain.license.dto.request.LicenseReqDto;
import com.project.dasihaebom.domain.license.dto.response.LicenseResDto;

public interface LicenseCommandService {

    LicenseResDto.LicenseCreateResDto createLicense(LicenseReqDto.LicenseCreateReqDto licenseCreateReqDto, long workerId);

    void updateLicense(long licenseId, LicenseReqDto.LicenseUpdateReqDto licenseUpdateReqDto, long workerId);

    void deleteLicense(long licenseId, long workerId);
}
