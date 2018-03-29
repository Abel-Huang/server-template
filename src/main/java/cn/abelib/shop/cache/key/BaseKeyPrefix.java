package cn.abelib.shop.cache.key;

/**
 * Created by abel on 2018/2/1.
 */
public abstract class BaseKeyPrefix implements KeyPrefix {

    private int expire;

    private String keyPrefix;

    public BaseKeyPrefix(int  expire, String keyPrefix){
        this.expire = expire;
        this.keyPrefix = keyPrefix;
    }

    public BaseKeyPrefix(String keyPrefix){
        this(0, keyPrefix);
    }

    /**
     *  默认的过期时间为0
     * @return
     */
    @Override
    public int expire() {
        return expire;
    }

    /**
     *  获取文件前缀
     * @return
     */
    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + keyPrefix;
    }
}
