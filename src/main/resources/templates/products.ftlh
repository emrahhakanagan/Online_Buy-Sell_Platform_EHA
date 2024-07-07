<!DOCTYPE html>
<html>
<head>
    <img src="/images/logo.jpg" alt="Logo" style="width:100px;height:auto;">
    <title>ONLINE BUY-SELL PLATFORM EHA</title>
</head>
<body>
<H1> BUY-SELL EHA! </H1>
<hr>
<H4> Products from all over Germany</H4>
<form action="/" method="get">
    Search by product name: <input type="text" name="title"><br>
    <input type="submit" value="Search">
</form>
<#list products as product>
    <div>
        <p><b>${product.title}</b> ${product.price} EUR | <a href="/product/${product.id}">Details...</a></p>
    </div>
    <#else>
    <H3>There are no products available at the moment.</H3>
</#list>
<hr>
<H3>Create new product</H3>
<form action="/product/create" method="post">
    Name product: <input type="text" name="title"/><br><br>
    Description: <input type="text" name="description"/><br><br>
    Price: <input type="number" name="price"/><br><br>
    City: <input type="text" name="city"/><br><br>
    Author name: <input type="text" name="author"/><br><br>
    <input type="submit" value="Add Product">
</form>
</body>
</html>