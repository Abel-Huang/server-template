package cn.abelib.tmall.service;

import cn.abelib.tmall.bean.User;

import java.util.List;

/**
 * Created by abel on 2017/11/12.
 */
public interface UserService {
    /**
     * 统计User的数量
     * @return
     */
    Integer countUser();

    /**
     * 列出所有的User
     * @param
     * @return
     */
    List<User> listAllUser();

    /**
     * 插入Product，并且回填id
     * @param user
     * @return
     */
    Integer insertUser(User user);

    /**
     * 删除对应id的User
     * @param id
     * @return
     */
    Integer deleteUser(Integer id);

    /**
     *  更新User
     * @param user
     * @return
     */
    Integer updateUser(User user);

    /**
     * 通过id获取User
     * @param id
     * @return
     */
    User getUserById(Integer id);
}
