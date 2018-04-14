package cn.abelib.shop.service.search;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Created by abel on 2018/4/12.
 *  商品搜索的区间定义
 */
public class ProductSearchBlock {
    /**
     *  价格区间定义
     */
    public static final Map<String, ProductSearchBlock> PRICE_BLOCK;
    /**
     *  库存区间定义
     */
    public static final Map<String, ProductSearchBlock> STOCK_BLOCK;

    public static final ProductSearchBlock ALL = new ProductSearchBlock("key", -1, -1);

    /**
     *  初始化条件区间
     */
    static {
        PRICE_BLOCK = ImmutableMap.<String, ProductSearchBlock>builder()
                .put("*-10", new ProductSearchBlock("*-10", -1, 10))
                .put("10-100", new ProductSearchBlock("10-100", 10, 100))
                .put("100-1000", new ProductSearchBlock("100-1000", 100, 1000))
                .put("1000-10000", new ProductSearchBlock("1000-10000", 1000, 10000))
                .put("10000-*", new ProductSearchBlock("10000-*", 10000, -1))
                .build();
        STOCK_BLOCK = ImmutableMap.<String, ProductSearchBlock>builder()
                .put("*-10", new ProductSearchBlock("*-10", -1, 10))
                .put("10-50", new ProductSearchBlock("10-50", 10, 50))
                .put("50-100", new ProductSearchBlock("50-100", 50, 100))
                .put("100-200", new ProductSearchBlock("100-200", 100, 200))
                .put("200-*", new ProductSearchBlock("200-*", 200, -1))
                .build();
    }

    private String key;
    private int min;
    private int max;

    public ProductSearchBlock(String key, int min, int max) {
        this.key = key;
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static ProductSearchBlock matchPrice(String key){
        ProductSearchBlock block = PRICE_BLOCK.get(key);
        if (block == null){
            return ALL;
        }
        return block;
    }

    public static ProductSearchBlock matchStock(String key){
        ProductSearchBlock block = PRICE_BLOCK.get(key);
        if (block == null){
            return ALL;
        }
        return block;
    }
}
