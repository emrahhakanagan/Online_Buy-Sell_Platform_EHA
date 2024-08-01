package com.agan.onlinebuysellplatform.service;

import com.agan.onlinebuysellplatform.model.GermanCity;
import com.agan.onlinebuysellplatform.model.Product;
import com.agan.onlinebuysellplatform.model.User;
import com.agan.onlinebuysellplatform.model.enums.Role;
import com.agan.onlinebuysellplatform.repository.ProductRepository;
import com.agan.onlinebuysellplatform.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GermanCityService germanCityService;

    @Mock
    private MultipartFile file1;

    @Mock
    private MultipartFile file2;

    @Mock
    private MultipartFile file3;

    @InjectMocks
    private ProductService productService;

    private User user;

    @BeforeEach
    void save() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        user.setConfirmed(false);
    }

    @Test
    public void testListProducts() {
        Product product1 = new Product();
        product1.setTitle("Product 1");

        Product product2 = new Product();
        product2.setTitle("Product 2");

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> products = productService.listProducts(null);
        assertEquals(2, products.size());
        assertEquals("Product 1", products.get(0).getTitle());
        assertEquals("Product 2", products.get(1).getTitle());
    }

    @Test
    public void testSearchProduct() {
        Product product1 = new Product();
        product1.setTitle("Product 1");

        when(productRepository.searchProductByCity(1L)).thenReturn(Collections.singletonList(product1));
        when(productRepository.searchProductByKeywordTitle("test")).thenReturn(Collections.singletonList(product1));
        when(productRepository.searchProductByKeywordTitleAndCities("test", 1L)).thenReturn(Collections.singletonList(product1));

        List<Product> productsByCity = productService.searchProduct(1L, null);
        assertEquals(1, productsByCity.size());
        assertEquals("Product 1", productsByCity.get(0).getTitle());

        List<Product> productsByKeyword = productService.searchProduct(null, "test");
        assertEquals(1, productsByKeyword.size());
        assertEquals("Product 1", productsByKeyword.get(0).getTitle());

        List<Product> productsByKeywordAndCity = productService.searchProduct(1L, "test");
        assertEquals(1, productsByKeywordAndCity.size());
        assertEquals("Product 1", productsByKeywordAndCity.get(0).getTitle());
    }

    @Test
    public void testShowMessageSearchProduct() {
        // Мокируем метод productRepository.findAll()
        when(productRepository.findAll()).thenReturn(Arrays.asList(new Product(), new Product()));

        // Первый случай: продукты существуют, поиск по городу
        when(germanCityService.getCityById(1L)).thenReturn(new GermanCity(1L, "Berlin", new ArrayList<>()));
        String message = productService.showMessageSearchProduct(1L, null, Arrays.asList(new Product(), new Product()));
        assertTrue(message.contains("in Berlin 2 Product(s) found based on the request"));

        // Второй случай: продукты существуют, поиск по ключевому слову
        message = productService.showMessageSearchProduct(null, "test", Arrays.asList(new Product(), new Product()));
        assertTrue(message.contains("in all cities 2 Product(s) found based on the request"));

        // Третий случай: продукты существуют, но не найдены по запросу
        message = productService.showMessageSearchProduct(null, null, Collections.emptyList());
        assertTrue(message.contains("No products found based on the request! You can see other products on our platform;"));

        // Четвертый случай: продукты не существуют в базе данных
        when(productRepository.findAll()).thenReturn(Collections.emptyList());
        message = productService.showMessageSearchProduct(1L, null, Arrays.asList(new Product(), new Product()));
        assertTrue(message.contains("No products found on our platform;"));
    }

    @Test
    void testFindByTitle() {
        Product product = new Product();
        product.setTitle("Test Product");

        when(productRepository.findByTitle("Test Product")).thenReturn(Collections.singletonList(product));

        List<Product> products = productService.listProducts("Test Product");

        assertEquals(1, products.size());
        assertEquals("Test Product", products.get(0).getTitle());
        verify(productRepository, times(1)).findByTitle("Test Product");
    }

    @Test
    void testSaveProduct() throws Exception {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("test@example.com");

        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        MultipartFile file1 = mock(MultipartFile.class);
        MultipartFile file2 = mock(MultipartFile.class);
        MultipartFile file3 = mock(MultipartFile.class);
        when(file1.getSize()).thenReturn(100L);
        when(file2.getSize()).thenReturn(200L);
        when(file3.getSize()).thenReturn(300L);

        Product product = new Product();
        product.setTitle("Test Product");

        productService.saveProduct(principal, product, Collections.singletonList(1L), file1, file2, file3);

        verify(productRepository, times(2)).save(any(Product.class));
        assertEquals("test@example.com", product.getUser().getEmail());
    }

    @Test
    public void testGetUserByPrincipal() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        User result = productService.getUserByPrincipal(principal);
        assertEquals(user, result);

        result = productService.getUserByPrincipal(null);
        assertNotNull(result);
        assertEquals(new User(), result);
    }

    @Test
    void testDeleteProduct() {
        Product product = new Product();
        product.setId(1L);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetProductById() {
        Product product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);
        assertEquals(product, result);

        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        result = productService.getProductById(2L);
        assertEquals(null, result);
    }
}
