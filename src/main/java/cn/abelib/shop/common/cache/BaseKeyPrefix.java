package cn.abelib.shop.common.cache;

/**
 * Created by abel on 2018/2/1.
 */
public class BaseKeyPrefix implements KeyPrefix {

    private int expire;

    private String keyPrefix;

    private Class clazz;

    protected BaseKeyPrefix(int  expire, String keyPrefix){
        this.expire = expire;
        this.keyPrefix = keyPrefix;
    }

    protected BaseKeyPrefix(String keyPrefix){
        this(0, keyPrefix);
    }

    protected BaseKeyPrefix(String keyPrefix, Class clazz){
        this.clazz = clazz;
        this.keyPrefix = keyPrefix;
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
        String className = this.clazz.getSimpleName();
        return className + ":" + keyPrefix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseKeyPrefix that = (BaseKeyPrefix) o;

        if (expire != that.expire) return false;
        return keyPrefix != null ? keyPrefix.equals(that.keyPrefix) : that.keyPrefix == null;
    }
}
