package cn.abelib.biz.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author abel
 * @date 2017/9/11
 */
public class CartVo {
    private List<CartProductVo> cartProductVoList;
    private BigDecimal cartTotal;
    private Boolean allChecked;
    private String imageHost;

    public List<CartProductVo> getCartProductVoList() {
        return cartProductVoList;
    }

    public void setCartProductVoList(List<CartProductVo> cartProductVoList) {
        this.cartProductVoList = cartProductVoList;
    }

    public BigDecimal getCartTotal() {
        return cartTotal;
    }

    public void setCartTotal(BigDecimal cartTotal) {
        this.cartTotal = cartTotal;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }
}
