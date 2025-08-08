package com.project.dasihaebom.global.constant.valid;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PatternConstants {

    // USER
    public static final String USER_PHONE_NUMBER_PATTERN = "^\\d{11}$";
    public static final String USER_PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^*()_+=\\-\\[\\],.?~])[A-Za-z\\d!@#$%^*()_+=\\-\\[\\],.?~]{8,12}$";
    public static final String USER_USERNAME_PATTERN = "^[가-힣]{2,}$";
    public static final String USER_BIRTHDATE_PATTERN = "^\\d{6}$";

    public static final String USER_LOGIN_ID_PATTERN = "^[a-zA-Z\\d]{5,16}$";
    public static final String USER_CORP_NUMBER_PATTERN = "^\\d{10}$";

}
