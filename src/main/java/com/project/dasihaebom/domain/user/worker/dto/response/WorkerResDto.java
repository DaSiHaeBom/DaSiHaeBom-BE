package com.project.dasihaebom.domain.user.worker.dto.response;

import com.project.dasihaebom.domain.user.Role;
import com.project.dasihaebom.domain.user.worker.entity.Gender;
import lombok.Builder;

public class WorkerResDto {

    @Builder
    public record WorkerProfileResDto(
            Role role,
            String phoneNumber,
            String username,
            String birthDate,
            Gender gender,
            String address
    ) {
    }
}
