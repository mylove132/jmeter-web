package com.okjiaoyu.jmeter.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: liuzhanhui
 * @Decription:
 * @Date: Created in 2019-01-22:10:23
 * Modify date: 2019-01-22:10:23
 */
public class DateUtil {

    /**
     * 时间戳转为时间格式
     * @param timeStanp
     * @return
     */
    public static Date timeStampTansforDate(Long timeStanp){
        Date date = new Date(timeStanp);
        return date;
    }

    /**
     * 格式化为标准时间
     * @param date
     * @return
     */
    public static String formatDate(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }
}
