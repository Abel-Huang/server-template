package cn.abelib.tmall.controller;

import cn.abelib.tmall.bean.Product;
import cn.abelib.tmall.bean.PropertyValue;
import cn.abelib.tmall.service.ProductService;
import cn.abelib.tmall.service.PropertyValueService;
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
public class PropertyValueController {
    @Autowired
    private PropertyValueService propertyValueService = null;
    @Autowired
    private ProductService productService = null;

    @RequestMapping("admin_propertyValue_edit")
    public ModelAndView editPropertyValue(Integer productId){
        ModelAndView mv = new ModelAndView();
        Product product = productService.getProductById(productId);
        propertyValueService.init(product);
        List<PropertyValue> propertyValueList = propertyValueService.listAllPropertyValue(product.getId());
        mv.addObject("propertyValueList", propertyValueList);
        mv.addObject("product", product);
        mv.setViewName("admin/editPropertyValue");
        return mv;
    }

    @RequestMapping("admin_propertyValue_update")
    public ModelAndView updatePropertyValue(PropertyValue propertyValue){
        ModelAndView mv = new ModelAndView();
        propertyValueService.updatePropertyValue(propertyValue);
        mv.setViewName("admin/editPropertyValue");
        return mv;
    }
}
