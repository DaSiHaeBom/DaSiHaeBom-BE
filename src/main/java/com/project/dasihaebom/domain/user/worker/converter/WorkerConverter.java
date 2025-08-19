package com.project.dasihaebom.domain.user.worker.converter;

import com.project.dasihaebom.domain.location.entity.Coordinates;
import com.project.dasihaebom.domain.user.LoginType;
import com.project.dasihaebom.domain.user.Role;
import com.project.dasihaebom.domain.user.worker.dto.request.WorkerReqDto;
import com.project.dasihaebom.domain.user.worker.dto.response.WorkerResDto;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkerConverter {

    // [수정] 파라미터 변수명 변경 (List<Double> coordinates -> List<Double> workerCoordinatesAsList)
    public static Worker toWorker(WorkerReqDto.WorkerCreateReqDto workerCreateReqDto, List<Double> workerCoordinatesAsList) {

        // List<Double>을 Coordinates 객체로 변환하는 로직
        Coordinates coordinates = new Coordinates(workerCoordinatesAsList.get(1), workerCoordinatesAsList.get(0)); // 순서: 위도, 경도

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

    public static WorkerResDto.WorkerProfileResDto toWorkerProfileResDto(Worker worker) {
        return WorkerResDto.WorkerProfileResDto.builder()
                .role(worker.getRole())
                .phoneNumber(worker.getPhoneNumber())
                .username(worker.getUsername())
                .birthDate(worker.getBirthDate())
                .gender(worker.getGender())
                .address(worker.getAddress())
                .build();
    }
}
