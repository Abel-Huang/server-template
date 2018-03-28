package cn.abelib.tmall.service;

import cn.abelib.tmall.bean.Product;
import cn.abelib.tmall.bean.PropertyValue;

import java.util.List;

/**
 * Created by abel on 2017/11/12.
 */
public interface PropertyValueService {
    void init(Product product);

    void updatePropertyValue(PropertyValue propertyValue);

    PropertyValue getPropertyValueById(Integer propertyId, Integer productId);

    List<PropertyValue> listAllPropertyValue(Integer productId);
}
