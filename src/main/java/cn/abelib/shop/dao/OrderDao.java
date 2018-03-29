package cn.abelib.shop.dao;


import cn.abelib.shop.pojo.Orders;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by abel on 2017/9/14.
 */
@Mapper
public interface OrderDao {
    @Insert({"insert into orders(order_no, user_id, shipping_id, payment, pay_type, postage," +
            "status, pay_time, send_time, end_time, close_time, create_time, update_time)values(" +
            "#{orderNo}, #{userId}, #{shippingId}, #{payment}, #{payType}," +
            "#{postage}, #{status}, now(), now(), now(), now(), now(), now())"})
    Integer insertOrder(Orders orders);

    @Select("select * from orders where user_id=#{userId} and order_no=#{orderNo}")
    Orders selectByOrderNoAndId(@Param("userId") Integer userId, @Param("orderNo") Long orderNo);

    @Update({"update orders set order_no=#{orderNo}, user_id=#{userId}, shipping_id=#{shippingId}, payment=#{payment}, pay_type=#{payType}," +
            "postage=#{postage}, status=#{status}, pay_time=#now(), send_time=now(), end_time=now(), " +
            "close_time=now(), create_time=now(), update_time=now()"})
    Integer updateOrder(Orders orders);

    @Select("select * from orders where user_id=#{userId}")
    List<Orders> selectByUserId(@Param("userId") Integer userId);

    @Select("select * from orders order by id asc")
    List<Orders> listOrder();

    @Delete("delete from orders where id=#{id}")
    Integer deleteOrder(Integer id);

    @Select("select * from orders where order_no=#{orderNo}")
    Orders selectByOrderNo(Long orderNo);
}
