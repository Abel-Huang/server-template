package cn.abelib.tmall.service.impl;

import cn.abelib.tmall.bean.Order;
import cn.abelib.tmall.bean.OrderItem;
import cn.abelib.tmall.bean.Product;
import cn.abelib.tmall.dao.OrderItemDao;
import cn.abelib.tmall.service.OrderItemService;
import cn.abelib.tmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by abel on 2017/11/14.
 */
@Service
public class OrderItemServiceImpl implements OrderItemService {
    @Autowired
    private OrderItemDao orderItemDao = null;
    @Autowired
    private ProductService productService = null;

    @Override
    public List<OrderItem> listAllOrderItem() {
        return orderItemDao.listAllOrderItem();
    }

    @Override
    public Integer insertOrderItem(OrderItem orderItem) {
        return orderItemDao.insertOrderItem(orderItem);
    }

    @Override
    public Integer deleteOrderItem(Integer id) {
        return orderItemDao.deleteOrderItem(id);
    }

    @Override
    public Integer updateOrderItem(OrderItem orderItem) {
        return orderItemDao.updateOrderItem(orderItem);
    }

    @Override
    public OrderItem getOrderItemById(Integer id) {
        return orderItemDao.getOrderItemById(id);
    }

    @Override
    public Order fill(Order order) {
        List<OrderItem> orderItemList = orderItemDao.listAllOrderItem();
        setProduct(orderItemList);
        Float total = 0f;
        int totalNumber = 0;
        for (OrderItem orderItem : orderItemList){
            total += orderItem.getNumber() * orderItem.getProduct().getPromotePrice();
            totalNumber += orderItem.getNumber();
        }
        order.setTotal(total);
        order.setTotalNumber(totalNumber);
        order.setOrderItems(orderItemList);
        return order;
    }

    @Override
    public List<Order> fill(List<Order> orderList) {
        for (int i =0; i<orderList.size(); i++) {
            orderList.set(i, fill(orderList.get(i)));
        }
        return orderList;
    }

    public List<OrderItem> setProduct(List<OrderItem> orderItemList){
       for (int i =0 ; i< orderItemList.size(); i++){
          orderItemList.set(i, setProduct(orderItemList.get(i)));
       }
        return orderItemList;
    }

    private OrderItem setProduct(OrderItem orderItem){
        Product product = productService.getProductById(orderItem.getProductId());
        orderItem.setProduct(product);
        return orderItem;
    }
}
