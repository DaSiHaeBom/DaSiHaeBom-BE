package com.project.dasihaebom.domain.license.service.query;

import com.project.dasihaebom.domain.license.dto.response.LicenseResDto;

public interface LicenseQueryService {

    LicenseResDto.LicenseDetailResDto getLicenseDetail(long licenseId, long workerId);

    LicenseResDto.LicenseListResDto getMyLicensesList(long workerId);
}
