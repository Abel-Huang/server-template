package cn.abelib.biz.service;


import cn.abelib.st.core.result.Response;
import cn.abelib.biz.vo.OrderProductVo;
import cn.abelib.biz.vo.OrderVo;
import com.github.pagehelper.PageInfo;

/**
 *
 * @author abel
 * @date 2017/12/14
 */
public interface OrderService {
    Response<OrderVo> createOrder(Integer userId, Integer shippingId);

    Response cancelOrder(Integer userId, Long orderNo);

    Response<OrderProductVo> getOrderCartProduct(Integer userId);

    Response<OrderVo> getOrderDetail(Integer userId, Long orderNo);

    Response<PageInfo> getOrderList(Integer userId, Integer pageNum, Integer pageSize);

    Response<PageInfo> getOrderList(Integer pageNum, Integer pageSize);

    Response<OrderVo> getOrderDetail(Long orderNo);

    Response<PageInfo> orderSearch(Long orderNo, Integer pageNum, Integer pageSize);

    Response sendGoods(Long orderNo);
}
