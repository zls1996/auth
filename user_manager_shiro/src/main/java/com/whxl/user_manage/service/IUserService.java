package com.whxl.user_manage.service;

import com.whxl.user_manage.dto.ManageState;
import com.whxl.user_manage.entity.User;
import com.whxl.user_manage.dto.LoginState;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IUserService {
    public User getUserById(Integer id);

    public List<User> getAllUser();

    public String addUser(User user);

    public String deleteUserById(Integer id);

    String deleteUserByName(String username);

    /**
     * 登录模块
     * @param username
     * @param password
     * @param type
     * @param response
     * @return
     */
    public LoginState login(String username, String password, HttpServletResponse response);

    void logout(HttpServletRequest request, HttpServletResponse response);

    /**
     * 更新用户
     */
    public String updateUser(User user);

    public void updateUserById(Integer id);

    User getUserByName(String username);

    String[] getUserNameAndPassword(String sid);

    /**
     * 得到当前user
     * @param request
     * @return
     */
    User getCurrentLoginUser(HttpServletRequest request);

    /**
     * 创建管理员
     *
     * @param request
     * @param username
     * @param passwd
     * @param nickname
     * @param createtype
     * @return
     */
    ManageState createManager(HttpServletRequest request, String username, String passwd, String nickname, String createtype);

    ManageState updateManager(HttpServletRequest request, String username, String passwd, String nickname, String createtype);

    ManageState deleteManager(HttpServletRequest request, String username);

    List<User> selectManager(HttpServletRequest request);

    /**
     * 根据权限来获得对应用户列表
     * @param roleType
     * @return
     */
    List<User> getUserByRole(Integer roleType);
}
