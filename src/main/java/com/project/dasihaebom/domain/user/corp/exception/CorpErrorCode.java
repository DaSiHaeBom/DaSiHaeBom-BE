package com.project.dasihaebom.domain.user.corp.exception;

import com.project.dasihaebom.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CorpErrorCode implements BaseErrorCode {
    // ErrorCode
    CORP_NOT_FOUND(HttpStatus.NOT_FOUND, "CORP404", "회원을 찾을 수 없습니다."),
    CORP_NUMBER_API_ERROR(HttpStatus.BAD_GATEWAY, "CORP502","사업자 번호 검증 API 오류"),
    CORP_DUPLICATED(HttpStatus.CONFLICT, "CORP403", "이미 가입된 사업자 번호 또는 사용자입니다."),
    SIGN_UP_PHONE_VALIDATION_DOES_NOT_EXIST(HttpStatus.UNAUTHORIZED, "CORP401", "회원 가입 휴대폰 인증을 시도하지 않았거나 변조되었거나 만료되었습니다."),
    PROFILE_PHONE_VALIDATION_DOES_NOT_EXIST(HttpStatus.UNAUTHORIZED, "CORP401", "회원 가입 휴대폰 인증을 시도하지 않았거나 변조되었거나 만료되었습니다."),
    CORP_VALIDATION_FAILURE(HttpStatus.UNAUTHORIZED, "CORP401_2", "사업자 등록 조회 인증을 시도하지 않았거나 변조되었거나 만료되었습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
