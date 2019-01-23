//点击菜单箭头变化
$(".page-sidebar .sidebar-menu a").each(function () {
    $(this).click(function () {
        var Oele = $(this).children('.menu-expand');
        if ($(Oele)) {
            if ($(Oele).hasClass('glyphicon-chevron-right')) {
                $(Oele).removeClass('glyphicon-chevron-right').addClass('glyphicon-chevron-down');
            } else {
                $(Oele).removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-right');
            }
        }
        //选中增加active
        if (!$(this).hasClass('panel-heading')) {
            $(".page-sidebar .sidebar-menu a").removeClass('active');
            $(this).addClass('active');
        }
    });
});

/**
 * 获取所有的dubbo服务
 */
function getDubboServices() {
    var register_protocol = $("#registry_protocol").find("option:selected").text();
    var register_address = $("#registry_address").find("option:selected").text();
    $("#interface_list").empty();
    if (register_protocol == "zookeeper" && register_address != "===请选择===") {
        $.bootstrapLoading.start({loadingTips: "正在请求数据，请稍候..."});
        $.ajax({
            type: "POST",
            url: "/getDubboServices",
            data: {address: register_address},
            dataType: "json",
            success: function (data) {
                var response = data.data.serviceList;
                for (service in response) {
                    var option = '<option>' + response[service] + '</option>';
                    $("#interface_list").append(option);
                }
            },
            complete: function () {
                $.bootstrapLoading.end();
            }
        });
    }
};

/**
 * 获取dubbo接口的方法
 */
function getDubboMethods() {
    $("#method_name").val("");
    $("#method_list").empty();
    $.ajax({
        type: "POST",
        url: "/getDubboMethods",
        data: {
            address: $("#registry_address").val() != "===请选择===" ? $("#registry_address").val() : "172.18.4.48:2181",
            serviceName: $("#interface_name").val()
        },
        dataType: "json",
        success: function (data) {
            var response = data.data.methodList;
            for (service in response) {
                var option = '<option>' + response[service] + '</option>';
                $("#method_list").append(option);
            }
        }
    });
};

/**
 * 动态添加参数
 * @param obj
 */
function addCenterIpGrp(obj) {
    html = '<div class="input-group centerParam">' +
        '<label class="input-group-addon">请求类型：</label>' +
        '<input type="text" class="form-control request_param_type">' +
        '<label class="input-group-addon">参数值：</label>' +
        '<input type="text" class="form-control request_param_value">' +
        '<span class="input-group-btn">' +
        '<button class="btn btn-info" type="button" data-toggle="tooltip" title="删除" id="delCenterIpGrp"><span class="glyphicon glyphicon-minus"></span></button>' +
        '</span>' +
        '</div>';
    obj.insertAdjacentHTML('beforebegin', html)
};
/**
 * 删除参数列
 *
 */
$(document).on('click', '#delCenterIpGrp', function () {
    var el = this.parentNode.parentNode;
    el.parentNode.removeChild(el);
});

/**
 * 测试dubbo接口
 */
