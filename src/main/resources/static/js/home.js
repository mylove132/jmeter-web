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

function getReportList(fileName, callback) {
    var result = [];
    $.ajax({
        url: "/reportList",
        data: {
            fileName: fileName,
            userName: $("#userName").text()
        },
        dataType: "json",
        type: "POST",
        success: function (response) {
            result = response.data.reportNameList;
            callback(result);
        }
    });
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
                        "              <button class=\"btn btn-primary get_report_list\" type='button'>获取报告列表</button>" +

                        "</td>"
                        + "<td><select class='form-control input-md report_show_list'><option>==请选择==</option></select></td></tr>"
                    $("#jmxList").append(html);
                }
            }
        }
    })
}

function getUserJmxScriptToTimerTask() {
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
                for (i in result) {
                    $("#timer_task_script_name").append('<option>' + result[i].fileName + '</option>');
                }
            }
        }
    })
}

$(document).on('change', '.report_show_list', function (e) {
    $(this).closest('tr').find('td').each(function (i, v) {
        if (i == 0) {
            name = $(this).text();
        }
        else if (i == 1) {
            file = $(this).text();
        } else if (i == 4) {
            let reportDirName = file.split(".")[0];
            let lastSpiltIndex = reportDirName.lastIndexOf('_');
            let ns = reportDirName.substr(0,lastSpiltIndex);
            var reportName = $(this).find("option:selected").text();
            let rn = new Date(reportName).valueOf()/1000;
            if (reportName != "==请选择==") {
                var url = encodeURI("http://172.18.4.55:8082/"+name+"/" + ns+"_"+rn + "/index.html");
                console.log(url);
                window.open(url);
            } else {
                alert("请选择报告")
            }
        }
    });

})
/**
 * 删除脚本
 */
var name, file, reportNameList;
$(document).on('click', '.delete-btn', function (e) {
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
        url: "/deleteJmxFile",
        type: "POST",
        data: {
            userName: name,
            fileName: file
        },
        dataType: "json",
        success: function (response) {
            getUserJmxList()
        }
    })
});

/**
 * 下载脚本文件
 */
$(document).on('click', '.download-btn', function (e) {
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
$(document).on('click', '.generate-btn', function (e) {
    e.preventDefault();
    $(this).text("执行中...");
    $(this).attr('disabled', true);
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
        url: "/execJmeterScript",
        type: "POST",
        data: {
            userName: name,
            fileName: file
        },
        dataType: "json",
        success: function (response) {
            if (response.code == "0") {
                alert("执行成功");
                getUserJmxList();
            } else {
                thisBtn.text("执行失败");
            }
        }
    })
});

/**
 * 获取报告列表
 */
$(document).on('click', '.get_report_list', function (e) {
    $(this).closest('tr').find('td').each(function (i, v) {
        if (i == 0) {
            name = $(this).text();
        }
        else if (i == 1) {
            file = $(this).text();
        } else if (i == 4) {
            var select = $(this).children();
            getReportList(file, function (data) {
                if (data.length == 0){
                    alert("没有测试报告，请压测")
                }
                for (reportName in data) {
                    var names = data[reportName].split('_');
                    var timeName = names[names.length - 1];
                    console.log(formatDate((timeName)))
                    select.append('<option>' + formatDate(timeName) + '</option>');
                }
            });

        }
    });

});

/**
 * 时间格式方法
 *
 * @param {any} timeStamp  时间戳，秒级/毫秒级
 * @param {any} type 格式化时间类型，默认  Y-M-D H:I:S
 * @returns {string} formatTime 格式化后的时间 例如： 2017-05-05 12:09:22
 */
