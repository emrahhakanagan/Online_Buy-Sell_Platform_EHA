package com.agan.onlinebuysellplatform.controller;

import com.agan.onlinebuysellplatform.model.Product;
import com.agan.onlinebuysellplatform.model.User;
import com.agan.onlinebuysellplatform.service.GermanCityService;
import com.agan.onlinebuysellplatform.service.ProductService;
import com.agan.onlinebuysellplatform.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private GermanCityService germanCityService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ProductController productController;

    private Principal principal;
    private Model model;

    @BeforeEach
    void setup() {
        principal = mock(Principal.class);
        model = new ExtendedModelMap();
    }

    @Test
    @DisplayName("Should return products view with search results")
    public void testProducts_WithSearchResults() {
        String keyword = "test";
        Long cityId = 1L;
        List<Product> products = Arrays.asList(new Product(), new Product());
        String messageSearchProduct = "Found 2 products";

        when(productService.searchProduct(cityId, keyword)).thenReturn(products);
        when(productService.showMessageSearchProduct(cityId, keyword, products)).thenReturn(messageSearchProduct);
        when(productService.getUserByPrincipal(principal)).thenReturn(new User());
        when(germanCityService.getAllCities()).thenReturn(Collections.emptyList());

        String viewName = productController.products(keyword, cityId, principal, model);

        assertEquals("products", viewName);
        assertEquals(products, model.getAttribute("products"));
        assertNotNull(model.getAttribute("user"));
        assertEquals(keyword, model.getAttribute("searchWord"));
        assertEquals(Collections.emptyList(), model.getAttribute("allCities"));
        assertEquals(messageSearchProduct, model.getAttribute("messageSearchProduct"));
    }

    @Test
    @DisplayName("Should return product info view with product details")
    public void testProductInfo_WithProductDetails() {
        Long productId = 1L;
        Product product = new Product();
        product.setUser(new User());

        when(productService.getProductById(productId)).thenReturn(product);
        when(productService.getUserByPrincipal(principal)).thenReturn(new User());

        String viewName = productController.productInfo(productId, model, principal);

        assertEquals("product-info", viewName);
        assertEquals(product, model.getAttribute("product"));
        assertNotNull(model.getAttribute("user"));
        assertEquals(product.getImages(), model.getAttribute("images"));
        assertEquals(product.getUser(), model.getAttribute("authorProduct"));
    }

    @Test
    @DisplayName("Should create product and redirect to my products")
    public void testCreateProduct_AndRedirectToMyProducts() {
        MockMultipartFile file1 = new MockMultipartFile("file1", new byte[]{});
        MockMultipartFile file2 = new MockMultipartFile("file2", new byte[]{});
        MockMultipartFile file3 = new MockMultipartFile("file3", new byte[]{});
        Product product = new Product();
        List<Long> cityIds = Arrays.asList(1L, 2L, 3L);
        BindingResult bindingResult = mock(BindingResult.class);
        Model model = new ExtendedModelMap();

        String viewName = productController.createProduct(product, bindingResult, principal, model, file1, file2, file3, cityIds);

        verify(productService, times(1)).saveProduct(principal, product, cityIds, file1, file2, file3);
        assertEquals("redirect:/my/products", viewName);
    }

    @Test
    @DisplayName("Should delete product and redirect to my products when the product belongs to the user")
    public void testDeleteProduct_AndRedirectToMyProducts() {
        Long productId = 1L;

        Product mockProduct = new Product();
        mockProduct.setId(productId);
        User mockUser = new User();
        mockUser.setId(1L);
        mockProduct.setUser(mockUser);

        String viewName = productController.deleteProduct(productId, principal);

        verify(productService, times(1)).deleteProduct(productId, principal);

        assertEquals("redirect:/my/products", viewName);
    }

    @Test
    @DisplayName("Should return my products view with user's products")
    public void testUserProducts_WithUserProducts() {
        User user = new User();
        user.setProducts(Arrays.asList(new Product(), new Product()));

        when(productService.getUserByPrincipal(principal)).thenReturn(user);
        when(germanCityService.getAllCities()).thenReturn(Collections.emptyList());

        String viewName = productController.userProducts(principal, model);

        assertEquals("my-products", viewName);
        assertEquals(user, model.getAttribute("user"));
        assertEquals(user.getProducts(), model.getAttribute("products"));
        assertEquals(Collections.emptyList(), model.getAttribute("germanCities"));
    }
}
