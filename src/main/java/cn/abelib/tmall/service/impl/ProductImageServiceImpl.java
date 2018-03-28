package cn.abelib.tmall.service.impl;

import cn.abelib.tmall.bean.ProductImage;
import cn.abelib.tmall.bean.ProductImageParam;
import cn.abelib.tmall.dao.ProductImageDao;
import cn.abelib.tmall.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by abel on 2017/11/15.
 */
@Service
public class ProductImageServiceImpl implements ProductImageService{
    @Autowired
    private ProductImageDao productImageDao = null;
    @Override
    public Integer insertProductImage(ProductImage productImage) {
        return productImageDao.insertProductImage(productImage);
    }

    @Override
    public Integer updateProductImage(ProductImage productImage) {
        return productImageDao.updateProductImage(productImage);
    }

    @Override
    public ProductImage getProductImageById(Integer id) {
        return productImageDao.getProductImageById(id);
    }

    @Override
    public List<ProductImage> listAllProductImage(Integer productId, String imageType) {
        ProductImageParam productImageParam = new ProductImageParam();
        productImageParam.setImageType(imageType);
        productImageParam.setProductId(productId);
        return productImageDao.listAllProductImage(productImageParam);
    }
}
