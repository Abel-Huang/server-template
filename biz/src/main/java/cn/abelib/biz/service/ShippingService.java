package cn.abelib.biz.service;


import cn.abelib.st.core.result.Response;
import cn.abelib.biz.pojo.Shipping;
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
