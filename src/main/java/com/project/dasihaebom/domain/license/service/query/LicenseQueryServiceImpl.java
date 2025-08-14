package com.project.dasihaebom.domain.license.service.query;

import com.project.dasihaebom.domain.license.converter.LicenseConverter;
import com.project.dasihaebom.domain.license.dto.response.LicenseResDto;
import com.project.dasihaebom.domain.license.entity.License;
import com.project.dasihaebom.domain.license.exception.LicenseErrorCode;
import com.project.dasihaebom.domain.license.exception.LicenseException;
import com.project.dasihaebom.domain.license.repository.LicenseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LicenseQueryServiceImpl implements LicenseQueryService {

    private final LicenseRepository licenseRepository;

    @Override
    public LicenseResDto.LicenseDetailResDto getLicenseDetail(long licenseId, long workerId) {
        License license = licenseRepository.findById(licenseId)
                .orElseThrow(() -> new LicenseException(LicenseErrorCode.LICENSE_NOT_FOUND));

        if (!Objects.equals(license.getWorker().getId(), workerId)) {
            throw new LicenseException(LicenseErrorCode.LICENSE_ACCESS_DENIED);
        }

        return LicenseConverter.toLicenseDetailResDto(license);
    }

    @Override
    public LicenseResDto.LicenseListResDto getMyLicensesList(long workerId) {
        List<License> licenses = licenseRepository.findAllByWorkerId(workerId);

        List<LicenseResDto.LicenseDetailResDto> licenseDetailResDtoList = licenses.stream()
                .map(LicenseConverter::toLicenseDetailResDto)
                .toList();

        return LicenseConverter.toLicenseListResDto(licenseDetailResDtoList);
    }
}
