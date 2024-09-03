package com.agan.onlinebuysellplatform.repository;

import com.agan.onlinebuysellplatform.config.TestConfig;
import com.agan.onlinebuysellplatform.model.GermanCity;
import com.agan.onlinebuysellplatform.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestConfig.class)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Product product1, product2;
    private GermanCity city;
    private String title;

    @BeforeEach
    public void setup() {
        title = "Test Product";

        product1 = new Product();
        product1.setTitle(title);
        product1.setDescription("This is a test product");
        product1.setPrice(100);
        product1.setCities(new ArrayList<>()); // Создаем изменяемый список
        entityManager.persist(product1);

        product2 = new Product();
        product2.setTitle(title);
        product2.setDescription("Another test product");
        product2.setPrice(150);
        product2.setCities(new ArrayList<>()); // Создаем изменяемый список
        entityManager.persist(product2);

        city = new GermanCity();
        city.setCity_name("Berlin");
        entityManager.persist(city);

        product1.getCities().add(city); // Теперь можно добавить город в список
        entityManager.persist(product1);
        entityManager.flush();
    }


    @Test
    @DisplayName("Should save product successfully")
    public void testSaveProduct() {
        product1.setCities(new ArrayList<>(Collections.singletonList(city))); // Создаем изменяемый список

        Product savedProduct = productRepository.save(product1);
        assertNotNull(savedProduct.getId());
        assertEquals("Test Product", savedProduct.getTitle());
    }

    @Test
    @DisplayName("Should find products by title when products exist")
    public void testFindByTitle_WhenProductsExist() {
        List<Product> foundProducts = productRepository.findByTitle(title);

        assertNotNull(foundProducts);
        assertEquals(2, foundProducts.size());
        assertTrue(foundProducts.stream().allMatch(product -> product.getTitle().equals(title)));
    }

    @Test
    @DisplayName("Should return empty list when no products found by title")
    public void testFindByTitle_WhenNoProductsExist() {
        String title = "Nonexistent Product";

        List<Product> foundProducts = productRepository.findByTitle(title);

        assertNotNull(foundProducts);
        assertTrue(foundProducts.isEmpty());
    }

    @Test
    @DisplayName("Should find products by city ID when products exist")
    public void testSearchProductByCity_WhenProductsExist() {
        List<Product> foundProducts = productRepository.searchProductByCity(city.getId());

        assertFalse(foundProducts.isEmpty(), "Products should be found for the given city ID");
        assertTrue(foundProducts.stream().allMatch(p -> p.getCities().contains(city)),
                "All found products should be associated with the given city");
    }

    @Test
    @DisplayName("Should return empty list when no products are found for the city ID")
    public void testSearchProductByCity_WhenNoProductsExist() {
        GermanCity city = new GermanCity();
        city.setCity_name("Berlin");
        entityManager.persist(city);

        entityManager.flush();

        List<Product> foundProducts = productRepository.searchProductByCity(city.getId());

        assertTrue(foundProducts.isEmpty(), "No products should be found for the given city ID");
    }

    @Test
    @DisplayName("Should find products when keyword matches title")
    public void testSearchProductByKeywordTitle_WhenKeywordMatchesTitle() {
        Product product3 = new Product();
        product3.setTitle("Another Product");
        product3.setDescription("Description for Another Product"); // Добавьте описание
        product3.setPrice(200); // Заполните другие обязательные поля
        product3.setCities(new ArrayList<>()); // Если города обязательны
        entityManager.persist(product3);

        entityManager.flush();

        List<Product> foundProducts = productRepository.searchProductByKeywordTitle("Test");

        assertFalse(foundProducts.isEmpty(), "Products should be found for the given keyword");
        assertEquals(2, foundProducts.size(), "Two products should be found with the keyword 'Test'");
        assertTrue(foundProducts.stream().allMatch(p -> p.getTitle().toLowerCase().contains("test")),
                "All found products should contain the keyword 'Test' in the title");
    }


    @Test
    @DisplayName("Should return empty list when no product matches the keyword")
    public void testSearchProductByKeywordTitle_WhenNoProductMatchesKeyword() {
        List<Product> foundProducts = productRepository.searchProductByKeywordTitle("NonExistingKeyword");

        assertTrue(foundProducts.isEmpty(), "No products should be found for a non-existing keyword");
    }

    @Test
    @DisplayName("Should return products when matching key and city found")
    public void testSearchProductByKeywordTitleAndCities_WhenMatchesFound() {
        product1.setCities(Collections.singletonList(city));

        entityManager.persist(product1);
        entityManager.flush();

        List<Product> foundProducts = productRepository.searchProductByKeywordTitleAndCities("Test", city.getId());

        assertFalse(foundProducts.isEmpty());
        assertEquals(1, foundProducts.size());
        assertEquals("Test Product", foundProducts.get(0).getTitle());
        assertTrue(foundProducts.get(0).getCities().contains(city));
    }

    @Test
    @DisplayName("Should return empty list when no matching key and city found")
    public void testSearchProductByKeywordTitleAndCities_WhenNoMatchesFound() {
        product1.setCities(Collections.singletonList(city));

        entityManager.persist(city);
        entityManager.persist(product1);
        entityManager.flush();

        List<Product> foundProducts = productRepository.searchProductByKeywordTitleAndCities("Nonexistent", 999L);

        assertTrue(foundProducts.isEmpty(), "No products should be found when there are no matches");
    }

}
