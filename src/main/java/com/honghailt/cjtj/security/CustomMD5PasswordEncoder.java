package com.honghailt.cjtj.security;

import com.honghailt.cjtj.utils.MD5Encrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 自定义密码加密器，适配原来的数据
 */
public class CustomMD5PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return MD5Encrypt.encrypt(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String aPassword = MD5Encrypt.encrypt(rawPassword.toString());
        return aPassword.equals(encodedPassword);
    }
}
