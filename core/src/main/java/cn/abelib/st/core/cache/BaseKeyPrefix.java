package cn.abelib.st.core.cache;

import java.util.Objects;

/**
 * @author abel
 * @date 2018/2/1
 */
public class BaseKeyPrefix implements KeyPrefix {

    private int expire;
    private String keyPrefix;
    private Class clazz;

    public BaseKeyPrefix(int  expire, String keyPrefix){
        this.expire = expire;
        this.keyPrefix = keyPrefix;
    }

    public BaseKeyPrefix(String keyPrefix){
        this(0, keyPrefix);
    }

    public BaseKeyPrefix(String keyPrefix, Class clazz){
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BaseKeyPrefix that = (BaseKeyPrefix) o;

        if (expire != that.expire) {
            return false;
        }
        return Objects.equals(keyPrefix, that.keyPrefix);
    }
}
