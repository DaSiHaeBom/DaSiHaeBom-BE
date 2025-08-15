package com.project.dasihaebom.domain.user.worker.service.command;

import com.project.dasihaebom.domain.user.Role;
import com.project.dasihaebom.domain.user.worker.dto.request.WorkerReqDto;

public interface WorkerCommandService {

    void createWorker(WorkerReqDto.WorkerCreateReqDto workerCreateReqDto);

    void updateWorker(WorkerReqDto.WorkerUpdateReqDto workerUpdateReqDto, long workerId);

    void deleteUser(long userId, Role role, String accessToken, String refreshToken);
}
