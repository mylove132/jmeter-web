package com.okjiaoyu.jmeter.util;

import java.text.ParseException;
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
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串

     * @return
     */
    public static String timeStamp2Date(String seconds) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds+"000")));
    }
    /**
     * 日期格式字符串转换成时间戳
     * @return
     */
    public static String date2TimeStamp(String date_str){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return String.valueOf(sdf.parse(date_str).getTime()/1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 取得当前时间戳（精确到秒）
     * @return
     */
    public static String timeStamp(){
        long time = System.currentTimeMillis();
        String t = String.valueOf(time/1000);
        return t;
    }


    public static void main(String[] args) throws ParseException {
        String timeStamp = timeStamp();
        System.out.println("timeStamp="+timeStamp);

        String date = timeStamp2Date(timeStamp);
        System.out.println("date="+date);

        String timeStamp2 = date2TimeStamp(date);
        System.out.println(timeStamp2);
    }
}
