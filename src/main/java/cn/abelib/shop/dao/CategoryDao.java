package cn.abelib.shop.dao;


import cn.abelib.shop.pojo.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by abel on 2017/9/14.
 */
@Mapper
public interface CategoryDao {
    @Insert("insert into category(parent_id, name, status, sort_order, create_time, update_time) values("+
            "#{parentId}, #{name}, #{status}, #{sortOrder}, now(), now())")
    Integer insert(Category category);

    @Update("update category set name=#{name}, update_time=now() where id=#{id}")
    Integer update(Category category);

    @Select("select id, name, status, sort_order, create_time, update_time from category where id = #{id}")
    Category selectByCategoryId(@Param("id") Integer id);

    @Select("select name from category where id = #{id}")
    String selectNameByCategoryId(@Param("id") Integer id);

    @Select("select * from category where id = #{id}")
    List<Category> getCategoryChildrenByParentId(@Param("id") Integer id);
}
