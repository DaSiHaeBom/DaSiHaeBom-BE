package com.project.dasihaebom.domain.resume.exception;

import com.project.dasihaebom.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResumeErrorCode implements BaseErrorCode {
    RESUME_NOT_FOUND(HttpStatus.NOT_FOUND, "RESUME_404_1", "해당 사용자의 이력서를 찾을 수 없습니다."),
    JSON_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "RESUME_500_1", "자격증 정보 처리 중 오류가 발생했습니다.")
    ;


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
