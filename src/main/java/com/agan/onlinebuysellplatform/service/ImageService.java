package com.agan.onlinebuysellplatform.service;

import com.agan.onlinebuysellplatform.model.Image;
import com.agan.onlinebuysellplatform.model.Product;
import com.agan.onlinebuysellplatform.repository.ImageRepository;
import com.agan.onlinebuysellplatform.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ProductRepository productRepository;
    private final ProductService productService;
    private final ImageRepository imageRepository;

    @Transactional(readOnly = true)
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));
    }

    @Transactional
    public void deleteImageById(Long id) {
        Image image = imageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Image not found"));
        Product product = image.getProduct();

        imageRepository.delete(image);

        if (image.getId().equals(product.getPreviewImageId())) {
            if (!product.getImages().isEmpty()) {
                Image newPreviewImage = product.getImages().get(0);
                newPreviewImage.setPreviewImage(true);
                product.setPreviewImageId(newPreviewImage.getId());
            } else {
                setDefaultImage(product);
            }
            productRepository.save(product);
        }
    }

    private void setDefaultImage(Product product) {
        productService.setDefaultImage(product);
    }
}
