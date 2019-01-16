package cn.abelib.common.tools;


import java.util.UUID;

/**
 *
 * @author abel
 * @date 2018/2/2
 */
public class UuidUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
