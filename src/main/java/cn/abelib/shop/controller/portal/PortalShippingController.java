package cn.abelib.shop.controller.portal;

import cn.abelib.shop.common.constant.BusinessConstant;
import cn.abelib.shop.common.constant.StatusConstant;
import cn.abelib.shop.common.result.Response;
import cn.abelib.shop.pojo.Shipping;
import cn.abelib.shop.pojo.User;
import cn.abelib.shop.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by abel on 2017/9/12.
 * 用户收获地址
 */
@RestController
@RequestMapping("/portal/shipping")
public class PortalShippingController {
    @Autowired
    private ShippingService shippingService;

    /**
     *  新增收货地址
     * @param session
     * @param shipping
     * @return
     */
    @PostMapping("/add")
    public Response add(HttpSession session, Shipping shipping){
        User user = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return shippingService.add(user.getId(), shipping);
    }

    @PostMapping("/delete")
    public Response delete(HttpSession session, Integer shippingId){
        User user = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return shippingService.del(user.getId(), shippingId);
    }

    @PostMapping("/update")
    public Response update(HttpSession session, Shipping shipping){
        User user = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return shippingService.update(user.getId(), shipping);
    }

    @GetMapping("/select")
    public Response select(HttpSession session, Integer shippingId){
        User user = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return shippingService.select(user.getId(), shippingId);
    }

    @GetMapping("/list")
    public Response listProduct(HttpSession session,
                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){
        User currentUser = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (currentUser == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        return shippingService.list(currentUser.getId(), pageNum, pageSize);
    }
}
