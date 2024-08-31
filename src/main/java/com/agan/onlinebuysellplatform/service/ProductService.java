package com.agan.onlinebuysellplatform.service;

import com.agan.onlinebuysellplatform.model.GermanCity;
import com.agan.onlinebuysellplatform.model.Image;
import com.agan.onlinebuysellplatform.model.Product;
import com.agan.onlinebuysellplatform.model.User;
import com.agan.onlinebuysellplatform.repository.ImageRepository;
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
    private final ImageRepository imageRepository;

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
                .filter(file -> file != null && file.getSize() > 0)
                .map(this::toImageEntity)
                .collect(Collectors.toList());

        if (images.isEmpty()) {
            setDefaultImage(product);
        } else {
            images.stream().
                    findFirst().
                    ifPresent(image -> image.setPreviewImage(true));

            product.getImages().addAll(images);
            product.setPreviewImageId(images.get(0).getId());
        }

        List<GermanCity> cities = cityIds.stream()
                .map(germanCityService::getCityById)
                .collect(Collectors.toList());

        product.setCities(cities);

        log.info("Saving new Product. Title: {}; Author email: {}", product.getTitle(), product.getUser().getEmail());

        Product productFromDb = productRepository.save(product);

        log.info("==> Preview image ID: {}", product.getImages().get(0).getId());

        productFromDb.setPreviewImageId(productFromDb.getImages().get(0).getId());

        productRepository.save(productFromDb);
    }

    @Transactional
    public void updateProduct(Long id, Principal principal, Product updatedProduct, List<Long> cityIds,
                              MultipartFile... files) {
        try {
            Product product = getProductById(id);

            if (product != null && product.getUser().equals(getUserByPrincipal(principal))) {
                log.info("Product found, updating details...");
                product.setTitle(updatedProduct.getTitle());
                product.setDescription(updatedProduct.getDescription());
                product.setPrice(updatedProduct.getPrice());

                List<GermanCity> cities = cityIds.stream()
                        .map(germanCityService::getCityById)
                        .collect(Collectors.toList());

                product.getCities().clear();
                product.getCities().addAll(cities);

                List<Image> existingImages = product.getImages();

                if (files != null && files.length > 0) {
                    log.info("Processing files...");

                    int minSize = Math.min(files.length, existingImages.size());

                    for (int i = 0; i < minSize; i++) {
                        MultipartFile file = files[i];

                        if (file != null && !file.isEmpty()) {
                            log.info("Updating existing image at index {}", i);
                            Image newImage = toImageEntity(file);
                            newImage.setProduct(product);

                            imageRepository.save(newImage);
                            log.info("Saved new image with ID: {}", newImage.getId());

                            Image oldImage = existingImages.get(i);
                            imageRepository.delete(oldImage);
                            existingImages.set(i, newImage);

                            if (i == 0) {
                                newImage.setPreviewImage(true);
                                product.setPreviewImageId(newImage.getId());
                                log.info("Set preview image ID: {}", newImage.getId());
                            } else {
                                newImage.setPreviewImage(false);
                            }
                        }
                    }

                    for (int i = existingImages.size(); i < files.length; i++) {
                        MultipartFile file = files[i];

                        if (file != null && !file.isEmpty()) {
                            log.info("Adding new image at index {}", i);
                            Image newImage = toImageEntity(file);
                            newImage.setProduct(product);

                            imageRepository.save(newImage);
                            log.info("Saved new image with ID: {}", newImage.getId());

                            existingImages.add(newImage);

                            if (i == 0) {
                                newImage.setPreviewImage(true);
                                product.setPreviewImageId(newImage.getId());
                                log.info("Set preview image ID: {}", newImage.getId());
                            } else {
                                newImage.setPreviewImage(false);
                            }
                        }
                    }

                    product.setImages(existingImages);
                    productRepository.save(product);
                    log.info("Product saved successfully with updated images.");
                }
            }

        } catch (Exception e) {
            log.error("Error during product update", e);
            throw e;
        }
    }

    public User getUserByPrincipal(Principal principal) {
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

    public void setDefaultImage(Product product) {
        Image defaultImage = new Image();
        defaultImage.setName("default-product");
        defaultImage.setOriginalFileName("default-product.png");
        defaultImage.setContentType("image/png");
        defaultImage.setSize(0L);
        defaultImage.setProduct(product);
        defaultImage.setPreviewImage(true);

        product.addImageToProduct(defaultImage);
        product.setPreviewImageId(defaultImage.getId());
    }

    @Transactional
    public void deleteProduct(Long id, Principal principal) {
        User currentUser = userService.getUserByPrincipal(principal);

        Product product = productRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Product with id: " + id + " does not exist"));

        if (!product.getUser().equals(currentUser)) {
            throw new RuntimeException("You do not have permission to delete this product");
        }

        productRepository.deleteById(id);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}
