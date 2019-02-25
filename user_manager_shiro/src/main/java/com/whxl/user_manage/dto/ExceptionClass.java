package com.whxl.user_manage.dto;

/**
 * Created By 朱立松 on 2019/2/17
 * 异常信息类
 */
public class ExceptionClass {

    private Boolean suc;

    private String msg;

    private Exception exception;


    public ExceptionClass(Boolean suc, String msg, Exception exception) {
        this.suc = suc;
        this.msg = msg;
        this.exception = exception;
    }

    public Boolean getSuc() {
        return suc;
    }

    public void setSuc(Boolean suc) {
        this.suc = suc;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
