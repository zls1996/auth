package com.whxl.user_manage.dao;

import com.whxl.user_manage.entity.Priviledge;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created By 朱立松 on 2019/1/18
 * 权限dao
 */
@Repository
@Mapper
//要加上@Mapper
public interface PriviledgeDao {
    List<String> getPriviledgesByRoleType(Integer roleType);
}
