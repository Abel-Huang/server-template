package cn.abelib.shop.controller.portal;


import cn.abelib.shop.common.constant.BusinessConstant;
import cn.abelib.shop.common.constant.StatusConstant;
import cn.abelib.shop.common.result.Response;
import cn.abelib.shop.pojo.User;
import cn.abelib.shop.service.CartService;
import cn.abelib.shop.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;



/**
 * Created by abel on 2017/9/12.
 * 用户购物车模块接口
 */
@RestController
@RequestMapping("/portal/cart")
public class PortalCartController {
    @Autowired
    private CartService cartService;

    /**
     *  添加商品到购物车
     * @param session
     * @param count
     * @param productId
     * @return
     */
    @PostMapping("/add")
    public Response<CartVo> add(HttpSession session, Integer count, Integer productId){
        User user = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return cartService.add(user.getId(), productId, count);
    }

    @PostMapping("/update")
    public Response<CartVo> update(HttpSession session, Integer count, Integer productId){
        User user = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return cartService.update(user.getId(), productId, count);
    }

    @PostMapping("/delete")
    public Response<CartVo> delete(HttpSession session, String productIds){
        User user = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return cartService.delete(user.getId(), productIds);
    }

    @GetMapping("/list")
    public Response<CartVo> list(HttpSession session) {
        User user = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (user == null) {
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return cartService.list(user.getId());
    }

    // 全选
    @GetMapping("/selectAll")
    public Response<CartVo> selectAll(HttpSession session) {
        User user = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (user == null) {
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return cartService.selectOrUnselectAll(user.getId(), BusinessConstant.Cart.CHECKED);
    }

    // 全反选
    @GetMapping("/unSelectAll")
    public Response<CartVo> unSelectAll(HttpSession session) {
        User user = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (user == null) {
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return cartService.selectOrUnselectAll(user.getId(), BusinessConstant.Cart.UN_CHECKED);
    }

    // 单选
    @GetMapping("/select")
    public Response<CartVo> select(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (user == null) {
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return cartService.selectOrUnselect(user.getId(), productId, BusinessConstant.Cart.CHECKED);
    }

    // 单反选
    @GetMapping("/unSelect")
    public Response<CartVo> unSelect(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (user == null) {
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return cartService.selectOrUnselect(user.getId(), productId, BusinessConstant.Cart.UN_CHECKED);
    }

    // 查询用户当前购物车的产品数量，计算的是所有的产品的总数量
    @GetMapping("cartCount")
    public Response<Integer> getCartProductCount(HttpSession session){
        User user = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (user == null) {
            return Response.success(StatusConstant.GENERAL_SUCCESS, 0);
        }
        return cartService.getCartProductCount(user.getId());
    }
}
