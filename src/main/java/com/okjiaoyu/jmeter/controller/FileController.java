package com.okjiaoyu.jmeter.controller;

import com.okjiaoyu.jmeter.response.CommonResponse;
import com.okjiaoyu.jmeter.response.ErrorCode;
import com.okjiaoyu.jmeter.response.Response;
import com.okjiaoyu.jmeter.util.ConfigUtil;
import com.okjiaoyu.jmeter.util.DateUtil;
import com.okjiaoyu.jmeter.util.DeleteFolderUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FileController {


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
            return CommonResponse.makeErrRsp("目录没有文件");
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
                    String timeStamp = fileName.substring(fileName.lastIndexOf("_") + 1, fileName.length()).split("\\.")[0];
                    String date = DateUtil.formatDate(DateUtil.timeStampTansforDate(Long.parseLong(timeStamp)));
                    result.put("createTime", date);
                    resultList.add(result);
                }
            }
        }
        return CommonResponse.makeOKRsp(resultList);
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
                    String reportPath = ConfigUtil.getInstance().getValue("jmeterReportPath")+"/"+userName+"/"+fileName.split("\\.")[0];
                    if (new File(reportPath).exists()){
                        isReportDelete = DeleteFolderUtil.delFolder(reportPath);
                    }else{
                        System.out.println(reportPath+"目录不存在");
                        isReportDelete = true;
                    }
                    String logPath = ConfigUtil.getInstance().getValue("jmeterExecLogPath")+"/"+userName+"/"+fileName.split("\\.")[0]+".log";
                    if (new File(logPath).exists()){
                        isLogDelete = new File(logPath).delete();
                    }else {
                        System.out.println(logPath+"日志不存在");
                        isLogDelete = true;
                    }
                    String jtlPath = ConfigUtil.getInstance().getValue("jmeterResultPath")+"/"+userName+"/"+fileName.split("\\.")[0]+".jtl";
                    if (new File(jtlPath).exists()){
                        isJtlDelete = new File(jtlPath).delete();
                    }else {
                        System.out.println(jtlPath+"结果不存在");
                        isJtlDelete = true;
                    }
                    Map<String, Object> result = new HashMap<>();
                    result.put("isScriptDelete", deleteFile.delete());
                    result.put("isReportDelete", isReportDelete);
                    result.put("isLogDelete", isLogDelete);
                    result.put("isJtlDelete", isJtlDelete);
                    return CommonResponse.makeOKRsp(result);
                } else {
                    return CommonResponse.makeErrRsp("删除文件不存在");
                }
            } else {
                return CommonResponse.makeErrRsp("删除文件不存在");
            }
        }
        return CommonResponse.makeErrRsp("删除文件不存在");
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
     * @param fileName
     * @param userName
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "execJmeterScript", method = RequestMethod.POST)
    public Response execJmeterScript(String fileName, String userName) throws IOException {

        //脚本位置
        String scriptFilePath = ConfigUtil.getInstance().getValue("fileGeneratePath") + "/" + userName + "/" + fileName;

        //报告位置
        String rootPath = ConfigUtil.getInstance().getValue("jmeterReportPath") + "/" + userName;
        File rootFile = new File(rootPath);
        if (!rootFile.isDirectory() || !rootFile.exists()) {
            rootFile.mkdirs();
        }
        File reportDir = new File(rootPath + "/" + fileName.split("\\.")[0]);
        if (!reportDir.isDirectory() || !reportDir.exists()) {
            reportDir.mkdirs();
        }
        //结果文件存放位置
        String jtlRootPath = ConfigUtil.getInstance().getValue("jmeterResultPath") + "/" + userName;
        File jtlRootFile = new File(jtlRootPath);
        if (!jtlRootFile.isDirectory() || !jtlRootFile.exists()) {
            jtlRootFile.mkdirs();
        }
        //执行日志存放位置
        String logRootPath = ConfigUtil.getInstance().getValue("jmeterExecLogPath")+"/"+userName;
        File logFile = new File(logRootPath);
        if (!logFile.isDirectory() || !logFile.exists()) {
            logFile.mkdirs();
        }
        String cmd = "/Users/liuzhanhui/Documents/jmeter/apache-jmeter-3.1/bin/jmeter -n -t "
                + scriptFilePath
                + " -l "
                + jtlRootPath + "/"
                + fileName.split("\\.")[0] + ".jtl"
                + " -e -o "
                + reportDir.getAbsolutePath()
                +" -j "
                +logRootPath+"/"+fileName.split("\\.")[0] + ".log";

        try {
            Process ps = Runtime.getRuntime().exec(cmd);
            ps.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            System.out.println(result);
        } catch (Exception e) {
            return CommonResponse.makeErrRsp("执行脚本出错，请下载检查脚本");
        }
        Map<String,String> result = new HashMap<>();
        result.put("reportPath",reportDir.getAbsolutePath());
        result.put("logPath",logRootPath+"/"+fileName.split("\\.")[0] + ".log");
        return CommonResponse.makeOKRsp(result);
    }

}
