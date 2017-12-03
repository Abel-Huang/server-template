package cn.abelib.tmall.controller;

import cn.abelib.tmall.bean.Category;
import cn.abelib.tmall.service.CategoryService;
import cn.abelib.tmall.util.ImageUtil;
import cn.abelib.tmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by abel on 2017/11/4.
 */
@Controller
@RequestMapping("")
public class CategoryController {
    @Autowired
    private CategoryService categoryService = null;

    @RequestMapping("admin_category_list")
    public ModelAndView listAllCategory(Page page){
        ModelAndView mv= new ModelAndView();
        List<Category> categoryList = categoryService.listAllCategory(page);
        int count = categoryService.countCategory();
        page.setTotal(count);
        mv.addObject("categoryList", categoryList);
        mv.addObject("page", page);
        mv.setViewName("admin/listCategory");
        return mv;
    }

    @RequestMapping(value = "admin_category_insert", method = RequestMethod.POST)
    public ModelAndView insertCategory(Category category, @RequestParam("imageFile")MultipartFile imageFile)throws IOException{
        categoryService.insertCategory(category);
        ModelAndView mv = new ModelAndView();
        // Í¼Æ¬ÉÏ´«
        if (imageFile != null && !imageFile.isEmpty()){
            String fileName = imageFile.getOriginalFilename();
            System.err.print(fileName);

            File fileFolder = new File("H:\\Encode\\j2ee\\tmall-backend\\src\\main\\webapp\\img\\category");
            File file = new File(fileFolder, category.getId() + ".jpg");
            if ( !file.getParentFile().exists())
                file.getParentFile().mkdirs();
            imageFile.transferTo(file);
            BufferedImage img = ImageUtil.change2jpg(file);
            ImageIO.write(img, "jpg", file);
        }
        mv.setViewName("redirect:/admin_category_list");
        return mv;
    }

    @RequestMapping("admin_category_delete")
    public ModelAndView deleteCategory(Integer id){
        ModelAndView mv =new ModelAndView();
        categoryService.deleteCategory(id);
        File fileFolder = new File("H:\\Encode\\j2ee\\tmall-backend\\src\\main\\webapp\\img\\category");
        File file = new File(fileFolder, id.intValue() + ".jpg");
        mv.setViewName("redirect:/admin_category_list");
        return mv;
    }

    @RequestMapping("admin_category_edit")
    public ModelAndView edit(Integer id){
        Category category = categoryService.getCategoryById(id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("category", category);
        mv.setViewName("admin/editCategory");
        return mv;
    }

    @RequestMapping(value = "admin_category_update", method = RequestMethod.POST)
    public ModelAndView updateCategory(Category category, @RequestParam("imageFile")MultipartFile imageFile)throws IOException{
        categoryService.updateCategory(category);
        ModelAndView mv = new ModelAndView();
        if (imageFile != null && !imageFile.isEmpty()){
            File imageFolder = new File("H:\\Encode\\j2ee\\tmall-backend\\src\\main\\webapp\\img\\category");
            File file = new File(imageFolder, category.getId() + ".jpg");
            imageFile.transferTo(file);
            BufferedImage img = ImageUtil.change2jpg(file);
            ImageIO.write(img, "jpg", file);
        }
        mv.setViewName("redirect:/admin_category_list");
        return mv;
    }
}
