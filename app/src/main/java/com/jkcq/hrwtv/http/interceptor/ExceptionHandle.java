package com.jkcq.hrwtv.http.interceptor;

import android.net.ParseException;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;

import retrofit2.HttpException;


public class ExceptionHandle {

    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static ResponeThrowable handleException(Throwable e) {
        ResponeThrowable ex;
        Log.e("MyLog", "错误信息==" + e.toString());
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponeThrowable(e, ERROR.HTTP_ERROR);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex.message = "网络错误";
                    break;
            }
            if (ex.message.contains("Unable to resolve host")) {
                ex.message = "please_check_that_your_network_is_connected";
            }
            return ex;
        } else if (e instanceof ServerException) {
            ServerException resultException = (ServerException) e;
            ex = new ResponeThrowable(resultException, resultException.code);
            ex.message = resultException.message;
            if (ex.message.contains("Unable to resolve host")) {
                ex.message = "please_check_that_your_network_is_connected";
            }

            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ResponeThrowable(e, ERROR.PARSE_ERROR);
            ex.message = "解析错误";

            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ResponeThrowable(e, ERROR.NETWORD_ERROR);
            ex.message = "连接失败";

            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ResponeThrowable(e, ERROR.SSL_ERROR);
            ex.message = "证书验证失败";

            return ex;
        } else if (e instanceof ConnectTimeoutException) {
            ex = new ResponeThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.message = "error_Connection_timeout";
            //新增 当服务器出问题时取消弹窗,提示用户

            return ex;
        } else if (e instanceof java.net.SocketTimeoutException) {
            ex = new ResponeThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.message = "error_Connection_timeout";
            //新增 当服务器出问题时取消弹窗,提示用户

            return ex;
        } else if (e instanceof ServerException) {
            String message = ((ServerException) e).message;
            ex = new ResponeThrowable(e, ERROR.UNKNOWN);

            if (!TextUtils.isEmpty(message)) {
                ex.message = message;
            } else {
                ex.message = "未知错误";
            }
            if (ex.message.contains("Unable to resolve host")) {
                ex.message = "common_please_check_that_your_network_is_connected";
            }

            return ex;
        } else {
            ex = new ResponeThrowable(e, ERROR.UNKNOWN);
            if (TextUtils.isEmpty(e.getMessage())) {
                ex.message = "未知错误";
            } else {
                ex.message = e.getMessage();
            }
            if (ex.message.contains("Unable to resolve host")) {
                ex.message = "common_please_check_that_your_network_is_connected";
            }
            if (ex.message.contains("没有访问权限！")) {
                ex.message = "";
            }
            Log.e("MyLog", "网络错误==" + ex.toString());
            return ex;
        }
    }


    /**
     * 约定异常
     */
    class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;

        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = 1006;
    }

    public static class ResponeThrowable extends Exception {
        @Override
        public String toString() {
            return "ResponeThrowable{" +
                    "code=" + code +
                    ", message='" + message + '\'' +
                    '}';
        }

        public int code;
        public String message;

        public ResponeThrowable(Throwable throwable, int code) {
            super(throwable);
            this.code = code;

        }
    }

    public class ServerException extends RuntimeException {

        public ServerException(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int code;
        public String message;
    }
}

