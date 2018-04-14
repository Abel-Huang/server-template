package cn.abelib.shop.controller.filter;

import cn.abelib.shop.common.constant.BusinessConstant;
import cn.abelib.shop.common.tools.CookieUtil;
import cn.abelib.shop.common.tools.JsonUtil;
import cn.abelib.shop.service.redis.RedisStringService;
import cn.abelib.shop.pojo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by abel on 2018/2/31.
 */
@Component
@WebFilter(urlPatterns = "/")
public class SessionExpireFilter implements Filter{
    @Autowired
    private RedisStringService redisStringService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = CookieUtil.readToken(request);
        if (StringUtils.isNotEmpty(token)){
            String userJson = redisStringService.get(token);
            User user = JsonUtil.str2Obj(userJson, User.class);
            if (user != null){
                // user不为空 重置session时间
                redisStringService.expire(token, BusinessConstant.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
