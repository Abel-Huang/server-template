package cn.abelib.biz.constant;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 *
 * @author abel
 * @date 2017/9/6
 */
public class BusinessConstant {

    public interface Search {
        String PRODUCT_SEARCH_INDEX = "shopping";
        String PRODUCT_SEARCH_TYPE = "product";
    }
    public interface RedisCacheExtime{
        /**
         * 1 minute
         */
        Integer REDIS_SESSION_EXTIME = 60 * 30;
        //
        /**
         * 1 second
         */
        Integer REDIS_CACHE_EXTIME = 60 * 1;
    }

    public interface Role {
        /**
         * 普通用户
         */
        Integer ROLE_CUSTOMER = 0;
        /**
         * 管理员
         */
        Integer ROLE_ADMIN = 1;
    }

    public interface Category{
        /**
         * 当前分类不可用
         */
        Integer STATUS_FALSE = 0;
        /**
         * 当前分类可用
         */
        Integer STATUS_TRUE = 1;
    }

    public interface Cart{
        /**
         * 选中
         */
        Integer CHECKED = 0;
        /**
         * 未选中
         */
        Integer UN_CHECKED = 1;

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    public interface ProductListOrder{
        Set<String> PRICE_ORDER = Sets.newHashSet("price_desc", "price_asc");
    }

    public enum ProductStatusEnum{
        /**
         * 在线
         */
        ON_SALE(1, "在线");

        private String value;
        private int code;

        ProductStatusEnum(int code, String value){
            this.code = code;
            this.value = value;
        }
        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    public enum PaymentTypeEnum{
        /**
         *  在线支付
         */
        ON_LINE_PAY(1, "在线支付");

        private String value;
        private int code;

        PaymentTypeEnum(int code, String value){
            this.code = code;
            this.value = value;
        }
        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public static PaymentTypeEnum codeOf(int code){
            for(PaymentTypeEnum paymentTypeEnum : values()){
                if(paymentTypeEnum.getCode() == code){
                    return paymentTypeEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }
    }

    public enum OrderStatusEnum {
        CANCELED(0, "已取消"),
        NO_PAY(10, "未支付"),
        PAID(20, "已付款"),
        SHIPPED(40, "已发货"),
        ORDER_SUCCESS(50, "订单完成"),
        ORDER_CLOSE(60, "订单关闭");


        OrderStatusEnum(Integer code, String value) {
            this.code = code;
            this.value = value;
        }

        private String value;
        private Integer code;

        public String getValue() {
            return value;
        }

        public Integer getCode() {
            return code;
        }

        public static OrderStatusEnum codeOf(int code){
            for(OrderStatusEnum orderStatusEnum : values()){
                if(orderStatusEnum.getCode() == code){
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }
    }
}
