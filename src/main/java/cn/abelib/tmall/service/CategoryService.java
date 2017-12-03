package cn.abelib.tmall.service;

import cn.abelib.tmall.bean.Category;
import cn.abelib.tmall.util.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by abel on 2017/11/4.
 */
@Service
public interface CategoryService {
    /**
     * 统计Category的总数
     * @return
     */
    Integer countCategory();
    /**
     * 列出所有的Category
     * @return
     */
    List<Category> listAllCategory(Page page);

    /**
     * 插入Category, 并且回填id
     * @param category
     * @return 影响数据库行数
     */
    Integer insertCategory(Category category);

    /**
     * 删除 指定id 的 Category
     * @param id
     * @return
     */
    Integer deleteCategory(Integer id);

    /**
     *  修改 Category
     * @param category
     * @return
     */
    Integer updateCategory(Category category);

    /**
     * 通过 id 获取
     * @param id
     * @return
     */
    Category getCategoryById(Integer id);
}
