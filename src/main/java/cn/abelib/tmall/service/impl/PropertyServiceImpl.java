package cn.abelib.tmall.service.impl;

import cn.abelib.tmall.bean.Property;
import cn.abelib.tmall.dao.PropertyDao;
import cn.abelib.tmall.service.PropertyService;
import cn.abelib.tmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by abel on 2017/11/9.
 */
@Service
public class PropertyServiceImpl implements PropertyService{
    @Autowired
    private PropertyDao propertyDao = null;

    @Override
    public List<Property> listAllProperty(Integer categoryId) {
        return propertyDao.listAllProperty(categoryId);
    }

    @Override
    public Property getPropertyById(Integer id) {
        return propertyDao.getPropertyById(id);
    }

    @Override
    public Integer insertProperty(Property property) {
        return propertyDao.insertProperty(property);
    }

    @Override
    public Integer deleteProperty(Integer id) {
        return propertyDao.deleteProperty(id);
    }

    @Override
    public Integer updateProperty(Property property) {
        return propertyDao.updateProperty(property);
    }
}

