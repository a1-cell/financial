<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"/>

    <title>Title</title>
        <script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js"></script>
        <script>
        $(function (){
            $("#send").click(function () {
                var phone = $("[name='phone']").val();
                var reg_phone = /^1\d{10}$/;
                if(reg_phone.test(phone)){
                    $.ajax({
                        "type":"get",
                        "url":"sendSms?phone="+phone,
                        "dataType":"json",
                        "success":function (res) {
                            if(res){
                                alert("短信发送成功！")
                            }else{
                                alert("服务器繁忙")
                            }
                        }
                    })
                }else{
                    alert("手机号码格式有误")
                }
            });
        });
    </script>
</head>
<body>
<h2>百信银行存管系统</h2>
<h3>个人绑卡注册</h3>
<form method="post" action="createUser">
    姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：<input type="text" name="realname" value="郑祎"><br/>
    身份证号：<input type="text" name="idcard" value="110104199109062510"><br/>
    银行卡号：<input type="text" name="bankCard" value="6226220122825655"><br/>
    手&nbsp;机&nbsp;号&nbsp;：<input type="text" name="phone" value="13901379648"><br/>
    验&nbsp;证&nbsp;码&nbsp;：<input type="text" name="randomCode" style="width: 60px"><input id="send" type="button" value="发送验证码"><br/>

    交易密码：<input type="text" value=""><br/>
    <input type="hidden" name="platformNo" value="${platformNo}">
    <input type="hidden" name="data" value="${data}">
    <input type="submit" value="开户">
</form>

</body>
</html>