var user=document.getElementById('user');
var pas=document.getElementById('pas');
var btn=document.getElementById('btn');
btn.onclick=function(){
    var u=user.value;
    var p=pas.value;
    $("span:eq(0)").html("").show();
    $("span:eq(1)").html("").show();
    if(u == null || u.trim() == ""){
        $("span:eq(0)").html("用户名不能为空").show();
        user.focus();
    }
    if(p == null || p.trim() == ""){
        $("span:eq(0)").html("密码不能为空").show();
        user.focus();
    }

    $.ajax({
        "url":"/doLogin",
        "data":{
            "username":u,
            "passwd":p,
            "type":$("input[name='管理员类型']:checked").val()
        },
        "type":"POST",
        "dataType":"text",
        "success":function (result) {
            if (result == "输入正确") {
                alert(result);
                location.assign("/index");
            }else{
                alert(result);
                location.assign("/page/error");
            }
        },
        "error":function(e){
            console.log(e);
            //throw e;
        }
    })

}