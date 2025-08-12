package com.project.dasihaebom.domain.user.worker.service.query;

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
    public Optional<Worker> findWorkerByLoginId(String loginId) {
        return workerRepository.findByPhoneNumber(loginId);
    }
}
