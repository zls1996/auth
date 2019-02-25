package com.whxl.user_manage.dto;

/**
 * Created By 朱立松 on 2019/1/19
 * 返回管理员管理状态
 */
public enum ManageState {

    INSERT_SUCCESS(true, "插入成功"),
    INSERT_FAILURE(false, "插入失败"),
    DELETE_SUCCESS(true, "删除成功"),
    DELETE_FAILURE(false, "删除失败"),
    UPDATE_SUCCESS(true, "修改成功"),
    UPDATE_FAILURE(false, "修改失败"),
    SELECT_SUCCESS(true, "查询成功"),
    SELECT_FAILURE(false, "查询失败"),
    UNAUTHORIZED_PRIVILEDGE(false, "未授权权限");

    private Boolean suc;

    private String msg;

    ManageState(Boolean suc, String msg) {
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
    }}
