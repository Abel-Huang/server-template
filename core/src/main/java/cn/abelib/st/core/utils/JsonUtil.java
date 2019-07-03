package cn.abelib.st.core.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.text.SimpleDateFormat;

/**
 *
 * @author abel
 * @date 2017/12/14
 *  序列化工具类
 */
@Slf4j
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     *  使用静态代码块在类第一次加载时初始化
     */
    static {
        // 对象的所有字段都会被转为Json中的字段
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);
        // 取消默认将日期转为timestamp形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 忽略bean为空时转成json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        // 日期格式统一
        objectMapper.setDateFormat(new SimpleDateFormat(DateUtil.STD_FORMAT));
        // 忽略在json字符串中存在，java对象中不存在对应属性的情况
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     *  对象转成字符串
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String obj2Str(T t){
        if (t == null){
            return null;
        }
        try {
            return t instanceof String ? (String) t : objectMapper.writeValueAsString(t);
        } catch (Exception e) {
            log.warn("Parse Object to String error", e);
        }
        return null;
    }

    /**
     *  对象转成pretty字符串
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String obj2StrPretty(T t){
        if (t == null){
            return null;
        }
        try {
            return t instanceof String ? (String) t : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(t);
        } catch (Exception e) {
            log.warn("Parse Object to String error", e);
            return null;
        }
    }

    /**
     *  对象转成字符串
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T str2Obj(String str, Class<T> clazz){
        if (StringUtils.isEmpty(str) || clazz == null){
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
        }catch (Exception e){
            log.warn("Parse String to Object error", e);
            return null;
        }
    }

    /**
     *  用于处理复杂类型的对象
     * @param str
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T str2Obj(String str, TypeReference<T> typeReference){
        if (StringUtils.isEmpty(str) || typeReference == null){
            return null;
        }
        try {
            return (T)(typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str, typeReference));
        }catch (Exception e){
            log.warn("Parse String to Object error", e);
            return null;
        }
    }

    /**
     *  用于处理复杂类型的对象
     * @param str
     * @param collectionClass
     * @param itemClass
     * @param <T>
     * @return
     */
    public static <T> T str2Obj(String str, Class<?> collectionClass, Class<?> ... itemClass){
        if (str == null || collectionClass == null || itemClass == null || itemClass.length < 1){
            return null;
        }
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, itemClass);
        try {
            return objectMapper.readValue(str, javaType);
        }catch (Exception e){
            log.warn("Parse String to Object error", e);
            return null;
        }
    }
}
