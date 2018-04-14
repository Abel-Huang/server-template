package cn.abelib.shop.service.search;

import cn.abelib.shop.common.result.Response;

/**
 * Created by abel on 2018/4/9.
 *  商品检索接口
 */
public interface ProductSearchService {
    /**
     *  索引商品
     * @param productId
     */
    void index(Integer productId);

    void syncIndex(Integer productId);

    /**
     *  移除索引
     * @param productId
     */
    void remove(Integer productId);

    Long syncRemove(Integer productId);

    Response<ProductSearchResult> query(ProductSearchCondition searchCondition);
}
