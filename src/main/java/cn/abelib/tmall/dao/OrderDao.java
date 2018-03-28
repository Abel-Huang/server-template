package cn.abelib.tmall.dao;

import cn.abelib.tmall.bean.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by abel on 2017/11/14.
 */
@Repository
public interface OrderDao {
    Integer insertOrder(Order order);

    Integer updateOrder(Order order);

    Integer deleteOrder(Integer id);

    List<Order> listAllOrder();

    Order getOrderById(Integer id);
}
