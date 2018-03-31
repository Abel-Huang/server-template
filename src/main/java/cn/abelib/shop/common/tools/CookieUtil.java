package cn.abelib.shop.common.tools;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by abel on 2017/12/15.
 *  Cookie
 */
@Slf4j
public class CookieUtil {
    private final static String COOKIE_DOMAIN = ".localhost";
    private final static String COOKIE_NAME = "abel_login_token";

    /**
     *  cookie 读取
     * @param request
     * @return
     */
    public static String readToken(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies){
                log.info("read cookieName : {}, cookieValue : {}", cookie.getName(), cookie.getValue());
                if (StringUtils.equals(cookie.getName(), COOKIE_NAME)){
                    log.info("return  cookieName : {}, cookieValue : {}", cookie.getName(), cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    /**
     *  cookie 写入
     * @param response
     * @param token
     */
    public static void writeToken(HttpServletResponse response, String token){
        Cookie cookie = new Cookie(COOKIE_NAME, token);
       // cookie.setDomain(COOKIE_DOMAIN);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        // maxage不设置 cookie就不会写入硬盘 而是写在内存 只在当前页面有效 如果设置为-1 就代表为永久
        cookie.setMaxAge(60 * 60 * 24 * 30); // 30 days
        log.info("write cookieName : {}, cookieValue : {}", cookie.getName(), cookie.getValue());
        response.addCookie(cookie);
    }

    /**
     *  cookie 移除
     * @param request
     * @param response
     */
    public static void removeToken(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies){
                if (StringUtils.equals(cookie.getName(), COOKIE_NAME)){
                   // cookie.setDomain(COOKIE_DOMAIN);
                    cookie.setPath("/");
                    // 删除cookie
                    cookie.setMaxAge(0);
                    log.info("delete  cookieName : {}, cookieValue : {}", cookie.getName(), cookie.getValue());
                    response.addCookie(cookie);
                    return;
                }
            }
        }
    }
}
