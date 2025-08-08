package com.project.dasihaebom.domain.auth.service.command;

import com.project.dasihaebom.domain.auth.converter.AuthConverter;
import com.project.dasihaebom.domain.auth.entity.WorkerAuth;
import com.project.dasihaebom.domain.auth.repository.WorkerAuthRepository;
import com.project.dasihaebom.domain.user.corp.entity.Corp;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthCommandServiceImpl implements AuthCommandService {

    private final WorkerAuthRepository workerAuthRepository;

    @Override
    public void savePassword(Object user, String encodedPassword) {

        // 지금 비밀번호를 저장할 객체가 Worker 인가?
        if (user instanceof Worker worker) {
            WorkerAuth workerAuth = AuthConverter.toWorkerAuth(encodedPassword, worker);
            workerAuthRepository.save(workerAuth);
        }
        if (user instanceof Corp) {
            return;
        }
    }
}
