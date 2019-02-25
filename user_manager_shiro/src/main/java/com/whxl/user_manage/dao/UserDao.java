package com.whxl.user_manage.dao;

import com.whxl.user_manage.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
@Mapper
//要加上@Mapper
public interface UserDao {
    /**
     * 根据用户名寻找用户
     * @param username
     * @return
     */
    User getUserByUsername(String username);

    Integer insertUser(@RequestParam User user);

    User getUserById(Integer id);

    Integer deleteUserById(Integer id);

    List<User> getAllUser();

    Integer updateUser(User user);

    List<User> getUserByRole(@Param("roleType") Integer roleType);
}
