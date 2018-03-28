package cn.abelib.tmall.controller;

import cn.abelib.tmall.bean.Product;
import cn.abelib.tmall.bean.ProductImage;
import cn.abelib.tmall.service.ProductImageService;
import cn.abelib.tmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by abel on 2017/11/16.
 */
@Controller
@RequestMapping("")
public class ProductImageController {
    @Autowired
    private ProductService productService = null;
    @Autowired
    private ProductImageService productImageService = null;

    @RequestMapping("admin_productImage_insert")
    public ModelAndView insertProductImage(){
        ModelAndView mv = new ModelAndView();
        return mv;
    }

    @RequestMapping("admin_productImage_delete")
    public ModelAndView deleteProductImage(){
        ModelAndView mv = new ModelAndView();
        return mv;
    }

    @RequestMapping("admin_productImage_list")
    public ModelAndView listAllProductImage(Integer productId){
        ModelAndView mv = new ModelAndView();
        Product product = productService.getProductById(productId);
        List<ProductImage> singleList = productImageService.listAllProductImage(productId, ProductImageService.type_single);
        List<ProductImage> detailList = productImageService.listAllProductImage(productId, ProductImageService.type_detail);
        mv.addObject("product", product);
        mv.addObject("singleList", singleList);
        mv.addObject("detailList", detailList);
        mv.setViewName("admin/listProductImage");
        return mv;
    }
}
