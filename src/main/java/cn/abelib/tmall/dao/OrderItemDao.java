package cn.abelib.tmall.dao;

import cn.abelib.tmall.bean.OrderItem;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by abel on 2017/11/14.
 */
@Repository
public interface OrderItemDao {
    List<OrderItem> listAllOrderItem();

    Integer insertOrderItem(OrderItem orderItem);

    Integer deleteOrderItem(Integer id);

    Integer updateOrderItem(OrderItem orderItem);

    OrderItem getOrderItemById(Integer id);

}
