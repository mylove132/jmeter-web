package com.okjiaoyu.jmeter.service;
import com.okjiaoyu.jmeter.entity.AutoGenerateJmxEntity;
import com.okjiaoyu.jmeter.util.ConfigUtil;
import com.okjiaoyu.jmeter.util.DateUtil;
import com.okjiaoyu.jmeter.util.JMXAutoGenerateUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

@Service("autoGenerateService")
public class AutoGenerateJmxService {


    public String autoGenerateJmxFile(AutoGenerateJmxEntity entity) {
        final String fileName;
        String time = DateUtil.timeStamp();
        JMXAutoGenerateUtil jmxAutoGenerate = new JMXAutoGenerateUtil();
        String generateValue = jmxAutoGenerate.generateFileHead(entity.getJmeterVersion())
                + jmxAutoGenerate.generateTopashTree()
                + jmxAutoGenerate.generateThreadGroup(entity.getPreNumber(), entity.getPreTime())
                + jmxAutoGenerate.generatePreData(entity.getTimeOut(), entity.getPreName(), entity.getZkAddress(),
                entity.getDubboInterfaceName(), entity.getMethodName(),
                entity.getRequestBeanRenfence(),
                entity.getParam())
                + jmxAutoGenerate.assertGui(entity.getAssertText())
                + jmxAutoGenerate.generateWatchResultTree()
                + jmxAutoGenerate.generateSumResult();
        OutputStream os = null;
        String rootPath = ConfigUtil.getInstance().getValue("fileGeneratePath");
        File userPath = new File(rootPath + "/" + entity.getUserName());
        if (!userPath.exists()) {
            userPath.mkdirs();
        }
        fileName = entity.getPreName() + "_" + time + ".jmx";
        try {
            os = new FileOutputStream(new File(rootPath + "/" + entity.getUserName() + "/" + fileName));
            os.write(generateValue.getBytes());
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return fileName;
    }
}
