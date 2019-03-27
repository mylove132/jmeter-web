package com.okjiaoyu.jmeter.controller;

import com.okjiaoyu.jmeter.response.CommonResponse;
import com.okjiaoyu.jmeter.response.ErrorCode;
import com.okjiaoyu.jmeter.response.Response;
import com.okjiaoyu.jmeter.util.ConfigUtil;
import com.okjiaoyu.jmeter.util.DateUtil;
import com.okjiaoyu.jmeter.util.DeleteFolderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
public class FileController {

    static ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 2,
            200,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<>(2));
    /**
     * 获取用户的脚本目录
     *
     * @param userName
     * @return
     */
    @PostMapping(value = "/jmxFileList")
    public Response jmxFileList(String userName) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> result = null;
        String rootPath = ConfigUtil.getInstance().getValue("fileGeneratePath");
        File rootFile = new File(rootPath);
        if (!rootFile.isDirectory() || rootFile.listFiles().length == 0) {
            return CommonResponse.makeErrRsp("没有脚本文件");
        }
        File[] userFils = rootFile.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        boolean flag = false;
        for (File userFile : userFils) {
            if (userFile.isDirectory() && userFile.getName().equals(userName)) {
                flag = true;
                break;
            }
        }
        if (flag) {
            File userFile = new File(rootPath + "/" + userName);
            File[] jmxFiles = userFile.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isFile() && pathname.getName().endsWith(".jmx");
                }
            });
            if (jmxFiles.length > 0) {
                for (File jmxFile : jmxFiles) {
                    result = new HashMap<>();
                    result.put("createPerson", userName);
                    result.put("fileName", jmxFile.getName());
                    String fileName = jmxFile.getName();
                    String[] timeStamps = fileName.split("_");
                    String timeStamp = "";
                    if (timeStamps.length > 2) {
                        timeStamp = timeStamps[1];
                    } else if (timeStamps.length == 2) {
                        timeStamp = timeStamps[1].split("\\.")[0];
                    }
                    String date = DateUtil.timeStamp2Date(timeStamp);
                    result.put("createTime", date);
                    resultList.add(result);
                }
            }
        }
        return CommonResponse.makeOKRsp(resultList);
    }

    @PostMapping(value = "/reportList")
    public Response jmxReportList(String userName, String fileName) {
        List<String> reportNameList = new ArrayList<>();
        String rootPath = ConfigUtil.getInstance().getValue("jmeterReportPath");
        File userFile = new File(rootPath + "/" + userName);
        if (!userFile.isDirectory() || !userFile.exists()) {
            return CommonResponse.makeOKRsp();
        }
        File[] reportList = userFile.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        if (reportList.length == 0) {
            return CommonResponse.makeOKRsp();
        } else {
            String startFileName = fileName.split("_")[0];
            for (File file : reportList) {
                if (file.getName().startsWith(startFileName)) {
                    reportNameList.add(file.getName());
                }
            }
        }
        Map<String, List<String>> result = new HashMap<>();
        result.put("reportNameList", reportNameList);
        return CommonResponse.makeOKRsp(result);
    }

    /**
     * 删除脚本文件
     *
     * @param userName
     * @param fileName
     * @return
     */
    @PostMapping(value = "/deleteJmxFile")
    public Response deleteJmxFile(String userName, String fileName) {
        String rootPath = ConfigUtil.getInstance().getValue("fileGeneratePath");
        File userFile = new File(rootPath + "/" + userName);
        if (userFile.isDirectory() && userFile.listFiles().length > 0) {
            File[] fileList = userFile.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isFile() && pathname.getName().endsWith(".jmx");
                }
            });
            if (fileList.length > 0) {
                File deleteFile = null;
                boolean flag = false;
                for (File file : fileList) {
                    if (file.getName().equals(fileName)) {
                        flag = true;
                        deleteFile = file;
                        break;
                    }
                }
                if (flag) {
                    boolean isReportDelete = false;
                    boolean isLogDelete = false;
                    boolean isJtlDelete = false;
                    String reportPath = ConfigUtil.getInstance().getValue("jmeterReportPath") + "/" + userName;
                    if (new File(reportPath).exists()) {
                        File[] reportDirs = new File(reportPath).listFiles(new FileFilter() {
                            @Override
                            public boolean accept(File pathname) {
                                return pathname.isDirectory() && pathname.getName().startsWith(fileName.split("_")[0] + "_" + fileName.split("_")[1]);
                            }
                        });
                        if (reportDirs.length > 0) {
                            for (File reportDir : reportDirs) {
                                isReportDelete = DeleteFolderUtil.delFolder(reportDir.getAbsolutePath());
                                if (!isReportDelete) {
                                    isReportDelete = false;
                                    break;
                                }
                            }
                        } else {
                            isReportDelete = true;
                        }
                    } else {
                        System.out.println(reportPath + "目录不存在");
                        isReportDelete = true;
                    }
                    String logPath = ConfigUtil.getInstance().getValue("jmeterExecLogPath") + "/" + userName;
                    if (new File(logPath).exists()) {
                        File[] logFiles = new File(logPath).listFiles(new FileFilter() {
                            @Override
                            public boolean accept(File pathname) {
                                return pathname.isFile() && pathname.getName().startsWith(fileName.split("_")[0] + "_" + fileName.split("_")[1]);
                            }
                        });
                        if (logFiles.length > 0) {
                            for (File file : logFiles) {
                                isLogDelete = file.delete();
                                if (!isLogDelete) {
                                    isLogDelete = false;
                                    break;
                                }
                            }
                        } else {
                            isLogDelete = true;
                        }
                    } else {
                        System.out.println(logPath + "日志不存在");
                        isLogDelete = true;
                    }
                    String jtlPath = ConfigUtil.getInstance().getValue("jmeterResultPath") + "/" + userName;
                    if (new File(jtlPath).exists()) {
                        File[] jtlFiles = new File(jtlPath).listFiles(new FileFilter() {
                            @Override
                            public boolean accept(File pathname) {
                                return pathname.isFile() && pathname.getName().startsWith(fileName.split("_")[0] + "_" + fileName.split("_")[1]);
                            }
                        });
                        if (jtlFiles.length > 0) {
                            for (File jtlFile : jtlFiles) {
                                isJtlDelete = jtlFile.delete();
                                if (!isJtlDelete) {
                                    isJtlDelete = false;
                                    break;
                                }
                            }
                        }
                    } else {
                        System.out.println(jtlPath + "结果不存在");
                        isJtlDelete = true;
                    }
                    Map<String, Object> result = new HashMap<>();
                    result.put("isScriptDelete", deleteFile.delete());
                    result.put("isReportDelete", isReportDelete);
                    result.put("isLogDelete", isLogDelete);
                    result.put("isJtlDelete", isJtlDelete);
                    return CommonResponse.makeOKRsp(result);
                }
            }

        }
        return CommonResponse.makeOKRsp();
    }

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/singleFileUpload")
    public Response fileUpload(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        File fileDir = new File(ConfigUtil.getInstance().getValue("fileDownLoadPath"));
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        String path = fileDir.getAbsolutePath();
        try {
            file.transferTo(new File(fileDir.getAbsolutePath(), fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return CommonResponse.makeRsp(ErrorCode.UPLOAD_FILE_TRANSFER_FAIL);
        }
        Map<String, String> result = new HashMap<String, String>();
        result.put("name", file.getOriginalFilename());
        result.put("size", String.valueOf(file.getSize()));
        return CommonResponse.makeOKRsp(result);
    }

    /**
     * 文件下载
     *
     * @param response
     */
    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public void downFileFromServer(String userName, String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        response.setContentType("application/octet-stream");
        //response.setContentType("multipart/form-data;charset=UTF-8");也可以明确的设置一下UTF-8，测试中不设置也可以。
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));

        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            String donwloadPath = new File(ConfigUtil.getInstance().getValue("fileGeneratePath") + "/" + userName, fileName).getAbsolutePath();
            System.out.println(donwloadPath);
            bis = new BufferedInputStream(new FileInputStream(new File(ConfigUtil.getInstance().getValue("fileGeneratePath") + "/" + userName, fileName)));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 执行jmeter脚本
     *
     * @param fileName
     * @param userName
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "execJmeterScript", method = RequestMethod.POST)
    public Response execJmeterScript(String fileName, String userName) throws IOException {

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
        String cmd = ConfigUtil.getInstance().getValue("jmeterPath")+" -n -t "
                + scriptFilePath
                + " -l "
                +
                jtlRootPath
                + " -e -o "
                + reportDir.getAbsolutePath()
                + " -j "
                + logRootPath;

            executor.execute(new ExecJmeterScript(cmd));

        if (fileName.split("_").length > 1) {
            String[] names = fileName.split("\\.")[0].split("_");
            boolean rename = new File(scriptFilePath).renameTo(
                    new File(ConfigUtil.getInstance().getValue("fileGeneratePath") + "/" +
                            userName + "/" + names[0] + "_" + names[1] + "_" + time + ".jmx"));
            if (!rename) {
                return CommonResponse.makeRsp(ErrorCode.SCRIPT_RENAME_FAIL);
            }
        } else {
            boolean rename = new File(scriptFilePath).renameTo(
                    new File(ConfigUtil.getInstance().getValue("fileGeneratePath") + "/" +
                            userName + "/" + fileName.split("\\.")[0] + "_" + time + ".jmx"));
            if (!rename) {
                return CommonResponse.makeRsp(ErrorCode.SCRIPT_RENAME_FAIL);
            }
        }
        Map<String, String> result = new HashMap<>();
        result.put("reportPath", reportDir.getAbsolutePath());
        result.put("logPath", logRootPath + "/" + fileName.split("\\.")[0] + ".log");
        return CommonResponse.makeOKRsp(result);
    }

}
class ExecJmeterScript implements Runnable{

    private String cmd;
    public ExecJmeterScript(String cmd){
        this.cmd = cmd;
    }

    @Override
    public void run() {
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
            throw new RuntimeException("执行脚本出错");
        }
    }
}
