package cn.abelib.biz.controller.filter;

import cn.abelib.biz.constant.BusinessConstant;
import cn.abelib.st.core.utils.CookieUtil;
import cn.abelib.st.core.utils.JsonUtil;
import cn.abelib.st.core.data.redis.RedisStringService;
import cn.abelib.biz.pojo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 *
 * @author abel
 * @date 2018/2/31
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
