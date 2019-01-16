package cn.abelib.shop.controller.portal;


import cn.abelib.data.redis.RedisStringService;
import cn.abelib.common.constant.BusinessConstant;
import cn.abelib.common.tools.CookieUtil;
import cn.abelib.common.tools.JsonUtil;
import cn.abelib.shop.pojo.User;
import cn.abelib.common.result.Response;
import cn.abelib.common.constant.StatusConstant;
import cn.abelib.shop.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author abel
 * @date 2017/9/12
 *  用户模块接口
 */
@RestController
@RequestMapping("/portal/user")
public class PortalUserController {
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
            // 写入Cookie
            CookieUtil.writeToken(httpServletResponse, session.getId());
            // 将用户信息写入到Redis中
            redisStringService.set(session.getId(), BusinessConstant.RedisCacheExtime.REDIS_SESSION_EXTIME, JsonUtil.obj2Str(response.getBody()));
        }
        return response;
    }


    /**
     *  退出登录
     * @param request
     * @param response
     * @return
     */
    @GetMapping(value = "/logout.do")
    public Response logout(HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtil.readToken(request);
        if (StringUtils.isEmpty(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        CookieUtil.removeToken(request, response);
        if (!redisStringService.exists(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        redisStringService.delete(token);
        return Response.success(StatusConstant.GENERAL_SUCCESS);
    }

    /**
     *  用户注册
     * @param user
     * @return
     */
    @PostMapping(value = "/register.do")
    public Response register(User user){
        return userService.register(user);
    }

    /**
     *  获取用户信息
     * @param request
     * @return
     */
    @GetMapping(value = "/get_user_info.do")
    public Response<User> getUserInfo(HttpServletRequest request){
        String token = CookieUtil.readToken(request);
        if (StringUtils.isEmpty(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        String userJson = redisStringService.get(token);
        User user = JsonUtil.str2Obj(userJson, User.class);
        if (user != null){
            return Response.success(StatusConstant.GENERAL_SUCCESS, user);
        }
        return Response.failed(StatusConstant.USER_NOT_LOGIN);
    }

    /**
     *  修改用户密码
     * @param request
     * @param originalPass
     * @param newPass
     * @return
     */
    @PostMapping(value = "/reset_password.do")
    public Response<String> resetPassword(HttpServletRequest request, String originalPass, String newPass){
        String token = CookieUtil.readToken(request);
        if (StringUtils.isEmpty(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        String userJson = redisStringService.get(token);
        User user = JsonUtil.str2Obj(userJson, User.class);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return userService.resetPassword(originalPass, newPass, user);
    }

    /**
     *  修改用户信息
     * @param request
     * @param user
     * @return
     */
    @PostMapping(value = "/update_user_info.do")
    public Response<User> updateUserInfo(HttpServletRequest request, User user){
        String token = CookieUtil.readToken(request);
        if (StringUtils.isEmpty(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        String userJson = redisStringService.get(token);
        User currentUser = JsonUtil.str2Obj(userJson, User.class);
        if (currentUser == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }

        user.setId(currentUser.getId());
        user.setUserName(currentUser.getUserName());
        Response<User> response = userService.updateUserInfo(user);
        if (response.isSuccess()){
            response.getBody().setUserName(currentUser.getUserName());
            redisStringService.set(token, BusinessConstant.RedisCacheExtime.REDIS_SESSION_EXTIME, JsonUtil.obj2Str(response.getBody()));
        }
        return response;
    }
}
