package com.project.dasihaebom.domain.auth.converter;

import com.project.dasihaebom.domain.auth.entity.Auth;
import com.project.dasihaebom.domain.user.corp.entity.Corp;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthConverter {

    // Worker ========================================================
    public static Auth toWorkerAuth(String encodedPassword, Worker worker) {
        return Auth.builder()
                .password(encodedPassword)
                .isTemp(false)
                .worker(worker)
                .build();
    }

    // Corp ==========================================================
    public static Auth toCorpAuth(String encodedPassword, Corp corp) {
        return Auth.builder()
                .password(encodedPassword)
                .isTemp(false)
                .corp(corp)
                .build();
    }
}
