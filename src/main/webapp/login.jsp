<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <link rel="stylesheet" type="text/css" href="css/common.css">
    <link rel="stylesheet" type="text/css" href="css/login.css">
    <script type="javascript" src="js/jquery.js"></script>
    <script type="javascript" src="js/login.js"></script>
</head>
<body>
<div class="header-simple">
    <div class="w">
        <a class="logo" href="index.html">MMall</a>
    </div>
</div>
<div class="user-wrap">
    <div class="user-container w">
        <div class="user-title">用户登录</div>
        <div class="user-box">
            <form action="user/login.do" method="post">
            <div class="user-item">
                <img src="images/user.png" style="display: inline-block;float: left;margin-right: 10px">
                <input type="text" class="user-context" id="username" placeholder="用户名" autocomplete="off">
            </div>
            <div class="user-error">
                <i class="err-icon"></i>
                <p class="error-msg">请输入用户名!</p>
            </div>
            <div class="user-item">
                <!--<label class="user-label" for="password">-->
                <!--<i class="fa fa-lock"></i>-->
                <!--</label>-->
                <img src="images/pass.png" style="display: inline-block;float: left;margin-right: 10px">
                <input type="password" class="user-context" id="password" placeholder="密码" autocomplete="off">
            </div>
            <div class="pass-error">
                <i class="err-icon"></i>
                <p class="error-msg">请输入密码!</p>
            </div>
            <input type="submit" class="login-button" id="submit" >登录</input>
            <div class="link-item" >
                <a class="link register" href="register.jsp">免费注册</a>
                <a class="link pass-forget" href="pass-reset.jsp">忘记密码</a>
            </div>
            </form>
        </div>
    </div>
</div>
<div class="footer">
    <div class="w">
        <div class="link">
            <a href=""></a>
        </div>
    </div>
    <div class="copyright"> Copyright © 2017 happymmall.com All Rights Reserved </div>
</div>
</body>
</html>
