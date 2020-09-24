package com.example.network.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    /**
     * 时间格式化 由于服务器返回的都是CST时间,格式化将时间强制设置到-6区
     *
     * @param date       时间
     * @param dateFormat 格式字符串
     * @return
     */
    public static String getDateString(Date date, String dateFormat) {
        if (date == null)
            return "";

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        // sdf.setTimeZone(TimeZone.getTimeZone("GMT-6:00"));
        return sdf.format(date);
    }
}
