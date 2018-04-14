package cn.abelib.shop.service.mq;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by abel on 2018/4/13.
 *  商品消息结构体
 */
@Slf4j
public class ProductMessage {
    public static final String INDEX = "index";
    public static final String REMOVE = "remove";
    public static final int MAX_RETRY = 3;

    private Integer productId;
    private String operation;
    private int retry = 0;

    /**
     *  防止序列化失败
     */
    public ProductMessage() {
    }

    public ProductMessage(Integer productId, String operation, int retry) {
        this.productId = productId;
        this.operation = operation;
        this.retry = retry;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }
}
