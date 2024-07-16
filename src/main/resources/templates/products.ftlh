<#import "blocks/template.ftlh" as t>
<t.template user>
    <h1 style="...">Search Products</h1><hr>
    <form method="get" action="">
        <div class="form-row">
            <div class="form-group col-md-2">
                <label for="searchCity">City</label>
                <select id="searchCity" class="form-control">
                    <option value="">City is not selected</option>
                    <option value="Berlin">Berlin</option>
                    <option value="Dusseldorf">Dusseldorf</option>
                    <option value="Frankfurt">Frankfurt</option>
                </select>
            </div>
            <div class="form-group col-md-10">
                <label for="searchWord">Search Product</label>
                <input type="text" name="searchWord"  <#if searchWord>value="${searchWord}"</#if> class="form-control" id="searchWord">
            </div>
        </div>
        <button style="..." type="submit" class="btn btn-dark">Find</button>
    </form>
    <hr>
    <div class="card-columns">
        <#list products as product>
            <a href="/product/${product.id}" class="product-link">
                <div class="card text-white bg-dark my-3 product">
                    <img src="/images/${product.previewImageId}" class="card-img-top">
                    <div class="card-body">
                        <h5 class="card-title">${product.title}</h5>
                        <p class="card-text">${product.price} ₽</p>
                    </div>
                    <div class="card-footer">
                        ${product.city}
                    </div>
                </div>
            </a>
        </#list>
        <h1 style="...">Not found</h1>
    </div>
</t.template>
