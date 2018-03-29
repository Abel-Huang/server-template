package cn.abelib.shop.controller.admin;


import cn.abelib.shop.common.constant.BusinessConstant;
import cn.abelib.shop.pojo.User;
import cn.abelib.shop.common.result.Response;
import cn.abelib.shop.common.constant.StatusConstant;
import cn.abelib.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;


/**
 * Created by abel on 2017/9/12.
 * 后台管理系统
 */
@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    @Autowired
    private UserService userService;

    /**
     *  用户登录
     * @param userName
     * @param userPassword
     * @param session
     * @return
     */
    @PostMapping(value = "/login")
    public Response<User> login(String userName, String userPassword, HttpSession session){
        Response<User> response = userService.login(userName, userPassword);
        if (response.isSuccess()){
            User user  = response.getBody();
            if (user.getRole().equals(BusinessConstant.Role.ROLE_ADMIN)){
                session.setAttribute(BusinessConstant.CURRENT_USER, user);
                return response;
            }else {
                return Response.failed(StatusConstant.NOT_ADMIN_ERROR);
            }
        }
        return response;
    }
}
