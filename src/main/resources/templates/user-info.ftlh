<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="font-size: 2rem;">Information about User</h1>
    <hr>
    <div>
        <img src="/static/images/avatar.png" style="height: 120px" alt="User Avatar">
        <br>
        <br>
        <h2>${user.name}</h2>
        <br>
        Phone Number: <b>${user.phoneNumber}</b>
    </div>
    <br>
    <hr>
    <h1>Products of User ${user.name}</h1>
    <br>
    <div class="card-columns">
        <#list products as product>
            <a href="/product/${product.id}" class="product-link">
                <div class="card text-white bg-dark my-3 product">
                    <img src="/images/${product.previewImageId}" class="card-img-top" alt="Product Image">
                    <div class="card-body">
                        <h5 class="card-title">${product.title}</h5>
                        <p class="card-text">${product.price} ₽</p>
                    </div>
                    <div class="card-footer">
                        ${product.user.name}
                    </div>
                </div>
            </a>
        <#else>
        <h1>Not Found</h1>
        </#list>
    </div>
</@t.template>
