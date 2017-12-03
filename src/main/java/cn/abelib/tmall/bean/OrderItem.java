package cn.abelib.tmall.bean;

import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by abel on 2017/11/11.
 */
@Component
public class OrderItem implements Serializable {
    private static final long serialVersionUID = 3767445660094078158L;

    private Integer id;
    private Integer productId;
    private Integer orderId;
    private Integer userId;
    private Integer number;

    // ·ÇÊý¾Ý¿â×Ö¶Î
    private Product product;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
