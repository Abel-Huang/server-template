package cn.abelib.shop.vo;

import java.math.BigDecimal;

/**
 *
 * @author abel
 * @date 2018/1/15
 * 商品列表Vo
 */
public class ProductListVo {
    private Integer id;
    private Integer categoryId;
    private String name;
    private String subTitle;
    private String mainImage;
    private BigDecimal price;

    private Integer status;

    // 存放图片的主机地址
    private String imageHost;

    @Override
    public String toString() {
        return "ProductListVo{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", mainImage='" + mainImage + '\'' +
                ", price=" + price +
                ", status=" + status +
                ", imageHost='" + imageHost + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
