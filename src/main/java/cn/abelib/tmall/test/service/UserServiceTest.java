package cn.abelib.tmall.test.service;


import cn.abelib.tmall.bean.User;
import cn.abelib.tmall.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by abel on 2017/11/17.
 */
@Service
public class UserServiceTest {
    @Autowired
    private UserService userService = null;


    @Test
    public void TestUserList(){
        List<User> userList = userService.listAllUser();
        for (User user : userList){
            System.out.println("user " + user);
        }
    }

}
