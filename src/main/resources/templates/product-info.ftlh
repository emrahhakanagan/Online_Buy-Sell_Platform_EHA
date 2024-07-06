<!DOCTYPE html>
<html>
<head>
    <title>ONLINE BUY-SELL PLATFORM EHA</title>
</head>
<body>
<H1> BUY-SELL EHA! </H1><hr>
<H4>Detailed Information about the Product</H4>
<b>Name product:</b>${product.title}<br>
<b>Description:</b>${product.description}<br>
<b>Price:</b>${product.price}<br>
<b>City:</b>${product.city}<br>
<b>Author:</b>${product.author}<br>
<hr>
<H3>Delete Product</H3>
<form action="/product/delete/${product.id}" method="post">
        <input type="submit" value="Delete Product">
</form>
</body>
</html>