package cn.abelib.tmall.dao;

import cn.abelib.tmall.bean.Property;
import cn.abelib.tmall.util.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by abel on 2017/11/6.
 */
@Repository
public interface PropertyDao {

    List<Property> listAllProperty(Integer categoryId);

    Property getPropertyById(Integer id);

    Integer insertProperty(Property property);

    Integer deleteProperty(Integer id);

    Integer updateProperty(Property property);
}
