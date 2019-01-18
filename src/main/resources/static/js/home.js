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
 */
$(document).on('click', '#delCenterIpGrp', function () {
    var el = this.parentNode.parentNode;
    el.parentNode.removeChild(el);
});

function testInterface () {
    var service_name = $("#interface_name").val();
    var method_name = $("#method_name").val();
    var protocol = $("#registry_protocol").find("option:selected").text();
    var address = $("#registry_address").find("option:selected").text();
    console.log(protocol+"---->"+address);
    if (protocol == "===请选择==="||protocol == null){
        protocol = "zookeeper";
    }
    if (address == "===请选择==="||address == null){
        $.confrim({
            title: '协议地址!',
            content: '是否使用dev的zk地址?',
            confirm: function(){
                address = "10.10.6.3:2181";
            },
            cancel: function(){
                address == "===请选择===";
            }
        });

    }
    if (service_name == null || service_name == ""){
        $("#service_tip").css('display','block');
        $("#interface_name").focus();
    }
    if (method_name == null || method_name == ""){
        $("#method_tip").css('display','block');
        $("#method_name").focus();
    }
    if ($(".request_param_type").length<1){
        $("#param_tip").css('display','block');
    }
    $.ajax({
        url:"",
        data:{
            serviceName:service_name,
            methodName:method_name
        },
        dataType:"json",
        success:function(result){

        }
    })
}
