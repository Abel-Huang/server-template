package cn.abelib.tmall.service;

import cn.abelib.tmall.bean.Product;
import cn.abelib.tmall.util.Page;

import java.util.List;

/**
 * Created by abel on 2017/11/9.
 */
public interface ProductService {
    /**
     * 列出所有的Product
     * @param
     * @return
     */
    List<Product> listAllProduct(Integer categoryId);

    /**
     * 插入Product，并且回填id
     * @param product
     * @return
     */
    Integer insertProduct(Product product);

    /**
     * 删除对应id的Product
     * @param id
     * @return
     */
    Integer deleteProduct(Integer id);

    /**
     *  更新Product
     * @param product
     * @return
     */
    Integer updateProduct(Product product);

    /**
     * 通过id获取Product
     * @param id
     * @return
     */
    Product getProductById(Integer id);
}
