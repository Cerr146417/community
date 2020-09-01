package com.nowcoder.community.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName CookieUtil
 * @Description Cookie工具类
 * @Author cxc
 * @Date 2020/9/1 9:54
 * @Verseion 1.0
 **/
public class CookieUtil {

    /**
     * 通过名字获取cookie
     * @param request request
     * @param name cookie名字
     * @return cookie
     * @throws IllegalAccessException
     */
    public static String getValue(HttpServletRequest request,String name) throws IllegalAccessException {
        if (request == null || name == null){
            throw new IllegalAccessException("参数为空!");
        }

        Cookie [] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies){
                if (cookie.getName().equals(name)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
