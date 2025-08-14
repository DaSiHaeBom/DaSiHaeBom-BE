package com.project.dasihaebom.domain.license.service.command;

import com.project.dasihaebom.domain.license.converter.LicenseConverter;
import com.project.dasihaebom.domain.license.dto.request.LicenseReqDto;
import com.project.dasihaebom.domain.license.dto.response.LicenseResDto;
import com.project.dasihaebom.domain.license.entity.License;
import com.project.dasihaebom.domain.license.repository.LicenseRepository;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import com.project.dasihaebom.domain.user.worker.exception.WorkerErrorCode;
import com.project.dasihaebom.domain.user.worker.exception.WorkerException;
import com.project.dasihaebom.domain.user.worker.repository.WorkerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

        License license = LicenseConverter.toLicense(licenseCreateReqDto);

        licenseRepository.save(license);

        return LicenseConverter.toLicenseCreateResDto(license);
    }
}
