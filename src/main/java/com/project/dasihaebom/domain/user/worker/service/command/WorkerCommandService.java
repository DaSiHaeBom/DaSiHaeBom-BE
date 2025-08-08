package com.project.dasihaebom.domain.user.worker.service.command;

import com.project.dasihaebom.domain.user.worker.dto.request.WorkerReqDto;

public interface WorkerCommandService {

    void createWorker(WorkerReqDto.WorkerCreateReqDto workerCreateReqDto);

    void updateWorker(WorkerReqDto.WorkerUpdateReqDto workerUpdateReqDto);
}
