<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath }"/>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>COIP: IAUCCD - 注册页面</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <%--网页图标--%>
    <link rel="shortcut icon" href="static/images/COIPIB.png" type="image/x-icon">
    <!--全局样式表-->
    <link href="./static/css/global.css" rel="stylesheet"/>
    <%--<link rel="shortcut icon" href="./static/images/Logo_40.png" type="image/x-icon">--%>
    <link rel="stylesheet" href="./static/css/login/font.css">
    <link rel="stylesheet" href="./static/css/login/xadmin.css">

    <style>
        .rePassword, .rePassword:focus {
            border-color: #f44336 !important;
        }

        .disableEmail, .disableEmail:focus {
            pointer-events: none !important;
            cursor: none !important;
            color: #D2D2D2 !important;
            background-color: #9E9E9E !important;
            opacity: 1 !important;
        }
    </style>
</head>

<body class="login-bg">

<div class="login layui-anim layui-anim-up">
    <div class="message">COIP: IAUCCD - 注册</div>
    <div id="darkbannerwrap"></div>
    <form class="layui-form" action="" method="post">
        <input type="text" name="username" id="username" lay-verify="required|username" placeholder="请输入用户名"
               autocomplete="off" class="layui-input">
        <hr class="hr15">
        <input type="text" name="email" id="email" lay-verify="required|email" placeholder="请输入邮箱"
               autocomplete="off" class="layui-input">
        <hr class="hr15">
        <input type="password" name="password" id="password" lay-verify="required|password" placeholder="请输入密码"
               autocomplete="off" class="layui-input">
        <hr class="hr15">
        <input type="password" name="rePassword" id="rePassword" lay-verify="required|rePassword" placeholder="请确认密码"
               autocomplete="off" class="layui-input">
        <hr class="hr15">
        <div class="layui-form-item">
            <div class="layui-input-inline">
                <input type="text" name="codeCaptcha" id="codeCaptcha" lay-verify="required|codeCaptcha" placeholder="请输入验证码"
                       autocomplete="off" class="layui-input">
            </div>
            <label class="field-wrap" style="cursor:pointer;">
                <img src="" id="captchaImg" align='absmiddle' height="40px"
                     style="margin-top: 5px" onclick="changeCaptcha()">
            </label>
            <span id="code_span" style="color: green"></span>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-inline">
                <input type="text" name="emailCaptcha" id="emailCaptcha" lay-verify="required|emailCaptcha"  placeholder="请输入邮箱验证码"
                       autocomplete="off" class="layui-input">
            </div>
            <label class="field-wrap" style="float:left; margin-top: 6px;">
                <button style="width: 100%;" class="layui-btn layui-btn-radius" id="sendEmail" onclick="return false;">发送邮件</button>
            </label>
            <label style="float:left; margin-top: 6px; display: none;" class="field-wrap layui-btn layui-btn-radius disableEmail" id="disableSendEmail">60s</label>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-inline">
                <p style="text-align: left"><a href="${ctx}/login">已有账号？前往登录</a></p>
            </div>
        </div>
        <button style="width: 100%" class="layui-btn layui-btn-radius" lay-submit="" lay-filter="submit">注册</button>
        <hr class="hr15" >
    </form>
</div>
<!--遮罩-->
<div class="blog-mask animated layui-hide"></div>
<!-- layui.js -->
<script src="./static/plug/layui/layui.js"></script>
<script src='./static/js/jquery/jquery.min.js'></script>
<script>
    $(function () {
        changeCaptcha();
        checkIdentityOfPassword();
        $("#sendEmail").unbind("click").bind("click", function () {
           sendEmail();
        });
    });

    // 检查两次密码输入的一致性
    function checkIdentityOfPassword() {
        $("#rePassword").on("input propertychange", function () {
            console.log(1);
            $("#rePassword").addClass("rePassword");
            var passWord = $("#password").val();
            var rePassWord = $("#rePassword").val();
            if (passWord == rePassWord) {
                $("#rePassword").removeClass("rePassword");
            }
        });
    }

    // 更换验证码
    function changeCaptcha() {
        $.get('${ctx}/reglogin/codeCaptcha', function (data) {
            $("#captchaImg").attr('src', 'data:image/jpeg;base64,' + data.data.image);
        });
    }

    function sendEmail() {
        var email = $("#email").val();
        var codeCaptcha = $("#codeCaptcha").val();
        $.ajax({
            type: 'get',
            url: '${ctx}/reglogin/emailCaptcha',
            data: {"email": email, "codeCaptcha": codeCaptcha},
            dataType: 'json',
            success: function (data) {
                if (data.code !== 200) {
                    layer.msg(data.msg, {icon: 2});
                    changeCaptcha();
                    return false;
                } else {
                    suspendEmailService();
                }
            }
        });
        return false;
    }

    function suspendEmailService() {
        $("#sendEmail").hide();
        $("#disableSendEmail").show();

        var time = 60;
        var p = $("#disableSendEmail")[0];
        var set = setInterval(function () {
            time--;
            p.innerHTML = time + "s";
            if (time === 0) {
                $("#sendEmail").show();
                $("#disableSendEmail").hide();
                clearInterval(set);
            }
        }, 1000);
    };

    layui.use(['form', 'layer'], function(){
        var form = layui.form;
        var layer = layui.layer;
        var $ = layui.jquery;

        function checkRegisterInfo(username, email, password, rePassword, codeCaptcha, emailCaptcha) {
            if (username.trim() == "" || username.trim() == null) return "请输入用户名！";
            if (email.trim() == "" || email.trim() == null) return "请输入邮箱！";
            if (password == "" || password == null) return "请输入密码！";
            if (rePassword == "" || rePassword == null) return "请输入确认密码！";
            if (codeCaptcha == "" || codeCaptcha == null) return "请输入验证码！";
            if (emailCaptcha == "" || emailCaptcha == null) return "请输入邮箱验证码！";
            if (!(password==rePassword)) return "两次输入密码不一致！";
            return "";
        }
        //监听提交
        form.on('submit(submit)', function(){
            var username = $("#username").val();
            var email = $("#email").val();
            var password = $("#password").val();
            var rePassword = $("#rePassword").val();
            var codeCaptcha = $("#codeCaptcha").val();
            var emailCaptcha = $("#emailCaptcha").val();

            var hint = checkRegisterInfo(username, email, password, rePassword, codeCaptcha, emailCaptcha);
            if (hint != "") {
                layer.msg(hint, {icon:2});
                return false;
            }

            $.ajax({
                type: 'post',
                url: '${ctx}/reglogin/register',
                data: {"username": username, "email":email, "password": password, "codeCaptcha": codeCaptcha, "emailCaptcha": emailCaptcha},
                dataType: 'json',
                success: function (data) {
                    if (data.code !== 200) {
                        layer.msg(data.msg,{icon: 2});
                        changeCaptcha();
                        return false;
                    } else {
                        location = "${ctx}/";
                    }

                }
            });
            return false;
        });
    });

</script>
</body>
</html>