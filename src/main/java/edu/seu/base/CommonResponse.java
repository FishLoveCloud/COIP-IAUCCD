package edu.seu.base;

import com.alibaba.fastjson.JSON;

import java.util.Map;

public class CommonResponse {

    int code;
    String msg;
    Map<String, Object> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public CommonResponse() {
    }

    public CommonResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CommonResponse(int code, String msg, Map<String, Object> data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public String toJSONString() {
        return JSON.toJSONString(this);
    }
}