function formatDate(timeStamp, type = 'Y-M-D H:I:S', auto = false) {
    let time = (timeStamp + '').length === 10 ? new Date(parseInt(timeStamp) * 1000) : new Date(parseInt(timeStamp));
    let _year = time.getFullYear();
    let _month = (time.getMonth() + 1) < 10 ? '0' + (time.getMonth() + 1) : (time.getMonth() + 1);
    let _date = time.getDate() < 10 ? '0' + time.getDate() : time.getDate();
    let _hours = time.getHours() < 10 ? '0' + time.getHours() : time.getHours();
    let _minutes = time.getMinutes() < 10 ? '0' + time.getMinutes() : time.getMinutes();
    let _secconds = time.getSeconds() < 10 ? '0' + time.getSeconds() : time.getSeconds();
    let formatTime = '';
    let distinctTime = new Date().getTime() - time.getTime();

    if (auto) {
        if (distinctTime <= (1 * 60 * 1000)) {
            // console.log('一分钟以内，以秒数计算');
            let _s = Math.floor((distinctTime / 1000) % 60);
            formatTime = _s + '秒前';
        } else if (distinctTime <= (1 * 3600 * 1000)) {
            // console.log('一小时以内,以分钟计算');
            let _m = Math.floor((distinctTime / (60 * 1000)) % 60);
            formatTime = _m + '分钟前';
        } else if (distinctTime <= (24 * 3600 * 1000)) {
            // console.log('一天以内，以小时计算');
            let _h = Math.floor((distinctTime / (60 * 60 * 1000)) % 24);
            formatTime = _h + '小时前';
        } else if (distinctTime <= (30 * 24 * 3600 * 1000)) {
            let _d = Math.floor((distinctTime / (24 * 60 * 60 * 1000)) % 30);
            formatTime = _d + '天前';
            // console.log('30天以内,以天数计算');
        } else {
            // 30天以外只显示年月日
            formatTime = _year + '-' + _month + '-' + _date;
        }
    } else {

        switch (type) {
            case 'Y-M-D H:I:S':
                formatTime = _year + '-' + _month + '-' + _date + ' ' + _hours + ':' + _minutes + ':' + _secconds;
                break;
            case 'Y-M-D H:I:S zh':
                formatTime = _year + '年' + _month + '月' + _date + '日  ' + _hours + ':' + _minutes + ':' + _secconds;
                break;
            case 'Y-M-D H:I':
                formatTime = _year + '-' + _month + '-' + _date + ' ' + _hours + ':' + _minutes;
                break;
            case 'Y-M-D H':
                formatTime = _year + '-' + _month + '-' + _date + ' ' + _hours;
                break;
            case 'Y-M-D':
                formatTime = _year + '-' + _month + '-' + _date;
                break;
            case 'Y-M-D zh':
                formatTime = _year + '年' + _month + '月' + _date + '日';
                break;
            case 'Y-M':
                formatTime = _year + '-' + _month;
                break;
            case 'Y':
                formatTime = _year;
                break;
            case 'M':
                formatTime = _month;
                break;
            case 'D':
                formatTime = _date;
                break;
            case 'H':
                formatTime = _hours;
                break;
            case 'I':
                formatTime = _minutes;
                break;
            case 'S':
                formatTime = _secconds;
                break;
            default:
                formatTime = _year + '-' + _month + '-' + _date + ' ' + _hours + ':' + _minutes + ':' + _secconds;
                break;
        }
    }
    // 返回格式化的日期字符串
    return formatTime;
}

$(document).ready(function () {
    getUserJmxList();
    getUserJmxScriptToTimerTask();
});

$(document).on('click', '#add_timer_task_btn', function () {
    $("#update").modal('show');
});

function addTimerTask() {
    var scriptName = $("#timer_task_script_name").find("option:selected").text();
    var time = $("#task_timer").val();

    alert(scriptName);
    alert(time);

    $.ajax({
        url: "/updateTimerTask",
        data: {
            fileName: scriptName,
            time: time,
            userName: $("#userName").text()
        },
        dataType: "json",
        type: "post",
        success: function (response) {
            console.log(response)
            if (response.code == "0") {
                $("#update").modal('hide');
                var userName = $("#userName").text();
                var fName = scriptName;
                var t = time;
                var html = "<tr><td>" + userName + "</td>" +
                    "<td>" + fName + "</td>" +
                    "<td>" + t + "</td>" +
                    "<td>" + "<button class=\"btn btn-danger delete-timer_btn\" type='button'>删除</button>" +
                    "              <button class=\"btn btn-primary update_timer-btn\" type='button'>修改</button>";
                $("#timer_task_list").append(html);
            } else {
                if (response.code == "7002") {
                    alert(response.msg);
                } else {
                    alert("添加定时任务失败");
                }
            }
        }
    })
}

function getTimerTaskList() {
    $.ajax({
        url: "/timerList",
        data: {
            userName: $("#userName").text()
        },
        dataType:"json",
        type:"POST",
        success: function (response) {
            if (response.code == "0") {
                response.data.fileName
            }
        }
    })
}
