package cn.abelib.tmall.controller;

import cn.abelib.tmall.bean.Category;
import cn.abelib.tmall.bean.Property;
import cn.abelib.tmall.service.CategoryService;
import cn.abelib.tmall.service.PropertyService;
import cn.abelib.tmall.util.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.List;

/**
 * Created by abel on 2017/11/6.
 */
@Controller
@RequestMapping("")
public class PropertyController {
    @Autowired
    private PropertyService propertyService = null;
    @Autowired
    private CategoryService categoryService = null;

    @RequestMapping("/admin_property_list")
    public ModelAndView listAllProperty(Integer categoryId, Page page){
        Category category = categoryService.getCategoryById(categoryId);
        ModelAndView mv =new ModelAndView();
        PageHelper.offsetPage(page.getStart(), page.getCount());
        List<Property> propertyList = propertyService.listAllProperty(categoryId);
        int total = (int) new PageInfo<>(propertyList).getTotal();
        page.setTotal(total);
        page.setParam("&categoryId=" + category.getId());
        mv.addObject("propertyList", propertyList);
        mv.addObject("category", category);
        mv.addObject("page", page);
        mv.setViewName("admin/listProperty");
        return mv;
    }

    @RequestMapping("admin_property_insert")
    public ModelAndView insertProperty(Property property){
        ModelAndView mv = new ModelAndView();
        propertyService.insertProperty(property);
        mv.setViewName("redirect:admin_property_list?categoryId="+property.getCategoryId());
        return mv;
    }

    @RequestMapping("admin_property_delete")
    public ModelAndView deleteProperty(Integer id){
        ModelAndView mv = new ModelAndView();
        Property property = propertyService.getPropertyById(id);
        propertyService.deleteProperty(id);
        mv.setViewName("redirect:admin_property_list?categoryId="+property.getCategoryId());
        return mv;
    }

    @RequestMapping("admin_property_edit")
    public ModelAndView editProperty(Integer id){
        ModelAndView mv = new ModelAndView();
        Property property = propertyService.getPropertyById(id);
        Category category = categoryService.getCategoryById(property.getCategoryId());
        property.setCategory(category);
        mv.addObject("property", property);
        mv.addObject("category", category);
        mv.setViewName("admin/editProperty");
        return mv;
    }

    @RequestMapping("admin_property_update")
    public ModelAndView updateProper(Property property){
        ModelAndView mv = new ModelAndView();
        propertyService.updateProperty(property);
        mv.setViewName("redirect:admin_property_list?categoryId="+property.getCategoryId());
        return mv;
    }
}
