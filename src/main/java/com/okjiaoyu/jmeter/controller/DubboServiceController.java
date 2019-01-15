package com.okjiaoyu.jmeter.controller;

import com.okjiaoyu.jmeter.entity.AutoGenerateJmxEntity;
import com.okjiaoyu.jmeter.response.CommonResponse;
import com.okjiaoyu.jmeter.response.ErrorCode;
import com.okjiaoyu.jmeter.response.Response;
import com.okjiaoyu.jmeter.service.AutoGenerateJmxService;
import com.okjiaoyu.jmeter.util.ZkServiceUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class DubboServiceController {

    @Autowired
    private AutoGenerateJmxService autoGenerateJmxService;

    @RequestMapping(value = "getDubboServices",method = RequestMethod.POST)
    public Response getDubboServices(String address){
        Map<String, String[]> serviceMap = ZkServiceUtil.getInterfaceMethods(address);
        if (serviceMap != null && !serviceMap.isEmpty()){
            return CommonResponse.makeOKRsp(serviceMap);
        }
        return CommonResponse.makeRsp(ErrorCode.DUBBO_SERVICE_FAIL);
    }

    @RequestMapping(value = "generateJmxFile",method = RequestMethod.POST)
    public Response autoGenerateJmxFile(AutoGenerateJmxEntity entity){
        Map<String, Object> result = new HashedMap();
        String fileName = autoGenerateJmxService.autoGenerateJmxFile(entity);
        if (null == fileName || "".equals(fileName)){
            return CommonResponse.makeRsp(ErrorCode.AUTO_GENERATE_JMX_FILE_FAIL);
        }else {
            result.put("fileName",fileName);
            return CommonResponse.makeOKRsp(result);
        }
    }
}
