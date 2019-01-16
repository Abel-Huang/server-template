package cn.abelib.shop.controller.portal;


import cn.abelib.data.redis.RedisStringService;
import cn.abelib.common.constant.BusinessConstant;
import cn.abelib.common.constant.StatusConstant;
import cn.abelib.common.result.Response;
import cn.abelib.common.tools.CookieUtil;
import cn.abelib.common.tools.JsonUtil;
import cn.abelib.shop.pojo.User;
import cn.abelib.shop.service.CartService;
import cn.abelib.shop.vo.CartVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;




/**
 *
 * @author abel
 * @date 2017/9/12
 * 用户购物车模块接口
 */
@RestController
@RequestMapping("/portal/cart")
public class PortalCartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private RedisStringService redisStringService;

    /**
     *  添加商品到购物车
     * @param request
     * @param count
     * @param productId
     * @return
     */
    @PostMapping("/add")
    public Response<CartVo> add(HttpServletRequest request, Integer count, Integer productId){
        String token = CookieUtil.readToken(request);
        if (StringUtils.isEmpty(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        String userJson = redisStringService.get(token);
        User user = JsonUtil.str2Obj(userJson, User.class);
        if (user != null){
            return cartService.add(user.getId(), productId, count);
        }
        return Response.failed(StatusConstant.USER_NOT_LOGIN);
    }

    /**
     *  更新
     * @param request
     * @param count
     * @param productId
     * @return
     */
    @PostMapping("/update")
    public Response<CartVo> update(HttpServletRequest request, Integer count, Integer productId){
        String token = CookieUtil.readToken(request);
        if (StringUtils.isEmpty(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        String userJson = redisStringService.get(token);
        User user = JsonUtil.str2Obj(userJson, User.class);
        if (user != null){
            return cartService.update(user.getId(), productId, count);
        }
        return Response.failed(StatusConstant.USER_NOT_LOGIN);
    }

    @PostMapping("/delete")
    public Response<CartVo> delete(HttpServletRequest request, String productIds){
        String token = CookieUtil.readToken(request);
        if (StringUtils.isEmpty(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        String userJson = redisStringService.get(token);
        User user = JsonUtil.str2Obj(userJson, User.class);
        if (user != null){
            return cartService.delete(user.getId(), productIds);
        }
        return Response.failed(StatusConstant.USER_NOT_LOGIN);
    }

    @GetMapping("/list")
    public Response<CartVo> list(HttpServletRequest request) {
        String token = CookieUtil.readToken(request);
        if (StringUtils.isEmpty(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        String userJson = redisStringService.get(token);
        User user = JsonUtil.str2Obj(userJson, User.class);
        if (user != null){
            return cartService.list(user.getId());
        }
        return Response.failed(StatusConstant.USER_NOT_LOGIN);
    }

    // 全选
    @GetMapping("/selectAll")
    public Response<CartVo> selectAll(HttpServletRequest request) {
        String token = CookieUtil.readToken(request);
        if (StringUtils.isEmpty(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        String userJson = redisStringService.get(token);
        User user = JsonUtil.str2Obj(userJson, User.class);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return cartService.selectOrUnselectAll(user.getId(), BusinessConstant.Cart.CHECKED);
    }

    // 全反选
    @GetMapping("/unSelectAll")
    public Response<CartVo> unSelectAll(HttpServletRequest request) {
        String token = CookieUtil.readToken(request);
        if (StringUtils.isEmpty(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        String userJson = redisStringService.get(token);
        User user = JsonUtil.str2Obj(userJson, User.class);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return cartService.selectOrUnselectAll(user.getId(), BusinessConstant.Cart.UN_CHECKED);
    }

    // 单选
    @GetMapping("/select")
    public Response<CartVo> select(HttpServletRequest request, Integer productId) {
        String token = CookieUtil.readToken(request);
        if (StringUtils.isEmpty(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        String userJson = redisStringService.get(token);
        User user = JsonUtil.str2Obj(userJson, User.class);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return cartService.selectOrUnselect(user.getId(), productId, BusinessConstant.Cart.CHECKED);
    }

    // 单反选
    @GetMapping("/unSelect")
    public Response<CartVo> unSelect(HttpServletRequest request, Integer productId) {
        String token = CookieUtil.readToken(request);
        if (StringUtils.isEmpty(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        String userJson = redisStringService.get(token);
        User user = JsonUtil.str2Obj(userJson, User.class);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return cartService.selectOrUnselect(user.getId(), productId, BusinessConstant.Cart.UN_CHECKED);
    }

    // 查询用户当前购物车的产品数量，计算的是所有的产品的总数量
    @GetMapping("cartCount")
    public Response<Integer> getCartProductCount(HttpServletRequest request){
        String token = CookieUtil.readToken(request);
        if (StringUtils.isEmpty(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        String userJson = redisStringService.get(token);
        User user = JsonUtil.str2Obj(userJson, User.class);
        if (user == null){
            return Response.success(StatusConstant.GENERAL_SUCCESS, 0);
        }
        return cartService.getCartProductCount(user.getId());
    }
}
