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

    private final String defaultImageName = "default-product";
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

        if (image.getId().equals(product.getPreviewImageId())) {
            product.getImages().remove(image);

            if (!product.getImages().isEmpty()) {
                Image newPreviewImage = product.getImages().get(0);
                newPreviewImage.setPreviewImage(true);
                product.setPreviewImageId(newPreviewImage.getId());
            } else {
                setDefaultImage(product);
                product.setPreviewImageId(getDefaultImageId());
            }
        }

        imageRepository.delete(image);
        productRepository.save(product);
    }

    private Long getDefaultImageId() {
        return imageRepository.findByName(defaultImageName)
                .map(Image::getId)
                .orElseThrow(() -> new EntityNotFoundException("Default image not found"));
    }

    private void setDefaultImage(Product product) {
        productService.setDefaultImage(product);
    }
}
