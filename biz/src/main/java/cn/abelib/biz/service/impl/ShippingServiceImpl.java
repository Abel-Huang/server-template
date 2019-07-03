package cn.abelib.biz.service.impl;


import cn.abelib.biz.constant.StatusConstant;
import cn.abelib.st.core.result.Response;
import cn.abelib.biz.dao.ShippingDao;
import cn.abelib.biz.pojo.Shipping;
import cn.abelib.biz.service.ShippingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
 *
 * @author abel
 * @date 2017/9/6
 *  收货地址服务接口
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class ShippingServiceImpl implements ShippingService{
    @Autowired
    private ShippingDao shippingDao;

    /**
     *  添加收货地址
     * @param userId
     * @param shipping
     * @return
     */
    @Override
    public Response add(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        int rowCount = shippingDao.insert(shipping);
        if (rowCount > 0){
            Map<String, Integer> result = Maps.newHashMap();
            result.put("shippingId", shipping.getId());
            return Response.success(StatusConstant.GENERAL_SUCCESS, result);
        }
        return Response.failed(StatusConstant.ADD_SHIPPING_FAILED);
    }

    /**
     *  删除收货地址
     * @param userId
     * @param shippingId
     * @return
     */
    @Override
    public Response del(Integer userId, Integer shippingId) {
        int resultCount = shippingDao.deleteByUserIdAndId(userId, shippingId);
        if (resultCount > 0){
            return Response.success(StatusConstant.GENERAL_SUCCESS);
        }
        return Response.failed(StatusConstant.DEL_SHIPPING_FAILED);
    }

    /**
     *  更新收货地址
     * @param userId
     * @param shipping
     * @return
     */
    @Override
    public Response update(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        int rowCount = shippingDao.updateByUserIdAndId(shipping);
        if (rowCount > 0){
            return Response.success(StatusConstant.GENERAL_SUCCESS);
        }
        return Response.failed(StatusConstant.UPT_SHIPPING_FAILED);
    }

    /**
     *  查找收货地址
     * @param userId
     * @param shippingId
     * @return
     */
    @Override
    public Response<Shipping> select(Integer userId, Integer shippingId){
        Shipping shipping = shippingDao.selectByIdAndUserId(shippingId, userId);
        if (shipping == null){
            return Response.failed(StatusConstant.SHIPPING_NOT_FOUND);
        }
        return Response.success(StatusConstant.GENERAL_SUCCESS, shipping);
    }

    /**
     *  分页list
     *  @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Response<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList = shippingDao.list(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return Response.success(StatusConstant.GENERAL_SUCCESS, pageInfo);
    }
}