function testDubbo() {

    $("#testResult").val("");
    var protocolName = $("#registry_protocol").find("option:selected").text();
    var address = $("#registry_address").find("option:selected").text();
    var interfaceName = $("#interface_name").val();
    var methodName = $("#method_name").val();
    var timeOut = $("#timeOut").val();

    var requestType = $(".request_param_type").val();
    var requestValue = $(".request_param_value").val();

    if (protocolName == "===请选择===") {
        bootbox.alert({
            size: "small",
            title: "注册中心协议为空",
            message: "请选择注册中心协议",
            callback: function () {
                $("#registry_protocol").focus();
            }
        });
        return;
    }
    if (address == "===请选择===") {
        bootbox.alert({
            size: "small",
            title: "注册中心地址为空",
            message: "请选择注册中心IP地址",
            callback: function () {
                $("#registry_address").focus();
            }
        });
        return;
    }
    if (interfaceName == "" || interfaceName == null) {
        bootbox.alert({
            size: "small",
            title: "输入为空",
            message: "测试接口不能为空",
            callback: function () {
                $("#interface_name").focus();
                return;
            }
        });
        return;
    }
    if (methodName == "" || methodName == null) {
        bootbox.alert({
            size: "small",
            title: "输入为空",
            message: "测试方法不能为空",
            callback: function () {
                $("#method_name").focus();
                return;
            }
        });
        return;
    }
    if (timeOut == "" || timeOut == null) {
        bootbox.alert({
            size: "small",
            title: "输入为空",
            message: "接口超时时间不能为空",
            callback: function () {
                $("#interface_name").focus();
                return;
            }
        });
        return;
    }
    var jsonParam = {};
    var paramArray = new Array();
    jsonParam["paramType"] = requestType;
    jsonParam["paramValue"] = requestValue;
    paramArray.push(jsonParam);

    var jsonData = {};
    jsonData["protocol"] = protocolName;
    jsonData["address"] = address;
    jsonData["interfaceName"] = interfaceName;
    jsonData["methodName"] = methodName;
    jsonData["timeOut"] = timeOut;
    jsonData["requestParamTypeArgs"] = paramArray;

    if (!window.localStorage) {
        alert("请使用高版本浏览器,此浏览器不支持localstorage");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/dubboTest",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(jsonData),
        dataType: "json",
        success: function (data) {
            if (data != null) {
                var storage = window.localStorage;
                storage.setItem("dubbo_test_value", JSON.stringify(jsonData));
                $("#testResult").val(JSON.stringify(data.data))
            }
        },
        error: function (message) {
            alert("测试失败")
        }
    });

};

/**
 * 生成脚本
 */
function generateReport() {
    if (window.localStorage.getItem("dubbo_test_value") == null) {
        alert("请先测试接口");
        return;
    }
    var dubboTestData = JSON.parse(window.localStorage.getItem("dubbo_test_value"));
    var jmeterVersion = $("#jemter_version").find("option:selected").text();
    var preInterfaceName = $("#pre_name").val();
    var preTime = $("#pre_time").val();
    var preNum = $("#pre_number").val();
    var assertText = $("#assert_text").val();

    if (jmeterVersion == "===请选择===") {
        bootbox.alert({
            size: "small",
            title: "jmeter版本为空",
            message: "请选择jmeter版本",
            callback: function () {
                $("#jemter_version").focus();
            }
        });
        return;
    }

    if (preInterfaceName == "" || preInterfaceName == null) {
        bootbox.alert({
            size: "small",
            title: "测试接口名称描述不能为空",
            message: "请输入测试接口名称",
            callback: function () {
                $("#pre_name").focus();
            }
        });
        return;
    }

    if (preTime == "" || preTime == null) {
        bootbox.alert({
            size: "small",
            title: "压测时长不能为空",
            message: "请输入压测时长",
            callback: function () {
                $("#pre_number").focus();
            }
        });
        return;
    }

    if (preNum == "" || preNum == null) {
        bootbox.alert({
            size: "small",
            title: "测试并发数不能为空",
            message: "请输入压测并发数",
            callback: function () {
                $("#pre_number").focus();
            }
        });
        return;
    }

    if (assertText == "" || assertText == null) {
        bootbox.alert({
            size: "small",
            title: "断言不能为空",
            message: "请输入断言内容",
            callback: function () {
                $("#assert_text").focus();
            }
        });
        return;
    }
    console.log(dubboTestData)
    var jsonData = {};
    jsonData["userName"] = $("#userName").text();
    jsonData["jmeterVersion"] = jmeterVersion;
    jsonData["timeOut"] = dubboTestData['timeOut'];
    jsonData["dubboInterfaceName"] = dubboTestData['interfaceName'];
    jsonData["methodName"] = dubboTestData['methodName'];
    jsonData["requestBeanRenfence"] = dubboTestData.requestParamTypeArgs[0].paramType;
    jsonData["param"] = dubboTestData.requestParamTypeArgs[0].paramValue;
    jsonData["zkAddress"] = dubboTestData['address'];
    jsonData["preTime"] = preTime;
    jsonData["preNumber"] = preTime;
    jsonData["preName"] = preInterfaceName;
    jsonData["assertText"] = assertText;

    console.log("测试数据：" + JSON.stringify(jsonData));

    $.ajax({
        url: "/generateJmxFile",
        contentType: "application/json; charset=utf-8",
        type: "POST",
        data: JSON.stringify(jsonData),
        dataType: "json",
        success: function (data) {
            getUserJmxList();
        }
    })

}

