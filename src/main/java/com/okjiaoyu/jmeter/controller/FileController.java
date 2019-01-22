package com.okjiaoyu.jmeter.controller;

import com.okjiaoyu.jmeter.response.CommonResponse;
import com.okjiaoyu.jmeter.response.ErrorCode;
import com.okjiaoyu.jmeter.response.Response;
import com.okjiaoyu.jmeter.util.ConfigUtil;
import com.okjiaoyu.jmeter.util.DateUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FileController {


    /**
     * 获取用户的脚本目录
     * @param userName
     * @return
     */
    @PostMapping(value = "/jmxFileList")
    public Response jmxFileList(String userName) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();
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
                    System.out.println("******************"+jmxFile.getAbsolutePath());
                    System.out.println("******************"+jmxFile.getName());
                    result.put("createPerson", userName);
                    result.put("fileName", jmxFile.getName());
                    String date = DateUtil.formatDate(DateUtil.timeStampTansforDate(Long.parseLong(jmxFile.getName().
                            split("_")[1].split("\\.")[0])));
                    result.put("createTime", date);
                    resultList.add(result);
                }
            }
        }
        return CommonResponse.makeOKRsp(resultList);
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
    public void downFileFromServer(String fileName, HttpServletResponse response) {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(new File(ConfigUtil.getInstance().getValue("fileDownLoadPath"), fileName)));
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
}
