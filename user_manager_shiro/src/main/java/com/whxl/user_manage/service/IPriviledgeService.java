package com.whxl.user_manage.service;

import com.whxl.user_manage.entity.Priviledge;

import java.util.List;

/**
 * Created By 朱立松 on 2019/1/18
 * 权限操作接口
 */
public interface IPriviledgeService {

    /**
     * 根据用户角色得到角色权限
     * @param roleType
     * @return
     */
    List<String> getPriviledgesByRoleType(Integer roleType);
}
