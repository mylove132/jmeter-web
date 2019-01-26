package com.okjiaoyu.jmeter.controller;

import com.okjiaoyu.jmeter.response.CommonResponse;
import com.okjiaoyu.jmeter.response.ErrorCode;
import com.okjiaoyu.jmeter.util.ConfigUtil;
import com.okjiaoyu.jmeter.util.DateUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * @Author: liuzhanhui
 * @Decription:
 * @Date: Created in 2019-01-24:13:59
 * Modify date: 2019-01-24:13:59
 */
public class ScriptTimerTask implements Runnable {

    private String fileName;
    private String userName;

    public ScriptTimerTask(String fileName, String userName) {
        this.fileName = fileName;
        this.userName = userName;
    }

    @Override
    public void run() {
        String time = DateUtil.timeStamp();
        //脚本位置
        String scriptFilePath = ConfigUtil.getInstance().getValue("fileGeneratePath") + "/" + userName + "/" + fileName;

        //报告位置
        String rootPath = ConfigUtil.getInstance().getValue("jmeterReportPath") + "/" + userName;
        File rootFile = new File(rootPath);
        if (!rootFile.isDirectory() || !rootFile.exists()) {
            rootFile.mkdirs();
        }
        File reportDir = null;
        if (fileName.split("_").length > 2){
            reportDir = new File(rootPath + "/" + fileName.split("_")[0]+"_"+ fileName.split("_")[1] + "_" + time);
        }else {
            reportDir = new File(rootPath + "/" + fileName.split("\\.")[0] + "_" + time);
        }
        if (!reportDir.isDirectory() || !reportDir.exists()) {
            reportDir.mkdirs();
        }
        //结果文件存放位置
        String jtlRootPath = ConfigUtil.getInstance().getValue("jmeterResultPath") + "/" + userName;
        File jtlRootFile = new File(jtlRootPath);
        if (!jtlRootFile.isDirectory() || !jtlRootFile.exists()) {
            jtlRootFile.mkdirs();
        }
        if (fileName.split("_").length > 2){
            jtlRootPath = jtlRootPath + "/"+fileName.split("_")[0]+"_"+fileName.split("_")[1]+"_"+time+".jtl";
        }else {
            jtlRootPath = jtlRootPath + "/"
                    + fileName.split("\\.")[0] + "_" + time + ".jtl";
        }
        //执行日志存放位置
        String logRootPath = ConfigUtil.getInstance().getValue("jmeterExecLogPath") + "/" + userName;
        File logFile = new File(logRootPath);
        if (!logFile.isDirectory() || !logFile.exists()) {
            logFile.mkdirs();
        }
        if (fileName.split("_").length > 2){
            logRootPath = logRootPath + "/"+fileName.split("_")[0]+"_"+fileName.split("_")[1]+"_"+time+".log";
        }else {
            logRootPath = logRootPath + "/" + fileName.split("\\.")[0] + "_" + time + ".log";
        }
        String cmd = "/Users/liuzhanhui/Documents/jmeter/apache-jmeter-3.1/bin/jmeter -n -t "
                + scriptFilePath
                + " -l "
                +
                jtlRootPath
                + " -e -o "
                + reportDir.getAbsolutePath()
                + " -j "
                + logRootPath;

        try {
            Process ps = Runtime.getRuntime().exec(cmd);
            ps.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (Exception e) {
           throw new RuntimeException("执行脚本失败");
        }
        if (fileName.split("_").length > 1) {
            String[] names = fileName.split("\\.")[0].split("_");
            boolean rename = new File(scriptFilePath).renameTo(
                    new File(ConfigUtil.getInstance().getValue("fileGeneratePath") + "/" +
                            userName + "/" + names[0] + "_" + names[1] + "_" + time + ".jmx"));
            if (!rename) {
                throw new RuntimeException("重命名脚本文件失败");
            }
        } else {
            boolean rename = new File(scriptFilePath).renameTo(
                    new File(ConfigUtil.getInstance().getValue("fileGeneratePath") + "/" +
                            userName + "/" + fileName.split("\\.")[0] + "_" + time + ".jmx"));
            if (!rename) {
                throw new RuntimeException("重命名脚本文件失败");
            }
        }
    }
}
