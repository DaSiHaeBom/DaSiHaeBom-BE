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

    public static final String USER_BLANK_LOGIN_ID = "아이디 누락";
    public static final String USER_BLANK_CEO_NAME = "대표자명 누락";
    public static final String USER_BLANK_CORP_NUMBER = "사업자 번호 누락";
    public static final String USER_BLANK_CORP_NAME = "회사/점포명 누락";

    public static final String USER_BLANK_CODE = "휴대폰 인증 코드 누락";

    // PATTERN EXCEPTION
    public static final String USER_WRONG_PHONE_NUMBER = "공백없는 숫자 11자리만 허용";
    public static final String USER_WRONG_PASSWORD = "공백없는 8-12자리의 대/소문자+숫자+특수문자만 허용, 사용불가 특수문자 : <, >, {, }, |, ;, ', \"";
    public static final String USER_WRONG_USERNAME = "공백없는 2자리 이상의 한글만 허용";
    public static final String USER_WRONG_BIRTHDATE = "공백없는 숫자 6자리만 허용";

    public static final String USER_WRONG_LOGIN_ID = "공백없는 5-16자리의 영문+숫자만 허용, 숫자로만 이루어진 아이디 사용 불가";
    public static final String USER_WRONG_CORP_NUMBER = "공백없는 숫자 10자리만 허용";

    public static final String USER_WRONG_CODE = "공백없는 숫자 6자리만 허용";

    // CORP NUMBER API MESSAGE
    public static final String CORP_NUMBER_IS_NOT_REGISTERED = "국세청에 등록되지 않은 사업자등록번호입니다.";
}
