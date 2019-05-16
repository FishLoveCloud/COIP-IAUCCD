<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath }"/>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>COIP: IAUCCD - 修改密码页面</title>
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
    <div class="message">COIP: IAUCCD - 修改密码</div>
    <div id="darkbannerwrap"></div>
    <form class="layui-form" action="" method="post">
        <input type="password" name="newPassword" id="newPassword" placeholder="请输入新密码"
               autocomplete="off" class="layui-input">
        <hr class="hr15">
        <input type="password" name="confirmPassword" id="confirmPassword" placeholder="请确认密码"
               autocomplete="off" class="layui-input">
        <hr class="hr15">
        <button style="width: 100%;" class="layui-btn layui-btn-radius" lay-filter="submit" lay-submit="">修改</button>
    </form>
</div>
<!-- layui.js -->
<script src="./static/plug/layui/layui.js"></script>
<script src='./static/js/jquery/jquery.min.js'></script>
<script>
    layui.use(['form', 'layer'], function() {
        var form = layui.form;
        var layer = layui.layer;
        var $ = layui.jquery;

        function checkModifyInfo(newPassword, confirmPassword) {
            if (newPassword.trim() === "" || newPassword.trim() == null) return "请输入新密码";
            if (confirmPassword === "" || confirmPassword == null) return "请输入确认密码！";
            return "";
        }


        function getUrlParam(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) {
                return decodeURI(r[2]);
            }
            return null;
        }

        //监听提交
        form.on('submit(submit)', function(){
            var newPassword = $("#newPassword").val();
            var confirmPassword = $("#confirmPassword").val();
            var hint = checkModifyInfo(newPassword, confirmPassword);
            if (hint !== "") {
                layer.msg(hint, {icon:2});
                return false;
            }

            if(newPassword !== confirmPassword){
                layer.msg("输入的密码不一致", {icon: 2});
                return false;
            }

            $.ajax({
                type: 'post',
                url: '${ctx}/reglogin/changePasswordFromFind',
                data: {"ticket":  getUrlParam("ticket"), "newPassword": newPassword},
                dataType: 'json',
                success: function (data) {
                    if (data.code !== 200) {
                        layer.msg(data.msg,{icon: 2});
                        return false;
                    } else {
                        layer.msg("密码修改成功，即将跳转登陆页面！",{icon: 6});
                        setTimeout('window.location.href = "${ctx}/login.jsp"', 3000);
                    }
                }
            });
            return false;
        });

    });

</script>
</body>
</html>