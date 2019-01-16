package cn.abelib.common.constant;

import cn.abelib.common.result.Meta;


/**
 *
 * @author abel
 * @date 2017/11/8
 */
public class StatusConstant {

    // 原因短语
    private static final String RESPONSE_SUCCESS = "success";
    private static final String SERVER_ERROR = "服务端错误";
    private static final String ACCOUNT_EMPTY = "账号不存在";
    // 这里会将具体的错误信息附加到后面
    private static final String BIND_ERROR = "参数校验异常: %s";

    private static final String ADD_CAT_ERROR = "添加分类失败";
    private static final String UPDATE_CAT_ERROR = "修改分类失败";


    private static final String WRONG_PASS_ERRORS = "账号或者密码错误";
    private static final String UPDATE_PASS_ERRORS = "更新用户密码错误";
    private static final String UPDATE_USER_ERRORS = "更新用户信息错误";
    private static final String INSERT_USER_ERRORS = "注册用户失败";
    private static final String EXISTS_ACCOUNT_ERRORS = "账号已存在";
    private static final String USER_NOT_LOGIN_ERROR = "用户未登录";
    private static final String NOT_ADMIN_ERRORS = "用户不是管理员";


    private static final String ADD_PRODUCT_ERROR = "添加商品失败";
    private static final String UPDATE_PRODUCT_ERROR = "修改商品失败";
    private static final String UPDATE_ADD_PRODUCT_ERROR = "新增或修改商品失败";
    private static final String UPDATE_PRODUCT_STATUS_ERROR = "修改商品销售状态失败";
    private static final String PRODUCT_NOT_FOUND_ERROR = "商品不存在或者已经下架";
    private static final String QUERY_PRODUCT_ERROR = "查找商品失败";

    private static final String ADD_SHIPPING_ERROR = "添加收货地址失败";
    private static final String DEL_SHIPPING_ERROR = "删除收货地址失败";
    private static final String UPT_SHIPPING_ERROR = "更新收货地址失败";
    private static final String SHIPPING_NOT_FOUND_ERROR = "无法查到该收货地址";

    private static final String CART_EMPTY_ERROR = "购物车为空";
    private static final String PRODUCT_NOSALE_ERROR = "%s产品不是处于售卖状态";
    private static final String STOCK_LACK_ERROR = "%s产品库存不足";
    private static final String ORDER_NOT_FOUND_ERROR = "订单不存在";
    private static final String ALREADY_PAY_ERROR = "已付款，无法取消订单";


    //General 5001XX
    public static Meta GENERAL_SUCCESS = new Meta(0, RESPONSE_SUCCESS);
    public static Meta GENERAL_SERVER_ERROR = new Meta(500100, SERVER_ERROR);
    public static Meta PRAM_BIND_ERROR = new Meta(500101, BIND_ERROR);

    //Login 5002xx
    public static Meta ACCOUNT_NOT_EXISTS = new Meta(500200, ACCOUNT_EMPTY);
    public static Meta WRONG_PASS_ERROR = new Meta(500201, WRONG_PASS_ERRORS);
    public static Meta ACCOUNT_ALREADY_EXISTS = new Meta(500202, EXISTS_ACCOUNT_ERRORS);
    public static Meta USER_NOT_LOGIN = new Meta(500203, USER_NOT_LOGIN_ERROR);
    public static Meta UPDATE_PASS_ERROR = new Meta(500204, UPDATE_PASS_ERRORS);
    public static Meta UPDATE_USER_ERROR = new Meta(500205, UPDATE_USER_ERRORS);
    public static Meta NOT_ADMIN_ERROR = new Meta(500206, NOT_ADMIN_ERRORS);
    public static Meta INSERT_USER_ERROR = new Meta(500207, INSERT_USER_ERRORS);

    //Category 5003xx
    public static Meta ADD_CAT_FAILED = new Meta(500300, ADD_CAT_ERROR);
    public static Meta UPDATE_CAT_FAILED = new Meta(500301, UPDATE_CAT_ERROR);

    //Product 5004xx
    public static Meta ADD_PRODUCT_FAILED = new Meta(500400, ADD_PRODUCT_ERROR);
    public static Meta UPDATE_PRODUCT_FAILED = new Meta(500401, UPDATE_PRODUCT_ERROR);
    public static Meta UPDATE_ADD_PRODUCT_FAILED = new Meta(500402, UPDATE_ADD_PRODUCT_ERROR);
    public static Meta UPDATE_PRODUCT_STATUS_FAILED = new Meta(500403, UPDATE_PRODUCT_STATUS_ERROR);
    public static Meta PRODUCT_NOT_FOUND = new Meta(500404, PRODUCT_NOT_FOUND_ERROR);
    public static Meta QUERY_PRODUCT_FAILED = new Meta(500405, QUERY_PRODUCT_ERROR);

    //Shipping 5005xx
    public static Meta ADD_SHIPPING_FAILED = new Meta(500500, ADD_SHIPPING_ERROR);
    public static Meta DEL_SHIPPING_FAILED = new Meta(500501, DEL_SHIPPING_ERROR);
    public static Meta UPT_SHIPPING_FAILED = new Meta(500502, UPT_SHIPPING_ERROR);
    public static Meta SHIPPING_NOT_FOUND = new Meta(500503, SHIPPING_NOT_FOUND_ERROR);

    //Order 5006xx
    public static Meta CART_EMPTY_FAILED = new Meta(500600, CART_EMPTY_ERROR);
    public static Meta PRODUCT_NOSALE_FAILED = new Meta(500601, PRODUCT_NOSALE_ERROR);
    public static Meta STOCK_LACK_FAILED = new Meta(500602, STOCK_LACK_ERROR);
    public static Meta ORDER_NOT_FOUND_FAILED = new Meta(500603, ORDER_NOT_FOUND_ERROR);
    public static Meta ALREADY_PAY_FAILED = new Meta(500604, ALREADY_PAY_ERROR);

}

