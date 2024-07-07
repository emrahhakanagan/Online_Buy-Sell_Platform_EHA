<!DOCTYPE html>
<html>
<head>
    <img src="/images/logo.jpg" alt="Logo" style="width:100px;height:auto;">
    <title>ONLINE BUY-SELL PLATFORM EHA</title>
</head>
<body>
<H1> BUY-SELL EHA! </H1><hr>
<H4>Detailed Information about the Product</H4>
<#if images?has_content>
    <#list images as img>
        <img src="/images/${img.id}" height="60px"/><br><br>
    </#list>
</#if>
<b>Name product:</b> ${product.title}<br>
<b>Description:</b> ${product.description}<br>
<b>Price:</b> ${product.price}<br>
<b>City:</b> ${product.city}<br>
<b>Author:</b> ${product.author}<br>
<hr>
<H3>Delete Product</H3>
<form action="/product/delete/${product.id}" method="post">
    <input type="submit" value="Delete Product">
</form>
</body>
</html>
