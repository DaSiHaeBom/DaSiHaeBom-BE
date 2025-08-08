package com.project.dasihaebom.global.constant.valid;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageConstants {

    // NOT BLANK
    public static final String USER_BLANK_PHONE_NUMBER = "전화번호 누락";
    public static final String USER_BLANK_PASSWORD = "비밀번호 누락";
    public static final String USER_BLANK_USERNAME = "이름 누락";
    public static final String USER_BLANK_BIRTHDATE = "나이 누락";
    public static final String USER_BLANK_GENDER = "성별 누락";
    public static final String USER_BLANK_ADDRESS = "주소 누락";

    // PATTERN EXCEPTION
    public static final String USER_WRONG_PHONE_NUMBER = "공백없는 숫자 11자리만 허용";
    public static final String USER_WRONG_PASSWORD = "8-12자까지 대/소문자+숫자+특수문자 허용, 사용불가 특수문자 : <, >, {, }, |, ;, ', \"";
    public static final String USER_WRONG_USERNAME = "2자리 이상의 한글만 허용";
    public static final String USER_WRONG_BIRTHDATE = "생년월일은 숫자 6자리만 허용";
}
