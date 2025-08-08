package com.project.dasihaebom.domain.user.worker.dto.request;

import com.project.dasihaebom.domain.user.worker.entity.Gender;

public class WorkerReqDto {

    public record WorkerCreateReqDto(
            String phoneNumber,
            String password,
            String username,
            Integer age,
            Gender gender,
            String address
    ) {
    }

    public record WorkerUpdateReqDto(
            String phoneNumber,
            String username,
            Integer age,
            String address
    ) {
    }
}
