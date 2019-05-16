<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath }"/>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>COIP: IAUCCD - 找回密码页面</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4,
    initial-scale=0.8,target-densitydpi=low-dpi"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <%--网页图标--%>
    <link rel="shortcut icon" href="static/images/COIPIB.png" type="image/x-icon">
    <!--全局样式表-->
    <link href="./static/css/global.css" rel="stylesheet"/>
    <!--Layui-->
    <link href="static/plug/layui/css/layui.css" rel="stylesheet"/>
    <%--<link rel="shortcut icon" href="./static/images/Logo_40.png" type="image/x-icon">--%>
    <link rel="stylesheet" href="./static/css/login/font.css">
    <link rel="stylesheet" href="./static/css/login/xadmin.css">
</head>

<body class="login-bg">
<div class="login layui-anim layui-anim-up">
    <div class="message">COIP: IAUCCD - 找回密码</div>
    <div id="darkbannerwrap"></div>
    <form class="layui-form" action="" method="post">
        <input type="text" name="email" id="email" placeholder="请输入注册邮箱"
               autocomplete="off" class="layui-input">
        <hr class="hr15">
        <div class="layui-form-item">
            <div class="layui-input-inline">
                <input type="text" name="codeCaptcha" id="codeCaptcha" placeholder="请输入验证码"
                       autocomplete="off" class="layui-input">
            </div>
            <label class="field-wrap" style="cursor:pointer;">
                <img src="" id="captchaImg" align='absmiddle' height="40px"
                     style="margin-top: 5px" onclick="changeCaptcha()">
            </label>
            <span id="code_span" style="color: green"></span>
        </div>
        <hr class="hr15">
        <button style="width: 100%;" id="find" class="layui-btn layui-btn-radius" lay-filter="submit" lay-submit="">找回密码</button>
    </form>
</div>
<!-- layui.js -->
<script src="./static/plug/layui/layui.js"></script>
<script src='./static/js/jquery/jquery.min.js'></script>
<script>
    $(function () {
        changeCaptcha();
    });
    // 更换验证码
    function changeCaptcha() {
        $.get('${ctx}' + '/reglogin/codeCaptcha', function (data) {
            $("#captchaImg").attr('src', 'data:image/jpeg;base64,' + data.data.image);
        });
    }
    layui.use(['form', 'layer'], function() {
        var form = layui.form;
        var layer = layui.layer;
        var $ = layui.jquery;

        function checkModifyInfo(email, codeCaptcha) {
            if (email.trim() === "" || email.trim() == null) return "请输入注册邮箱！";
            if (codeCaptcha === "" || codeCaptcha == null) return "请输入确认密码！";
            return "";
        }

        //监听提交
        form.on('submit(submit)', function(){
            var email = $("#email").val();
            var codeCaptcha = $("#codeCaptcha").val();
            var hint = checkModifyInfo(email, codeCaptcha);
            if (hint !== "") {
                layer.msg(hint, {icon:2});
                return false;
            }

            $.ajax({
                type: 'post',
                url: '${ctx}/reglogin/findPassword',
                data: {"email": email, "codeCaptcha": codeCaptcha},
                dataType: 'json',
                success: function (data) {
                    if (data.code !== 200) {
                        layer.msg(data.msg,{icon: 2});
                        return false;
                    } else {
                        layer.msg(data.msg,{icon: 1});
                        document.getElementById("find").disabled=true;
                        setTimeout('document.getElementById("find").disabled=false;', 60000);
                    }
                }
            });
            return false;
        });

    });

</script>
</body>
</html>