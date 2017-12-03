package cn.abelib.tmall.service.impl;

import cn.abelib.tmall.bean.Category;
import cn.abelib.tmall.bean.Product;
import cn.abelib.tmall.dao.CategoryDao;
import cn.abelib.tmall.dao.ProductDao;
import cn.abelib.tmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by abel on 2017/11/9.
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao = null ;
    @Autowired
    private CategoryDao categoryDao = null;
    @Override
    public List<Product> listAllProduct(Integer categoryId) {
        List<Product> productList = productDao.listAllProduct(categoryId);
        return setCategory(productList);
    }

    @Override
    public Integer insertProduct(Product product) {
        return productDao.insertProduct(product);
    }

    @Override
    public Integer deleteProduct(Integer id) {
        return productDao.deleteProduct(id);
    }

    @Override
    public Integer updateProduct(Product product) {
        return productDao.updateProduct(product);
    }

    @Override
    public Product getProductById(Integer id) {
        Product product = productDao.getProductById(id);
        return setCategory(product);
    }

    public List<Product> setCategory(List<Product> productList){
        for (int i = 0;  i< productList.size(); i++){
           productList.set(i, setCategory(productList.get(i)));
        }
        return productList;
    }

    public Product setCategory(Product product){
        Integer cid = product.getCategoryId();
        Category category = categoryDao.getCategoryById(cid);
        product.setCategory(category);
        return product;
    }
}
