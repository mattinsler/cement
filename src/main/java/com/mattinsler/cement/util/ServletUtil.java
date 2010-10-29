package com.mattinsler.cement.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Oct 11, 2010
 * Time: 9:50:15 PM
 * To change this template use File | Settings | File Templates.
 */
public final class ServletUtil {
    public static String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (key.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
