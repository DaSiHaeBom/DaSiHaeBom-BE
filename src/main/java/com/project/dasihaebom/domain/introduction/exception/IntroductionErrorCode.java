package com.project.dasihaebom.domain.introduction.exception;

import com.project.dasihaebom.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum IntroductionErrorCode implements BaseErrorCode {
    // ========== Answer 관련 에러 ==========

    ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "INTRO_404_1", "해당하는 답변을 찾을 수 없습니다."),
    ANSWER_ALREADY_EXISTS(HttpStatus.CONFLICT, "INTRO_405_1", "이미 해당 질문에 대한 답변이 존재합니다."),
    QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "INTRO_404_2", "해당하는 질문을 찾을 수 없습니다.");

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
