<!DOCTYPE html>
<html>
<head>
    <img src="/images/logo.jpg" alt="Logo" style="width:100px;height:auto;">
    <title>ONLINE BUY-SELL PLATFORM EHA</title>
</head>
<body>
<H1> BUY-SELL EHA! </H1>
<#if user.email??>
    <form action="/logout" method="post" style="display: inline;">
        <input type="submit" value="Logout"/>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
<#else>
    <a href="/login">Sign in</a>
</#if>
<hr>
<H4> Products from all over Germany</H4>
<form action="/" method="get">
    Search by product name: <input type="text" name="title">
    <input type="submit" value="Search">
</form>
<#list products as product>
    <div>
        <p><b>${product.title}</b> ${product.price} EUR | <a href="/product/${product.id}">Details...</a></p>
    </div>
    <#else>
    <H3>There are no products available at the moment.</H3>
</#list>
<#if user.email??>
    <hr>
    <H3>Create new product</H3>
    <form action="/product/create" method="post" enctype="multipart/form-data">
        Name product: <input type="text" name="title"/><br><br>
        Description: <input type="text" name="description"/><br><br>
        Price: <input type="number" name="price"/><br><br>
        City: <input type="text" name="city"/><br><br>
        1st Image: <input type="file" name="file1"/><br><br>
        2nd Image: <input type="file" name="file2"/><br><br>
        3rd Image: <input type="file" name="file3"/><br><br>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <input type="submit" value="Add Product">
    </form>
</#if>
</body>
</html>