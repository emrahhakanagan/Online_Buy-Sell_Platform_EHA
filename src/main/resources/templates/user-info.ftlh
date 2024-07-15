<!DOCTYPE html>
<html>
<head>
    <img src="/static/images/logo.jpg" alt="Logo" style="width:100px;height:auto;">
    <title>ONLINE BUY-SELL PLATFORM EHA</title>
</head>
<body>
<H1> BUY-SELL EHA! </H1>
<H3>More detailed information about user ${user.name}</H3>
<b>Phone Number: </b> ${user.phoneNumber}<br>
<b>Email: </b> ${user.email}<br>
<h4>Products of User ${user.name}</h4>
<#list products as product>
    <div>
        <p><b>${product.title}</b> ${product.price} EUR | <a href="/product/${product.id}">Details...</a></p>
    </div>
<#else>
    <H3>There are no products available at the moment.</H3>
</#list>
<#if errorMessage??>
    <H2 style="color: red">@{errorMessage}</H2>
</#if>
</body>
</html>