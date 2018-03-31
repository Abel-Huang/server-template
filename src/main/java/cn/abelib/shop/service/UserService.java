package cn.abelib.shop.service;

import cn.abelib.shop.pojo.User;
import cn.abelib.shop.common.result.Response;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by abel on 2017/9/11.
 */

public interface UserService {
    Response<User> login(String userName, String userPassword);

    Response<String> register(User user);

    Response<String> resetPassword(String originalPass, String newPassword, User user);

    Response<User> updateUserInfo(User user);

    Response<String> checkAdminRole(User user);
}
