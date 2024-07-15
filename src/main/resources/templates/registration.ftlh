<!DOCTYPE html>
<html>
<head>
    <img src="/static/images/logo.jpg" alt="Logo" style="width:100px;height:auto;">
    <title>ONLINE BUY-SELL PLATFORM EHA</title>
</head>
<body>
<H1> BUY-SELL EHA! </H1>
<h4>Registration</h4>
<form action="/registration" method="post">
    Username: <input type="text" name="name"><br><br>
    Email: <input type="email" name="email"><br><br>
    Phone Number: <input type="text" name="phoneNumber"><br><br>
    Password: <input type="password" name="password"><br><br>
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <input type="submit" value="Sign up">
</form>
<#if errorMessage??>
    <H2 style="color: red">@{errorMessage}</H2>
</#if>
</body>
</html>
