package cn.abelib.tmall.controller;

import cn.abelib.tmall.bean.Category;
import cn.abelib.tmall.bean.Product;
import cn.abelib.tmall.service.CategoryService;
import cn.abelib.tmall.service.ProductService;
import cn.abelib.tmall.util.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by abel on 2017/11/9.
 */
@Controller
@RequestMapping("")
public class ProductController {
    @Autowired
    private CategoryService categoryService = null;
    @Autowired
    private ProductService productService = null;

    @RequestMapping("admin_product_list")
    public ModelAndView listAllProduct(Integer categoryId, Page page){
        ModelAndView mv = new ModelAndView();
        PageHelper.offsetPage(page.getStart(), page.getCount());
        Category category = categoryService.getCategoryById(categoryId);
        List<Product> productList = productService.listAllProduct(categoryId);
        int total = (int) new PageInfo<>(productList).getTotal();
        page.setTotal(total);
        page.setParam("&categoryId=" + category.getId());
        mv.addObject("productList", productList);
        mv.addObject("category", category);
        mv.addObject("page", page);

        mv.setViewName("admin/listProduct");
        return mv;
    }

    @RequestMapping("admin_product_insert")
    public ModelAndView insertProduct(Product product){
        ModelAndView mv = new ModelAndView();
        productService.insertProduct(product);
        mv.setViewName("redirect:admin_product_list?categoryId="+product.getCategoryId());
        return mv;
    }

    @RequestMapping("admin_product_delete")
    public ModelAndView deleteProduct(Integer id){
        ModelAndView mv = new ModelAndView();
        Product product = productService.getProductById(id);
        productService.deleteProduct(id);
        mv.setViewName("redirect:admin_product_list?categoryId="+product.getCategoryId());
        return mv;
    }

    @RequestMapping("admin_product_edit")
    public ModelAndView editProduct(Integer id){
        ModelAndView mv = new ModelAndView();
        Product product = productService.getProductById(id);
        Category category = categoryService.getCategoryById(product.getCategoryId());
        product.setCategory(category);
        mv.addObject("product", product);
        mv.addObject("category", category);
        mv.setViewName("admin/editProduct");
        return mv;
    }

    @RequestMapping("admin_product_update")
    public ModelAndView updateProduct(Product product){
        ModelAndView mv =new ModelAndView();
        productService.updateProduct(product);
        mv.setViewName("redirect:admin_product_list?categoryId="+product.getCategoryId());
        return mv;
    }
}
