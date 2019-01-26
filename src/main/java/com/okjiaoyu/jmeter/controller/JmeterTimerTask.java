package com.okjiaoyu.jmeter.controller;

import com.okjiaoyu.jmeter.cache.CacheEntity;
import com.okjiaoyu.jmeter.response.CommonResponse;
import com.okjiaoyu.jmeter.response.ErrorCode;
import com.okjiaoyu.jmeter.response.Response;
import com.okjiaoyu.jmeter.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

/**
 * @Author: liuzhanhui
 * @Decription:
 * @Date: Created in 2019-01-24:09:59
 * Modify date: 2019-01-24:09:59
 */
@RestController
@Component
public class JmeterTimerTask {

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private static Map<String, ScheduledFuture> futureMap = new HashMap<>();
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    @RequestMapping(value = "updateTimerTask", method = RequestMethod.POST)
    public Response startTimerTask(String userName, String fileName, String time) throws ParseException {
        CronTrigger cronTrigger = null;
        String amOrpm = time.substring(time.length() - 2);
        String date = time.substring(0, time.length() - 2);
        if (amOrpm.equalsIgnoreCase("am")) {
            String[] ds = date.split("-");
            String year = ds[0].trim();
            String month = String.valueOf(Integer.parseInt(ds[1].trim()));
            String day = String.valueOf(Integer.parseInt(ds[2].trim()));
            String hour = String.valueOf(Integer.parseInt(ds[3].split(":")[0].trim()));
            String minute = String.valueOf(Integer.parseInt(ds[3].split(":")[1].trim()));
            String dt = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + "00";
            long timer = Long.parseLong(DateUtil.date2TimeStamp(dt));
            if (timer < System.currentTimeMillis() / 1000) {
                return CommonResponse.makeRsp(ErrorCode.TIMER_LITTLE_CURRENT_TIME);
            }
            cronTrigger = new CronTrigger("0 " + minute + " " + hour + " " + day + " " + month + " ?");
        } else if (amOrpm.equalsIgnoreCase("pm")) {
            String[] ds = date.split("-");
            String year = ds[0].trim();
            String month = String.valueOf(Integer.parseInt(ds[1].trim()));
            String day = String.valueOf(Integer.parseInt(ds[2].trim()));
            String hour = String.valueOf(Integer.parseInt(ds[3].split(":")[0].trim()) + 12);
            String minute = String.valueOf(Integer.parseInt(ds[3].split(":")[1].trim()));
            String dt = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + "00";
            long timer = Long.parseLong(DateUtil.date2TimeStamp(dt));
            if (timer < System.currentTimeMillis() / 1000) {
                return CommonResponse.makeRsp(ErrorCode.TIMER_LITTLE_CURRENT_TIME);
            }
            cronTrigger = new CronTrigger("0 " + minute + " " + hour + " " + day + " " + month + " ?");
        }
        ScheduledFuture future = threadPoolTaskScheduler.schedule(new ScriptTimerTask(fileName, userName), cronTrigger);
        futureMap.put(userName + "-" + fileName + "-" + time, future);
        CacheEntity cacheEntity =  new CacheEntity(future, (1000*60*60*24L), System.currentTimeMillis());
        return CommonResponse.makeOKRsp(futureMap);
    }

    @RequestMapping(value = "cancelTaskTimer", method = RequestMethod.POST)
    public Response cancelTimerTask(String fileName, String userName, String time) {
        if (futureMap != null || futureMap.isEmpty()) {
            return CommonResponse.makeOKRsp();
        } else {
            if (!futureMap.keySet().contains(userName + "-" + fileName + "-" + time)) {
                return CommonResponse.makeOKRsp();
            }
            futureMap.get(userName + "-" + fileName + "-" + time).cancel(true);
            futureMap.remove(userName + "-" + fileName + "-" + time);
        }
        return CommonResponse.makeOKRsp();
    }

    @RequestMapping("timerList")
    public Response getTimerList(String userName, String fileName) {
        List<String> times = new ArrayList<>();
        if (futureMap.size() == 0 || futureMap.isEmpty()) {
            return CommonResponse.makeOKRsp();
        }
        for (String key : futureMap.keySet()) {
            if (key.contains(userName) && key.contains(fileName)) {
                String time = key.split(key.split("-")[0]+"-"+key.split("-")[1]+"-")[1];
                times.add(time);
            }
        }
        if (times.size() > 0) {
            Map<String, Map<String, List<String>>> result = new HashMap<>();
            Map<String, List<String>> timeMap = new HashMap<>();
            timeMap.put(fileName, times);
            result.put(userName, timeMap);
            return CommonResponse.makeOKRsp(result);
        }
        return CommonResponse.makeOKRsp();
    }

    static Map<String, List<Map<String, String>>> map = new HashMap<>();

    public static void main(String[] args) {

        List<Map<String, String>> arry = new ArrayList<>();
        Map<String, String> m = new HashMap<>();
        m.put("b", "c");
        Map<String, String> m1 = new HashMap<>();
        m1.put("3", "4");
        arry.add(m);
        map.put("a", arry);
        System.out.println(map.get("a"));
        arry.add(m1);
        map.put("a", arry);
        System.out.println(map.get("a"));
        String c = "11";
        System.out.println(String.valueOf(Integer.parseInt(c)));
    }
}
