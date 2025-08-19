package com.project.dasihaebom.domain.user.worker.service.query;

import com.project.dasihaebom.domain.user.Role;
import com.project.dasihaebom.domain.user.worker.converter.WorkerConverter;
import com.project.dasihaebom.domain.user.worker.dto.response.WorkerResDto;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import com.project.dasihaebom.domain.user.worker.exception.WorkerErrorCode;
import com.project.dasihaebom.domain.user.worker.exception.WorkerException;
import com.project.dasihaebom.domain.user.worker.repository.WorkerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class WorkerQueryServiceImpl implements WorkerQueryService {

    private final WorkerRepository workerRepository;

    @Override
    public WorkerResDto.WorkerProfileResDto getWorkerProfile(long workerId, Role role) {
        if (role == Role.CORP) {
            throw new WorkerException(WorkerErrorCode.ROLE_IS_NOT_WORKER);
        }

        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new WorkerException(WorkerErrorCode.WORKER_NOT_FOUND));

        return WorkerConverter.toWorkerProfileResDto(worker);
    }
}
