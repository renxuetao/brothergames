package com.utils;

/**
 * Created by g on 2016/3/22.
 */
public class JniUtils {

    static {
        System.loadLibrary("read");
    }

    public static native String getKey();
}
