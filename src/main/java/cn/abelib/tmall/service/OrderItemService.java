package cn.abelib.tmall.service;

import cn.abelib.tmall.bean.Order;
import cn.abelib.tmall.bean.OrderItem;

import java.util.List;

/**
 * Created by abel on 2017/11/14.
 */
public interface OrderItemService {
    List<OrderItem> listAllOrderItem();

    Integer insertOrderItem(OrderItem orderItem);

    Integer deleteOrderItem(Integer id);

    Integer updateOrderItem(OrderItem orderItem);

    OrderItem getOrderItemById(Integer id);

    Order fill(Order order);

    List<Order> fill(List<Order> orderList);
}
