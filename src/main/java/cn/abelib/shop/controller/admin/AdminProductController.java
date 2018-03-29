package cn.abelib.shop.controller.admin;

import cn.abelib.shop.common.constant.BusinessConstant;
import cn.abelib.shop.pojo.Product;
import cn.abelib.shop.pojo.User;
import cn.abelib.shop.service.UserService;
import cn.abelib.shop.service.ProductService;
import cn.abelib.shop.common.result.Response;
import cn.abelib.shop.common.constant.StatusConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

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


    /**
     *  列表
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public Response listProduct(HttpSession session,
                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){
        User currentUser = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (currentUser == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        if (userService.checkAdminRole(currentUser).isSuccess()){
            return productService.listProduct(pageNum, pageSize);
        }else {
            return Response.failed(StatusConstant.NOT_ADMIN_ERROR);
        }
    }

    /**
     *  查找
     * @param session
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/search")
    public Response productSearch(HttpSession session, String productName, Integer productId,
                                  @RequestParam(value = "pageNum", defaultValue = "10") Integer pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "1") Integer pageSize) {
        User currentUser = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (currentUser == null) {
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        if (userService.checkAdminRole(currentUser).isSuccess()) {
            return productService.productSearch(productName, productId, pageNum, pageSize);
        } else {
            return Response.failed(StatusConstant.NOT_ADMIN_ERROR);
        }
    }

    /**
     *  保存商品, 需要检查是否是管理员权限
     * @param session
     * @param product
     * @return
     */
    @PostMapping("/save")
    public Response saveProduct(HttpSession session, Product product){
        User currentUser = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (currentUser == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        if (userService.checkAdminRole(currentUser).isSuccess()){
            return productService.saveOrUpdateProduct(product);
        }else {
            return Response.failed(StatusConstant.NOT_ADMIN_ERROR);
        }
    }

    /**
     *  修改商品的销售状态
     * @param session
     * @param productId
     * @param status
     * @return
     */
    @PostMapping("/setStatus")
    public Response setSaleStatus(HttpSession session, Integer productId, Integer status){
        User currentUser = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (currentUser == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        if (userService.checkAdminRole(currentUser).isSuccess()){
            return productService.setSalesStatus(productId, status);
        }else {
            return Response.failed(StatusConstant.NOT_ADMIN_ERROR);
        }
    }

    /**
     *  获取商品详情
     * @param session
     * @param productId
     * @return
     */
    @PostMapping("/detail")
    public Response getDetail(HttpSession session, Integer productId){
        User currentUser = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (currentUser == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        if (userService.checkAdminRole(currentUser).isSuccess()){

        }else {
            return productService.getProductDetail(productId);
        }
        return Response.failed(StatusConstant.NOT_ADMIN_ERROR);
    }

    @GetMapping("/delete")
    public Response deleteProduct(Integer id){
        return Response.failed(StatusConstant.GENERAL_SUCCESS);
    }

    @PostMapping
    public Response upload(){
        return Response.failed(StatusConstant.GENERAL_SUCCESS);
    }
}
