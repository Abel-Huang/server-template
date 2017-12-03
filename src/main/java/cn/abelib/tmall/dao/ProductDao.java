package cn.abelib.tmall.dao;

import cn.abelib.tmall.bean.Product;
import cn.abelib.tmall.util.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by abel on 2017/11/9.
 */
@Repository
public interface ProductDao {

    List<Product> listAllProduct(Integer categoryId);

    Integer insertProduct(Product product);

    Integer deleteProduct(Integer id);

    Integer updateProduct(Product product);
    
    Product getProductById(Integer id);
}
