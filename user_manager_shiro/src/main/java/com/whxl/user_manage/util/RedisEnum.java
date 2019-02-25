package com.whxl.user_manage.util;

/**
 * Created By 朱立松 on 2019/1/18
 * Redis的几个枚举名
 */
public enum RedisEnum {

    /**
     * 存储用户的hashmap
     * key：随机32位
     * value:管理员用户
     */
    USER_HASHMAP("user_hashmap");

    private String name;

    RedisEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}


