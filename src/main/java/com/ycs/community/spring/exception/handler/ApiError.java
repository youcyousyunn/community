package com.ycs.community.spring.exception.handler;

public class ApiError {
    private Integer status;
    private String code;
    private String msg;
    private long time;

    public ApiError(Integer status, String msg) {
        super();
        this.status = status;
        this.msg = msg;
    }

    public ApiError(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "status=" + status +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", time=" + time +
                '}';
    }
}
