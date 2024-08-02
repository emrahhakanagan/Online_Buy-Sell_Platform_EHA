package com.agan.onlinebuysellplatform.service;

import com.agan.onlinebuysellplatform.model.GermanCity;
import com.agan.onlinebuysellplatform.model.Image;
import com.agan.onlinebuysellplatform.model.Product;
import com.agan.onlinebuysellplatform.model.User;
import com.agan.onlinebuysellplatform.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final GermanCityService germanCityService;
    private final UserService userService;

    public List<Product> listProducts(String title) {
        if (title != null) return productRepository.findByTitle(title);
        return productRepository.findAll();
    }

    public List<Product> searchProduct(Long cityId, String keyword) {
        List<Product> products;

        if (cityId != null || (keyword != null && !keyword.isEmpty())) {
            if (keyword != null) {
                keyword = keyword.trim().toLowerCase();
            }

            if (cityId != null && (keyword == null || keyword.isEmpty())) {
                products = productRepository.searchProductByCity(cityId);
            } else if (keyword != null && !keyword.isEmpty() && cityId == null) {
                products = productRepository.searchProductByKeywordTitle(keyword);
            } else {
                products = productRepository.searchProductByKeywordTitleAndCities(keyword, cityId);
            }
        } else {
            products = listProducts(keyword);
        }

        return products;
    }

    public String showMessageSearchProduct(Long cityId, String keyword, List<Product> products) {
        List<Product> isExistAnyProduct = productRepository.findAll();

        if (!isExistAnyProduct.isEmpty()) {
            if (cityId != null) {
                String cityNameById = germanCityService.getCityById(cityId).getCity_name();
                return "in " + cityNameById + " " + products.size() + " Product(s) found based on the request";
            } else if (cityId == null && keyword != null && !keyword.isEmpty()) {
                return "in all cities " + products.size() + " Product(s) found based on the request";
            } else if (products == null || products.isEmpty()) {
                return "No products found based on the request! You can see other products on our platform;";
            } else {
                return "";
            }
        } else {
            return "No products found on our platform;";
        }
    }

    public void saveProduct(Principal principal, Product product, List<Long> cityIds, MultipartFile... files) {
        product.setUser(getUserByPrincipal(principal));

        List<Image> images = Arrays.stream(files)
                .filter(file -> file.getSize() != 0)
                .map(this::toImageEntity)
                .peek(image -> {
                    if (product.getImages().isEmpty()) {
                        image.setPreviewImage(true);
                    }
                    product.addImageToProduct(image);
                })
                .toList();

        List<GermanCity> cities = cityIds.stream()
                .map(germanCityService::getCityById)
                .collect(Collectors.toList());

        product.setCities(cities);

        log.info("Saving new Product. Title: {}; Author email: {}", product.getTitle(), product.getUser().getEmail());

        Product productFromDb = productRepository.save(product);
        productFromDb.setPreviewImageId(productFromDb.getImages().get(0).getId());

        productRepository.save(productFromDb);
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) {
            throw new RuntimeException("Principal is null and user does not exist");
        }
        return userService.getUserByPrincipal(principal);
    }

    @SneakyThrows
    private Image toImageEntity(MultipartFile file) {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new RuntimeException("Product with id: " + id + " does not exist");
        }
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}
