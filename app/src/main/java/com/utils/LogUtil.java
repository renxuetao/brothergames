package com.utils;

import android.util.Log;

public class LogUtil {

    /**
     * debug tag.
     */
    private static String STag = "tag";

    /**
     * debug sign.
     */
    private static boolean isDebug = false;

    public static void setTag(String tag) {
        STag = tag;
    }

    public static void isDebug(boolean debug) {
        isDebug = debug;
    }

    public static void i(String msg) {
        if (isDebug)
            Log.i(STag, msg);
    }

    public static void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void i(Throwable e) {
        if (isDebug)
            Log.i(STag, "", e);
    }

    public static void i(Throwable e, String msg) {
        if (isDebug)
            Log.i(STag, msg, e);
    }

    public static void v(String msg) {
        if (isDebug)
            Log.v(STag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            Log.v(tag, msg);
    }

    public static void v(Throwable e) {
        if (isDebug)
            Log.v(STag, "", e);
    }

    public static void v(Throwable e, String msg) {
        if (isDebug)
            Log.v(STag, msg, e);
    }

    public static void d(String msg) {
        if (isDebug)
            Log.d(STag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            Log.d(tag, msg);
    }

    public static void d(Throwable e) {
        if (isDebug)
            Log.d(STag, "", e);
    }

    public static void d(Throwable e, String msg) {
        if (isDebug)
            Log.d(STag, msg, e);
    }

    public static void e(String msg) {
        if (isDebug)
            Log.e(STag, msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug)
            Log.e(tag, msg);
    }

    public static void e(Throwable e) {
        if (isDebug)
            Log.e(STag, "", e);
    }

    public static void e(Throwable e, String msg) {
        if (isDebug)
            Log.e(STag, msg, e);
    }

    public static void w(String msg) {
        if (isDebug)
            Log.w(STag, msg);
    }

    public static void w(String tag, String msg) {
        if (isDebug)
            Log.w(tag, msg);
    }

    public static void w(Throwable e) {
        if (isDebug)
            Log.w(STag, "", e);
    }

    public static void w(Throwable e, String msg) {
        if (isDebug)
            Log.w(STag, msg, e);
    }

    public static void wtf(String msg) {
        if (isDebug)
            Log.wtf(STag, msg);
    }

    public static void wtf(String tag, String msg) {
        if (isDebug)
            Log.wtf(tag, msg);
    }

    public static void wtf(Throwable e) {
        if (isDebug)
            Log.wtf(STag, "", e);
    }

    public static void wtf(Throwable e, String msg) {
        if (isDebug)
            Log.wtf(STag, msg, e);
    }
}
