package cn.abelib.biz.service;

import cn.abelib.st.core.result.Response;
import cn.abelib.biz.pojo.Category;

import java.util.List;


/**
 *
 * @author abel
 * @date 2017/11/4
 */

public interface CategoryService {
    Response<String> addCategory(String categoryName, Integer parentId);

    Response<String> updateCategoryByName(Integer categoryId, String categoryName);

    Response<List<Category>>  getChildrenParallelCategory(Integer categoryId);

    Response<List<Integer>>  getCategoryAndChildrenById(Integer categoryId);

}
