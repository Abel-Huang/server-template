package cn.abelib.shop.dao;


import cn.abelib.shop.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by abel on 2017/9/12.
 */
@Mapper
public interface UserDao {
    @Insert("insert into user(nick_name, user_name, user_password, salt,  role, phone, create_time, update_time) values(" +
            "#{nickName}, #{userName}, #{userPassword}, #{salt}, #{role}, #{phone}, now(), now())")
    Integer insertUser(User user);

    @Delete("delete from user where id=#{id}")
    Integer deleteUser(@Param("id")Integer id);

    @Update("update user set nick_name=#{nickName}, user_password=#{userPassword}, salt=#{salt}, role=#{role}, phone=#{phone}, update_time=now() where id=#{id}")
    Integer updateUser(User user);

    @Select("select * from user where id=#{id}")
    User getUserById(@Param("id") Integer id);

    @Select("select count(1) from user where user_name=#{userName}")
    Integer checkUserName(@Param("userName") String  userName);

    @Select("select * from user where user_name=#{userName}")
    User selectLogin (@Param("userName") String  userName);

    @Select("select count(1) from user where id=#{id} and user_password=#{userPassword}")
    Integer checkPassword(@Param("id") Integer id, @Param("userPassword") String userPassword);
}
