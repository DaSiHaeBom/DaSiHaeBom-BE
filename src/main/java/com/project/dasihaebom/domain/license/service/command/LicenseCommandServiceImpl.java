package com.project.dasihaebom.domain.license.service.command;

import com.project.dasihaebom.domain.license.converter.LicenseConverter;
import com.project.dasihaebom.domain.license.dto.request.LicenseReqDto;
import com.project.dasihaebom.domain.license.dto.response.LicenseResDto;
import com.project.dasihaebom.domain.license.entity.License;
import com.project.dasihaebom.domain.license.exception.LicenseErrorCode;
import com.project.dasihaebom.domain.license.exception.LicenseException;
import com.project.dasihaebom.domain.license.repository.LicenseRepository;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import com.project.dasihaebom.domain.user.worker.exception.WorkerErrorCode;
import com.project.dasihaebom.domain.user.worker.exception.WorkerException;
import com.project.dasihaebom.domain.user.worker.repository.WorkerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.project.dasihaebom.global.util.UpdateUtils.updateIfChanged;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LicenseCommandServiceImpl implements LicenseCommandService {

    private final LicenseRepository licenseRepository;
    private final WorkerRepository workerRepository;

    @Override
    public LicenseResDto.LicenseCreateResDto createLicense(LicenseReqDto.LicenseCreateReqDto licenseCreateReqDto, long workerId) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new WorkerException(WorkerErrorCode.WORKER_NOT_FOUND));

        License license = LicenseConverter.toLicense(licenseCreateReqDto, worker);

        licenseRepository.save(license);

        return LicenseConverter.toLicenseCreateResDto(license);
    }

    @Override
    public void updateLicense(long licenseId, LicenseReqDto.LicenseUpdateReqDto licenseUpdateReqDto, long workerId) {
        License license = licenseRepository.findById(licenseId)
                .orElseThrow(() -> new LicenseException(LicenseErrorCode.LICENSE_NOT_FOUND));

        if (!Objects.equals(license.getWorker().getId(), workerId)) {
            throw new LicenseException(LicenseErrorCode.LICENSE_ACCESS_DENIED);
        }

        updateIfChanged(licenseUpdateReqDto.name(), license.getName(), license::changeName);
        updateIfChanged(licenseUpdateReqDto.issuedAt(), license.getIssuedAt(), license::changeIssuedAt);
        updateIfChanged(licenseUpdateReqDto.issuer(), license.getIssuer(), license::changeIssuer);
    }

    @Override
    public void deleteLicense(long licenseId, long workerId) {
        License license = licenseRepository.findById(licenseId)
                .orElseThrow(() -> new LicenseException(LicenseErrorCode.LICENSE_NOT_FOUND));

        if (!Objects.equals(license.getWorker().getId(), workerId)) {
            throw new LicenseException(LicenseErrorCode.LICENSE_ACCESS_DENIED);
        }

        licenseRepository.deleteById(licenseId);
    }
}
