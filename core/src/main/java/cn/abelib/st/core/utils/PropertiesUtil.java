package cn.abelib.st.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 *
 * @author abel
 * @date 2018/1/14
 */
public class PropertiesUtil {
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    private static Properties properties;
    // static代码块先于普通代码块， 普通代码快先于构造代码块
    static {
        String fileName = "application.properties";
        properties = new Properties();
        try {
            properties.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName), "UTF-8"));
        } catch (IOException e) {
            logger.error("properties file read exception", e);
        }
    }
    public static String getProperty(String key){
        String value = properties.getProperty(key.trim());
        if (StringUtils.isBlank(value)){
            return null;
        }
        return value.trim();
    }

    /**
     *  set default
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getProperty(String key, String defaultValue){
        String value = properties.getProperty(key.trim());
        if (StringUtils.isBlank(value)){
            value = defaultValue;
        }
        return value.trim();
    }
}
