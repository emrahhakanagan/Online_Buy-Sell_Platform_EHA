package com.agan.onlinebuysellplatform.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    private Product product;
    private User user;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        product = new Product();
        user = new User();

        product.setUser(user);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should create product with default values")
    public void testCreateProduct_WithDefaultValues() {
        assertNull(product.getId());
        assertNull(product.getTitle());
        assertNull(product.getDescription());
        assertNull(product.getPrice());
        assertTrue(product.getImages().isEmpty());
        assertNotNull(product.getUser());
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

    @Test
    @DisplayName("Should set and get user with correct association")
    public void testSetAndGetUser_Association() {
        assertEquals(user, product.getUser());

        assertTrue(user.getProducts().isEmpty(), "User should not automatically have the product in products list");

        user.getProducts().add(product);
        assertTrue(user.getProducts().contains(product), "User should contain the product after adding it explicitly");
    }

    @Test
    @DisplayName("Should fail validation when title is blank")
    public void testBlankTitle() {
        product.setTitle("");
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Title cannot be blank")));
    }

    @Test
    @DisplayName("Should fail validation when description is too short")
    public void testShortDescription() {
        product.setDescription("Too short");
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Description must be between 10 and 500 characters")));
    }

    @Test
    @DisplayName("Should fail validation when price is negative")
    public void testNegativePrice() {
        product.setPrice(-100);
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Price must be a positive number")));
    }
}
