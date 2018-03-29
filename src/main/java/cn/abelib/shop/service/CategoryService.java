package cn.abelib.shop.service;

import cn.abelib.shop.common.result.Response;
import cn.abelib.shop.pojo.Category;

import java.util.List;


/**
 * Created by abel on 2017/11/4.
 */

public interface CategoryService {
    Response<String> addCategory(String categoryName, Integer parentId);

    Response<String> updateCategoryByName(Integer categoryId, String categoryName) ;Response<List<Category>>  getChildrenParallelCategory(Integer categoryId);

    Response<List<Integer>>  getCategoryAndChildrenById(Integer categoryId);

}
