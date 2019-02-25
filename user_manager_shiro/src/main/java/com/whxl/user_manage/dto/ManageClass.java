package com.whxl.user_manage.dto;

/**
 * Created By 朱立松 on 2019/2/16
 */
public class ManageClass {

    private Boolean suc;

    private String msg;

    public ManageClass(Boolean suc, String msg) {
        this.suc = suc;
        this.msg = msg;
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
}
