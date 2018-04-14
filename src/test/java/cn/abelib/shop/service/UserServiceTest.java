package cn.abelib.shop.service;

import cn.abelib.shop.common.constant.BusinessConstant;
import cn.abelib.shop.common.tools.Md5Util;
import cn.abelib.shop.dao.UserDao;
import cn.abelib.shop.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * Created by abel on 2018/1/26.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    /**
     *  success
     */
    @Test
    public void registerTest() {
        User user = new User();
        user.setUserName("1234");
        user.setNickName("123");
        user.setUserPassword("123");
        user.setPhone("1234567");
        user.setRole(BusinessConstant.Role.ROLE_CUSTOMER);
        user.setSalt("12345");
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        int insertCount = userDao.insertUser(user);
        System.err.println(insertCount);
    }

    @Test
    public void checkPasswordTest() {
        int resultCount = userDao.checkPassword(4, Md5Util.dbPassword("123456789","d2qWBz1bm42"));
        System.err.println(resultCount);
    }
    @Test
    public void updateUserTest() {
        User user = userDao.getUserById(1);
        user.setNickName("Tom");
        int updateCount = userDao.updateUser(user);
        System.err.println(updateCount);
    }

}
