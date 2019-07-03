package cn.abelib.biz.cache;

import cn.abelib.biz.constant.BusinessConstant;
import cn.abelib.biz.pojo.*;
import cn.abelib.st.core.cache.BaseKeyPrefix;

/**
 *
 * @author abel
 * @date 2018/2/3
 */
public class KeyPrefixFactory {
    public static BaseKeyPrefix userKeyPrefix(String keyPrefix){
        return new BaseKeyPrefix(keyPrefix, User.class);
    }

    public static BaseKeyPrefix cartKeyPrefix(String keyPrefix){
        return new BaseKeyPrefix(keyPrefix, Cart.class);
    }

    public static BaseKeyPrefix categoryKeyPrefix(String keyPrefix){
        return new BaseKeyPrefix(keyPrefix, BusinessConstant.Category.class);
    }

    public static BaseKeyPrefix orderKeyPrefix(String keyPrefix){
        return new BaseKeyPrefix(keyPrefix, Orders.class);
    }

    public static BaseKeyPrefix shippingKeyPrefix(String keyPrefix){
        return new BaseKeyPrefix(keyPrefix, Shipping.class);
    }

    /**
     * 商品部分
     * @param keyPrefix
     * @return
     */
    public static BaseKeyPrefix productKeyPrefix(String keyPrefix){
        return new BaseKeyPrefix(keyPrefix, Product.class);
    }

    public interface ProductKey{
        String LIST_PRODUCT = "list";
        String GET_PRODUCT = "get";
        String GET_PRODUCTS = "gets";
    }

    public static BaseKeyPrefix paymentKeyPrefix(String keyPrefix){
        return new BaseKeyPrefix(keyPrefix, Payment.class);
    }

    public static BaseKeyPrefix orderItemKeyPrefix(String keyPrefix){
        return new BaseKeyPrefix(keyPrefix, OrderItem.class);
    }
}
