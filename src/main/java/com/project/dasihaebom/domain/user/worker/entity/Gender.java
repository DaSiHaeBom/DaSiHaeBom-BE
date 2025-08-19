package com.project.dasihaebom.domain.user.worker.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {

    MALE("남"),
    FEMALE("여");

    private final String description; // 남, 여 값을 저장할 필드
}
