<!DOCTYPE html>
<html>
<head>
    <title>ONLINE BUY-SELL PLATFORM EHA</title>
</head>
<body>
<H1> BUY-SELL EHA! </H1><hr>
<H4> Products from all over Germany</H4>
<#list products as product>
    <div>
        <p><b>${product.title}</b> ${product.price} EUR | <a href="/product/${product.id}">Details...</a></p>
    </div>
</#list>
<hr>
<H3>Create new product</H3>
<form action="/product/create" method="post">
    Name product: <input type="text" name="titlr"/><br><br>
    Description: <input type="text" name="description"/><br><br>
    Price: <input type="number" name="titlr"/><br><br>
    City: <input type="text" name="city"/><br><br>
    Author name: <input type="text" name="author"/><br><br>
    <input type="submit" value="Add Product">
</form>
</body>
</html>