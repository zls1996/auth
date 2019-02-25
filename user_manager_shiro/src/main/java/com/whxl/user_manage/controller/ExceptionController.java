package com.whxl.user_manage.controller;


import com.whxl.user_manage.dto.ExceptionClass;
import com.whxl.user_manage.dto.LoginClass;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.thymeleaf.exceptions.TemplateInputException;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;


/**
 * Created By 朱立松 on 2019/2/17
 * 异常处理类
 */
@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public ExceptionClass unauthorizedHandler(UnauthorizedException e){
        return new ExceptionClass(false, "授权错误", e);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthorizationException.class)
    @ResponseBody
    public ExceptionClass unauthorizedHandler(AuthorizationException e){
        return new ExceptionClass(false, "未授予权限，用户可能未登录或登录已过期", e);
    }

//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    public ExceptionClass exception(HttpServletRequest request, Exception e){
//        if(e instanceof UnauthorizedException){
//            return new ExceptionClass(false, "授权错误", e);
//        }
//        if(e instanceof TemplateInputException){
//            return new ExceptionClass(false, "当前角色不允许访问该接口", e);
//        }
//        if(e instanceof AuthorizationException){
//            return new ExceptionClass(false, "未授予权限，用户可能未登录或登录已过期", e);
//        }
//
//        return new ExceptionClass(false,"未知错误", e);
//    }




}
