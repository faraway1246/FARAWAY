package com.faraway.demo.consumer.common;

import java.io.Serializable;

/**
 * @author: 张峰玮
 * @since: 2022/8/21 23:50
 * @version: 1.0
 * @description:
 */
public class JsonResult<T> implements Serializable {
    private String msg;
    private String returnCode;
    private T data;

    public JsonResult() {
        this(GlobalReturnCodeEnum.OK);
    }

    public JsonResult(String returnCode, String msg, T data) {
        this.msg = msg;
        this.returnCode = returnCode;
        this.data = data;
    }

    public JsonResult(String returnCode, String msg) {
        this.msg = msg;
        this.returnCode = returnCode;
    }

    public JsonResult(GlobalReturnCodeEnum globalReturnCode) {
        this.returnCode = globalReturnCode.getReturnCode();
        this.msg = globalReturnCode.getMsg();
    }

    public JsonResult(GlobalReturnCodeEnum globalReturnCode, T data) {
        this.returnCode = globalReturnCode.getReturnCode();
        this.msg = globalReturnCode.getMsg();
        this.data = data;
    }

    public JsonResult(GlobalReturnCodeEnum globalReturnCode, String msg, T data) {
        this.returnCode = globalReturnCode.getReturnCode();
        this.msg = msg;
        this.data = data;
    }

    public boolean success() {
        return GlobalReturnCodeEnum.OK.getReturnCode().equals(this.returnCode);
    }

    public String toString() {
        return "JsonResult{msg='" + this.msg + '\'' + ", returnCode='" + this.returnCode + '\'' + ", data=" + this.data + '}';
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getReturnCode() {
        return this.returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
