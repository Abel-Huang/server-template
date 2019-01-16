package cn.abelib.shop.dao;


import cn.abelib.shop.pojo.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 *
 * @author abel
 * @date 2017/9/9
 */
@Mapper
public interface ProductDao {
    @Select("select * from product order by id asc")
    List<Product> list();

    @Insert("insert into product(category_id, name, sub_title, main_image, sub_images, detail, price, stock, status, create_time, update_time) values(" +
            "#{categoryId}, #{name}, #{subTitle}, #{mainImage}, #{subImages}, #{detail}, #{price}, #{stock}, #{status}, now(), now())")
    Integer insert(Product product);

    @Delete("delete from product where id=#{id}")
    Integer delete(Integer id);

    @Select("select * from product where id=#{id}")
    Product selectById(Integer id);

    @Update("update product set category_id=#{categoryId}, name=#{name}, sub_title=#{subTitle}, main_image=#{mainImage}, sub_images#={subImages}," +
            "detail=#{detail}, price=#{price}, stock=#{stock}, status=#{status}, update_time=now() where id=#{id}")
    Integer updateProduct(Product product);

    @Select("select * from product where name like #{productName} and id=#{id}")
    List<Product> selectProductByIdOrName(@Param("name") String productName, @Param("id") Integer id);

    @Select("select * from product where name like #{productName} and id=#{categoryId}")
    Product selectProductByCategoryIdOrName(@Param("name") String productName, @Param("categoryId") Integer categoryId);
}
