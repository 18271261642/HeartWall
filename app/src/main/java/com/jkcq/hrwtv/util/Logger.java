package com.jkcq.hrwtv.util;

import android.util.Log;



public class Logger {
    // public static int LOG_LEVEL = 0;
    public static int LOWEST_LOG_LEVEL = 0;
    private static int SYSTEM = 1;
    private static int VERBOS = 2;
    private static int DEBUG = 3;
    private static int INFO = 4;
    private static int WARN = 5;
    private static int ERROR = 6;

    public Logger() {
        //com.jkcq.ble.utils.Logger.isDebug = isDebuggable();
    }

    public static void i(String tag, String message) {
        if (message == null) {
            return;
        }
        if (LOWEST_LOG_LEVEL <= INFO) {
            Log.i(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (message == null) {
            return;
        }
        if (LOWEST_LOG_LEVEL <= ERROR) {
            Log.e(tag, message);
        }
    }

    public static void d(String tag, String message) {
        if (message == null) {
            return;
        }
        if (LOWEST_LOG_LEVEL <= DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (message == null) {
            return;
        }
        if (LOWEST_LOG_LEVEL <= WARN) {
            Log.w(tag, message);
        }
    }

    public static void v(String tag, String message) {
        if (message == null) {
            return;
        }
        if (LOWEST_LOG_LEVEL <= VERBOS) {
            Log.v(tag, message);
        }
    }

    public static void s(String message) {
        if (message == null) {
            return;
        }
        if (LOWEST_LOG_LEVEL <= SYSTEM) {
            System.out.println(message);
        }
    }

    static String className;
    static String methodName;
    static int lineNumber;

    public static boolean isDebuggable() {
        return true;
    }

    private static String createLog(String log) {
        return "[" + methodName + ":" + lineNumber + "]==" + log;
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    public static void e(String message) {
        if (!isDebuggable())
            return;

        // Throwable instance must be created before any methods
        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, createLog(message));
    }

    public static void i(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(message));
    }

    public static void d(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.d(className, createLog(message));
    }

    public static void v(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.v(className, createLog(message));
    }

    public static void w(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.w(className, createLog(message));
    }
}