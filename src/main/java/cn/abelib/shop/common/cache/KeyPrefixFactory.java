package cn.abelib.shop.common.cache;

import cn.abelib.shop.pojo.*;

/**
 * Created by abel on 2018/2/3.
 */
public class KeyPrefixFactory {
    public static BaseKeyPrefix userKeyPrefix(String keyPrefix){
        return new BaseKeyPrefix(keyPrefix, User.class);
    }

    public static BaseKeyPrefix cartKeyPrefix(String keyPrefix){
        return new BaseKeyPrefix(keyPrefix, Cart.class);
    }

    public static BaseKeyPrefix categoryKeyPrefix(String keyPrefix){
        return new BaseKeyPrefix(keyPrefix, Category.class);
    }

    public static BaseKeyPrefix orderKeyPrefix(String keyPrefix){
        return new BaseKeyPrefix(keyPrefix, Orders.class);
    }

    public static BaseKeyPrefix shippingKeyPrefix(String keyPrefix){
        return new BaseKeyPrefix(keyPrefix, Shipping.class);
    }

    public static BaseKeyPrefix productKeyPrefix(String keyPrefix){
        return new BaseKeyPrefix(keyPrefix, Product.class);
    }

    public static BaseKeyPrefix paymentKeyPrefix(String keyPrefix){
        return new BaseKeyPrefix(keyPrefix, Payment.class);
    }

    public static BaseKeyPrefix orderItemKeyPrefix(String keyPrefix){
        return new BaseKeyPrefix(keyPrefix, OrderItem.class);
    }
}
