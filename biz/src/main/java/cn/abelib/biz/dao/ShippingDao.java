package cn.abelib.biz.dao;


import cn.abelib.biz.pojo.Shipping;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 *
 * @author abel
 * @date 2017/9/6
 */
@Mapper
public interface ShippingDao {
    @Select("select * from shipping where user_id=#{userId}")
    List<Shipping> list(@Param("userId") Integer userId);

    @Select("select * from shipping where user_id=#{userId} and id=#{shippingId}")
    Shipping selectByIdAndUserId(@Param("shippingId") Integer shippingId, @Param("userId") Integer userId);

    @Select("select * from shipping where id=#{shippingId}")
    Shipping selectById(@Param("shippingId") Integer shippingId);

    @Insert("insert into shipping(user_id, receiver_name, receiver_phone, receiver_province, receiver_city," +
            "receiver_district, receiver_address, receiver_zip, create_time, update_time) values(" +
            "#{userId}, #{receiverName}, #{receiverPhone}, #{receiverProvince}, #{receiverCity}, " +
            "#{receiverDistrict}, #{receiverAddress}, #{receiverZip}, now(), now())")
    Integer insert(Shipping shipping);

    @Delete("delete from shipping where id=#{id}")
    Integer delete(Integer id);

    @Delete("delete from shipping where user_id=#{userId} and id=#{shippingId}")
    Integer deleteByUserIdAndId(@Param("userId") Integer userId, @Param("shippingId") Integer shippingId);

    @Update("update shipping set receiver_name=#{receiverName}, receiver_phone=#{receiverPhone}, receiver_province=#{receiverProvince}," +
            "receiver_city=#{receiverCity}, receiver_district=#{receiverDistrict}, receiver_address=#{receiverAddress}" +
            ",receiver_zip=#{receiverZip}, update_time=now() where id=#{id} and user_id=#{userId}")
    Integer updateByUserIdAndId(Shipping shipping);
}
