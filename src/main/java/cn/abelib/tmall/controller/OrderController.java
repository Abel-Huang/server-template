package cn.abelib.tmall.controller;

import cn.abelib.tmall.bean.Order;
import cn.abelib.tmall.service.OrderItemService;
import cn.abelib.tmall.service.OrderService;
import cn.abelib.tmall.util.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by abel on 2017/11/15.
 */
@Controller
@RequestMapping("")
public class OrderController {
    @Autowired
    private OrderService orderService = null;
    @Autowired
    private OrderItemService orderItemService = null;

    @RequestMapping("admin_order_list")
    public ModelAndView listAllOrder(Page page){
        ModelAndView mv = new ModelAndView();
        PageHelper.offsetPage(page.getStart(), page.getCount());
        List<Order> orderList = orderService.listAllOrder();
        int total = (int) new PageInfo<>(orderList).getTotal();
        page.setTotal(total);
        orderList = orderItemService.fill(orderList);
        mv.addObject("page", page);
        mv.addObject("orderList", orderList);
        mv.setViewName("admin/listOrder");
        return mv;
    }

    @RequestMapping("admin_order_delivery")
    public ModelAndView delivery(Order order) throws IOException{
        ModelAndView mv = new ModelAndView();
        order.setDeliveryDate(new Date());
        order.setOrderStatus(OrderService.waitConfirm);
        orderService.updateOrder(order);
        mv.setViewName("redirect:admin_order_list");
        return mv;

    }
}
