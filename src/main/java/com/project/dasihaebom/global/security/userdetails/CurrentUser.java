package com.project.dasihaebom.global.security.userdetails;

import com.project.dasihaebom.domain.user.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
// 비밀번호를 제외한 객체로 비밀번호 외부 유출 최소화
public class CurrentUser {

    private final Long id;

    private final String loginId;

    private final Role role;
}
