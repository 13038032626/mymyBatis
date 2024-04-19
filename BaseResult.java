package com.example.demo;

import java.io.Serializable;

public class BaseResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private T data;
    private String code;
    private String msg;

    private BaseResult() {

    }

    public BaseResult(String code, String msg, T data) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }


    public BaseResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> BaseResult<T> getSuccess(T data) {
        return new BaseResult<T>(RespCode.SUCCESS.getCode(), RespCode.SUCCESS.getMsg(), data);
    }

    public static <T> BaseResult<T> getSuccess(T data, String msg) {
        return new BaseResult<T>(RespCode.SUCCESS.getCode(), msg, data);
    }
    public static <T> BaseResult<?> failBecauseOfDeinsert(T data){
        return BaseResult.newBuilder().code(
                        RespCode.DEINSERT_FAIL.getCode()).
                msg(RespCode.DEINSERT_FAIL.getMsg()).
                data(data).
                build();
    }

    public static <T> BaseResult<?> failBecauseOfParam(T data){
        return BaseResult.newBuilder().code(
                        RespCode.PARAM_FAIL.getCode()).
                msg(RespCode.PARAM_FAIL.getMsg()).
                data(data).
                build();
    }
    public static <T> BaseResult<?> failBecauseOfUnknown(T data){
        return BaseResult.newBuilder().code(
                        RespCode.UNKNOWN_FAIL.getCode()).
                msg(RespCode.UNKNOWN_FAIL.getMsg()).
                data(data).
                build();
    }

    public static <T> ResultBuilder<T> newBuilder() {
        return new ResultBuilder<>();
    }

    public static class ResultBuilder<T> {
        private T data;
        private String code;
        private String msg;


        public ResultBuilder<T> code(String code) {
            this.code = code;
            return this;
        }

        public ResultBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ResultBuilder<T> msg(String msg) {
            this.msg = msg;
            return this;
        }

        public BaseResult<T> build() {
            return new BaseResult<>(this.code, this.msg, this.data);
        }
    }

}
