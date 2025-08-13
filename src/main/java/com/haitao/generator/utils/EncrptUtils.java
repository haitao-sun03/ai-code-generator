package com.haitao.generator.utils;

import org.springframework.util.DigestUtils;

public class EncrptUtils {
    public static String getEncryptPassword(String userPassword) {
        // 盐值，混淆密码
        final String SALT = "xxx";
        return DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
    }

}
