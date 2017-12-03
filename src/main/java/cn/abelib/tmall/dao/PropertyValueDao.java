package cn.abelib.tmall.dao;

import cn.abelib.tmall.bean.Product;
import cn.abelib.tmall.bean.PropertyValue;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by abel on 2017/11/12.
 */
@Repository
public interface PropertyValueDao {
    void init(Product product);

    Integer insertPropertyValue(PropertyValue propertyValue);

    Integer updatePropertyValue(PropertyValue propertyValue);

    PropertyValue getPropertyValueById(Integer propertyId, Integer productId);

    List<PropertyValue> listAllPropertyValue(Integer productId);
}
