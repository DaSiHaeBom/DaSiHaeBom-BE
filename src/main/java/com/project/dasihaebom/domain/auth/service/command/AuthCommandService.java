package com.project.dasihaebom.domain.auth.service.command;

import com.project.dasihaebom.domain.auth.dto.request.AuthReqDto;

public interface AuthCommandService {

    void savePassword(Object user, String encodedPassword);

    void changePassword(AuthReqDto.AuthPasswordChangeReqDto authPasswordChangeReqDto, String loginId);

    void tempPassword(AuthReqDto.AuthTempPasswordReqDto authTempPasswordReqDto);
}
