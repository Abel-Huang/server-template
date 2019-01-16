package cn.abelib.data.search;


import com.google.common.collect.Sets;

import java.util.Set;

/**
 *
 * @author abel
 * @date 2017/12/14
 *  商品排序
 */
public class ProductSort {
    public static final String DEFAULT_SORT_KEY = "updateTime";

    public static final String PRICE_SORT_KEY = "price";

    private static final Set<String> SORT_KEYS = Sets.newHashSet(
            DEFAULT_SORT_KEY,
            "createTime",
            "stock",
            PRICE_SORT_KEY
    );


    public static String getSortKey(String key) {
        if (!SORT_KEYS.contains(key)) {
            key = DEFAULT_SORT_KEY;
        }
        return key;
    }
}
