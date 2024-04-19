package com.example.demo;

public enum RespCode {
    SUCCESS("200", "query success"),
    UNKNOWN_FAIL("500","未知异常"),
    PARAM_FAIL("501", "参数格式错误"),
    DEINSERT_FAIL("502", "检测到SQL注入");

    private String code;
    private String msg;


    RespCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
