<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Edit Product</h1>
    <hr>

    <#if bindingResult?? && bindingResult.hasErrors()>
        <div style="color: red;">
            <p>Please fix the following errors:</p>
            <ul>
                <#list bindingResult.allErrors as error>
                    <li>${error.defaultMessage}</li>
                </#list>
            </ul>
        </div>
    </#if>

    <form action="/product/edit/${product.id}" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label for="productName">Product Name</label>
            <input type="text" required class="form-control" id="productName" name="title" value="${product.title}">
        </div>

        <div class="form-group">
            <label for="price">Price</label>
            <div class="input-group mb-3">
                <input type="number" required class="form-control" id="price" name="price" value="${product.price}">
                <div class="input-group-append">
                    <span class="input-group-text">€</span>
                </div>
            </div>
        </div>

        <div class="form-group">
            <label for="cities">Cities</label>
            <select id="cities" required name="cityIds" class="form-control" multiple>
                <#list cities as city>
                    <option value="${city.id}" <#if product.cities?seq_contains(city)>selected</#if>>
                        ${city.city_name}
                    </option>
                </#list>
            </select>
        </div>

        <div class="form-group">
            <label for="description">Description</label>
            <textarea required class="form-control" id="description" name="description">${product.description}</textarea>
        </div>

        <#if images?size == 1>

                <div>
                    <img src="/images/${product.previewImageId}" alt="Image ${product.previewImageId}" width="150px" height="150px">
                    <input type="file" name="file1" id="file1">
                </div>

            <#list 2..3 as i>
                <div>
                    <input type="file" name="file${i}" id="file${i}">
                </div>
            </#list>
         <#else>
            <#list images as image>
                <div>
                    <img src="/images/${image.id}" alt="Image ${image.id}" width="150px" height="150px">
                    <input type="file" name="file${image?index + 1}" id="file${image?index + 1}">
                </div>
            </#list>
             <#list 1..(3 - images?size) as i>
                 <div>
                     <input type="file" name="file${images?size + i}" id="file${images?size + i}">
                 </div>
             </#list>
        </#if>


        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button type="submit" style="width: 100%" class="btn btn-dark">Update</button>
    </form>
    <a href="/my/products" class="btn btn-secondary mt-3">Back</a>
</@t.template>
