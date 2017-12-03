package cn.abelib.tmall.service;

import cn.abelib.tmall.bean.ProductImage;

import java.util.List;

/**
 * Created by abel on 2017/11/15.
 */
public interface ProductImageService {
    String type_single = "type_single";
    String type_detail = "type_detail";

    Integer insertProductImage(ProductImage productImage);

    Integer updateProductImage(ProductImage productImage);

    ProductImage getProductImageById(Integer id);

    List<ProductImage> listAllProductImage(Integer productId, String imageType);
}
