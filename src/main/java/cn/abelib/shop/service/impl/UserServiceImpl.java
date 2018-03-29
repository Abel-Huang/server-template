package cn.abelib.shop.service.impl;

import cn.abelib.shop.common.constant.BusinessConstant;
import cn.abelib.shop.pojo.User;
import cn.abelib.shop.cache.RedisStringService;
import cn.abelib.shop.cache.key.UserKeyPrefix;
import cn.abelib.shop.common.exception.GlobalException;
import cn.abelib.shop.common.result.Response;
import cn.abelib.shop.common.constant.StatusConstant;
import cn.abelib.shop.common.tools.Md5Util;
import cn.abelib.shop.dao.UserDao;
import cn.abelib.shop.service.UserService;
import cn.abelib.shop.vo.UserVo;
import com.alibaba.druid.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by abel on 2018/2/5.
 */
@Service
public class UserServiceImpl implements UserService {
    // cookie name
    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisStringService redisService;

    /**
     *  获取User，如果缓存中有对应key值的User，就从缓存中获取，如果没有对应的值，
     *  则从数据库中获取，并且将内容写到缓存中
     * @param id
     * @return
     */
    public User getById(Integer id){
        // 命中缓存
        User user = redisService.get(UserKeyPrefix.getById, ""+id, User.class);
        if (user != null){
            return user;
        }
        // 未命中缓存，取数据库
        user = userDao.getUserById(id);
        if (user != null){
            redisService.set(UserKeyPrefix.getById, ""+id, user);
        }
        return user;
    }

    public User getByToken(HttpServletResponse response, String token){
        if (StringUtils.isEmpty(token)){
            return null;
        }
        User user = redisService.get(UserKeyPrefix.token, token, User.class);
        // 延长有效期
        if (user != null){
            addCookie(response, token, user);
        }
        return user;
    }

    /**
     *  login
     * @return
     */
    public String login(HttpServletResponse response, UserVo userVo){
//        if (userVo == null){
//            throw new GlobalException(StatusConstant.GENERAL_SERVER_ERROR);
//        }
//        String userName = userVo.getUserName();
//        String userPassword = userVo.getUserPassword();
//        // 判断账号是否存在
//        User user = getById(1);
//        if (user == null)
//            return null;
        return null;
    }

    public boolean updatePassword(String token, Integer id, String formPass){
        User user = getById(id);
        if (user == null){
            throw new GlobalException(StatusConstant.ACCOUNT_NOT_EXISTS);
        }
        User newUser = new User();
        newUser.setId(id);
        newUser.setUserPassword(Md5Util.dbPassword(formPass, user.getSalt()));
        userDao.updateUser(newUser);
        // 处理缓存
        redisService.delete(UserKeyPrefix.getById, "" + id);
        user.setUserPassword(newUser.getUserPassword());
        redisService.set(UserKeyPrefix.token, token, user);
        return true;
    }
    private void addCookie(HttpServletResponse response, String token, User user){
        redisService.set(UserKeyPrefix.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(UserKeyPrefix.token.expire());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     *  用户登录接口
     * @param userName
     * @param userPassword
     * @return
     */
    public Response<User> login(String userName, String userPassword){
        Integer resultCount = userDao.checkUserName(userName);
        if (resultCount == 0){
            return Response.failed(StatusConstant.ACCOUNT_NOT_EXISTS);
        }
        //  这里需要进行密码的转换
        User user = userDao.selectLogin(userName);
        String dbPassword = Md5Util.dbPassword(userPassword, user.getSalt());

        if (!dbPassword.equals(user.getUserPassword())){
            return Response.failed(StatusConstant.WRONG_PASS_ERROR);
        }
        user.setUserPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return Response.success(StatusConstant.GENERAL_SUCCESS, user);
    }

    /**
     *  用户注册接口
     * @param user
     * @return
     */
    public Response<String> register(User user){
        Integer resultCount = userDao.checkUserName(user.getUserName());
        if (resultCount > 0){
            return Response.failed(StatusConstant.ACCOUNT_ALREADY_EXISTS);
        }
        user.setRole(BusinessConstant.Role.ROLE_CUSTOMER);
        String salt = Md5Util.randSalt(11);
        String dbPassword = Md5Util.dbPassword(user.getUserPassword(), salt);
        user.setSalt(salt);
        user.setUserPassword(dbPassword);
        user.setRole(BusinessConstant.Role.ROLE_CUSTOMER);

        resultCount = userDao.insertUser(user);
        if (resultCount == 0){
            return Response.failed(StatusConstant.INSERT_USER_ERROR);
        }
        return Response.success(StatusConstant.GENERAL_SUCCESS);
    }

    /**
     *  修改用户密码
     * @param originalPass
     * @param newPassword
     * @param user
     * @return
     */
    public Response<String> resetPassword(String originalPass, String newPassword, User user){
        int resultCount = userDao.checkPassword(user.getId(), Md5Util.dbPassword(originalPass, user.getSalt()));
        if (resultCount == 0){
            return Response.failed(StatusConstant.WRONG_PASS_ERROR);
        }
        user.setUserPassword(Md5Util.dbPassword(newPassword, user.getSalt()));
        user.setUserPassword(Md5Util.dbPassword(newPassword, user.getSalt()));

        int updateCount = userDao.updateUser(user);
        if (updateCount > 0){
            return Response.success(StatusConstant.GENERAL_SUCCESS);
        }
        return Response.failed(StatusConstant.UPDATE_PASS_ERROR);
    }

    /**
     *  更改用户个人信息
     * @param user
     * @return
     */
    public Response<User> updateUserInfo(User user){
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setNickName(user.getNickName());
        updateUser.setPhone(user.getPhone());
        updateUser.setRole(user.getRole());

        int updateCount = userDao.updateUser(user);
        if (updateCount > 0){
            return Response.success(StatusConstant.GENERAL_SUCCESS);
        }
        return Response.failed(StatusConstant.UPDATE_USER_ERROR);
    }

    /**
     *  通过User的Role属性判断是否是管理员
      * @param user
     * @return
     */
    public Response<String> checkAdminRole(User user){
        if (user != null && user.getRole() == BusinessConstant.Role.ROLE_ADMIN){
            return Response.success(StatusConstant.GENERAL_SUCCESS);
        }
        return Response.failed(StatusConstant.GENERAL_SERVER_ERROR);
    }
}
