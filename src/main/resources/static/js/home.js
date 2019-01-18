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
    html = '<div class="input-group centerIp">' +
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


function testDubbo() {
    alert($("#registry_protocol").find("option:selected").text())
    alert($("#registry_address").find("option:selected").text())
    alert($("#interface_name").val())
    alert($("#method_name").val())
    alert($("#timeOut").val())

    var arr = new Array();
    var element = {
        "paramType": "com.noriental.adminsvr.request.RequestEntity",
        "paramValue": '{"entity":[200,201,202]}'
    };
    arr.push(element);

    $.ajax({
        type: "POST",
        url: "/dubboTest",
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify({
            address: $("#registry_address").find("option:selected").text(),
            protocol: $("#registry_protocol").find("option:selected").text(),
            interfaceName: $("#interface_name").val(),
            methodName: $("#method_name").val(),
            version: $("#timeOut").val(),
            requestParamTypeArgs: arr
        }),
        dataType: "json",
        success: function (data) {
            alert(data)
        }
    });


    function tojson(arr){
        if(!arr.length) return null;
        var i = 0;
        len = arr.length,
            array = [];
        for(;i<len;i++){
            array.push({"paramType":arr[i][0],"paramValue":arr[i][1]});
        }
        return JSON.stringify(array);
    }

};