package cn.abelib.tmall.controller;

import cn.abelib.tmall.bean.User;
import cn.abelib.tmall.service.UserService;
import cn.abelib.tmall.util.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by abel on 2017/11/12.
 */
@Controller
@RequestMapping("")
public class UserController {
    @Autowired
    private UserService userService = null;

    @RequestMapping("admin_user_list")
    public ModelAndView listAllUser(Page page){
        ModelAndView mv = new ModelAndView();
        PageHelper.offsetPage(page.getStart(), page.getCount());
        List<User> userList = userService.listAllUser();
        int total = (int) new PageInfo<>(userList).getTotal();
        page.setTotal(total);
        mv.addObject("userList", userList);
        mv.addObject("page", page);
        mv.setViewName("admin/listUser");
        return mv;
    }
}
