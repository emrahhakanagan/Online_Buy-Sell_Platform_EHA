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

        <div id="images-container">
            <#list images as image>
                <div class="image-block" id="image-${image.id}">
                    <img src="/images/${image.id}" alt="Image ${image.id}" class="img-thumbnail" width="150px" height="150px">
                    <input type="file" name="files" id="file${image?index + 2}" class="form-control-file mt-2">
                    <button type="button" class="btn btn-danger mt-2 delete-button" data-image-id="${image.id}">
                        Delete
                    </button>
                </div>
            </#list>
        </div>

        <#if product.images?size < imagesLimit>
            <#list 1..(imagesLimit - product.images?size) as i>
                <div class="form-group mt-2">
                    <input type="file" name="files" id="file${product.images?size + i}" class="form-control-file">
                </div>
            </#list>
        </#if>

        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button type="submit" style="width: 100%" class="btn btn-dark mt-3">Update</button>
    </form>
    <a href="/my/products" class="btn btn-secondary mt-3">Back</a>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        $(document).ready(function() {
            $('.delete-button').on('click', function() {
                var imageId = $(this).data('image-id');
                if (confirm('Are you sure you want to delete this image?')) {
                    $.ajax({
                        url: '/delete-image/' + imageId,
                        type: 'DELETE',
                        headers: {
                            'X-CSRF-TOKEN': '${_csrf.token}'
                        },
                        success: function(result) {
                            $('#image-' + imageId).remove();
                            alert('Image deleted successfully.');
                        },
                        error: function(xhr, status, error) {
                            console.error('Error during fetch:', error);
                            alert('Failed to delete image.');
                        }
                    });
                }
            });
        });
    </script>
</@t.template>
