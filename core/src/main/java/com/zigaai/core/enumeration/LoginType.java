package com.zigaai.core.enumeration;

import com.zigaai.core.exception.LoginIllegalArgumentException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 登录类型
 */
@Getter
@AllArgsConstructor
public enum LoginType {

    /**
     * 用户名密码
     */
    USERNAME_PASSWORD((byte) 1),

    /**
     * 手机号验证码
     */
    PHONE_VALID_CODE((byte) 2);

    private final byte val;

    private static final Map<Byte, LoginType> LOGIN_TYPE_MAP = Collections.unmodifiableMap(Arrays.stream(values()).collect(Collectors.toMap(LoginType::getVal, Function.identity())));

    public static LoginType getByVal(Byte val) {
        LoginType loginType = LOGIN_TYPE_MAP.get(val);
        if (loginType == null) {
            throw new LoginIllegalArgumentException("非法的登录类型");
        }
        return loginType;
    }
}
