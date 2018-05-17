package cn.abelib.shop.service.impl;


import cn.abelib.shop.common.constant.BusinessConstant;
import cn.abelib.shop.common.constant.StatusConstant;
import cn.abelib.shop.common.exception.GlobalException;
import cn.abelib.shop.common.result.Response;
import cn.abelib.shop.common.tools.BigDecimalUtil;
import cn.abelib.shop.common.tools.DateUtil;
import cn.abelib.shop.common.tools.PropertiesUtil;
import cn.abelib.shop.dao.*;
import cn.abelib.shop.pojo.*;
import cn.abelib.shop.service.OrderService;
import cn.abelib.shop.vo.OrderItemVo;
import cn.abelib.shop.vo.OrderProductVo;
import cn.abelib.shop.vo.OrderVo;
import cn.abelib.shop.vo.ShippingVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;


/**
 * Created by abel on 2017/12/14.
 */
@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private CartDao cartDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private ShippingDao shippingDao;

    /**
     *  创建订单
     * @param userId
     * @param shippingId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Response<OrderVo> createOrder(Integer userId, Integer shippingId){
        List<Cart> cartList = cartDao.selectCheckedCartByUserId(userId);

        Response response = this.getCartOrderItem(userId, cartList);
        if (!response.isSuccess()){
            return response;
        }
        List<OrderItem> orderItemList = (List<OrderItem>) response.getBody();
        BigDecimal payment = this.calOrderTotalPrice(orderItemList);

        //填充订单对象
        Orders order = this.assembleOrder(userId, shippingId, payment);
        if (order == null){
            throw new GlobalException(StatusConstant.GENERAL_SERVER_ERROR);
        }
        // 判断购物车是否为空
        if (CollectionUtils.isEmpty(cartList)){
            return Response.failed(StatusConstant.CART_EMPTY_FAILED);
        }
        for (OrderItem orderItem : orderItemList){
            orderItem.setOrderNo(order.getOrderNo());
            // 将orderItem的循环插入改为批量插入数据库
            // todo
            orderItemDao.insert(orderItem);
        }
        this.reduceStock(orderItemList);
        this.cleanCart(cartList);

        //返回给前端的数据
        OrderVo orderVo = this.assembleOrderVo(order, orderItemList);
        return Response.success(StatusConstant.GENERAL_SUCCESS, orderVo);
    }

    /**
     *  组装OrderVo
     * @param order
     * @param orderItemList
     * @return
     */
    private OrderVo assembleOrderVo(Orders order, List<OrderItem> orderItemList){
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPayment(order.getPayment());
        orderVo.setPayType(order.getPayType());
        orderVo.setPayTypeDesc(BusinessConstant.PaymentTypeEnum.codeOf(orderVo.getPayType()).getValue());

        orderVo.setPostage(order.getPostage());
        orderVo.setStatus(order.getStatus());
        orderVo.setStatusDesc(BusinessConstant.OrderStatusEnum.codeOf(orderVo.getStatus()).getValue());
        orderVo.setShippingId(order.getShippingId());
        Shipping shipping = shippingDao.selectById(orderVo.getShippingId());
        if (shipping != null){
            orderVo.setReceiverName(shipping.getReceiverName());
            orderVo.setShippingVo(this.assembleShippingVo(shipping));
        }
        orderVo.setPayTime(DateUtil.dateToStr(order.getPayTime()));
        orderVo.setSendTime(DateUtil.dateToStr(order.getSendTime()));
        orderVo.setEndTime(DateUtil.dateToStr(order.getEndTime()));
        orderVo.setCloseTime(DateUtil.dateToStr(order.getCloseTime()));

        orderVo.setImageHost(PropertiesUtil.getProperty(""));

        List<OrderItemVo> orderItemVoList = Lists.newArrayList();

        for (OrderItem orderItem : orderItemList){
            OrderItemVo orderItemVo = this.assembleOrderItemVo(orderItem);
            orderItemVoList.add(orderItemVo);
        }
        orderVo.setOrderItemVoList(orderItemVoList);
        return orderVo;
    }

    /**
     *  组装OrderItemVo
     * @param orderItem
     * @return
     */
    private OrderItemVo assembleOrderItemVo(OrderItem orderItem){
        OrderItemVo orderItemVo = new OrderItemVo();
        orderItemVo.setOrderNo(orderItem.getOrderNo());
        orderItemVo.setProductId(orderItem.getProductId());
        orderItemVo.setProductName(orderItem.getProductName());
        orderItemVo.setProductImage(orderItem.getProductImage());
        orderItemVo.setCurrentPrice(orderItem.getCurrentPrice());
        orderItemVo.setTotalPrice(orderItem.getTotalPrice());
        orderItemVo.setCreateTime(DateUtil.dateToStr(orderItem.getCreateTime()));
        return orderItemVo;
    }

    /**
     *  组装ShippingVo
     * @param shipping
     * @return
     */
    private ShippingVo assembleShippingVo(Shipping shipping){
        ShippingVo shippingVo = new ShippingVo();
        shippingVo.setReceiverName(shipping.getReceiverName());
        shippingVo.setReceiverPhone(shipping.getReceiverPhone());
        shippingVo.setReceiverProvince(shipping.getReceiverProvince());
        shippingVo.setReceiverCity(shipping.getReceiverCity());
        shippingVo.setReceiverDistrict(shipping.getReceiverDistrict());
        shippingVo.setReceiverAddress(shipping.getReceiverAddress());
        shippingVo.setReceiverZip(shipping.getReceiverZip());
        return shippingVo;
    }

    /**
     *  需要减少库存量
     * @param orderItemList
     */
    private void reduceStock(List<OrderItem> orderItemList){
        for (OrderItem orderItem : orderItemList){
            Product product = productDao.selectById(orderItem.getProductId());
            product.setStock(product.getStock() - orderItem.getQuantity());
            int rowCount = productDao.updateProduct(product);
            if (rowCount == 0){
                throw new GlobalException(StatusConstant.GENERAL_SERVER_ERROR);
            }
        }
    }

    /**
     *  清空购物车
     * @param cartList
     */
    private void cleanCart(List<Cart> cartList){
        for (Cart cart : cartList){
            int rowCount = cartDao.deleteById(cart.getId());
            if (rowCount == 0){
                throw new GlobalException(StatusConstant.GENERAL_SERVER_ERROR);
            }
        }
    }

    /**
     *  组装并插入订单
     * @param userId
     * @param shippingId
     * @param payment
     * @return
     */
    private Orders assembleOrder(Integer userId, Integer shippingId, BigDecimal payment){
        Orders order = new Orders();
        Long orderNo = this.generateOrderNo();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setShippingId(shippingId);
        order.setPayment(payment);

        order.setStatus(BusinessConstant.OrderStatusEnum.NO_PAY.getCode());
        order.setPostage(new BigDecimal("0"));
        order.setPayType(BusinessConstant.PaymentTypeEnum.ON_LINE_PAY.getCode());

        //在数据库中插入订单
        int rowCount = orderDao.insertOrder(order);
        if (rowCount > 0){
            return order;
        }
        return null;
    }

    //生成订单号
    private long generateOrderNo(){
        long currentTime = System.currentTimeMillis();
        return currentTime+ new Random(currentTime%10).nextInt(100);
    }

    /**
     *  计算订单总价
     * @param orderItemList
     * @return
     */
    private BigDecimal calOrderTotalPrice(List<OrderItem> orderItemList){
        BigDecimal payment = new BigDecimal("0");
        for (OrderItem orderItem :orderItemList){
            payment = BigDecimalUtil.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
        }
        return payment;
    }

    /**
     *  填充订单明细行
     * @param userId
     * @param cartList
     * @return
     */
    private Response<List<OrderItem>> getCartOrderItem(Integer userId, List<Cart> cartList){
        List<OrderItem> orderItemList = Lists.newArrayList();
        // 判断购物车是否为空
        if (CollectionUtils.isEmpty(cartList)){
            return Response.failed(StatusConstant.CART_EMPTY_FAILED);
        }
        for(Cart cart : cartList){
            OrderItem orderItem = new OrderItem();
            Product product = productDao.selectById(cart.getProductId());
            // 校验售货状态
            if (BusinessConstant.ProductStatusEnum.ON_SALE.getCode() != product.getStatus()){
                return Response.failed(StatusConstant.PRODUCT_NOSALE_FAILED);
            }
            // 校验库存状态
            if (cart.getQuantity() > product.getStock()){
                return Response.failed(StatusConstant.STOCK_LACK_FAILED);
            }
            orderItem.setUserId(userId);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setCurrentPrice(product.getPrice());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cart.getQuantity()));
            orderItemList.add(orderItem);
        }
        return Response.success(StatusConstant.GENERAL_SUCCESS, orderItemList);
    }

    /**
     *  取消订单
     * @param userId
     * @param orderNo
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Response cancelOrder(Integer userId, Long orderNo){
        Orders order = orderDao.selectByOrderNoAndId(userId, orderNo);
        if (order == null){
            return Response.failed(StatusConstant.ORDER_NOT_FOUND_FAILED);
        }
        if (order.getStatus() != BusinessConstant.OrderStatusEnum.NO_PAY.getCode()){
            return Response.failed(StatusConstant.ALREADY_PAY_FAILED);
        }
        Orders updateOrder = new Orders();
        updateOrder.setId(order.getId());
        updateOrder.setStatus(BusinessConstant.OrderStatusEnum.CANCELED.getCode());
        int rowCount = orderDao.updateOrder(updateOrder);
        if (rowCount > 0){
            return Response.success(StatusConstant.GENERAL_SUCCESS);
        }
        throw new GlobalException(StatusConstant.GENERAL_SERVER_ERROR);
    }

    /**
     *  获取订单中购物车中的商品信息
     * @param userId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Response<OrderProductVo> getOrderCartProduct(Integer userId){
        OrderProductVo orderProductVo = new OrderProductVo();

        // 从购物车中获取数据
        List<Cart> cartList = cartDao.selectCheckedCartByUserId(userId);
        Response response = this.getCartOrderItem(userId, cartList);
        if (!response.isSuccess()){
            return response;
        }
        List<OrderItem> orderItemList = (List<OrderItem>) response.getBody();

        List<OrderItemVo> orderItemVoList = Lists.newArrayList();
        BigDecimal payment = this.calOrderTotalPrice(orderItemList);
        orderProductVo.setTotalPrice(payment);
        orderProductVo.setOrderItemVoList(orderItemVoList);
        orderProductVo.setImageHost(PropertiesUtil.getProperty(""));
        return Response.success(StatusConstant.GENERAL_SUCCESS, orderProductVo);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Response<OrderVo> getOrderDetail(Integer userId, Long orderNo) {
        Orders order = orderDao.selectByOrderNoAndId(userId, orderNo);
        if (order != null) {
            List<OrderItem> orderItemList = orderItemDao.selectByUserIdOrderNo(userId, orderNo);
            OrderVo orderVo = this.assembleOrderVo(order, orderItemList);
            return Response.success(StatusConstant.GENERAL_SUCCESS, orderVo);
        }
        return Response.failed(StatusConstant.ORDER_NOT_FOUND_FAILED);
    }

    /**
     *  获取订单列表
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Response<PageInfo> getOrderList(Integer userId, Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<Orders> ordersList = orderDao.selectByUserId(userId);
        List<OrderVo> orderVoList = this.assembleOrderVo(ordersList, userId);
        PageInfo pageInfo = new PageInfo(ordersList);
        pageInfo.setList(orderVoList);
        return Response.success(StatusConstant.GENERAL_SUCCESS);
    }

    private List<OrderVo> assembleOrderVo(List<Orders> ordersList, Integer userId){
        List<OrderVo> orderVoList = Lists.newArrayList();
        for (Orders order : ordersList){
            List<OrderItem> orderItemList = Lists.newArrayList();
            if (userId == null){
                // 针对admin，不需要带userId
                orderItemList = orderItemDao.selectByOrderNo(order.getOrderNo());
            }else {
                orderItemList = orderItemDao.selectByUserIdOrderNo(userId, order.getOrderNo());
            }
            OrderVo orderVo = assembleOrderVo(order, orderItemList);
            orderVoList.add(orderVo);
        }
        return orderVoList;
    }

    /**
     *  后台方法
     *  获取订单列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Response<PageInfo> getOrderList(Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<Orders> ordersList = orderDao.listOrder();
        List<OrderVo> orderVoList = this.assembleOrderVo(ordersList, null);
        PageInfo pageInfo = new PageInfo(ordersList);
        pageInfo.setList(orderVoList);
        return Response.success(StatusConstant.GENERAL_SUCCESS);
    }

    /**
     *  后台方法
     *  获取订单详情
     * @param orderNo
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Response<OrderVo> getOrderDetail(Long orderNo) {
        Orders order = orderDao.selectByOrderNo(orderNo);
        if (order != null) {
            List<OrderItem> orderItemList = orderItemDao.selectByOrderNo(orderNo);
            OrderVo orderVo = this.assembleOrderVo(order, orderItemList);
            return Response.success(StatusConstant.GENERAL_SUCCESS, orderVo);
        }
        return Response.failed(StatusConstant.ORDER_NOT_FOUND_FAILED);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Response<PageInfo> orderSearch(Long orderNo, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Orders order = orderDao.selectByOrderNo(orderNo);
        if (order != null) {
            List<OrderItem> orderItemList = orderItemDao.selectByOrderNo(orderNo);
            OrderVo orderVo = this.assembleOrderVo(order, orderItemList);
            PageInfo pageInfo = new PageInfo(Lists.newArrayList());
            pageInfo.setList(Lists.newArrayList(orderVo));
            return Response.success(StatusConstant.GENERAL_SUCCESS, pageInfo);
        }
        return Response.failed(StatusConstant.ORDER_NOT_FOUND_FAILED);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Response sendGoods(Long orderNo){
        Orders order = orderDao.selectByOrderNo(orderNo);
        if (order != null) {
            if (order.getStatus() == BusinessConstant.OrderStatusEnum.PAID.getCode()){
                order.setStatus(BusinessConstant.OrderStatusEnum.SHIPPED.getCode());
                order.setSendTime(new Date());
                int rowCount = orderDao.updateOrder(order);
                if (rowCount == 0){
                    throw new GlobalException(StatusConstant.GENERAL_SERVER_ERROR);
                }
                return Response.success(StatusConstant.GENERAL_SUCCESS);
            }
        }
        return Response.failed(StatusConstant.ORDER_NOT_FOUND_FAILED);
    }
}
