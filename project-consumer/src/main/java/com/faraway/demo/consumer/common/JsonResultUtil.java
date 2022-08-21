package com.faraway.demo.consumer.common;

/**
 * @author: 张峰玮
 * @since: 2022/8/21 23:52
 * @version: 1.0
 * @description:
 */
public class JsonResultUtil {
    public JsonResultUtil() {
    }

    public static JsonResult ok() {
        return new JsonResult(GlobalReturnCodeEnum.OK);
    }

    public static <T> JsonResult ok(T t) {
        return new JsonResult(GlobalReturnCodeEnum.OK, t);
    }

    public static <T> JsonResult ok(String msg, T t) {
        return new JsonResult(GlobalReturnCodeEnum.OK, msg, t);
    }

    public static <T> JsonResult ok(String returnCode, String msg, T t) {
        return new JsonResult(returnCode, msg, t);
    }

    public static JsonResult serverError() {
        return new JsonResult(GlobalReturnCodeEnum.SERVER_ERROR);
    }

    public static JsonResult serverError(String msg) {
        return new JsonResult(GlobalReturnCodeEnum.SERVER_ERROR, msg);
    }

    public static JsonResult serverError(String returnCode, String msg) {
        return new JsonResult(returnCode, msg);
    }

    public static <E> JsonResult serverError(String returnCode, String msg, E e) {
        return new JsonResult(returnCode, msg, e);
    }

    public static JsonResult serverError(GlobalReturnCodeEnum globalReturnCode) {
        return new JsonResult(globalReturnCode);
    }

    public static JsonResult error() {
        return new JsonResult(GlobalReturnCodeEnum.BUSINESS_ERROR);
    }

    public static JsonResult error(String msg) {
        return new JsonResult(GlobalReturnCodeEnum.BUSINESS_ERROR, msg, (Object)null);
    }

    public static <E> JsonResult error(E e) {
        return new JsonResult(GlobalReturnCodeEnum.BUSINESS_ERROR, e);
    }

    public static <E> JsonResult<E> error(Exception exception) {
        return new JsonResult(GlobalReturnCodeEnum.BUSINESS_ERROR, exception.getMessage(), (Object)null);
    }

    public static <E> JsonResult<E> error(GlobalReturnCodeEnum globalReturnCode) {
        return new JsonResult(globalReturnCode);
    }

    public static <E> JsonResult<E> error(GlobalReturnCodeEnum globalReturnCode, E e) {
        return new JsonResult(globalReturnCode, e);
    }

    public static <E> JsonResult<E> error(String returnCode, Exception exception) {
        return new JsonResult(returnCode, exception.getMessage(), (Object)null);
    }

    public static <E> JsonResult<E> error(String msg, E e) {
        return new JsonResult(GlobalReturnCodeEnum.BUSINESS_ERROR, msg, e);
    }

    public static <E> JsonResult<E> error(String returnCode, String msg, E e) {
        return new JsonResult(returnCode, msg, e);
    }

    public static JsonResult error(String returnCode, String msg, Exception exception) {
        return new JsonResult(returnCode, msg, exception.getMessage());
    }
}
