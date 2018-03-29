package cn.abelib.shop.common.tools;


import java.util.UUID;

/**
 * Created by abel on 2018/2/2.
 */
public class UuidUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
