package com.okjiaoyu.jmeter.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5Util
 */
public class MD5Util {
    private static MessageDigest md;
    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();
    private static final String SALT = "okay";
    private static final String ALGORITH_NAME = "md5";
    private static final int HASH_ITERATIONS = 2;

    static {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String MD5_16bit(String input) {
        String hash = MD5_32bit(input);
        if (hash == null) {
            return null;
        }
        return hash.substring(8, 24);
    }

    public static String MD5_32bit(String input) {
        if (input == null || input.length() == 0) {
            return null;
        }
        md.update(input.getBytes());
        byte[] digest = md.digest();
        String hash = convertToString(digest);
        return hash;
    }

    private static String convertToString(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }
    public static String encrypt(String pswd) {
        String newPassword = new SimpleHash(ALGORITH_NAME, pswd, ByteSource.Util.bytes(SALT), HASH_ITERATIONS).toHex();
        return newPassword;
    }

    public static String encrypt(String username, String pswd) {
        String newPassword = new SimpleHash(ALGORITH_NAME, pswd, ByteSource.Util.bytes(username + SALT),
                HASH_ITERATIONS).toHex();
        return newPassword;
    }

    public static void main(String[] args) {
        System.out.println(MD5_16bit("fwjioejfiowejfiowjfiwfjowejfei"));
        System.out.println(MD5Util.encrypt("zhangchaoying", "123456"));
    }
}
