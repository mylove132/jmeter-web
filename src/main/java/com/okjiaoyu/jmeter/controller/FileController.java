package com.okjiaoyu.jmeter.controller;

import com.okjiaoyu.jmeter.response.CommonResponse;
import com.okjiaoyu.jmeter.response.ErrorCode;
import com.okjiaoyu.jmeter.response.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FileController {

    /**
     * 文件上传
     * @param file
     * @return
     */
    @RequestMapping(value = "/singleFileUpload")
    public Response fileUpload(MultipartFile file){
        String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
        File fileDir = new File("D:\\tmp");
        if(!fileDir.exists()){
            fileDir.mkdir();
        }
        String path = fileDir.getAbsolutePath();
        try {
            file.transferTo(new File(fileDir.getAbsolutePath(),fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return CommonResponse.makeRsp(ErrorCode.UPLOAD_FILE_TRANSFER_FAIL);
        }
        Map<String ,String> result = new HashMap<String, String>();
        result.put("name",file.getOriginalFilename());
        result.put("size",String.valueOf(file.getSize()));
        return CommonResponse.makeOKRsp(result);
    }

    /**
     * 文件下载
     * @param response
     */
    @RequestMapping(value = "/downloadFile", method= RequestMethod.GET)
    public void downFileFromServer(String fileName, String filePath,HttpServletRequest request, HttpServletResponse response){
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(new File("tmp",fileName)));
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
