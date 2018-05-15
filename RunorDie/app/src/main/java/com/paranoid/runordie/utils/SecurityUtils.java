package com.paranoid.runordie.utils;

import android.util.Base64;

public class SecurityUtils {

    private static final int FLAGS = Base64.DEFAULT;

    public static String encode(String src) {
        return new String(Base64.encode(src.getBytes(), FLAGS));
    }

    public static String decode(String src) {
        return (src != null) ? new String(Base64.decode(src, FLAGS)) : null;
    }
}
