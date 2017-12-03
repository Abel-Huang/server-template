package cn.abelib.tmall.bean;

import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by abel on 2017/11/11.
 */
@Component
public class ProductImage implements Serializable {
    private static final long serialVersionUID = -2658826471901119602L;

    private Integer id;
    private Integer productId;
    private String imageType;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType == null ? null : imageType.trim();
    }

    public Integer getId() {
        return id;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getImageType() {
        return imageType;
    }

}
