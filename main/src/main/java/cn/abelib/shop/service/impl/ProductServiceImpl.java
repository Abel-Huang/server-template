package cn.abelib.shop.service.impl;


import cn.abelib.common.cache.CacheKey;
import cn.abelib.common.cache.KeyPrefixFactory;
import cn.abelib.common.constant.BusinessConstant;
import cn.abelib.common.exception.GlobalException;
import cn.abelib.common.result.Response;
import cn.abelib.common.constant.StatusConstant;
import cn.abelib.common.tools.CacheKeyUtil;
import cn.abelib.common.tools.DateUtil;
import cn.abelib.common.tools.JsonUtil;
import cn.abelib.common.tools.PropertiesUtil;
import cn.abelib.data.redis.RedisStringService;
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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 *
 * @author abel
 * @date 2017/9/9
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
    @Autowired
    private RedisStringService redisStringService;

    /**
     *  分页list, 缓存内容是 List<ProductListVo>
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Response<PageInfo> listProduct(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ProductListVo> productListVoList;
        List<Product> productList;
        PageInfo pageInfo;
        // 构造商品的key
        String params = CacheKeyUtil.queryParam(pageNum.toString(), pageSize.toString());
        String realKey = CacheKeyUtil.md5Key(new CacheKey(KeyPrefixFactory.productKeyPrefix(KeyPrefixFactory.ProductKey.LIST_PRODUCT), params));
        if (redisStringService.exists(realKey)){
            // 从缓存中获得数据
            String result = redisStringService.get(realKey);
            pageInfo = JsonUtil.str2Obj(result, List.class, PageInfo.class);
        }else {
            // 从数据库中获得数据
            productList = productDao.list();
            productListVoList = Lists.newArrayList();
            for (Product product : productList){
                ProductListVo productListVo = getProductListVo(product);
                productListVoList.add(productListVo);
            }
            pageInfo = new PageInfo(productList);
            pageInfo.setList(productListVoList);
            // 将数据写入缓存中
            redisStringService.set(realKey, BusinessConstant.RedisCacheExtime.REDIS_CACHE_EXTIME, JsonUtil.obj2Str(pageInfo));
        }

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

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
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
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
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
                    // 清除旧有商品的缓存
                    String realKey = CacheKeyUtil.md5Key(new CacheKey(KeyPrefixFactory.productKeyPrefix(KeyPrefixFactory.ProductKey.GET_PRODUCT),
                            CacheKeyUtil.queryParam(product.getId().toString())));
                    if (redisStringService.exists(realKey)){
                        redisStringService.delete(realKey);
                    }
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
     *  修改商品状态
     * @param productId
     * @param status
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Response<String> setSalesStatus(Integer productId, Integer status) {
        if (productId == null || status == null){
            throw new GlobalException(StatusConstant.PRAM_BIND_ERROR);
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productDao.updateProduct(product);
        if (rowCount > 0){
            // 清除旧有商品的缓存
            String realKey = CacheKeyUtil.md5Key(new CacheKey(KeyPrefixFactory.productKeyPrefix(KeyPrefixFactory.ProductKey.GET_PRODUCT),
                    CacheKeyUtil.queryParam(productId.toString())));
            if (redisStringService.exists(realKey)){
                redisStringService.delete(realKey);
            }
            return Response.success(StatusConstant.GENERAL_SUCCESS);
        }
        return Response.failed(StatusConstant.UPDATE_PRODUCT_STATUS_FAILED);
    }

    /**
     *  后台用户
     * @param productId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Response<ProductDetailVo> getProductDetail(Integer productId){
        if (productId == null){
            throw new GlobalException(StatusConstant.PRAM_BIND_ERROR);
        }
        ProductDetailVo productDetailVo;
        // 构造商品Vo的key
        String realKey = CacheKeyUtil.md5Key(new CacheKey(KeyPrefixFactory.productKeyPrefix(KeyPrefixFactory.ProductKey.GET_PRODUCT),
                CacheKeyUtil.queryParam(productId.toString())));

        if (redisStringService.exists(realKey)){
            // 从缓存中获得数据
            String result = redisStringService.get(realKey);
            productDetailVo = JsonUtil.str2Obj(result, ProductDetailVo.class);
        }else {
            Product product = productDao.selectById(productId);
            if (product == null){
                Response.failed(StatusConstant.PRODUCT_NOT_FOUND);
            }
            // 从数据库中获得数据
            productDetailVo = getProductDetailVo(product);
            // 将数据写入缓存中
            redisStringService.set(realKey, BusinessConstant.RedisCacheExtime.REDIS_CACHE_EXTIME, JsonUtil.obj2Str(productDetailVo));
        }

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

    /**
     *  对应的是单个商品
     *  删除商品, 需要更新缓存列表和对应的缓存文件
     * @param id
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Integer deleteProduct(Integer id) {
        // 构造商品的key
        String realKey = CacheKeyUtil.md5Key(new CacheKey(KeyPrefixFactory.productKeyPrefix(KeyPrefixFactory.ProductKey.GET_PRODUCT),
                CacheKeyUtil.queryParam(id.toString())));
        if (redisStringService.exists(realKey)){
            redisStringService.delete(realKey);
        }
        return productDao.delete(id);
    }

    /**
     *  前台用户
     * @param productId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Response<ProductDetailVo> getProductDetailPortal(Integer productId){
        if (productId == null){
            throw new GlobalException(StatusConstant.PRAM_BIND_ERROR);
        }

        ProductDetailVo productDetailVo;
        // 构造商品Vo的key
        String params = CacheKeyUtil.queryParam(productId.toString());
        String realKey = CacheKeyUtil.md5Key(new CacheKey(KeyPrefixFactory.productKeyPrefix(CacheKeyUtil.key()), params));
        if (redisStringService.exists(realKey)){
            // 从缓存中获得数据
            String result = redisStringService.get(realKey);
            productDetailVo = JsonUtil.str2Obj(result, ProductDetailVo.class);
        }else {
            // 从数据库中获得数据
            Product product = productDao.selectById(productId);
            if (product == null){
                Response.failed(StatusConstant.PRODUCT_NOT_FOUND);
            }
            if (product.getStatus() != BusinessConstant.ProductStatusEnum.ON_SALE.getCode()){
                Response.failed(StatusConstant.PRODUCT_NOT_FOUND);
            }
            productDetailVo = getProductDetailVo(product);
            // 将数据写入缓存中
            redisStringService.set(realKey, BusinessConstant.RedisCacheExtime.REDIS_CACHE_EXTIME, JsonUtil.obj2Str(productDetailVo));
        }

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
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
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
