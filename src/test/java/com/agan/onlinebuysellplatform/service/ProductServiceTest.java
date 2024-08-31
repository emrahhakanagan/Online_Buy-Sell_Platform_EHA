package com.agan.onlinebuysellplatform.service;

import com.agan.onlinebuysellplatform.model.GermanCity;
import com.agan.onlinebuysellplatform.model.Product;
import com.agan.onlinebuysellplatform.model.User;
import com.agan.onlinebuysellplatform.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserService userService;

    @Mock
    private GermanCityService germanCityService;

    @InjectMocks
    private ProductService productService;

    private Product product1;
    private GermanCity germanCity;
    private User mockUser;
    private Principal mockPrincipal;

//    @BeforeEach
//    void setUpMocks() {
//        MockitoAnnotations.openMocks(this);
//    }

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");

        product1 = new Product();
        product1.setTitle("Product 1");
        product1.setId(1L);
        product1.setUser(mockUser);

        mockPrincipal = mock(Principal.class);
    }

    @Test
    @DisplayName("Should return list of products when products exist")
    public void testListProducts_WhenProductsExist() {

        Product product2 = new Product();
        product2.setTitle("Product 2");

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> products = productService.listProducts(null);
        assertEquals(2, products.size());
        assertEquals("Product 1", products.get(0).getTitle());
        assertEquals("Product 2", products.get(1).getTitle());
    }

    @Test
    @DisplayName("Should return empty list when no products exist")
    public void testListProducts_WhenNoProductsExist() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        List<Product> products = productService.listProducts(null);
        assertTrue(products.isEmpty());
    }

    @Test
    @DisplayName("Should return list of products when searching by city")
    public void testSearchProduct_WhenSearchingByCity() {
        when(productRepository.searchProductByCity(1L)).thenReturn(Collections.singletonList(product1));

        List<Product> productsByCity = productService.searchProduct(1L, null);
        assertEquals(1, productsByCity.size());
        assertEquals("Product 1", productsByCity.get(0).getTitle());
    }

    @Test
    @DisplayName("Should return empty list when no products found by city")
    public void testSearchProduct_WhenNoProductsFoundByCity() {
        when(productRepository.searchProductByCity(1L)).thenReturn(Collections.emptyList());

        List<Product> productsByCity = productService.searchProduct(1L, null);
        assertTrue(productsByCity.isEmpty());
    }

    @Test
    @DisplayName("Should return list of products when searching by keyword")
    public void testSearchProduct_WhenSearchingByKeyword() {
        when(productRepository.searchProductByKeywordTitle("test")).thenReturn(Collections.singletonList(product1));

        List<Product> productsByKeyword = productService.searchProduct(null, "test");
        assertEquals(1, productsByKeyword.size());
        assertEquals("Product 1", productsByKeyword.get(0).getTitle());
    }

    @Test
    @DisplayName("Should return empty list when no products found by keyword")
    public void testSearchProduct_WhenNoProductsFoundByKeyword() {
        when(productRepository.searchProductByKeywordTitle("test")).thenReturn(Collections.emptyList());

        List<Product> productsByKeyword = productService.searchProduct(null, "test");
        assertTrue(productsByKeyword.isEmpty());
    }

    @Test
    @DisplayName("Should return list of products when searching by keyword and city")
    public void testSearchProduct_WhenSearchingByKeywordAndCity() {
        when(productRepository.searchProductByKeywordTitleAndCities("test", 1L))
                .thenReturn(Collections.singletonList(product1));

        List<Product> productsByKeywordAndCity = productService.searchProduct(1L, "test");
        assertEquals(1, productsByKeywordAndCity.size());
        assertEquals("Product 1", productsByKeywordAndCity.get(0).getTitle());
    }

    @Test
    @DisplayName("Should return empty list when no products found by keyword and city")
    public void testSearchProduct_WhenNoProductsFoundByKeywordAndCity() {
        when(productRepository.searchProductByKeywordTitleAndCities("test", 1L))
                .thenReturn(Collections.emptyList());

        List<Product> productsByKeywordAndCity = productService.searchProduct(1L, "test");
        assertTrue(productsByKeywordAndCity.isEmpty());
    }

    @Test
    @DisplayName("Should show message with no products on platform when no products exist")
    public void testShowMessageSearchProduct_WhenNoProductsExist() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());
        String message = productService.showMessageSearchProduct(null, null, null);
        assertEquals("No products found on our platform;", message);
    }

    @Test
    @DisplayName("Should show message with no products found based on the request")
    public void testShowMessageSearchProduct_WhenProductsIsEmpty() {
        List<Product> allProducts = Arrays.asList(new Product(), new Product());
        List<Product> searchProducts = Collections.emptyList();
        when(productRepository.findAll()).thenReturn(allProducts);
        String message = productService.showMessageSearchProduct(null, null, searchProducts);
        assertEquals("No products found based on the request! You can see other products on our platform;", message);
    }

    @Test
    @DisplayName("Should show message with product count in all cities when products are found by keyword")
    public void testShowMessageSearchProduct_WhenProductsFoundByKeyword() {
        List<Product> products = Arrays.asList(new Product(), new Product());
        when(productRepository.findAll()).thenReturn(products);
        String message = productService.showMessageSearchProduct(null, "test", products);
        assertEquals("in all cities 2 Product(s) found based on the request", message);
    }

    @Test
    @DisplayName("Should show message with product count in city when products are found by city")
    public void testShowMessageSearchProduct_WhenProductsFoundByCity() {
        List<Product> products = Arrays.asList(new Product(), new Product());
        germanCity = new GermanCity();
        germanCity.setId(1L);
        germanCity.setCity_name("CityName");

        when(productRepository.findAll()).thenReturn(products);
        when(germanCityService.getCityById(1L)).thenReturn(germanCity);
        String message = productService.showMessageSearchProduct(1L, null, products);
        assertEquals("in CityName 2 Product(s) found based on the request", message);
    }

    @Test
    @DisplayName("Should show message with all products when no city or keyword is specified")
    public void testShowMessageSearchProduct_WhenNoCityAndKeyword() {
        List<Product> products = Arrays.asList(new Product(), new Product());
        when(productRepository.findAll()).thenReturn(products);
        String message = productService.showMessageSearchProduct(null, null, products);
        assertEquals("", message);
    }

    @Test
    @DisplayName("Should return products when products with given title are found")
    public void testFindByTitle_WhenProductsWithTitleAreFound() {
        when(productRepository.findByTitle("Product 1")).thenReturn(Collections.singletonList(product1));

        List<Product> products = productService.listProducts("Product 1");

        assertEquals(1, products.size());
        assertEquals("Product 1", products.get(0).getTitle());
        verify(productRepository, times(1)).findByTitle("Product 1");
    }

    @Test
    @DisplayName("Should return empty list when no products with given title are found")
    public void testFindByTitle_WhenNoProductsWithTitleAreFound() {
        when(productRepository.findByTitle("Nonexistent Product")).thenReturn(Collections.emptyList());

        List<Product> products = productService.listProducts("Nonexistent Product");

        assertTrue(products.isEmpty());
        verify(productRepository, times(1)).findByTitle("Nonexistent Product");
    }

    @Test
    @DisplayName("Should save product when all inputs are valid")
    public void testSaveProduct_WhenAllInputsAreValid(){
        lenient().when(mockPrincipal.getName()).thenReturn("test@example.com");

        lenient().when(userService.getUserByPrincipal(mockPrincipal)).thenReturn(mockUser);
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        MultipartFile file1 = mock(MultipartFile.class);
        MultipartFile file2 = mock(MultipartFile.class);
        MultipartFile file3 = mock(MultipartFile.class);
        when(file1.getSize()).thenReturn(100L);
        when(file2.getSize()).thenReturn(200L);
        when(file3.getSize()).thenReturn(300L);

        List<Long> cityIds = Arrays.asList(1L, 2L);
        GermanCity city1 = new GermanCity(1L, "City1", new ArrayList<>());
        GermanCity city2 = new GermanCity(2L, "City2", new ArrayList<>());
        when(germanCityService.getCityById(1L)).thenReturn(city1);
        when(germanCityService.getCityById(2L)).thenReturn(city2);

        productService.saveProduct(mockPrincipal, product1, cityIds, file1, file2, file3);

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository, times(2)).save(productCaptor.capture());
        Product savedProduct = productCaptor.getAllValues().get(0);

        assertEquals(mockUser, savedProduct.getUser());
        assertEquals(3, savedProduct.getImages().size());
        assertTrue(savedProduct.getImages().get(0).isPreviewImage());
        assertEquals(Arrays.asList(city1, city2), savedProduct.getCities());
    }

    @Test
    @DisplayName("Should throw exception when principal is null")
    public void testSaveProduct_WhenPrincipalIsNull() {
        List<Long> cityIds = new ArrayList<>();
        MultipartFile file = mock(MultipartFile.class);

        assertThrows(RuntimeException.class, () -> {
            productService.saveProduct(mockPrincipal, product1, cityIds, file);
        });
    }

    @Test
    @DisplayName("Should return user when principal is valid")
    public void testGetUserByPrincipal_WhenPrincipalIsValid() {
        Principal principal = mock(Principal.class);
        lenient().when(principal.getName()).thenReturn("username");

        User user = new User();
        user.setEmail("username");

        lenient().when(userService.getUserByPrincipal(principal)).thenReturn(user);

        User result = userService.getUserByPrincipal(principal);
        assertEquals(user, result);
    }

    @Test
    @DisplayName("Should delete product when it exists and belongs to the user")
    public void testDeleteProduct_WhenProductExistsAndBelongsToUser() {
        when(userService.getUserByPrincipal(mockPrincipal)).thenReturn(mockUser);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        productService.deleteProduct(1L, mockPrincipal);

        verify(productRepository, times(1)).deleteById(1L);
    }


    @Test
    @DisplayName("Should throw exception when product does not belong to the user")
    public void testDeleteProduct_WhenProductDoesNotBelongToUser() {
        User anotherUser = new User();
        anotherUser.setId(2L);

        product1.setUser(anotherUser);

        when(userService.getUserByPrincipal(mockPrincipal)).thenReturn(mockUser);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.deleteProduct(1L, mockPrincipal);
        });

        assertEquals("You do not have permission to delete this product", exception.getMessage());

        verify(productRepository, times(0)).deleteById(anyLong());
    }



    @Test
    @DisplayName("Should throw exception when product does not exist")
    public void testDeleteProduct_WhenProductDoesNotExist() {
        Principal mockPrincipal = mock(Principal.class);
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            productService.deleteProduct(1L, mockPrincipal);
        });

        assertEquals("Product with id: 1 does not exist", exception.getMessage());

        verify(productRepository, times(0)).deleteById(anyLong());
    }


    @Test
    @DisplayName("Should return product when product exists by given ID")
    public void testGetProductById_WhenProductExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        Product result = productService.getProductById(1L);
        assertEquals(product1, result);
    }

    @Test
    @DisplayName("Should return null when product does not exist by given ID")
    public void testGetProductById_WhenProductDoesNotExist() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        Product result = productService.getProductById(2L);
        assertNull(result);
    }
}
