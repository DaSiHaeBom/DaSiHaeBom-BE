package com.project.dasihaebom.domain.auth.converter;

import com.project.dasihaebom.domain.auth.entity.CorpAuth;
import com.project.dasihaebom.domain.auth.entity.WorkerAuth;
import com.project.dasihaebom.domain.user.corp.entity.Corp;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthConverter {

    // Worker ========================================================
    public static WorkerAuth toWorkerAuth(String encodedPassword, Worker worker) {
        return WorkerAuth.builder()
                .password(encodedPassword)
                .isTemp(false)
                .worker(worker)
                .build();
    }

    // Corp ==========================================================
    public static CorpAuth toCorpAuth(String encodedPassword, Corp corp) {
        return CorpAuth.builder()
                .password(encodedPassword)
                .isTemp(false)
                .corp(corp)
                .build();
    }
}
