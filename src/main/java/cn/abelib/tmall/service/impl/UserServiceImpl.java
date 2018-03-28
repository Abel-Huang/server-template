package cn.abelib.tmall.service.impl;

import cn.abelib.tmall.bean.User;
import cn.abelib.tmall.dao.UserDao;
import cn.abelib.tmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by abel on 2017/11/12.
 */
@Service
public class UserServiceImpl  implements UserService{
    @Autowired
    private UserDao userDao = null;
    @Override
    public Integer countUser() {
        return userDao.countUser();
    }

    @Override
    public List<User> listAllUser() {
        return userDao.listAllUser();
    }

    @Override
    public Integer insertUser(User user) {
        return userDao.insertUser(user);
    }

    @Override
    public Integer deleteUser(Integer id) {
        return userDao.deleteUser(id);
    }

    @Override
    public Integer updateUser(User user) {
        return userDao.updateUser(user);
    }

    @Override
    public User getUserById(Integer id) {
        return userDao.getUserById(id);
    }
}
