package cn.abelib.shop.service.impl;


import cn.abelib.shop.common.constant.BusinessConstant;
import cn.abelib.shop.pojo.Category;
import cn.abelib.shop.common.exception.GlobalException;
import cn.abelib.shop.common.result.Response;
import cn.abelib.shop.common.constant.StatusConstant;
import cn.abelib.shop.dao.CategoryDao;
import cn.abelib.shop.service.CategoryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * Created by abel on 2017/9/4.
 */
@Service
public class CategoryServiceImpl implements CategoryService{
    private static Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryDao categoryDao;

    /**
     *  添加分类
     * @param categoryName
     * @param parentId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Response<String> addCategory(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)){
            throw new GlobalException(StatusConstant.PRAM_BIND_ERROR);
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(BusinessConstant.Category.STATUS_TRUE);

        Integer rowCount = categoryDao.insert(category);
        if (rowCount > 0){
            return Response.success(StatusConstant.GENERAL_SUCCESS);
        }
        return  Response.success(StatusConstant.ADD_CAT_FAILED);
    }

    /**
     *  修改分类名称
     * @param categoryId
     * @param categoryName
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Response<String> updateCategoryByName(Integer categoryId, String categoryName) {
        if (categoryId == null || StringUtils.isBlank(categoryName)){
            throw new GlobalException(StatusConstant.PRAM_BIND_ERROR);
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryId);
        Integer rowCount = categoryDao.update(category);
        if (rowCount > 0){
            return Response.success(StatusConstant.GENERAL_SUCCESS);
        }
        return  Response.failed(StatusConstant.UPDATE_CAT_FAILED);
    }

    /**
     *  通过id获取子节点(子分类)的category列表
     * @param categoryId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Response<List<Category>>  getChildrenParallelCategory(Integer categoryId){
        List<Category> categoryList = categoryDao.getCategoryChildrenByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)){
            logger.info("未找到当前分类的子分类");
        }
        return Response.success(StatusConstant.GENERAL_SUCCESS, categoryList);
    }

    /**
     *  递归查询本节点的id及孩子节点的列表
     * @param categoryId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Response<List<Integer>>  getCategoryAndChildrenById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        getChildrenCategory(categorySet, categoryId);

        List<Integer> categoryIdList = Lists.newArrayList();
        if (categoryId != null){
            for (Category item : categorySet){
                categoryIdList.add(item.getId());
            }
        }
        return Response.success(StatusConstant.GENERAL_SUCCESS, categoryIdList);
    }

    /**
     *  递归算法
     * @param categorySet
     * @param categoryId
     * @return
     */
    private Set<Category> getChildrenCategory(Set<Category> categorySet, Integer categoryId){
        Category category = categoryDao.selectByCategoryId(categoryId);
        if (categoryId != null){
            categorySet.add(category);
        }
        List<Category> categoryList = categoryDao.getCategoryChildrenByParentId(categoryId);
        for (Category item : categoryList){
            getChildrenCategory(categorySet, item.getId());
        }
        return categorySet;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Category getCategoryById(Integer id) {
        return categoryDao.selectByCategoryId(id);
    }
}
