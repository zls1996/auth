package com.whxl.user_manage.controller;

import com.whxl.user_manage.dto.LoginClass;
import com.whxl.user_manage.dto.ManageClass;
import com.whxl.user_manage.dto.ManageState;
import com.whxl.user_manage.entity.User;
import com.whxl.user_manage.service.IRedisService;
import com.whxl.user_manage.service.IUserService;
import com.whxl.user_manage.dto.LoginState;
import com.whxl.user_manage.util.MD5Util;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;

import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created By 朱立松 on 2019/1/17
 */
@Controller
@RequestMapping("/usermanagement/v1")
public class ManagerController {


    @Autowired
    private IUserService userService;

    @Autowired
    private IRedisService redisService;


    @RequestMapping("get/{id}")
    public User getUser(@PathVariable("id") Integer id){
        return userService.getUserById(id);
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public String insertUser(User user){

        return userService.addUser(user);
    }

    /**
     * 登录页面
     * @return
     */
    @RequestMapping(value = "/log",method = RequestMethod.GET)
    public String login(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            //如果cookie中有已经生成的sid,返回首页
            if (cookie.getName().equals("sid")){
                //如果redis中含有对应的key就直接返回到首页
                if(redisService.hasKey(cookie.getValue())){
                    //从redis中获得用户名和密码
//                    String[] nameAndPass = userService.getUserNameAndPassword(cookie.getValue());
//
//                    UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(nameAndPass[0], nameAndPass[1]);
//                    //设置rememberme为true
//                    usernamePasswordToken.setRememberMe(true);
//                    Subject subject = SecurityUtils.getSubject();
//                    subject.login(usernamePasswordToken);
                    return "index";
                }
                return "login";
            }
        }
        return "login";

    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public LoginClass doLogin(HttpServletRequest request, String username, String passwd, HttpServletResponse response){

        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                //如果cookie中有已经生成的sid,返回首页
                if (cookie.getName().equals("sid")){
                    //如果redis中含有对应的key就直接返回到首页
                    if(redisService.hasKey(cookie.getValue())){
                        return new LoginClass(true, "该用户已登录");
                    }
                }
            }
        }


        LoginState state = userService.login(username, MD5Util.encode(passwd, "UTF-8"),  response);
        /**
         * 如果登陆成功,进行生成shiro用户
         */
        if(state == LoginState.CORRECT_INFO){
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, passwd);
            //设置rememberme为true
            usernamePasswordToken.setRememberMe(true);
            Subject subject = SecurityUtils.getSubject();
            subject.login(usernamePasswordToken);
            //设置过期时间为两分钟
            subject.getSession().setTimeout(1000*2*60);
        }

        LoginClass loginInfo = new LoginClass(state.getSuc(), state.getMsg());
        return loginInfo;
    }

    /**
     * 登出
     * @return
     */
    //@RequiresRoles({"super_admin", "sys_admin", "app_admin"})
//    @RequestMapping(value = "/leave")
//    @ResponseBody
//    public String doLogOut(HttpServletRequest request, HttpServletResponse response){
//        Subject subject = SecurityUtils.getSubject();
//        subject.logout();
//        userService.logout(request, response);
//        return "/login";
//    }

    /**
     * 实现用户的登出：暂时不要映射成/logout,有未知shiro bug
     * 需要超管或系管或应管的权限
     * @param request
     * @param response
     * @return
     */
    @RequiresRoles(value = {"super_admin", "sys_admin", "app_admin"}, logical = Logical.OR)
    @RequestMapping(value = "/leave", method = RequestMethod.DELETE)
    @ResponseBody
    public LoginClass leave(HttpServletRequest request, HttpServletResponse response){
        try{
            Subject subject = SecurityUtils.getSubject();
            if(subject != null){
                subject.logout();
                userService.logout(request, response);
                return new LoginClass(true, "登出成功！！！");
            }
            return new LoginClass(false, "登出失败：未登录");
        }catch (Exception e){
            return new LoginClass(false, "Unauthorized priviledge: logout");
        }

    }

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    //所需角色
    @RequiresRoles("super_admin")
    //所需权限
    @RequiresPermissions("create")
    @RequestMapping("/create")
    @ResponseBody
    public String create(){
        return "create Success";
    }


    /**
     * 错误界面
     * @return
     */
    @RequestMapping(value = "/error",method = RequestMethod.POST)
    public String error(){
        return "error";
    }


    /**
     * 创建管理员
     * @param username
     * @param passwd
     * @param nickname
     * @param createtype
     * @return
     */
    @RequiresRoles(value = {"sys_admin", "super_admin"}, logical = Logical.OR)
    @RequiresPermissions("create")
    @RequestMapping(value = "/manager", method = RequestMethod.PUT)
    @ResponseBody
    public ManageClass createManager(HttpServletRequest request, String username, String passwd, String nickname, String createtype){
        ManageState state = userService.createManager(request,username, passwd, nickname, createtype);
        ManageClass manageClass = new ManageClass(state.getSuc(), state.getMsg());
        return manageClass;
    }


    /**
     * 修改管理员
     * @param username
     * @param passwd
     * @param nickname
     * @param updatetype
     * @return
     */
    @RequiresRoles(value = {"sys_admin", "super_admin"}, logical = Logical.OR)
    @RequiresPermissions("update")
    @RequestMapping(value = "/manager", method = RequestMethod.POST)
    @ResponseBody
    public ManageClass updateManager(HttpServletRequest request,  String username, String passwd, String nickname, String updatetype){
        ManageState state = userService.updateManager(request,username,
                MD5Util.encode(passwd, "UTF-8"), nickname, updatetype);
        ManageClass manageClass = new ManageClass(state.getSuc(), state.getMsg());
        return manageClass;
    }


    /**
     * 删除管理员
     * @param username
     * @param request
     * @param username:用户名
     * @return
     */
    @RequiresRoles(value = {"sys_admin", "super_admin"}, logical = Logical.OR)
    @RequiresPermissions("delete")
    @RequestMapping(value = "/manager", method = RequestMethod.DELETE)
    @ResponseBody
    public ManageClass deleteManager(HttpServletRequest request,  String username){
        ManageState state = userService.deleteManager(request,username);
        ManageClass manageClass = new ManageClass(state.getSuc(), state.getMsg());
        return manageClass;
    }


    /**
     * 查询管理员
     * @param request
     * @return
     */
    @RequiresRoles(value = {"sys_admin", "super_admin"}, logical = Logical.OR)
    @RequiresPermissions("select")
    @RequestMapping(value = "/manager", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getManager(HttpServletRequest request){
        List<User> manageList = userService.selectManager(request);
        return manageList;
    }

    @RequiresPermissions("select")
    @RequiresRoles(value = {"sys_admin", "super_admin"}, logical = Logical.OR)
    @RequestMapping(value = "/currentuser", method = RequestMethod.GET)
    @ResponseBody
    public User getLoginUsers(HttpServletRequest request){
        User user= userService.getCurrentLoginUser(request);
        return user;
    }









}
