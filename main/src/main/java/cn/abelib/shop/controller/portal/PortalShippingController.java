package cn.abelib.shop.controller.portal;


import cn.abelib.common.constant.StatusConstant;
import cn.abelib.common.result.Response;
import cn.abelib.common.tools.CookieUtil;
import cn.abelib.common.tools.JsonUtil;
import cn.abelib.data.redis.RedisStringService;
import cn.abelib.shop.pojo.Shipping;
import cn.abelib.shop.pojo.User;
import cn.abelib.shop.service.ShippingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author abel
 * @date 2017/9/12
 * 用户收获地址
 */
@Slf4j
@RestController
@RequestMapping("/portal/shipping")
public class PortalShippingController {
    @Autowired
    private ShippingService shippingService;
    @Autowired
    private RedisStringService redisStringService;

    /**
     *  新增收货地址
     * @param request
     * @param shipping
     * @return
     */
    @PostMapping("/add")
    public Response add(HttpServletRequest request, Shipping shipping){
        String token = CookieUtil.readToken(request);
        if (StringUtils.isEmpty(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        String userJson = redisStringService.get(token);
        User user = JsonUtil.str2Obj(userJson, User.class);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        log.info("shipping {}", request.getRequestURI(), shipping);
        return shippingService.add(user.getId(), shipping);
    }

    @PostMapping("/delete")
    public Response delete(HttpServletRequest request, Integer shippingId){
        String token = CookieUtil.readToken(request);
        if (StringUtils.isEmpty(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        String userJson = redisStringService.get(token);
        User user = JsonUtil.str2Obj(userJson, User.class);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        log.info("shippingId {}", request.getRequestURI(), shippingId);
        return shippingService.del(user.getId(), shippingId);
    }

    @PostMapping("/update")
    public Response update(HttpServletRequest request, Shipping shipping){
        String token = CookieUtil.readToken(request);
        if (StringUtils.isEmpty(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        String userJson = redisStringService.get(token);
        User user = JsonUtil.str2Obj(userJson, User.class);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        log.info("shipping {}", request.getRequestURI(), shipping);
        return shippingService.update(user.getId(), shipping);
    }

    @GetMapping("/select")
    public Response select(HttpServletRequest request, Integer shippingId){
        String token = CookieUtil.readToken(request);
        if (StringUtils.isEmpty(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        String userJson = redisStringService.get(token);
        User user = JsonUtil.str2Obj(userJson, User.class);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        log.info("shippingId {}", request.getRequestURI(), shippingId);
        return shippingService.select(user.getId(), shippingId);
    }

    @GetMapping("/list")
    public Response listProduct(HttpServletRequest request,
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
        return shippingService.list(user.getId(), pageNum, pageSize);
    }
}
