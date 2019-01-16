package cn.abelib.shop.controller.intercepter;


import cn.abelib.common.constant.BusinessConstant;
import cn.abelib.common.constant.StatusConstant;
import cn.abelib.common.result.Response;
import cn.abelib.common.tools.CookieUtil;
import cn.abelib.common.tools.JsonUtil;
import cn.abelib.data.redis.RedisStringService;
import cn.abelib.shop.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 *
 * @author abel
 * @date 2018/2/7
 *     权限拦截器
 */
@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor{
    @Autowired
    private RedisStringService redisStringService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle");
        // 请求中的Controller中的方法名
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        User user = null;
        String token = CookieUtil.readToken(request);
        if (StringUtils.isNotEmpty(token)){
            String userJson = redisStringService.get(token);
            user = JsonUtil.str2Obj(userJson, User.class);
        }
        if (user == null || (user.getRole().intValue() != BusinessConstant.Role.ROLE_ADMIN)){
            response.reset();// 否则会出现异常
            response.setCharacterEncoding("UTF-8");// 否则会乱码
            response.setContentType("application/json;charset=UTF-8");

            PrintWriter out = response.getWriter();

            if (user == null){
                out.print(JsonUtil.obj2Str( Response.failed(StatusConstant.USER_NOT_LOGIN)));
            }else {
                out.print(JsonUtil.obj2Str( Response.failed(StatusConstant.NOT_ADMIN_ERROR)));
            }
            out.flush();
            out.close();
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("afterCompletion");
    }
}
