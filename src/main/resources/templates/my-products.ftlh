<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">My Products</h1>
    <hr>

    <a class="btn btn-dark mt-3 mb-3" style="text-align: center; width: 100%"
       data-toggle="collapse" href="#collapseExample" role="button"
       aria-expanded="true" aria-controls="collapseExample">
        Add New Products
    </a>

    <div class="collapse mb-3" id="collapseExample" style="">
        <form action="/product/create" method="post" enctype="multipart/form-data">
            <div class="form-group">
                <label for="productName">Product Name</label>
                <input type="text" required class="form-control"
                       id="productName" name="title" title="Please enter the product name">
            </div>
            <div class="form-group">
                <label for="price">Price</label>
                <div class="input-group mb-3">
                    <input type="number" required class="form-control" id="price" name="price"
                           title="Please enter a valid price">
                    <div class="input-group-append">
                        <span class="input-group-text">€</span>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="cities">Cities</label>
                <select id="cities" required name="cityIds" class="form-control" multiple
                        title="Please select at least one city">
                    <option value="">The city is not selected</option>
                    <#list germanCities as city>
                        <option value="${city.id}">${city.city_name}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group">
                <label for="description">Description</label>
                <textarea required class="form-control" id="description" name="description"
                          title="Please enter a description"></textarea>
            </div>
            <div class="form-group">
                <div class="custom-file">
                    <input type="file" name="file1" id="file1">
                    <label class="custom-file-label" for="file1">1st picture</label>
                </div>
            </div>
            <div class="form-group">
                <div class="custom-file">
                    <input type="file" name="file2" id="file2">
                    <label class="custom-file-label" for="file2">2nd picture</label>
                </div>
            </div>
            <div class="form-group">
                <div class="custom-file">
                    <input type="file" name="file3" id="file3">
                    <label class="custom-file-label" for="file3">3rd picture</label>
                </div>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>

            <button type="submit" style="width: 100%" class="btn btn-dark">Add</button>
        </form>
    </div>



    <div class="card-columns">
        <#list products as product>
            <a href="/product/${product.id}" class="product-link">
                <div class="card text-white bg-dark my-3 product">
                    <div style="display: flex; justify-content: space-between;">
                        <form action="/product/edit/${product.id}" method="get" style="display: inline-block;">
                            <button type="submit" class="btn btn-warning m-3">
                                Edit
                            </button>
                        </form>
                        <form action="/product/delete/${product.id}" method="post">
                            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                            <button type="submit" class="btn btn-danger m-3">
                                Delete
                            </button>
                        </form>
                    </div>
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
    </div>


</@t.template>