package com.agan.onlinebuysellplatform.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GermanCityTest {

    private GermanCity germanCity;

    @BeforeEach
    public void setUp() {
        germanCity = new GermanCity();
    }

    @Test
    @DisplayName("Should create GermanCity with default values")
    public void testCreateGermanCity_WithDefaultValues() {
        assertNull(germanCity.getId());
        assertNull(germanCity.getCity_name());
        assertNotNull(germanCity.getProducts());
        assertTrue(germanCity.getProducts().isEmpty());
    }

    @Test
    @DisplayName("Should set and get city name")
    public void testSetAndGetCityName() {
        String cityName = "Berlin";
        germanCity.setCity_name(cityName);
        assertEquals(cityName, germanCity.getCity_name());
    }

    @Test
    @DisplayName("Should set and get products")
    public void testSetAndGetProducts() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        Product product2 = new Product();
        products.add(product1);
        products.add(product2);

        germanCity.setProducts(products);
        assertEquals(products, germanCity.getProducts());
    }

    @Test
    @DisplayName("Should add product to GermanCity")
    public void testAddProductToGermanCity() {
        Product product = new Product();
        germanCity.getProducts().add(product);

        assertEquals(1, germanCity.getProducts().size());
        assertTrue(germanCity.getProducts().contains(product));
    }
}
