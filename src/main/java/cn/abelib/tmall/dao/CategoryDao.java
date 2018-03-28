package cn.abelib.tmall.dao;

import cn.abelib.tmall.bean.Category;
import cn.abelib.tmall.util.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by abel on 2017/11/4.
 */
@Repository
public interface CategoryDao {
    Integer countCategory();

    List<Category> listAllCategory(Page page);

    Integer insertCategory(Category category);

    Integer deleteCategory(Integer id);

    Integer updateCategory(Category category);

    Category getCategoryById(Integer id);
}
