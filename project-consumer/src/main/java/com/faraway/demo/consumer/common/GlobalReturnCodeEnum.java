package com.faraway.demo.consumer.common;

/**
 * @author: 张峰玮
 * @since: 2022/8/21 23:51
 * @version: 1.0
 * @description:
 */
public enum GlobalReturnCodeEnum {
    OK("00200", "OK"),
    BAD_REQUEST("00400", "无效的请求头"),
    NO_AUTHORIZATION("00401", "未授权"),
    URI_NOT_FOUND("00404", "请求路径不存在！"),
    REQUEST_METHOD_ERROR("00405", "非法请求方式"),
    JSON_ERROR("00420", "JSON格式错误"),
    PARAMS_ERROR("00421", "参数格式错误"),
    TOO_MANG_REQUESTS("00429", "请求限流中！"),
    SERVER_ERROR("00500", "服务器异常，请稍后重试！"),
    REQUEST_NO_BODY("00600", "请求缺失body"),
    REFERER_ERROR("00602", "B/S项目无效的referer"),
    BUSINESS_ERROR("00900", "操作失败！"),
    GATEWAY_TIME_OUT("00504", "网关超时，请重试一次!"),
    USER_LOGIN_FAIL("02002", "用户名或密码错误！"),
    INVALID_TOKEN("00601", "无效的TOKEN!"),
    NO_PERMISSION_TOKEN("00603", "TOKEN无对应权限!"),
    INVALID_APPID("00604", "APPID无效!"),
    NO_PREMISSION_BANAPPID("00605", "APPID已被禁用!"),
    NO_PREMISSION_OVERDUE("00606", "APPID已过期!"),
    NO_PERMISSION_URL("00607", "APPID无请求URL权限！"),
    INVALID_REFRESHTOKEN("00608", "无效的REFRESH_TOKEN!"),
    AUTH_ERROR("00610", "鉴权服务异常！"),
    IP_INVALID("00611", "请求ip不合法！"),
    EXPIRED("00612", "授权已到期"),
    DISABLED("00613", "授权已停用"),
    CODE_EXIST("25001", "模块代码已存在"),
    EXIST("00430", "已存在"),
    DATA_NOT_EXIST("00436", "数据不存在"),
    DATA_NOT_MATCHING("00437", "数据不匹配"),
    FILE_NOT_EXIST("00633", "文件不存在"),
    ILLEGAL_PARAMS("00421", "参数格式错误"),
    ILLEGAL_PARAMS_NULL("00422", "请求数据为空"),
    ILLEGAL_PARAMS_SUFFIX("00424", "上传文件仅支持格式为:'doc','docx','xls','xlsx','ppt','pptx','txt','zip','rar','pdf','txt','jpg','jepg','png'的文件"),
    ILLEGAL_PARAMS_LENGTH("00425", "文件大小不超过5M"),
    ILLEGAL_PARAMS_BASE64("00426", "无效的BASE64格式数据");

    final String returnCode;
    final String msg;

    public String getReturnCode() {
        return this.returnCode;
    }

    public String getMsg() {
        return this.msg;
    }

    private GlobalReturnCodeEnum(String returnCode, String msg) {
        this.returnCode = returnCode;
        this.msg = msg;
    }
}
