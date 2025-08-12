package com.project.dasihaebom.domain.user.worker.service.query;

import com.project.dasihaebom.domain.user.worker.entity.Worker;

import java.util.Optional;

public interface WorkerQueryService {

    Optional<Worker> findWorkerByLoginId(String loginId);
}
