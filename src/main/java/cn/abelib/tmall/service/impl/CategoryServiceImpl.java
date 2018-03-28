package cn.abelib.tmall.service.impl;

import cn.abelib.tmall.bean.Category;
import cn.abelib.tmall.dao.CategoryDao;
import cn.abelib.tmall.service.CategoryService;
import cn.abelib.tmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by abel on 2017/11/4.
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryDao categoryDao = null;

    public Integer countCategory() {
        return categoryDao.countCategory();
    }

    public List<Category> listAllCategory(Page page) {
        return categoryDao.listAllCategory(page);
    }

    public Integer insertCategory(Category category) {
        return categoryDao.insertCategory(category);
    }

    public Integer deleteCategory(Integer id) {
        return categoryDao.deleteCategory(id);
    }

    public Integer updateCategory(Category category) {
        return categoryDao.updateCategory(category);
    }

    public Category getCategoryById(Integer id) {
        return categoryDao.getCategoryById(id);
    }
}
