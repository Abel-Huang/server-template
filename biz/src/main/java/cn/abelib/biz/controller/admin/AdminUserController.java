package cn.abelib.biz.controller.admin;


import cn.abelib.st.core.data.redis.RedisStringService;
import cn.abelib.biz.constant.BusinessConstant;
import cn.abelib.st.core.utils.CookieUtil;
import cn.abelib.st.core.utils.JsonUtil;
import cn.abelib.biz.pojo.User;
import cn.abelib.st.core.result.Response;
import cn.abelib.biz.constant.StatusConstant;
import cn.abelib.biz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 *
 * @author abel
 * @date 2017/9/12
 * 后台管理系统
 */
@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisStringService redisStringService;
    /**
     *  用户登录
     * @param userName
     * @param userPassword
     * @param session
     * @return
     */
    @PostMapping(value = "/login.do")
    public Response<User> login(String userName, String userPassword, HttpSession session, HttpServletResponse httpServletResponse){
        Response<User> response = userService.login(userName, userPassword);
        if (response.isSuccess()){
            User user  = response.getBody();
            if (user.getRole().equals(BusinessConstant.Role.ROLE_ADMIN)){
                // 写入Cookie
                CookieUtil.writeToken(httpServletResponse, session.getId());
                // 将用户信息写入到Redis中
                redisStringService.set(session.getId(), BusinessConstant.RedisCacheExtime.REDIS_SESSION_EXTIME, JsonUtil.obj2Str(response.getBody()));
                return response;
            }else {
                return Response.failed(StatusConstant.NOT_ADMIN_ERROR);
            }
        }
        return response;
    }
}