/**
 * 获取用户脚本列表
 */
function getUserJmxList() {
    $("#jmxList").empty();
    $.ajax({
        url: "/jmxFileList",
        data: {
            userName: $("#userName").text()
        },
        dataType: "json",
        type: "POST",
        success: function (response) {
            if (response.code == "0" && response.data != null) {
                var result = response.data;
                for (res in result) {
                    var fileName = result[res].fileName;
                    var createTime = result[res].createTime;
                    var createPerson = result[res].createPerson;
                    var html = "<tr><td>" + createPerson + "</td>" +
                        "<td>" + fileName + "</td>" +
                        "<td>" + createTime + "</td>" +
                        "<td>" + "<button class=\"btn btn-danger delete-btn\" type='button'>删除</button>" +
                        "              <button class=\"btn btn-primary download-btn\" type='button'>下载</button>" +
                        "              <button class=\"btn btn-primary generate-btn\" type='button'>生成测试报告</button>" +
                        "              <button class=\"btn btn-info see-report-btn\" type='button'>查看测试报告</button>" + "</td></tr>"
                    $("#jmxList").append(html);
                }
            }
        }
    })
}

/**
 * 删除脚本
 */
var name, file;
$(document).on('click','.delete-btn',function (e) {
    e.preventDefault();
    $(this).closest('tr').find('td').each(function (i, v) {
        if (i == 0) {
            name = $(this).text();
        }
        else if (i == 1) {
            file = $(this).text();
        }
    });
    $.ajax({
        url:"/deleteJmxFile",
        type:"POST",
        data:{
            userName: name,
            fileName: file
        },
        dataType:"json",
        success:function (response) {
            getUserJmxList()
        }
    })
});

/**
 * 下载脚本文件
 */
$(document).on('click','.download-btn',function (e) {
    $(this).closest('tr').find('td').each(function (i, v) {
        if (i == 0) {
            name = $(this).text();
        }
        else if (i == 1) {
            file = $(this).text();
        }
    });
    var url = "/downloadFile";
    var form = $("<form></form>").attr("action", url).attr("method", "get");
    form.append($("<input></input>").attr("type", "hidden").attr("name", "fileName").attr("value", file));
    form.append($("<input></input>").attr("type", "hidden").attr("name", "userName").attr("value", name));
    form.appendTo('body').submit().remove();
});
/**
 * 生成测试报告
 */
$(document).on('click','.generate-btn',function (e) {
    e.preventDefault();
    $(this).text("执行中...");
    $(this).attr('disabled',true);
    var thisBtn = $(this);
    $(this).closest('tr').find('td').each(function (i, v) {
        if (i == 0) {
            name = $(this).text();
        }
        else if (i == 1) {
            file = $(this).text();
        }
    });
    $.ajax({
        url:"/execJmeterScript",
        type:"POST",
        data:{
            userName: name,
            fileName: file
        },
        dataType:"json",
        success:function (response) {
            if (response.code == "0"){
                thisBtn.text("执行成功");
            }else {
                thisBtn.text("执行失败");
            }
        }
    })
});

$(document).on('click','.see-report-btn',function (e) {
    $(this).closest('tr').find('td').each(function (i, v) {
        if (i == 0) {
            name = $(this).text();
        }
        else if (i == 1) {
            file = $(this).text();
        }
    });
    var url = encodeURI("http://127.0.0.1:8082/"+name+"/"+file.split(".")[0]+"/index.html");
    console.log(url);
    window.open(url);
});

$(document).ready(function () {
    getUserJmxList();
});
