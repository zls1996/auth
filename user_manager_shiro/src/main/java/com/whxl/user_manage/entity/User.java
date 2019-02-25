package com.whxl.user_manage.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Set;

/**
 * User类
 */
public class User implements Serializable {
    private static final long serialVersionUID = -436276783478927L;

    private Integer id;
    //用户名
    private String username;
    //昵称
    private String nickname;

    //密码
    @JsonIgnore
    private String password;
    //性别
    private int sex;
    //角色类型
    private Integer roleType;

    //由谁创建
    private Integer createBy;

    public User() {
    }

    public User(String username,String nickname,  String password, int sex, Integer roleType, Integer createBy) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.sex = sex;
        this.roleType = roleType;
        this.createBy = createBy;
    }

    public Integer getId() {
        return id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }


    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", sex=" + sex +
                ", roleType=" + roleType +
                ", createBy=" + createBy +
                '}';
    }
}
