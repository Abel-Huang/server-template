package cn.abelib.biz.controller.portal;


import cn.abelib.st.core.data.redis.RedisStringService;
import cn.abelib.biz.constant.StatusConstant;
import cn.abelib.st.core.result.Response;
import cn.abelib.st.core.utils.CookieUtil;
import cn.abelib.st.core.utils.JsonUtil;
import cn.abelib.biz.pojo.User;
import cn.abelib.biz.service.OrderService;
import cn.abelib.biz.vo.OrderProductVo;
import cn.abelib.biz.vo.OrderVo;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * @author abel
 * @date 2017/9/12
 */
@RestController
@RequestMapping("/portal/order")
public class PortalOrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisStringService redisStringService;

    /**
     *  创建订单
     * @param request
     * @param shippingId
     * @return
     */
    @PostMapping("/create.do")
    public Response<OrderVo> create(HttpServletRequest request, Integer shippingId){
        String token = CookieUtil.readToken(request);
        if (StringUtils.isEmpty(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        String userJson = redisStringService.get(token);
        User user = JsonUtil.str2Obj(userJson, User.class);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return orderService.createOrder(user.getId(), shippingId);
    }

    /**
     *  取消订单
     * @param request
     * @param orderNo
     * @return
     */
    @PostMapping("/cancel.do")
    public Response cancel(HttpServletRequest request, Long orderNo){
        String token = CookieUtil.readToken(request);
        if (StringUtils.isEmpty(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        String userJson = redisStringService.get(token);
        User user = JsonUtil.str2Obj(userJson, User.class);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return orderService.cancelOrder(user.getId(), orderNo);
    }

    /**
     *  获取订单中购物车中的商品信息
     * @param request
     * @return
     */
    @PostMapping("/get_order_cart_product.do")
    public Response<OrderProductVo> getOrderCartProduct(HttpServletRequest request){
        String token = CookieUtil.readToken(request);
        if (StringUtils.isEmpty(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        String userJson = redisStringService.get(token);
        User user = JsonUtil.str2Obj(userJson, User.class);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return orderService.getOrderCartProduct(user.getId());
    }

    @PostMapping("/detail.do")
    public Response<OrderVo> getOrderDetail(HttpServletRequest request, Long orderNo){
        String token = CookieUtil.readToken(request);
        if (StringUtils.isEmpty(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        String userJson = redisStringService.get(token);
        User user = JsonUtil.str2Obj(userJson, User.class);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return orderService.getOrderDetail(user.getId(), orderNo);
    }

    @PostMapping("/list.do")
    public Response<PageInfo> getOrderList(HttpServletRequest request,
                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){
        String token = CookieUtil.readToken(request);
        if (StringUtils.isEmpty(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        String userJson = redisStringService.get(token);
        User user = JsonUtil.str2Obj(userJson, User.class);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return orderService.getOrderList(user.getId(), pageNum, pageSize);
    }
}
