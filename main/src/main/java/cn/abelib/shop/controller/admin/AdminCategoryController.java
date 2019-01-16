package cn.abelib.shop.controller.admin;


import cn.abelib.common.tools.CookieUtil;
import cn.abelib.common.tools.JsonUtil;
import cn.abelib.data.redis.RedisStringService;
import cn.abelib.shop.pojo.Category;
import cn.abelib.common.result.Response;
import cn.abelib.common.constant.StatusConstant;
import cn.abelib.shop.pojo.User;
import cn.abelib.shop.service.CategoryService;
import cn.abelib.shop.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * @author abel
 * @date 2017/9/4
 * 分类管理接口
 */
@RestController
@RequestMapping("/admin/category")
public class AdminCategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisStringService redisStringService;

    @PostMapping(value = "/add.do")
    public Response addCategory(HttpServletRequest request, String categoryName,
                                @RequestParam(value = "parentId", defaultValue = "0") Integer parentId){
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
            return categoryService.addCategory(categoryName, parentId);
        }else {
            return Response.failed(StatusConstant.NOT_ADMIN_ERROR);
        }
    }

    @GetMapping("/set_category_name.do")
    public Response setCategoryName(HttpServletRequest request, String categoryName, Integer categoryId){
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
            return categoryService.updateCategoryByName(categoryId, categoryName);
        }else {
            return Response.failed(StatusConstant.NOT_ADMIN_ERROR);
        }
    }

    @PostMapping(value = "/get_children_list.do")
    public Response<List<Category>> getChildrenParallelCategory(HttpServletRequest request,
                                                      @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId){
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
            return categoryService.getChildrenParallelCategory(categoryId);
        }else {
            return Response.failed(StatusConstant.NOT_ADMIN_ERROR);
        }
    }
}
