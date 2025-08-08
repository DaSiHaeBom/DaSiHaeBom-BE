package com.project.dasihaebom.domain.auth.converter;

import com.project.dasihaebom.domain.auth.entity.WorkerAuth;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthConverter {

    public static WorkerAuth toWorkerAuth(String encodedPassword, Worker worker) {
        return WorkerAuth.builder()
                .password(encodedPassword)
                .isTemp(false)
                .worker(worker)
                .build();
    }
}
