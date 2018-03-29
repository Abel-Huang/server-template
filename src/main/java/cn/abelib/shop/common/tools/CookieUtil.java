package cn.abelib.shop.common.tools;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by abel on 2017/12/15.
 *  Cookie
 */
@Slf4j
public class CookieUtil {
    private final static String COOKIE_DOMAIN = ".abelib.cn";
    private final static String COOKIE_NAME = "abel_login_token";

    public static void writeToken(HttpServletResponse response, String token){
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setDomain(COOKIE_DOMAIN);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24 * 30); // 30 days
        log.info("write cookieName : {}, cookieValue : {}", cookie.getName(), cookie.getValue());
        response.addCookie(cookie);
    }
}
