package cn.abelib.tmall.service.impl;

import cn.abelib.tmall.bean.Order;
import cn.abelib.tmall.bean.User;
import cn.abelib.tmall.dao.OrderDao;
import cn.abelib.tmall.service.OrderService;
import cn.abelib.tmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by abel on 2017/11/14.
 */
@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private UserService userService = null;
    @Autowired
    private OrderDao orderDao = null;

    @Override
    public Integer insertOrder(Order order) {
        return orderDao.insertOrder(order);
    }

    @Override
    public Integer updateOrder(Order order) {
        return orderDao.updateOrder(order);
    }

    @Override
    public Integer deleteOrder(Integer id) {
        return orderDao.deleteOrder(id);
    }

    @Override
    public List<Order> listAllOrder() {
        List<Order> orderList = orderDao.listAllOrder();
        return setUser(orderList);
    }

    @Override
    public Order getOrderById(Integer id) {
        return orderDao.getOrderById(id);
    }

    public Order setUser(Order order){
        User user = userService.getUserById(order.getUserId());
        order.setUser(user);
        return order;
    }

    public List<Order> setUser(List<Order> orderList){
        for (int i =0; i<orderList.size(); i++){
            orderList.set(i, setUser(orderList.get(i)));
        }
        return orderList;
    }
}
