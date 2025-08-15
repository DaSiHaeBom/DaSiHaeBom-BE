package com.project.dasihaebom.domain.user.worker.converter;

import com.project.dasihaebom.domain.user.LoginType;
import com.project.dasihaebom.domain.user.Role;
import com.project.dasihaebom.domain.user.worker.dto.request.WorkerReqDto;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkerConverter {

    public static Worker toWorker(WorkerReqDto.WorkerCreateReqDto workerCreateReqDto, List<Double> coordinates) {
        return Worker.builder()
                .phoneNumber(workerCreateReqDto.phoneNumber())
                .username(workerCreateReqDto.username())
                .birthDate(workerCreateReqDto.birthDate())
                .gender(workerCreateReqDto.gender())
                .address(workerCreateReqDto.address())
                .role(Role.WORKER)
                .loginType(LoginType.LOCAL)
                .coordinates(coordinates)
                .build();
    }
}
