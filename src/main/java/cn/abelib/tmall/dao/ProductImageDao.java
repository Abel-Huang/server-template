package cn.abelib.tmall.dao;

import cn.abelib.tmall.bean.ProductImage;
import cn.abelib.tmall.bean.ProductImageParam;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by abel on 2017/11/15.
 */
@Repository
public interface ProductImageDao {
    Integer insertProductImage(ProductImage productImage);

    Integer updateProductImage(ProductImage productImage);

    ProductImage getProductImageById(Integer id);

    List<ProductImage> listAllProductImage(ProductImageParam productImageParam);
}
