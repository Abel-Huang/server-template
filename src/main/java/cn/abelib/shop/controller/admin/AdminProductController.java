package cn.abelib.shop.controller.admin;

import cn.abelib.shop.service.redis.RedisStringService;
import cn.abelib.shop.common.tools.CookieUtil;
import cn.abelib.shop.common.tools.JsonUtil;
import cn.abelib.shop.pojo.Product;
import cn.abelib.shop.pojo.User;
import cn.abelib.shop.service.UserService;
import cn.abelib.shop.service.ProductService;
import cn.abelib.shop.common.result.Response;
import cn.abelib.shop.common.constant.StatusConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by abel on 2017/9/9.
 */
@RestController
@RequestMapping("/admin/product")
public class AdminProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisStringService redisStringService;

    /**
     *  列表
     * @param request
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list.do")
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
        if (userService.checkAdminRole(user).isSuccess()){
            return productService.listProduct(pageNum, pageSize);
        }else {
            return Response.failed(StatusConstant.NOT_ADMIN_ERROR);
        }
    }

    /**
     *  查找
     * @param request
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/search.do")
    public Response productSearch(HttpServletRequest request, String productName, Integer productId,
                                  @RequestParam(value = "pageNum", defaultValue = "10") Integer pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "1") Integer pageSize) {
        String token = CookieUtil.readToken(request);
        if (StringUtils.isEmpty(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        String userJson = redisStringService.get(token);
        User user = JsonUtil.str2Obj(userJson, User.class);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return productService.productSearch(productName, productId, pageNum, pageSize);
        } else {
            return Response.failed(StatusConstant.NOT_ADMIN_ERROR);
        }
    }

    /**
     *  保存商品, 需要检查是否是管理员权限
     * @param request
     * @param product
     * @return
     */
    @PostMapping("/save.do")
    public Response saveProduct(HttpServletRequest request, Product product){
        String token = CookieUtil.readToken(request);
        if (StringUtils.isEmpty(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        String userJson = redisStringService.get(token);
        User user = JsonUtil.str2Obj(userJson, User.class);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        if (userService.checkAdminRole(user).isSuccess()){
            return productService.saveOrUpdateProduct(product);
        }else {
            return Response.failed(StatusConstant.NOT_ADMIN_ERROR);
        }
    }

    /**
     *  修改商品的销售状态
     * @param request
     * @param productId
     * @param status
     * @return
     */
    @PostMapping("/set_status.do")
    public Response setSaleStatus(HttpServletRequest request, Integer productId, Integer status){
        String token = CookieUtil.readToken(request);
        if (StringUtils.isEmpty(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        String userJson = redisStringService.get(token);
        User user = JsonUtil.str2Obj(userJson, User.class);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        if (userService.checkAdminRole(user).isSuccess()){
            return productService.setSalesStatus(productId, status);
        }else {
            return Response.failed(StatusConstant.NOT_ADMIN_ERROR);
        }
    }

    /**
     *  获取商品详情
     * @param request
     * @param productId
     * @return
     */
    @PostMapping("/detail.do")
    public Response getDetail(HttpServletRequest request, Integer productId){
        String token = CookieUtil.readToken(request);
        if (StringUtils.isEmpty(token)){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        String userJson = redisStringService.get(token);
        User user = JsonUtil.str2Obj(userJson, User.class);
        if (user == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        if (userService.checkAdminRole(user).isSuccess()){

        }else {
            return productService.getProductDetail(productId);
        }
        return Response.failed(StatusConstant.NOT_ADMIN_ERROR);
    }

    @GetMapping("/delete.do")
    public Response deleteProduct(Integer id){
        return Response.failed(StatusConstant.GENERAL_SUCCESS);
    }

    @PostMapping
    public Response upload(){
        return Response.failed(StatusConstant.GENERAL_SUCCESS);
    }
}
