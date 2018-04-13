package cn.abelib.shop.service.mongo;

/**
 * Created by abel on 2018/2/24.
 *  MongoDB 取时间戳后十位作为id
 */
public class IdGenerator {
    public static String generator(){
        long mills = System.currentTimeMillis();
        String millStr = (mills+"").substring(3);
        return millStr;
    }
}
