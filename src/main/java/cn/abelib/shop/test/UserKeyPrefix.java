package cn.abelib.shop.test;

import cn.abelib.shop.common.cache.BaseKeyPrefix;
import cn.abelib.shop.common.constant.BusinessConstant;

/**
 * Created by abel on 2018/2/2.
 *  生成User 模块的key
 */
public class UserKeyPrefix extends BaseKeyPrefix {
    public static final int TOKEN_EXPIRE = 60;
    private UserKeyPrefix(int expire, String keyPrefix) {
        super(expire, keyPrefix);
    }

    private UserKeyPrefix(String keyPrefix) {
        super(keyPrefix);
    }

    public static UserKeyPrefix getById = new UserKeyPrefix("id");
    public static UserKeyPrefix token = new UserKeyPrefix(TOKEN_EXPIRE,"token");
    public static UserKeyPrefix sesson = new UserKeyPrefix(BusinessConstant.RedisCacheExtime.REDIS_SESSION_EXTIME, "session");

    public static UserKeyPrefix cache = new UserKeyPrefix("cache");
}
