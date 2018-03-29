package cn.abelib.shop.controller.admin;


import cn.abelib.shop.common.constant.BusinessConstant;
import cn.abelib.shop.pojo.Category;
import cn.abelib.shop.common.result.Response;
import cn.abelib.shop.common.constant.StatusConstant;
import cn.abelib.shop.pojo.User;
import cn.abelib.shop.service.CategoryService;
import cn.abelib.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by abel on 2017/9/4.
 * 分类管理接口
 */
@RestController
@RequestMapping("/admin/category")
public class AdminCategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/add")
    public Response addCategory(HttpSession session, String categoryName,
                                        @RequestParam(value = "parentId", defaultValue = "0") Integer parentId){
        User currentUser = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (currentUser == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        if (userService.checkAdminRole(currentUser).isSuccess()){
            return categoryService.addCategory(categoryName, parentId);
        }else {
            return Response.failed(StatusConstant.NOT_ADMIN_ERROR);
        }
    }

    @GetMapping("/setCategoryName")
    public Response setCategoryName(HttpSession session, String categoryName, Integer categoryId){
        User currentUser = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (currentUser == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        if (userService.checkAdminRole(currentUser).isSuccess()){
            return categoryService.updateCategoryByName(categoryId, categoryName);
        }else {
            return Response.failed(StatusConstant.NOT_ADMIN_ERROR);
        }
    }

    @PostMapping(value = "/getChildrenList")
    public Response<List<Category>> getChildrenParallelCategory(HttpSession session,
                                                      @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId){
        User currentUser = (User) session.getAttribute(BusinessConstant.CURRENT_USER);
        if (currentUser == null){
            return Response.failed(StatusConstant.USER_NOT_LOGIN);
        }
        if (userService.checkAdminRole(currentUser).isSuccess()){
            return categoryService.getChildrenParallelCategory(categoryId);
        }else {
            return Response.failed(StatusConstant.NOT_ADMIN_ERROR);
        }
    }
}
