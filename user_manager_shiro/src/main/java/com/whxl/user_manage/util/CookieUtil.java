package com.whxl.user_manage.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

/**
 * Created By 朱立松 on 2019/1/17
 * Http请求中的Cookie操作工具
 */
public class CookieUtil {

    /**
     * 根据用户id来生成cookie
     * @param response
     * @return
     */
    public static String generateCookieForUser(HttpServletResponse response) {
        String randomStr = generateRandomStr();
        //生成新的记录用户的cookie
        Cookie cookie = new Cookie("sid", randomStr);
        //添加Cookie
        response.addCookie(cookie);
        return randomStr;
    }

    /**
     * 生成32位MD5加密的
     * @return
     */
    private static String generateRandomStr(){
        //生成6位随机数字加+字母的组合
        String ranStr = generateStr(6);
        return MD5Util.encode(ranStr, "UTF-8");
    }

    /**
     * 随机生成n位字母加数字组合
     * @param length
     * @return
     */
    private static String generateStr(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
        // 输出字母还是数字
        String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 取得大写字母还是小写字母
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;

    }

    public static void main(String[] args) {
        System.out.println(generateRandomStr());
    }
}
