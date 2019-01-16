package cn.abelib.common.tools;


import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 *
 * @author abel
 * @date 2018/1/14
 * 用于将从数据库中获取的Unix时间戳转换为
 */
public class DateUtil {
    public static final String STD_FORMAT = "yyyy-MM-dd HH:mm:ss";
    // String->Date
    public static Date strToDate(String dateTimeStr, String formatStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    public static Date strToDate(String dateTimeStr){
        return strToDate(dateTimeStr, STD_FORMAT);
    }

    // Date->String
    public static String dateToStr(Date date, String formatStr){
        if (date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formatStr);
    }

    public static String dateToStr(Date date) {
        return dateToStr(date, STD_FORMAT);
    }
}
