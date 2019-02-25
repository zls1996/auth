package com.whxl.user_manage.service.impl;


import com.whxl.user_manage.dao.UserDao;
import com.whxl.user_manage.dto.LoginState;
import com.whxl.user_manage.dto.ManageState;
import com.whxl.user_manage.entity.Role;
import com.whxl.user_manage.entity.User;
import com.whxl.user_manage.service.IUserService;
import com.whxl.user_manage.util.CookieUtil;
import com.whxl.user_manage.util.MD5Util;
import com.whxl.user_manage.util.RedisUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    private static final String CHARSET = "UTF-8";

    //Token的管理
//    @Autowired
//    private TokenManager tokenManager;

    @Autowired
    private RedisUtil redisUtil;


    @Autowired
    private UserDao userDao;

    @Override
    public User getUserById(Integer id) {
        return userDao.getUserById(id);
    }

    /**
     * 得到全部用户
     * @return
     */
    @Override
    public List<User> getAllUser() {
        return userDao.getAllUser();
    }

    @Override
    public List<User> getUserByRole(Integer roleType) {
        return userDao.getUserByRole(roleType);
    }

    /**
     * 增加用户
     * @param user
     * @return
     */
    @Override
    public String addUser(User user) {
        //先将前端传过来的密码加密，再进行插入
        user.setPassword(MD5Util.encode(user.getPassword(), "UTF-8"));
       Integer tag = userDao.insertUser(user);
       return (tag > 0)? "插入成功" : "插入失败";
    }

    /**
     * 根据用户名来删除用户
     * @param username
     * @return
     */
    @Override
    public String deleteUserByName(String username) {
        User user = getUserByName(username);
        return deleteUserById(user.getId());
    }

    /**
     * 根据id删除用户
     * @param id
     * @return
     */
    @Override
    public String deleteUserById(Integer id) {
        Integer tag = userDao.deleteUserById(id);
        return (tag > 0)? "删除成功" : "删除失败";
    }

    /**
     * 登录操作
     * @param username
     * @param password
     * @param type
     * @return
     */
    @Override
    public LoginState login(String username, String password, HttpServletResponse response) {
        User user = userDao.getUserByUsername(username);
        //用户为空，表示没有对应用户
        if(user == null){
            return LoginState.NO_SUCH_USER;
        }
        //密码不匹配，则输出密码错误
        if(!user.getPassword().equals(password)){
            return LoginState.WRONG_LOGIN_INFO;
        }

        if(user.getRoleType() != 1 && user.getRoleType() != 2){
            return LoginState.WRONG_MANAGER_TYPE;
        }

//        if(user.getRoleType() != type){
//            //如果是超级管理员选择以系统管理员登录
//            if(user.getRoleType() == 1 && type == 2){
//                //继续操作
//            }else{
//                //否则返回错误管理员类型
//                return LoginState.WRONG_MANAGER_TYPE;
//            }
//        }

        //为用户生成cookie，返回一个唯一uniqueStr
        String uniqueStr = CookieUtil.generateCookieForUser(response);
        //将随机生成的uniqueStr和用户放入到redis的hash表中
        redisUtil.set(uniqueStr, user);

        //用户成功登录，生成一个Token
        //TokenEntity tokenEntity = tokenManager.createToken(user.getId());
        //返回正确信息
        return LoginState.CORRECT_INFO;
    }

    @Override
    public String updateUser(User user) {
        Integer rows = userDao.updateUser(user);
        return (rows > 0) ? "修改成功":"修改失败";
    }

    @Override
    public void updateUserById(Integer id) {
        updateUser(getUserById(id));
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String sid = null;
        for(Cookie cookie: cookies){
            if(cookie.getName().equals("sid")){
                sid = cookie.getValue();
                //设置生存时间为0
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        //如果sid不为空，则将sid从redis中删除
        if(sid != null){

            redisUtil.del(sid);
            //删除用户在缓存中存储的权限
            redisUtil.del("user:priviledges");
        }

    }

    @Override
    public User getUserByName(String username) {
        return userDao.getUserByUsername(username);
    }

    @Override
    public String[] getUserNameAndPassword(String sid) {
        User user = (User) redisUtil.get(sid);
        String[] results = new String[2];
        results[0] = user.getUsername();
        results[1] = user.getPassword();
        return results;
    }

    /**
     * 得到当前user
     * @return
     */
    @Override
    public User getCurrentLoginUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            return null;
        }
        for(Cookie cookie: cookies){
            if(cookie.getName().equals("sid")){
                String key = cookie.getValue();
                User user = (User) redisUtil.get(key);
                return user;
            }
        }
        return null;
    }

    @Override
    public ManageState createManager(HttpServletRequest request, String username,
                                     String passwd, String nickname, String createtype) {
        //得到当前user
        User user = getCurrentLoginUser(request);
        if(user == null){
            return null;
        }
        //获得要插入用户的管理类型
        Integer roleType = getRoleType(createtype);

        //如果要插入super管理员，返回没有该权限
        if(roleType == Role.SUPER_ADMIN.getRoleId()){
            return ManageState.UNAUTHORIZED_PRIVILEDGE;
        }

        //如果当前用户是超级管理员
        if(user.getRoleType() == Role.SUPER_ADMIN.getRoleId()){
            //该newUser可是sys/app/device中的一种
            User newUser = new User(username,nickname, passwd, 0, roleType, user.getId());
            //插入用户
            String message = addUser(newUser);
            if(message.equals(ManageState.INSERT_SUCCESS.getMsg())){
                //插入成功
                return ManageState.INSERT_SUCCESS;
            }
            //插入失败
            return ManageState.INSERT_FAILURE;
        }
        //如果当前管理员是app或者device管理员
        if(user.getRoleType() == Role.APP_ADMIN.getRoleId() ||
            user.getRoleType() == Role.DEVICE_ADMIN.getRoleId()){
            return ManageState.UNAUTHORIZED_PRIVILEDGE;
        }

        //如果当前要创建的管理员类型是sys,而此时user也是sys，返回UNAUTHORIZED_PRIVILEDGE
        if(roleType == Role.SYS_ADMIN.getRoleId()){
            return ManageState.UNAUTHORIZED_PRIVILEDGE;
        }

        //剩下的是sys创建app/device
        User newUser = new User(username,nickname, passwd, 0, roleType, user.getId());
        //插入用户
        String message = addUser(newUser);
        if(message.equals(ManageState.INSERT_SUCCESS.getMsg())){
            //插入成功
            return ManageState.INSERT_SUCCESS;
        }
        //插入失败
        return ManageState.INSERT_FAILURE;


    }

    /**
     * 根据创建角色类型来得到roleType
     * @param createtype:sys/app/device
     * @return
     */
    private Integer getRoleType(String createtype) {
        if(createtype.equals("sys")){
            return 2;
        }
        if(createtype.equals("app")){
            return 3;
        }
        if(createtype.equals("device")){
            return 4;
        }
        return  1;
    }

    @Override
    public ManageState updateManager(HttpServletRequest request, String username, String passwd, String nickname, String updatetype) {
        //得到当前user
        User user = getCurrentLoginUser(request);
        if(user == null){
            return null;
        }
        //获得要修改用户的管理类型
        Integer roleType = getRoleType(updatetype);

        //如果要修改为super或device管理员，返回没有该权限
        if(roleType == Role.SUPER_ADMIN.getRoleId() || roleType == Role.DEVICE_ADMIN.getRoleId()){
            return ManageState.UNAUTHORIZED_PRIVILEDGE;
        }

        //如果当前用户是超级管理员
        if(user.getRoleType() == Role.SUPER_ADMIN.getRoleId()){
            User newUser = getUserByName(username);
            newUser.setUsername(username);
            newUser.setPassword(passwd);
            newUser.setNickname(nickname);
            newUser.setRoleType(roleType);
            //update
            String message = updateUser(newUser);
            if(message.equals(ManageState.UPDATE_SUCCESS.getMsg())){
                //修改成功
                return ManageState.UPDATE_SUCCESS;
            }
            //修改失败
            return ManageState.UPDATE_FAILURE;
        }
        //如果当前管理员是app或者device管理员
        if(user.getRoleType() == Role.APP_ADMIN.getRoleId() ||
                user.getRoleType() == Role.DEVICE_ADMIN.getRoleId()){
            return ManageState.UNAUTHORIZED_PRIVILEDGE;
        }

        //如果当前要修改的管理员类型是sys,而此时user也是sys，返回UNAUTHORIZED_PRIVILEDGE
        if(roleType == Role.SYS_ADMIN.getRoleId()){
            return ManageState.UNAUTHORIZED_PRIVILEDGE;
        }

        //剩下的是sys修改app/device
        User newUser = getUserByName(username);
        newUser.setUsername(username);
        newUser.setPassword(passwd);
        newUser.setNickname(nickname);
        newUser.setRoleType(roleType);
        //update
        String message = updateUser(newUser);
        if(message.equals(ManageState.UPDATE_SUCCESS.getMsg())){
            //修改成功
            return ManageState.UPDATE_SUCCESS;
        }
        //修改失败
        return ManageState.UPDATE_FAILURE;
    }

    /**
     * 删除用户
     * @param request
     * @param username
     * @return
     */
    @Override
    public ManageState deleteManager(HttpServletRequest request, String username) {
        //得到当前user
        User user = getCurrentLoginUser(request);
        if(user == null){
            return null;
        }

        /**
         * 如果用户要删除自己，返回权限不允许
         */
        if(user.getUsername().equals(username)){
            return ManageState.UNAUTHORIZED_PRIVILEDGE;
        }
        //如果当前用户是超级管理员
        if(user.getRoleType() == Role.SUPER_ADMIN.getRoleId()){
            String message = deleteUserByName(username);
            if(message.equals(ManageState.DELETE_SUCCESS.getMsg())){
                return ManageState.DELETE_SUCCESS;
            }
            return ManageState.DELETE_FAILURE;
        }
        //如果当前管理员是app或者device管理员
        if(user.getRoleType() == Role.APP_ADMIN.getRoleId() ||
                user.getRoleType() == Role.DEVICE_ADMIN.getRoleId()){
            return ManageState.UNAUTHORIZED_PRIVILEDGE;
        }

        //如果当前要删除的管理员类型是sys,而此时user也是sys，返回UNAUTHORIZED_PRIVILEDGE
        if(user.getRoleType() == Role.SYS_ADMIN.getRoleId()){
            return ManageState.UNAUTHORIZED_PRIVILEDGE;
        }

        //剩下的是sys删除app/device
        String message = deleteUserByName(username);
        if(message.equals(ManageState.DELETE_SUCCESS.getMsg())){
            return ManageState.DELETE_SUCCESS;
        }
        return ManageState.DELETE_FAILURE;
    }

    /**
     * 查询得到所有用户
     * @param request
     * @return
     */
    @Override
    public List<User> selectManager(HttpServletRequest request) {
        //得到当前user
        User user = getCurrentLoginUser(request);
        if(user == null){
            return null;
        }
        //为超级管理员
        if(user.getRoleType() == Role.SUPER_ADMIN.getRoleId()){
            return getAllUser();
        }
        //为sys管理员
        if(user.getRoleType() == Role.SYS_ADMIN.getRoleId()){
            return getUserByRole(user.getRoleType());
        }

        //其他管理员，返回null
        return null;

    }
}