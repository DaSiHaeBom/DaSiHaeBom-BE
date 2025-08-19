package com.project.dasihaebom.domain.user.worker.service.query;

import com.project.dasihaebom.domain.user.Role;
import com.project.dasihaebom.domain.user.worker.dto.response.WorkerResDto;
import com.project.dasihaebom.domain.user.worker.entity.Worker;

import java.util.Optional;

public interface WorkerQueryService {

    WorkerResDto.WorkerProfileResDto getWorkerProfile(long workerId, Role role);
}
