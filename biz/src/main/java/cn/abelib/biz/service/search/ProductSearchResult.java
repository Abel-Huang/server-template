package cn.abelib.biz.service.search;

import java.util.List;

/**
 *
 * @author abel
 * @date 2018/4/14
 *  从ES中返回的商品查询结果
 */
public class ProductSearchResult {
    private long totalHits;
    private List<Integer> productIds;

    public long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(long totalHits) {
        this.totalHits = totalHits;
    }

    public List<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Integer> productIds) {
        this.productIds = productIds;
    }

    public ProductSearchResult(long totalHits, List<Integer> productIds) {
        this.totalHits = totalHits;
        this.productIds = productIds;
    }
}
