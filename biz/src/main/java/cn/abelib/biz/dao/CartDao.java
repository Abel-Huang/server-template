package cn.abelib.biz.dao;


import cn.abelib.biz.pojo.Cart;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 *
 * @author abel
 * @date 2017/9/15
 */
@Mapper
public interface CartDao {
    @Insert("insert into cart(user_id, product_id, quantity, checked, create_time, update_time) values(" +
            "#{userId}, #{productId}, #{quantity}, #{checked}, now(), now())")
    Integer insert(Cart cart);

    @Update("update cart set user_id=#{userId}, product_id=#{productId}, quantity=#{quantity}, checked=#{checked}, update_time=now()")
    Integer update(Cart cart);

    @Select("select * from cart where user_id=#{userId} and product_id=#{productId}")
    Cart selectCartByUserIdProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    @Delete("delete from cart where user_id=#{userId} and id=#{id}")
    Integer deleteByUserIdAndId(@Param("userId") Integer userId, @Param("id") Integer id);

    @Delete("delete from cart where id=#{id}")
    Integer deleteById(@Param("id") Integer id);

    @Select("select * from cart order by id asc")
    List<Cart> list(Cart cart);

    @Select("select * from cart where user_id=#{userId}")
    List<Cart> selectCartByUserId(Integer userId);

    @Select("select * from cart where  user_id=#{userId} and checked = 0")
    Integer selectCartProductStatusByUserId(Integer userId);

    @Select("select * from cart ")
    Integer selectOrUnselectAll(Integer userId);

    @Select("select * from cart ")
    Integer selectOrUnselect(Integer userId, Integer productId);

    @Select("select sum(quantity) as count from cart where user_id=#{userId}")
    Integer selectCartProductCountByUserId(Integer userId);

    @Select("select * from cart where userId=#{userId} and checked=1")
    List<Cart> selectCheckedCartByUserId(Integer userId);
}
