package cn.abelib.shop.controller.portal;


import cn.abelib.shop.common.constant.BusinessConstant;
import cn.abelib.shop.common.constant.StatusConstant;
import cn.abelib.shop.common.result.Response;
import cn.abelib.shop.pojo.User;
import cn.abelib.shop.service.OrderService;
import cn.abelib.shop.vo.OrderProductVo;
import cn.abelib.shop.vo.OrderVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by abel on 2017/9/12.
 */
@RestController
@RequestMapping("/portal/order")
public class PortalOrderController {
    @Autowired
    private OrderService orderService;

    /**
     *  创建订单
     * @param session
     * @param shippingId
     * @return
     */
    @PostMapping("/create.do")
    public Response<OrderVo> create(HttpSession session, Integer shippingId){
        User user = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return orderService.createOrder(user.getId(), shippingId);
    }

    /**
     *  取消订单
     * @param session
     * @param orderNo
     * @return
     */
    @PostMapping("/cancel.do")
    public Response cancel(HttpSession session, Long orderNo){
        User user = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return orderService.cancelOrder(user.getId(), orderNo);
    }

    /**
     *  获取订单中购物车中的商品信息
     * @param session
     * @return
     */
    @PostMapping("/get_order_cart_product.do")
    public Response<OrderProductVo> getOrderCartProduct(HttpSession session){
        User user = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return orderService.getOrderCartProduct(user.getId());
    }

    @PostMapping("/detail.do")
    public Response<OrderVo> getOrderDetail(HttpSession session, Long orderNo){
        User user = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return orderService.getOrderDetail(user.getId(), orderNo);
    }

    @PostMapping("/list.do")
    public Response<PageInfo> getOrderList(HttpSession session,
                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){
        User user = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return orderService.getOrderList(user.getId(), pageNum, pageSize);
    }
}
