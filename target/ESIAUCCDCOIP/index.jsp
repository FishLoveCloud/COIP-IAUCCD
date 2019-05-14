<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; Charset=gb2312">
    <meta http-equiv="Content-Language" content="zh-CN">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no"/>
    <title>COIP: IAUCCD</title>
    <link rel="shortcut icon" href="" type="image/x-icon">
    <!--Layui-->
    <link href="./static/plug/layui/css/layui.css" rel="stylesheet"/>
    <!--font-awesome-->
    <link href="./static/plug/font-awesome/css/font-awesome.min.css" rel="stylesheet"/>
    <!--全局样式表-->
    <link href="./static/css/global.css" rel="stylesheet"/>
    <!-- 本页样式表 -->
    <link href="./static/css/index.css" rel="stylesheet" type="text/css"/>
    <style>
        th {
            font-size: 18px !important;
            text-align: center !important;
        }

        td {
            font-size: 17px !important;
        }

        span {
            color: red;
        }
        .input-result-right{
            font-size: 17px;
        }
    </style>
</head>
<body class="layui-layout-body">

<div class="layui-layout layui-layout-admin">
    <div class="layui-header">
        <a href="${ctx}/index.jsp">
            <div class="layui-logo" style="font-weight: bold">COIP: IAUCCD</div>
        </a>
        <!-- 头部区域（可配合layui已有的水平导航） -->
        <ul class="layui-nav layui-layout-left">
            <li class="layui-nav-item">
                <a href="javascript:;">文件</a>
            </li>
            <li class="layui-nav-item">
                <a href="javascript:;">编辑</a>
            </li>
            <li class="layui-nav-item">
                <a href="javascript:;">管理员</a>
            </li>
            <li class="layui-nav-item">
                <a href="javascript:;">帮助</a>
            </li>
        </ul>
        <!-- 注册 -->
        <ul class="layui-nav layui-layout-right head-nav-right" lay-filter="nav">
            <li class="layui-nav-item">
                <a href="javascript:;">登录&nbsp;/&nbsp;注册</a>
            </li>
        </ul>
        <!-- 手机和平板的导航开关 -->
        <a class="index-navicon" href="javascript:;" style="display: none;">
            <i class="fa fa-navicon"></i>
        </a>
    </div>
    <!-- 主体（一般只改变这里的内容） -->
    <div class="layui-body" style="margin-left: -180px;">
        <!-- 这个一般才是真正的主体内容 -->
        <div class="index-main">
            <!--页面左边-->
            <div class="index-main-left">
                <div>
                    <form class="layui-form" action="">
                        <fieldset class="layui-elem-field layui-field-title" style="margin-top: 30px;">
                            <legend>数据录入</legend>
                        </fieldset>

                        <div class="layui-form">
                            <table class="layui-table">
                                <colgroup>
                                    <col width="25%">
                                    <col width="30%">
                                    <col width="32%">
                                    <col width="13%">
                                </colgroup>
                                <thead>
                                <tr>
                                    <th></th>
                                    <th>指标</th>
                                    <th>指标数值</th>
                                    <th>（单位）</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td rowspan="4">园区产业集聚相关数据</td>
                                    <td>园区就业总人数<span>*</span></td>
                                    <td>
                                        <input type="text" id="total-employment" name="total-employment"
                                               placeholder="请输入..."
                                               lay-verify="required|number" autocomplete="off" class="layui-input">
                                    </td>
                                    <td>个</td>
                                </tr>
                                <tr>
                                    <td>实际实现营业收入<span>*</span></td>
                                    <td>
                                        <input type="text" id="actual-operating-income" name="actual-operating-income"
                                               placeholder="请输入..."
                                               lay-verify="required|number" autocomplete="off" class="layui-input">
                                    </td>
                                    <td>亿美元</td>
                                </tr>
                                <tr>
                                    <td>实际入园企业总数<span>*</span></td>
                                    <td>
                                        <input type="text" id="actual-total-enterprises" name="actual-total-enterprises"
                                               placeholder="请输入..."
                                               lay-verify="required|number" autocomplete="off" class="layui-input">
                                    </td>
                                    <td>个</td>
                                </tr>
                                <tr>
                                    <td>园区已开发工业用地面积<span>*</span></td>
                                    <td>
                                        <input type="text" id="site-area" name="site-area" placeholder="请输入..."
                                               lay-verify="required|number" autocomplete="off" class="layui-input">
                                    </td>
                                    <td>公顷</td>
                                </tr>
                                </tbody>
                            </table>

                            <table class="layui-table">
                                <colgroup>
                                    <col width="25%">
                                    <col width="30%">
                                    <col width="32%">
                                    <col width="13%">
                                </colgroup>
                                <tbody>
                                <tr>
                                    <td rowspan="5">城镇发展相关数据</td>
                                    <td>城镇人口数<span>*</span></td>
                                    <td>
                                        <input type="text" id="urban-population" name="urban-population"
                                               placeholder="请输入..."
                                               lay-verify="required|number" autocomplete="off" class="layui-input">
                                    </td>
                                    <td>人</td>
                                </tr>
                                <tr>
                                    <td>人口城镇化率<span>*</span></td>
                                    <td>
                                        <input type="text" id="urbanization-rate" name="urbanization-rate"
                                               placeholder="请输入..."
                                               lay-verify="required|number" autocomplete="off" class="layui-input">
                                    </td>
                                    <td>%</td>
                                </tr>
                                <tr>
                                    <td>城镇工业部门从业人数<span>*</span></td>
                                    <td>
                                        <input type="text" id="industrial-employment" name="industrial-employment"
                                               placeholder="请输入..."
                                               lay-verify="required|number" autocomplete="off" class="layui-input">
                                    </td>
                                    <td>人</td>
                                </tr>
                                <tr>
                                    <td>二三产业增加值占GDP比重<span>*</span></td>
                                    <td>
                                        <input type="text" id="gdp-proportion" name="gdp-proportion"
                                               placeholder="请输入..."
                                               lay-verify="required|number" autocomplete="off" class="layui-input">
                                    </td>
                                    <td>%</td>
                                </tr>
                                <tr>
                                    <td>人均GDP<span>*</span></td>
                                    <td>
                                        <input type="text" id="gdp-per-capita" name="gdp-per-capita"
                                               placeholder="请输入..."
                                               lay-verify="required|number" autocomplete="off" class="layui-input">
                                    </td>
                                    <td>美元/人/年</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </form>
                </div>
                <div>
                    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 30px;">
                        <legend>文件输入</legend>
                    </fieldset>
                    <div style="float: right;margin-bottom: 20px;">
                        <div class="layui-inline">
                            <button class="layui-btn">下载文件输入模板</button>
                        </div>
                        <div class="layui-inline">
                            <button class="layui-btn">上传输入的文件</button>
                        </div>
                    </div>
                </div>
            </div>

            <!--页面右边-->
            <div class="index-main-right">
                <div style="margin: 100px 0 80px 0;">
                    <div class="layui-form-item">
                        <div class="layui-input-block submit-btn">
                            <button class="layui-btn" lay-submit="" lay-filter="" style="margin-right: 30px;">
                                运行计算
                            </button>
                            <button type="reset" class="layui-btn">重置</button>
                        </div>
                    </div>
                </div>
                <hr>
                <div class="input-result-right">
                    <fieldset class="layui-elem-field layui-field-title">
                        <legend>输出结果</legend>
                    </fieldset>
                    <div class="layui-form-item">
                        <label class="layui-form-label" style="width: 50%;text-align: left">指标输出：</label>
                        <div class="layui-input-block">xxxxxx</div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label" style="width: 50%;text-align: left">该中国境外产业园区产业聚集与城镇化的耦合协调度为：</label>
                        <div class="layui-input-block">xxxxxx</div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label" style="width: 50%;text-align: left">耦合协调等级为：</label>
                        <div class="layui-input-block">xxxxxx</div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label" style="width: 50%;text-align: left">文件输出：</label>
                        <div class="layui-input-block"><button class="layui-btn">下载输出文件</button></div>
                    </div>
                </div>
            </div>

            <div class="clear"></div>
        </div>
    </div>
    <!-- 底部固定区域 -->
    <%--<div class="layui-footer" style="margin-left: -200px;">

    </div>--%>
    <!--分享窗体-->
    <div class="index-share layui-hide">
        <div class="index-share-body">
            <div style="width: 200px;height:100%;">
                <div class="bdsharebuttonbox">
                    <a class="bds_qzone" data-cmd="qzone" title="分享到QQ空间"></a>
                    <a class="bds_tsina" data-cmd="tsina" title="分享到新浪微博"></a>
                    <a class="bds_weixin" data-cmd="weixin" title="分享到微信"></a>
                    <a class="bds_sqq" data-cmd="sqq" title="分享到QQ好友"></a>
                </div>
            </div>
        </div>
    </div>
    <!--遮罩-->
    <div class="blog-mask animated layui-hide"></div>
    <!-- layui.js -->
    <script src="./static/plug/layui/layui.js"></script>
</div>
</body>
</html>