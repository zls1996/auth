package com.whxl.user_manage.service.impl;

import com.whxl.user_manage.dao.PriviledgeDao;
import com.whxl.user_manage.entity.Priviledge;
import com.whxl.user_manage.service.IPriviledgeService;
import com.whxl.user_manage.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created By 朱立松 on 2019/1/18
 */
@Service
public class PriviledgeService implements IPriviledgeService {

    @Autowired
    private PriviledgeDao priviledgeDao;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<String> getPriviledgesByRoleType(Integer roleType) {
        List<String> priviledges = (List<String>) redisUtil.get("user:priviledges");
        if(priviledges == null){
            priviledges = priviledgeDao.getPriviledgesByRoleType(roleType);
            redisUtil.set("roletype:"+roleType+":priviledges", priviledges);
        }
        return priviledges;
    }
}
