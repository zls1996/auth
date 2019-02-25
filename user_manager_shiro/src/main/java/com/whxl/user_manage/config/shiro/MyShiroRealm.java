package com.whxl.user_manage.config.shiro;

import com.whxl.user_manage.entity.Role;
import com.whxl.user_manage.entity.User;

import com.whxl.user_manage.service.IPriviledgeService;
import com.whxl.user_manage.service.IUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created By 朱立松 on 2019/1/16
 * 自定义ShiroRealm
 */
public class MyShiroRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(MyShiroRealm.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private IPriviledgeService priviledgeService;

    /**
     * 验证当前的用户
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户的输入账号
        String username = (String) token.getPrincipal();
        User user = userService.getUserByName(username);

        if(user == null){
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getUsername(), user.getPassword(), getName()
        );
        return authenticationInfo;
    }

    /**
     * 为当前用户授予角色和权限
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取用户信息
        String username = (String) principalCollection.getPrimaryPrincipal();
        if(username == null){
            return null;
        }
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //根据用户名来获得用户
        User user = userService.getUserByName(username);
        //进行授权角色
        authorizationInfo.addRole(Role.getRoleById(user.getRoleType()).getName());
        //进行授权角色
        List<String> permissionList = priviledgeService.getPriviledgesByRoleType(user.getRoleType());
        //authorizationInfo.setStringPermissions(permissionList);
        logger.info("当前用户权限是："+ permissionList);
        authorizationInfo.addStringPermissions(permissionList);
        return authorizationInfo;
    }
}
