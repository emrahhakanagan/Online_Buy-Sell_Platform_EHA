package com.agan.onlinebuysellplatform.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product();
    }

    @Test
    @DisplayName("Should create product with default values")
    public void testCreateProduct_WithDefaultValues() {
        assertNull(product.getId());
        assertNull(product.getTitle());
        assertNull(product.getDescription());
        assertNull(product.getPrice());
        assertTrue(product.getImages().isEmpty());
        assertNull(product.getUser());
        assertNull(product.getPreviewImageId());
        assertNull(product.getDateOfCreated());
        assertTrue(product.getCities().isEmpty());
    }

    @Test
    @DisplayName("Should initialize date of created")
    public void testOnCreate() throws Exception {
        Method onCreateMethod = Product.class.getDeclaredMethod("onCreate");
        onCreateMethod.setAccessible(true);
        onCreateMethod.invoke(product);

        assertNotNull(product.getDateOfCreated());
    }

    @Test
    @DisplayName("Should add image to product")
    public void testAddImageToProduct() {
        Image image = new Image();
        product.addImageToProduct(image);

        assertFalse(product.getImages().isEmpty());
        assertEquals(1, product.getImages().size());
        assertEquals(product, image.getProduct());
    }

    @Test
    @DisplayName("Should set and get title")
    public void testSetAndGetTitle() {
        String title = "Test Title";
        product.setTitle(title);
        assertEquals(title, product.getTitle());
    }

    @Test
    @DisplayName("Should set and get description")
    public void testSetAndGetDescription() {
        String description = "Test Description";
        product.setDescription(description);
        assertEquals(description, product.getDescription());
    }

    @Test
    @DisplayName("Should set and get price")
    public void testSetAndGetPrice() {
        int price = 100;
        product.setPrice(price);
        assertEquals(price, product.getPrice());
    }

    @Test
    @DisplayName("Should set and get preview image ID")
    public void testSetAndGetPreviewImageId() {
        Long previewImageId = 1L;
        product.setPreviewImageId(previewImageId);
        assertEquals(previewImageId, product.getPreviewImageId());
    }

    @Test
    @DisplayName("Should set and get user")
    public void testSetAndGetUser() {
        User user = new User();
        product.setUser(user);
        assertEquals(user, product.getUser());
    }

    @Test
    @DisplayName("Should set and get cities")
    public void testSetAndGetCities() {
        GermanCity city1 = new GermanCity();
        GermanCity city2 = new GermanCity();
        product.getCities().add(city1);
        product.getCities().add(city2);

        assertFalse(product.getCities().isEmpty());
        assertEquals(2, product.getCities().size());
    }
}
