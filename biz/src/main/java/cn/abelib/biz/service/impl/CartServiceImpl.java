package cn.abelib.biz.service.impl;


import cn.abelib.biz.constant.BusinessConstant;
import cn.abelib.biz.constant.StatusConstant;
import cn.abelib.st.core.exception.GlobalException;
import cn.abelib.st.core.result.Response;
import cn.abelib.st.core.utils.BigDecimalUtil;
import cn.abelib.st.core.utils.PropertiesUtil;
import cn.abelib.biz.dao.CartDao;
import cn.abelib.biz.dao.ProductDao;
import cn.abelib.biz.pojo.Cart;
import cn.abelib.biz.pojo.Product;
import cn.abelib.biz.service.CartService;
import cn.abelib.biz.vo.CartProductVo;
import cn.abelib.biz.vo.CartVo;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


/**
 *
 * @author abel
 * @date 2017/9/15
 */
@Service
public class CartServiceImpl implements CartService{
    @Autowired
    private CartDao cartDao;

    @Autowired
    private ProductDao productDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public Response<CartVo> add(Integer userId, Integer productId, Integer count){
        if (productId == null || count == null){
            throw new GlobalException(StatusConstant.PRAM_BIND_ERROR);
        }
        Cart cart = cartDao.selectCartByUserIdProductId(userId, productId);
        if (cart == null){
            // 产品不在当前购物车中，需要增加
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setChecked(BusinessConstant.Cart.CHECKED);
            cartItem.setProductId(productId);
            cartItem.setUserId(userId);

            cartDao.insert(cartItem);
        }else {
            count = count + cart.getQuantity();
            cart.setQuantity(count);
            cartDao.update(cart);
        }
        return this.list(userId);
    }


    private CartVo getCartVoLimit(Integer userId){
        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartDao.selectCartByUserId(userId);
        List<CartProductVo> cartProductVoList = Lists.newArrayList();
        BigDecimal cartTotal = new BigDecimal("0");

        if (CollectionUtils.isNotEmpty(cartList)){
            for (Cart cart : cartList){
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cart.getId());
                cartProductVo.setUserId(cart.getUserId());
                cartProductVo.setProductId(cart.getProductId());

                Product product = productDao.selectById(cart.getProductId());
                if (product != null){
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubTitle(product.getSubTitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());

                    int limitQuantity = 0;
                    if (product.getStock() >= cart.getQuantity()){
                        limitQuantity = cart.getQuantity();
                        cartProductVo.setLimitQuantity(BusinessConstant.Cart.LIMIT_NUM_SUCCESS);
                    }else {
                        limitQuantity = product.getStock();
                        cartProductVo.setLimitQuantity(BusinessConstant.Cart.LIMIT_NUM_FAIL);
                        Cart cartItem = new Cart();
                        cartItem.setId(cart.getId());
                        cartItem.setQuantity(limitQuantity);
                        int rowCount = cartDao.update(cartItem);
                        if (rowCount == 0){
                            throw new GlobalException(StatusConstant.GENERAL_SERVER_ERROR);
                        }
                    }
                    cartProductVo.setQuantity(limitQuantity);
                    cartProductVo.setProductTotal(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductVo.getQuantity()));
                    cartProductVo.setProductChecked(cart.getChecked());
                }
                if (cart.getChecked() == BusinessConstant.Cart.CHECKED){
                    cartTotal = BigDecimalUtil.add(cartTotal.doubleValue(), cartProductVo.getProductTotal().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }
        }
        // 购物车商品列表
        cartVo.setCartProductVoList(cartProductVoList);
        // 购物车商品总价
        cartVo.setCartTotal(cartTotal);
        cartVo.setAllChecked(this.getAllCheckedStatus(userId));
        // todo
        cartVo.setImageHost(PropertiesUtil.getProperty(""));
        return cartVo;
    }

    private boolean getAllCheckedStatus(Integer userId){
        if (userId == null){
            return false;
        }
        return cartDao.selectCartProductStatusByUserId(userId) == 0;
    }

    /**
     *  更新
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public Response<CartVo> update(Integer userId, Integer productId, Integer count){
        if (productId == null || count == null){
            throw new GlobalException(StatusConstant.PRAM_BIND_ERROR);
        }
        Cart cart = cartDao.selectCartByUserIdProductId(userId, productId);
        if (cart != null){
            cart.setQuantity(count);
        }
        cartDao.update(cart);
        return this.list(userId);
    }

    /**
     *  删除购物车中的商品，其中商品id可能是多个，中间用逗号隔开
     * @param userId
     * @param productIds
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public Response<CartVo> delete(Integer userId, String productIds){
        List<String> productList = Splitter.on(",").splitToList(productIds);
        if (CollectionUtils.isEmpty(productList)){
            throw new GlobalException(StatusConstant.PRAM_BIND_ERROR);
        }
        for (String str : productList){
            cartDao.deleteByUserIdAndId(userId, Integer.parseInt(str));
        }
        return this.list(userId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public Response<CartVo> list(Integer userId){
        CartVo cartVo = this.getCartVoLimit(userId);
        return Response.success(StatusConstant.GENERAL_SUCCESS, cartVo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public Response<CartVo> selectOrUnselectAll(Integer userId, Integer checked){
        cartDao.selectOrUnselectAll(userId);
        return this.list(userId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public Response<CartVo> selectOrUnselect(Integer userId, Integer productId, Integer checked){
        cartDao.selectOrUnselect(userId, productId);
        return this.list(userId);
    }

    /**
     *  获取当前购物车中的商品数量
     * @param userId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public Response<Integer> getCartProductCount(Integer userId){
        if (userId == null){
            throw new GlobalException(StatusConstant.PRAM_BIND_ERROR);
        }
        return Response.success(StatusConstant.GENERAL_SUCCESS, cartDao.selectCartProductCountByUserId(userId));
    }
}
