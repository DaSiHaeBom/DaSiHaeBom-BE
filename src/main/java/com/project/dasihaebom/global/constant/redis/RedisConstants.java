package com.project.dasihaebom.global.constant.redis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisConstants {

    // --------KEY--------

    // PHONE NUMBER VALIDATION
    public static final String KEY_CODE_SUFFIX = ":code";
    public static final String KEY_COOLDOWN_SUFFIX = ":cooldown";

    // LOGOUT
    public static final String KEY_ACCESS_TOKEN_SUFFIX = ":Alogout";
    public static final String KEY_REFRESH_TOKEN_SUFFIX = ":RToken";

    // SCOPE
    public static final String KEY_SCOPE_SUFFIX = ":scope";

    // -------VALUE-------

    // PHONE NUMBER VALIDATION
    public static final String VALUE_COOLDOWN = "cooldown...";

    // -------TIME--------

    // PHONE NUMBER VALIDATION
    public static final long CODE_EXP_TIME = 300000L;
    public static final long COOLDOWN_EXP_TIME = 60000L;

    // SCOPE
    public static final long SCOPE_EXP_TIME = 900000L;
}
