package com.whxl.user_manage.service.impl;

import com.whxl.user_manage.service.IRedisService;

import com.whxl.user_manage.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created By 朱立松 on 2019/1/18
 */
@Service
public class RedisServiceImpl implements IRedisService {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Boolean hasKey(String key) {
        return redisUtil.hasKey(key);
    }
}
