package com.project.dasihaebom.domain.auth.dto.request;

public class AuthReqDto {

    public record AuthLoginDto(
            String loginId,
            String password
    ) {
    }
}
