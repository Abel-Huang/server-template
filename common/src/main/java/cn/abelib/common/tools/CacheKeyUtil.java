package cn.abelib.common.tools;

import cn.abelib.common.cache.CacheKey;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author abel
 * @date 2018/4/5
 *  构造缓存Key值的工具类
 */
public class CacheKeyUtil {
    public static String md5Key(CacheKey cacheKey){
        return cacheKey.getKeyPrefix().getPrefix()+":"+ Md5Util.md5(cacheKey.getQueryParam());
    }

    public static String queryParam(String ... params){
        if (params == null  || params.length < 1) {
            return StringUtils.EMPTY;
        }
        StringBuilder paramsBuilder = new StringBuilder();
        for (String item : params){
            paramsBuilder.append(item);
        }
        return paramsBuilder.toString();
    }

    public static String key(){
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }
}
