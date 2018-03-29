package cn.abelib.shop.controller.portal;


import cn.abelib.shop.cache.RedisStringService;
import cn.abelib.shop.cache.key.UserKeyPrefix;
import cn.abelib.shop.common.constant.BusinessConstant;
import cn.abelib.shop.pojo.User;
import cn.abelib.shop.common.result.Response;
import cn.abelib.shop.common.constant.StatusConstant;
import cn.abelib.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by abel on 2017/9/12.
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
    public Response<User> login(String userName, String userPassword, HttpSession session){
        Response<User> response = userService.login(userName, userPassword);
        if (response.isSuccess()){

            //session.setAttribute(BusinessConstant.CURRENT_USER, response.getBody());
            redisStringService.set(UserKeyPrefix.sesson, session.getId(), response.getClass());
        }
        return response;
    }

    /**
     *  退出登录
     * @param session
     * @return
     */
    @GetMapping(value = "/logout")
    public Response logout(HttpSession session) {
        // 从session中移除用户信息
        session.removeAttribute(BusinessConstant.CURRENT_USER);
        return Response.success(StatusConstant.GENERAL_SUCCESS);
    }

    /**
     *  用户注册
     * @param user
     * @return
     */
    @PostMapping(value = "/register")
    public Response register(User user){
        return userService.register(user);
    }

    /**
     *  获取用户信息
     * @param session
     * @return
     */
    @GetMapping(value = "/getUserInfo")
    public Response<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (user != null){
            return Response.success(StatusConstant.GENERAL_SUCCESS, user);
        }
        return Response.failed(StatusConstant.USER_NOT_LOGIN);
    }

    /**
     *  修改用户密码
     * @param session
     * @param originalPass
     * @param newPass
     * @return
     */
    @PostMapping(value = "/resetPassword")
    public Response<String> resetPassword(HttpSession session, String originalPass, String newPass){
        User user = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return userService.resetPassword(originalPass, newPass, user);
    }

    /**
     *  修改用户信息
     * @param session
     * @param user
     * @return
     */
    @PostMapping(value = "/updateUserInfo")
    public Response<User> updateUserInfo(HttpSession session, User user){
        User currentUser = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (currentUser == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        user.setId(currentUser.getId());
        user.setUserName(currentUser.getUserName());
        Response<User> response = userService.updateUserInfo(user);
        if (response.isSuccess()){
            response.getBody().setUserName(currentUser.getUserName());
            session.setAttribute(BusinessConstant.CURRENT_USER, response.getBody());
        }
        return response;
    }
}
