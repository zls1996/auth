package com.whxl.user_manage.config.shiro;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created By 朱立松 on 2019/1/19
 * 通过过滤器设置shiroSession的过期时间
 */
public class ShiroSessionFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(ShiroSessionFilter.class);

    public List<String> excludes = new ArrayList<String>();

    //2分钟
    private long serverSessionTimeout = 120000L;//ms

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(logger.isDebugEnabled()){
            logger.debug("shiro session filter is open");
        }

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if(handleExcludeURL(request, response)){
            filterChain.doFilter(request, response);
            return;
        }

        Subject currentUser = SecurityUtils.getSubject();
        //如果当前用户已登录
        if(currentUser.isAuthenticated()){
            currentUser.getSession().setTimeout(serverSessionTimeout);
        }
        filterChain.doFilter(request, response);
    }

    private boolean handleExcludeURL(HttpServletRequest request, HttpServletResponse response) {
        if(excludes == null || excludes.isEmpty()){
            return false;
        }

        String url = request.getServletPath();
        for(String pattern: excludes){
            Pattern p = Pattern.compile("^" + pattern);
            Matcher m = p.matcher(url);
            if(m.find()){
                return true;
            }
        }

        return false;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if(logger.isDebugEnabled()){
            logger.debug("shiro session filter init~~~~~~~~~~~~");
        }
        String temp = filterConfig.getInitParameter("excludes");
        if (temp != null) {
            String[] url = temp.split(",");
            for (int i = 0; url != null && i < url.length; i++) {
                excludes.add(url[i]);
            }
        }
        String timeout = filterConfig.getInitParameter("serverSessionTimeout");
        if(StringUtils.isNotBlank(timeout)){
            this.serverSessionTimeout = NumberUtils.toLong(timeout,1200L)*1000L;
        }

    }
}
