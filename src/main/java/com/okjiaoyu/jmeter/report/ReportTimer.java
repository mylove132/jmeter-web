package com.okjiaoyu.jmeter.report;

import com.okjiaoyu.jmeter.util.ConfigUtil;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.util.*;

/**
 * @Author: liuzhanhui
 * @Decription:
 * @Date: Created in 2019-01-21:10:09
 * Modify date: 2019-01-21:10:09
 */
public class ReportTimer {

    private Timer timer;
    public ReportTimer(){
        timer = new Timer();
        timer.schedule(new ReportTimerTask(), getTime());
    }

    private Date getTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,00);
        calendar.set(Calendar.MINUTE,00);
        calendar.set(Calendar.SECOND,00);
        Date date = calendar.getTime();
        return date;
    }
}
class ReportTimerTask extends TimerTask {
    private static Logger log = Logger.getLogger(ReportTimerTask.class);
    @Override
    public void run() {
        String reportPath = ConfigUtil.getInstance().getValue("jmeterReportPath");
        File file = new File(reportPath);
        if (file.exists() && file.isDirectory()){
            File[] nameFileList = file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isDirectory();
                }
            });

            if (nameFileList != null && nameFileList.length > 0){
                for (File nameFile:nameFileList){
                    File[] typeFileList = nameFile.listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File pathname) {
                            return pathname.isDirectory();
                        }
                    });
                    for (File typeFile:typeFileList){
                        //报告类型目录
                        String fileTypePath = typeFile.getAbsolutePath();
                        File[] reportFileList = typeFile.listFiles(new FileFilter() {
                            @Override
                            public boolean accept(File pathname) {
                                return pathname.isDirectory();
                            }
                        });
                        if (reportFileList.length > 10){
                            List<String> reportNameList = new ArrayList<>();
                            for (File reportFile:reportFileList){
                                reportNameList.add(reportFile.getName());
                            }

                            Collections.sort(reportNameList, new Comparator<String>() {
                                @Override
                                public int compare(String o1, String o2) {
                                    return o2.compareTo(o1);
                                }
                            });

                            for (int i = 10;i < reportNameList.size();i++){
                                String deleteReportPath = fileTypePath+"/"+reportNameList.get(i);
                                File f = new File(deleteReportPath);
                                boolean deleteResult = f.delete();
                                if (deleteResult){
                                    log.info("删除报告目录成功："+deleteReportPath);
                                }else {
                                    log.error("删除报告目录失败："+deleteReportPath);
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}
