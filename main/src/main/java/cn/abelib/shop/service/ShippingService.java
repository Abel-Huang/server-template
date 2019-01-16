package cn.abelib.shop.service;


import cn.abelib.common.result.Response;
import cn.abelib.shop.pojo.Shipping;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;


/**
 *
 * @author abel
 * @date 2017/11/12
 */
@Service
public interface ShippingService {
    Response add(Integer userId, Shipping shipping);

    Response del(Integer userId, Integer shippingId);

    Response update(Integer userId, Shipping shipping);

    Response<Shipping> select(Integer userId, Integer shippingId);

    Response<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize);
}
