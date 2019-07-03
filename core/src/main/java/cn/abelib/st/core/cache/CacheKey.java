package cn.abelib.st.core.cache;

/**
 *
 * @author abel
 * @date 2018/4/2
 *  用来生成Redis Cache的key值
 */
public class CacheKey {
    private KeyPrefix keyPrefix;
    private String queryParam;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CacheKey cacheKey = (CacheKey) o;

        if (!keyPrefix.equals(cacheKey.keyPrefix)) {
            return false;
        }
        return queryParam.equals(cacheKey.queryParam);
    }

    @Override
    public String toString() {
        return "CacheKey{" +
                "keyPrefix=" + keyPrefix +
                ", queryParam='" + queryParam + '\'' +
                '}';
    }

    public CacheKey(){

    }

    public CacheKey(KeyPrefix keyPrefix, String queryParam){
        this.keyPrefix = keyPrefix;
        this.queryParam = queryParam;
    }

    public KeyPrefix getKeyPrefix() {
        return keyPrefix;
    }

    public String getQueryParam() {
        return queryParam;
    }
}
