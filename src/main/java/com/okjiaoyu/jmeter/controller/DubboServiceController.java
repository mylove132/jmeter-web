package com.okjiaoyu.jmeter.controller;

import com.alibaba.dubbo.rpc.service.GenericService;
import com.okjiaoyu.jmeter.entity.AutoGenerateJmxEntity;
import com.okjiaoyu.jmeter.entity.DubboEntity;
import com.okjiaoyu.jmeter.entity.RequestTypeArgments;
import com.okjiaoyu.jmeter.response.CommonResponse;
import com.okjiaoyu.jmeter.response.ErrorCode;
import com.okjiaoyu.jmeter.response.Response;
import com.okjiaoyu.jmeter.service.AutoGenerateJmxService;
import com.okjiaoyu.jmeter.util.ClassUtils;
import com.okjiaoyu.jmeter.util.ZkServiceUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class DubboServiceController {

    @Autowired
    private AutoGenerateJmxService autoGenerateJmxService;
    ZkServiceUtil zkServiceUtil = new ZkServiceUtil();

    @RequestMapping(value = "getDubboServices", method = RequestMethod.POST)
    public Response getDubboServices(String address) {
        Set<String> services = null;
        try {
            services = zkServiceUtil.getServices(address);
        }catch (RuntimeException e){
            return CommonResponse.makeErrRsp(e.getMessage());
        }

        if (services != null && !services.isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            result.put("serviceList", services);
            return CommonResponse.makeOKRsp(result);
        }
        return CommonResponse.makeRsp(ErrorCode.DUBBO_SERVICE_FAIL);
    }

    @RequestMapping(value = "getDubboMethods", method = RequestMethod.POST)
    public Response getDubboMethods(String address, String serviceName) {
        String[] methodList = zkServiceUtil.getMethods(address, serviceName);
        if (methodList != null && methodList.length > 0) {
            Map<String, Object> result = new HashMap<>();
            result.put("methodList", methodList);
            return CommonResponse.makeOKRsp(result);
        }
        return CommonResponse.makeRsp(ErrorCode.DUBBO_METHOD_FAIL);
    }

    @RequestMapping(value = "dubboTest", method = RequestMethod.POST)
    public Response testDubboInterface(@RequestBody DubboEntity entity) {
        if (entity.getInterfaceName() == null || entity.getInterfaceName().equals("")) {
            return CommonResponse.makeRsp(ErrorCode.REQUEST_PARAM_NULL.setMsg("接口不能为空"));
        }
        if (entity.getMethodName() == null || entity.getMethodName().equals("")) {
            return CommonResponse.makeRsp(ErrorCode.REQUEST_PARAM_NULL.setMsg("方法不能为空"));
        }
        List<RequestTypeArgments> requestTypeArgments = entity.getRequestParamTypeArgs();
        List<String> paramTypeList = new ArrayList<>();
        List<Object> paramValueList = new ArrayList<>();
        for (RequestTypeArgments typeArgments : requestTypeArgments) {
            if (typeArgments.getParamType() == null || typeArgments.getParamType().equals("")) {
                return CommonResponse.makeRsp(ErrorCode.REQUEST_PARAM_NULL.setMsg("参数类型不能为空"));
            }
            if (typeArgments.getParamValue() == null || typeArgments.getParamValue().equals("")) {
                return CommonResponse.makeRsp(ErrorCode.REQUEST_PARAM_NULL.setMsg("参数值不能为空"));
            }
            ClassUtils.parseParameter(paramTypeList, paramValueList, typeArgments);
        }
        if (entity.getAddress() == null || entity.getAddress().equals("")) {
            return CommonResponse.makeRsp(ErrorCode.REQUEST_PARAM_NULL.setMsg("协议地址不能为空"));
        }
        if (entity.getProtocol() == null || entity.getProtocol().equals("")) {
            return CommonResponse.makeRsp(ErrorCode.REQUEST_PARAM_NULL.setMsg("协议不能为空"));
        }
        if (entity.getTimeOut() == 0) {
            entity.setTimeOut(5000);
        }
        if (entity.getGroup().equals("") || entity.getGroup() == null) {
            entity.setGroup(null);
        }
        if (entity.getVersion() == null || entity.getVersion().equals("")) {
            entity.setVersion("3.0.0");
        }

        GenericService service = zkServiceUtil.getGenericService(entity);
        Object result = service.$invoke(entity.getMethodName(),
                paramTypeList.toArray(new String[paramTypeList.size()]),
                paramValueList.toArray(new Object[paramValueList.size()]));
        return CommonResponse.makeOKRsp(result);
    }

    @RequestMapping(value = "generateJmxFile", method = RequestMethod.POST)
    public Response autoGenerateJmxFile(AutoGenerateJmxEntity entity) {
        Map<String, Object> result = new HashedMap();
        String fileName = autoGenerateJmxService.autoGenerateJmxFile(entity);
        if (null == fileName || "".equals(fileName)) {
            return CommonResponse.makeRsp(ErrorCode.AUTO_GENERATE_JMX_FILE_FAIL);
        } else {
            result.put("fileName", fileName);
            return CommonResponse.makeOKRsp(result);
        }
    }
}
