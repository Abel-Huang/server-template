package cn.abelib.shop.controller.admin;


import cn.abelib.shop.common.constant.BusinessConstant;
import cn.abelib.shop.common.constant.StatusConstant;
import cn.abelib.shop.common.result.Response;
import cn.abelib.shop.pojo.User;
import cn.abelib.shop.service.OrderService;
import cn.abelib.shop.service.UserService;
import cn.abelib.shop.vo.OrderVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by abel on 2017/12/12.
 */
@RestController
@RequestMapping("/admin/order")
public class AdminOrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/list.do")
    public Response<PageInfo> getOrderList(HttpSession session,
                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){
        User currentUser = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (currentUser == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        if (userService.checkAdminRole(currentUser).isSuccess()){
            return orderService.getOrderList(pageNum, pageSize);
        }else {
            return Response.failed(StatusConstant.NOT_ADMIN_ERROR);
        }
    }

    @PostMapping("/detail.do")
    public Response<OrderVo> getOrderDetail(HttpSession session, Long orderNo){
        User currentUser = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (currentUser == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        if (userService.checkAdminRole(currentUser).isSuccess()){
            return orderService.getOrderDetail(orderNo);
        }else {
            return Response.failed(StatusConstant.NOT_ADMIN_ERROR);
        }
    }

    @PostMapping("/search.do")
    public Response<PageInfo> orderSearch(HttpSession session, Long orderNo,
                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){
        User currentUser = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (currentUser == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        if (userService.checkAdminRole(currentUser).isSuccess()){
            return orderService.orderSearch(orderNo, pageNum, pageSize);
        }else {
            return Response.failed(StatusConstant.NOT_ADMIN_ERROR);
        }
    }

    /**
     *  发货
     * @param session
     * @param orderNo
     * @return
     */
    @PostMapping("/send.do")
    public Response sendGoods(HttpSession session, Long orderNo){
        User currentUser = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (currentUser == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        if (userService.checkAdminRole(currentUser).isSuccess()){
            return orderService.sendGoods(orderNo);
        }else {
            return Response.failed(StatusConstant.NOT_ADMIN_ERROR);
        }
    }
}
