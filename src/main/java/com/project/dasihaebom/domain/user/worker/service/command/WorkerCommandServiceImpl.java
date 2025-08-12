package com.project.dasihaebom.domain.user.worker.service.command;

import com.project.dasihaebom.domain.auth.service.command.AuthCommandService;
import com.project.dasihaebom.domain.user.worker.converter.WorkerConverter;
import com.project.dasihaebom.domain.user.worker.dto.request.WorkerReqDto;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import com.project.dasihaebom.domain.user.worker.exception.WorkerErrorCode;
import com.project.dasihaebom.domain.user.worker.exception.WorkerException;
import com.project.dasihaebom.domain.user.worker.repository.WorkerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.project.dasihaebom.global.util.UpdateUtils.updateIfChanged;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class WorkerCommandServiceImpl implements WorkerCommandService {

    // Repo
    private final WorkerRepository workerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Service
    private final AuthCommandService authCommandService;


    @Override
    public void createWorker(WorkerReqDto.WorkerCreateReqDto workerCreateReqDto) {
        Worker worker = WorkerConverter.toWorker(workerCreateReqDto);
        try {
            workerRepository.save(worker);
            authCommandService.savePassword(worker, encodePassword(workerCreateReqDto.password()));
        } catch (DataIntegrityViolationException e) {
            throw new WorkerException(WorkerErrorCode.WORKER_DUPLICATED);
        }

        log.info("Worker created successfully");
    }

    @Override
    public void updateWorker(WorkerReqDto.WorkerUpdateReqDto workerUpdateReqDto, long workerId) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new WorkerException(WorkerErrorCode.WORKER_NOT_FOUND));

        // findById로 가져온 객체는 영속성 컨텍스트 안이라서 더티채킹 어쩌고저쩌고쏼라쏼라
        updateIfChanged(workerUpdateReqDto.phoneNumber(), worker.getPhoneNumber(), worker::changePhoneNumber);
        updateIfChanged(workerUpdateReqDto.username(), worker.getUsername(), worker::changeUsername);
        updateIfChanged(workerUpdateReqDto.birthDate(), worker.getBirthDate(), worker::changeBirthDate);
        updateIfChanged(workerUpdateReqDto.address(), worker.getAddress(), worker::changeAddress);
    }

    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
