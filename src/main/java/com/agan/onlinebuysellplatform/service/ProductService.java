package com.agan.onlinebuysellplatform.service;

import com.agan.onlinebuysellplatform.model.Image;
import com.agan.onlinebuysellplatform.model.Product;
import com.agan.onlinebuysellplatform.model.User;
import com.agan.onlinebuysellplatform.repository.ProductRepository;
import com.agan.onlinebuysellplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Product> listProducts(String title) {
        if (title != null) return productRepository.findByTitle(title);

        return productRepository.findAll();
    }

    @Transactional
    public void saveProduct(Principal principal, Product product, MultipartFile... files) throws IOException {
        AtomicBoolean isFirstImage = new AtomicBoolean(true);
        product.setUser(getUserByPrincipal(principal));

        Arrays.stream(files)
                        .filter(file -> file.getSize() != 0)
                        .map(file -> {
                            try {
                                Image image = toImageEntity(file);

                                if (isFirstImage.getAndSet(false)) {
                                    image.setPreviewImage(true);
                                }

                                return image;
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .forEach(product::addImageToProduct);

        log.info("Saving new Product. Title: {}; Author: {}", product.getTitle(), product.getAuthor());

        Product productFromDB = productRepository.save(product);
        productFromDB.setPreviewImageId(productFromDB.getImages().get(0).getId());

        productRepository.save(product);
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();

        return userRepository.findByEmail(principal.getName());
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
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
        productRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}
