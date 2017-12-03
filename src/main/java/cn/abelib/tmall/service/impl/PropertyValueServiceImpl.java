package cn.abelib.tmall.service.impl;

import cn.abelib.tmall.bean.Product;
import cn.abelib.tmall.bean.Property;
import cn.abelib.tmall.bean.PropertyValue;
import cn.abelib.tmall.dao.PropertyValueDao;
import cn.abelib.tmall.service.PropertyService;
import cn.abelib.tmall.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by abel on 2017/11/12.
 */
@Service
public class PropertyValueServiceImpl implements PropertyValueService {
    @Autowired
    private PropertyValueDao propertyValueDao = null;
    @Autowired
    private PropertyService propertyService = null;

    @Override
    public void init(Product product) {
        List<Property> propertyList = propertyService.listAllProperty(product.getCategoryId());
        for (Property property : propertyList){
            PropertyValue propertyValue = getPropertyValueById(property.getId(), product.getId());
            if (propertyValue == null){
                propertyValue = new PropertyValue();
                propertyValue.setProductId(product.getId());
                propertyValue.setPropertyId(property.getId());
                propertyValueDao.insertPropertyValue(propertyValue);
            }
        }
    }

    @Override
    public void updatePropertyValue(PropertyValue propertyValue) {
        propertyValueDao.updatePropertyValue(propertyValue);
    }

    @Override
    public PropertyValue getPropertyValueById(Integer propertyId, Integer productId) {
        return null;
    }

    @Override
    public List<PropertyValue> listAllPropertyValue(Integer productId) {
        return propertyValueDao.listAllPropertyValue(productId);
    }
}
