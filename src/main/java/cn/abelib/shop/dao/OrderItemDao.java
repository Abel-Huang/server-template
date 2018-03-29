package cn.abelib.shop.dao;


import cn.abelib.shop.pojo.OrderItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by abel on 2017/9/14.
 */
@Mapper
public interface OrderItemDao {
    @Select("select * from order_item order by id asc")
    List<OrderItem> list();

    //todo batch
    @Insert("insert ")
    Integer batchInsert(List<OrderItem> orderItemList);

    @Insert("insert into order_item (user_id, order_no, product_id, product_name, product_image, " +
            "current_price, quantity, total_price, create_time, update_time)values(" +
            "#{userId}, #{orderNo}, #{productId}, #{productName}, #{productImage}" +
            "#{currentPrice}, #{quantity}, #{totalPrice}, now(), now())")
    Integer insert(OrderItem orderItem);

    //todo replace *
    @Select("select * from order_item where user_id=#{userId} and order_no=#{orderNo}")
    List<OrderItem> selectByUserIdOrderNo(@Param("userId") Integer userId, @Param("orderNo") Long orderNo);

    @Select("select * from order_item where order_no=#{orderNo}")
    List<OrderItem> selectByOrderNo(@Param("orderNo") Long orderNo);
}
