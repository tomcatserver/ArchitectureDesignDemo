package com.example.webview.command;

public class WebConstants {
    public static final int FAILED = 0; // 失败
    public static final String JUMP_URL = "jump_url";

    public static class ERRORCODE {
        public static final int NO_METHOD = -1000;
        public static final int NO_AUTH = -1001;
        public static final int NO_LOGIN = -1002;
        public static final int ERROR_PARAM = -1003;
        public static final int ERROR_EXCEPTION = -1004;
    }

    public static class ERRORMESSAGE {
        public static final String NO_METHOD = "方法找不到";
        public static final String NO_AUTH = "方法权限不够";
        public static final String NO_LOGIN = "尚未登录";
        public static final String ERROR_PARAM = "参数错误";
        public static final String ERROR_EXCEPTION = "未知异常";
    }
}
