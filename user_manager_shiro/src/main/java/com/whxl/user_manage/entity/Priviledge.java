package com.whxl.user_manage.entity;

import java.io.Serializable;

/**
 * Created By 朱立松 on 2019/1/17
 * 对应权限实体表
 */
public class Priviledge implements Serializable {
    private Integer id;

    //权限名
    private String priviName;

    //对应接口路径
    private String interPath;

    //对应角色类型
    private String roleType;

    public Integer getId() {
        return id;
    }


    public String getPriviName() {
        return priviName;
    }

    public void setPriviName(String priviName) {
        this.priviName = priviName;
    }

    public String getInterPath() {
        return interPath;
    }

    public void setInterPath(String interPath) {
        this.interPath = interPath;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public Priviledge(String priviName, String interPath, String roleType) {
        this.priviName = priviName;
        this.interPath = interPath;
        this.roleType = roleType;
    }

    public Priviledge() {

    }
}
