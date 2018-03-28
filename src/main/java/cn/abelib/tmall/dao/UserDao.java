package cn.abelib.tmall.dao;

import cn.abelib.tmall.bean.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by abel on 2017/11/12.
 */
@Repository
public interface UserDao {
    Integer countUser();

    List<User> listAllUser();

    Integer insertUser(User user);

    Integer deleteUser(Integer id);

    Integer updateUser(User user);

    User getUserById(Integer id);
}
