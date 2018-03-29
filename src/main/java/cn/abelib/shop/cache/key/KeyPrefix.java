package cn.abelib.shop.cache.key;

/**
 * Created by abel on 2018/2/1.
 */
public interface KeyPrefix {
    int expire() ;

    String getPrefix();
}
