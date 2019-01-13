$(document).ready(function () {
        // $("#address").change(function () {
        //     var addressValue = $("#address").find("option:selected").text();
        //     if (addressValue == "=请选择===") {
        //         $.ajax({
        //             type: "POST",
        //             url: "/getDubboServices",
        //             data: {address: addressValue},
        //             dataType: "json",
        //             success: function (data) {
        //                 console.log(data);
        //             }
        //         });
        //     };
        // });
        $("#auto_generate_jmx_script").click(function () {
            $("#jmx_file_name").text("");
           $.ajax({
               type:"POST",
               url:"/generateJmxFile",
               data:{
                   jmeterVersion:$("#jmeter_version").find("option:selected").text(),
                   preTime: $("#preTime").val(),
                   preNumber: $("#preNumber").val(),
                   preName: $("#preName").val(),
                   zkAddress: $("#address").find("option:selected").text(),
                   dubboInterfaceName:$("#interfaceName").val(),
                   methodName: $("#methodName").val(),
                   requestBeanRenfence: $("#requestBeanName").val(),
                   param: $("#param").val()
               },
               dataType:"json",
               success:function (data) {
                   console.log(data);
                   if (data.code == 0) {
                       $("#jmx_file_name").text(data.data.fileName);
                   }else {
                       alert(data.msg);
                   }
               }
           })
        });
    $("#addTable").click(function(){
        var tr="<tr><td><input type=\"checkbox\" name=\"check\"/>"+
            "</td><td>参数类型:<select class='param_type'><option value='java.lang.String'>java.lang.String</option><option value='java.lang.Integter'>java.lang.Integter</option><option value='java.lang.Double'>java.lang.Double</option><option value='java.lang.Float'>java.lang.Float</option><option value='java.lang.Boolean'>java.lang.Boolean</option><option value='java.lang.Character'>java.lang.Character</option><option value='java.lang.Short'>java.lang.Short</option><option value='java.lang.Byte'>java.lang.Byte</option><option value='java.lang.Long'>java.lang.Long</option></select> " +
            "</td><td>参数名称:<input class='param_name' type='text' />" +
            "</td><td>参数值:<input class='param_value' type='text' /></td></tr>";
        $("#table").append(tr);

    });
    $("#deleteTable").click(function(){
        var check = document.getElementsByName("check");
        for(var i=0;i<check.length;i++){
            if(check[i].checked){
                document.getElementById('table').deleteRow(i);
                i--;
            }
        }
    });
});
