package cn.abelib.shop.service.search;

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

    /**
     *  移除索引
     * @param productId
     */
    void remove(Integer productId);
}
