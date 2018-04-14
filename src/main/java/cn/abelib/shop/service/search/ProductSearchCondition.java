package cn.abelib.shop.service.search;

/**
 * Created by abel on 2018/4/5.
 *  商品的查询条件
 */

public class ProductSearchCondition{
    private String name;
    private String categoryName;
    private String subTitle;
    private String detail;
    private String priceBlock;
    private String stockBlock;
    private String keywords;
    private String orderBy = "updateTime";
    private String order = "desc";
    private int start = 0;
    private int size = 10;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPriceBlock() {
        return priceBlock;
    }

    public void setPriceBlock(String priceBlock) {
        this.priceBlock = priceBlock;
    }

    public String getStockBlock() {
        return stockBlock;
    }

    public void setStockBlock(String stockBlock) {
        this.stockBlock = stockBlock;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getStart() {
        return start > 0 ? start : 0;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public int getSize() {
        if (this.size < 1) {
            return 10;
        } else if (this.size > 100) {
            return 100;
        } else {
            return this.size;
        }
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "ProductSearchCondition{" +
                "name='" + name + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", detail='" + detail + '\'' +
                ", priceBlock='" + priceBlock + '\'' +
                ", stockBlock='" + stockBlock + '\'' +
                ", orderBy='" + orderBy + '\'' +
                ", order='" + order + '\'' +
                ", start=" + start +
                ", size=" + size +
                '}';
    }
}
