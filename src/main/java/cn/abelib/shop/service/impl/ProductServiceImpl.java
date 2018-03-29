package cn.abelib.shop.service.impl;


import cn.abelib.shop.common.constant.BusinessConstant;
import cn.abelib.shop.common.exception.GlobalException;
import cn.abelib.shop.common.result.Response;
import cn.abelib.shop.common.constant.StatusConstant;
import cn.abelib.shop.common.tools.DateUtil;
import cn.abelib.shop.common.tools.PropertiesUtil;
import cn.abelib.shop.pojo.Category;
import cn.abelib.shop.pojo.Product;
import cn.abelib.shop.dao.CategoryDao;
import cn.abelib.shop.dao.ProductDao;
import cn.abelib.shop.service.CategoryService;
import cn.abelib.shop.service.ProductService;
import cn.abelib.shop.vo.ProductDetailVo;
import cn.abelib.shop.vo.ProductListVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by abel on 2017/9/9.
 */
@Service
public class ProductServiceImpl implements ProductService{
    private static Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductDao productDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private CategoryService categoryService;
    /**
     *  分页list
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Response<PageInfo> listProduct(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productDao.list();
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product product : productList){
            ProductListVo productListVo = getProductListVo(product);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return Response.success(StatusConstant.GENERAL_SUCCESS, pageInfo);
    }

    private ProductListVo getProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setStatus(product.getStatus());
        productListVo.setSubTitle(product.getSubTitle());
        productListVo.setPrice(product.getPrice());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setCategoryId(product.getCategoryId());

        // 会将imageHost放到配置文件中
        productListVo.setImageHost(PropertiesUtil.getProperty("", ""));
        return productListVo;
    }

    public Response<PageInfo> productSearch(String productName, Integer productId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(productName)){
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList = productDao.selectProductByIdOrName(productName, productId);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product product : productList){
            ProductListVo productListVo = getProductListVo(product);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return Response.success(StatusConstant.GENERAL_SUCCESS, pageInfo);
    }

    /**
     *  新增或者更新Product商品
     * @param product
     * @return
     */
    public Response<String> saveOrUpdateProduct(Product product) {
       if (product != null){
           if (StringUtils.isNotBlank(product.getMainImage())){
               String[] subImageArray = product.getSubImages().split(",");
               logger.info("mainImage: " + subImageArray[0]);
               if (subImageArray.length > 0){
                   product.setMainImage(subImageArray[0]);
               }
           }
           //判断是否是需要进行更新操作
           if (product.getId() != null){
                int rowCount = productDao.updateProduct(product);
                if (rowCount > 0){
                    return Response.success(StatusConstant.GENERAL_SUCCESS);
                }
                return Response.failed(StatusConstant.UPDATE_PRODUCT_FAILED);
           }else {
                int rowCount = productDao.insert(product);
               if (rowCount > 0){
                   return Response.success(StatusConstant.GENERAL_SUCCESS);
               }
               return Response.failed(StatusConstant.ADD_PRODUCT_FAILED);
           }
       }
        return Response.failed(StatusConstant.UPDATE_ADD_PRODUCT_FAILED);
    }

    /**
     *
     * @param productId
     * @param status
     * @return
     */
    public Response<String> setSalesStatus(Integer productId, Integer status) {
        if (productId == null || status == null){
            throw new GlobalException(StatusConstant.PRAM_BIND_ERROR);
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productDao.updateProduct(product);
        if (rowCount > 0){
            return Response.success(StatusConstant.GENERAL_SUCCESS);
        }
        return Response.failed(StatusConstant.UPDATE_PRODUCT_STATUS_FAILED);
    }

    /**
     *  后台用户
     * @param productId
     * @return
     */
    public Response<ProductDetailVo> getProductDetail(Integer productId){
        if (productId == null){
            throw new GlobalException(StatusConstant.PRAM_BIND_ERROR);
        }
        Product product = productDao.selectById(productId);
        if (product == null){
            Response.failed(StatusConstant.PRODUCT_NOT_FOUND);
        }
        ProductDetailVo productDetailVo = getProductDetailVo(product);
        return  Response.success(StatusConstant.GENERAL_SUCCESS, productDetailVo);
    }

    private ProductDetailVo getProductDetailVo(Product product){
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setSubTitle(product.getSubTitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setName(product.getName());
        productDetailVo.setDetail(product.getDetail());

        // 会将imageHost放到配置文件中
        productDetailVo.setImageHost(PropertiesUtil.getProperty("", ""));

        Category category = categoryDao.selectByCategoryId(product.getCategoryId());
        if (category == null){
            productDetailVo.setParentCategoryId(0);
        }else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }
        productDetailVo.setCreateTime(DateUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setCreateTime(DateUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }

    public Integer deleteProduct(Integer id) {
        return productDao.delete(id);
    }

    /**
     *  前台用户
     * @param productId
     * @return
     */
    public Response<ProductDetailVo> getProductDetailPortal(Integer productId){
        if (productId == null){
            throw new GlobalException(StatusConstant.PRAM_BIND_ERROR);
        }
        Product product = productDao.selectById(productId);
        if (product == null){
            Response.failed(StatusConstant.PRODUCT_NOT_FOUND);
        }
        if (product.getStatus() != BusinessConstant.ProductStatusEnum.ON_SALE.getCode()){
            Response.failed(StatusConstant.PRODUCT_NOT_FOUND);
        }
        ProductDetailVo productDetailVo = getProductDetailVo(product);
        return  Response.success(StatusConstant.GENERAL_SUCCESS, productDetailVo);
    }

    /**
     *  前台用户的搜索
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    public Response<PageInfo> getProductByKeywordOrCategory(String keyword, Integer categoryId, Integer pageNum, Integer pageSize, String orderBy){
        if (categoryId == null && StringUtils.isBlank(keyword)){
            throw new GlobalException(StatusConstant.PRAM_BIND_ERROR);
        }
        List<Integer> categoryIdList = Lists.newArrayList();
        if (categoryId != null){
            Category category = categoryDao.selectByCategoryId(categoryId);
            if (category == null && StringUtils.isBlank(keyword)){
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVo> productListVoList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVoList);
                return Response.success(StatusConstant.GENERAL_SUCCESS, pageInfo);
            }
            categoryIdList = categoryService.getCategoryAndChildrenById(category.getId()).getBody();
        }
        if (StringUtils.isNotBlank(keyword)){
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }
        PageHelper.startPage(pageNum, pageSize);

        if (StringUtils.isNotBlank(orderBy)){
            if (BusinessConstant.ProductListOrder.PRICE_ORDER.contains(orderBy)){
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
            }
        }
        List<Product> productList = Lists.newArrayList();
        Product product;
        for (Integer cId :categoryIdList){
            product = productDao.selectProductByCategoryIdOrName(StringUtils.isBlank(keyword)?null:keyword, cId);
            productList.add(product);
        }
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product p : productList){
            ProductListVo productListVo = getProductListVo(p);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return Response.success(StatusConstant.GENERAL_SUCCESS, pageInfo);
    }
}
