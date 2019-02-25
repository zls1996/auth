package com.whxl.user_manage.entity;

/**
 * Created By 朱立松 on 2019/1/17
 * 角色的枚举类
 */
public enum Role {

    //超级管理员
    SUPER_ADMIN(1, "super_admin"),
    //系统管理员
    SYS_ADMIN(2, "sys_admin"),
    //应用管理员
    APP_ADMIN(3, "app_admin"),
    //设备管理员
    DEVICE_ADMIN(4, "device_admin");

    private Integer roleId;

    private  String name;

    Role(Integer roleId, String name) {
        this.roleId = roleId;
        this.name = name;
    }


    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Role getRoleById(Integer id) {
        if (id == 1) {
            return SUPER_ADMIN;
        }
        if (id == 2) {
            return SYS_ADMIN;
        }
        if (id == 3) {
            return APP_ADMIN;
        }

        return DEVICE_ADMIN;
    }

}














