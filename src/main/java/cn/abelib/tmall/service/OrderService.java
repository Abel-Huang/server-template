package cn.abelib.tmall.service;

import cn.abelib.tmall.bean.Order;

import java.util.List;

/**
 * Created by abel on 2017/11/14.
 */
public interface OrderService {
    String waitPay = "waitPay";
    String waitDelivery = "waitDelivery";
    String waitConfirm = "waitConfirm";
    String waitReview = "waitReview";
    String finish = "finish";
    String delete = "delete";

    Integer insertOrder(Order order);

    Integer updateOrder(Order order);

    Integer deleteOrder(Integer id);

    List<Order> listAllOrder();

    Order getOrderById(Integer id);
}
