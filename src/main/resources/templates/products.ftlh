<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Search Product</h1><hr>
    <form method="get" action="/">
        <div class="form-row">
            <div class="form-group col-md-2">
                <label for="searchCity">in City</label>
                <select id="searchCity" name="cityId" class="form-control">
                    <option value="">All Cities</option>
                    <#list allCities as perCity>
                    <option value="${perCity.id}">${perCity.city_name}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group col-md-10">
                <label for="searchWord">Search by ads</label>
                <input type="text" name="searchWord" <#if searchWord??>value="${searchWord}"</#if> class="form-control"
                       id="searchWord">
            </div>
            <button style="width: 100%" type="submit" class="btn btn-dark">Search</button>
        </div>
    </form>
    <hr>
        <h4 class="text-left text-dark">${messageSearchProduct}</h4>

        <div class="card-columns">
            <#list products as product>
                <a href="/product/${product.id}" class="product-link">
                    <div class="card text-white bg-dark my-3 product">

                        <img src="/images/${product.previewImageId}"
                             class="card-img-top">
                        <div class="card-body">
                            <h5 class="card-title">${product.title}</h5>
                            <p class="card-text">${product.price} €</p>
                        </div>
                        <div class="card-footer">
                            <#list product.cities as city>
                                ${city.city_name}<#if city_has_next>, </#if>
                            </#list>
                        </div>

                    </div>
                </a>
            <#else>
                <h1 style="text-align: center">Not found</h1>
            </#list>
        </div>\
</@t.template>