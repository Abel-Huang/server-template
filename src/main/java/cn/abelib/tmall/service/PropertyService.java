package cn.abelib.tmall.service;

import cn.abelib.tmall.bean.Property;
import cn.abelib.tmall.util.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by abel on 2017/11/6.
 */
public interface PropertyService {

    List<Property> listAllProperty(Integer categoryId);

    Property getPropertyById(Integer id);

    Integer insertProperty(Property property);

    Integer deleteProperty(Integer id);

    Integer updateProperty(Property property);
}
