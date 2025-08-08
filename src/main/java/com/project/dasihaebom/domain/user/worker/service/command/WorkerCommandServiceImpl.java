package com.project.dasihaebom.domain.user.worker.service.command;

import com.project.dasihaebom.domain.auth.service.command.AuthCommandService;
import com.project.dasihaebom.domain.user.worker.converter.WorkerConverter;
import com.project.dasihaebom.domain.user.worker.dto.request.WorkerReqDto;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import com.project.dasihaebom.domain.user.worker.repository.WorkerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class WorkerCommandServiceImpl implements WorkerCommandService {

    // Repo
    private final WorkerRepository workerRepository;
//    private final BCryptPasswordEncoder passwordEncoder;

    // Service
    private final AuthCommandService authCommandService;


    @Override
    public void createWorker(WorkerReqDto.WorkerCreateReqDto workerCreateReqDto) {
        Worker worker = WorkerConverter.toWorker(workerCreateReqDto);
        workerRepository.save(worker);

        authCommandService.savePassword(worker, encodePassword(workerCreateReqDto.password()));

        log.info("Worker created successfully");
    }

    // TODO : 일단 미구현
    private String encodePassword(String rawPassword) {
//        return passwordEncoder.encode(rawPassword);
        return rawPassword;
    }
}
