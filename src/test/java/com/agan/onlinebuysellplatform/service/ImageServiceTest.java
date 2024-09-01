package com.agan.onlinebuysellplatform.service;

import com.agan.onlinebuysellplatform.model.Image;
import com.agan.onlinebuysellplatform.model.Product;
import com.agan.onlinebuysellplatform.repository.ImageRepository;
import com.agan.onlinebuysellplatform.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @Mock
    private ProductService productService;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ImageService imageService;

    private Product product;
    private Image image;

    @BeforeEach
    void setUp() {
        image = new Image();
        image.setId(1L);
        image.setName("Test Image");

        product = new Product();
        product.setId(1L);
        product.setPreviewImageId(1L);

        image.setProduct(product);
    }

    @Test
    @DisplayName("Should return image when ID is valid")
    public void testGetImageById_WhenIdIsValid() {
        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));

        Image foundImage = imageService.getImageById(1L);

        assertNotNull(foundImage);
        assertEquals(1L, foundImage.getId());
        verify(imageRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when ID is invalid")
    public void testGetImageById_WhenIdIsInvalid() {
        when(imageRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            imageService.getImageById(1L);
        });

        assertEquals("Image not found", exception.getMessage());

        verify(imageRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should delete image and update preview image when image ID is valid and is the current preview image")
    public void testDeleteImageById_WhenImageIsValidAndIsPreviewImage() {
        Image newPreviewImage = new Image();
        newPreviewImage.setId(2L);
        newPreviewImage.setProduct(product);

        product.setImages(new ArrayList<>(Arrays.asList(image, newPreviewImage)));

        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        imageService.deleteImageById(1L);

        verify(imageRepository, times(1)).delete(image);
        verify(productRepository, times(1)).save(product);

        assertEquals(2L, product.getPreviewImageId());
    }

    @Test
    @DisplayName("Should delete image and set default image when it is the last image")
    public void testDeleteImageById_WhenImageIsLastImage() {
        Image defaultImage = new Image();
        defaultImage.setId(100L);
        defaultImage.setName("default-product");

        product.setImages(new ArrayList<>(Collections.singletonList(image)));

        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));
        when(imageRepository.findByName("default-product")).thenReturn(Optional.of(defaultImage));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        imageService.deleteImageById(1L);

        verify(imageRepository, times(1)).delete(image);
        verify(productRepository, times(1)).save(product);
        assertEquals(100L, product.getPreviewImageId());
    }

    @Test
    @DisplayName("Should return default image ID when default image is found")
    public void testGetDefaultImageId_WhenDefaultImageIsFound() {
        Image defaultImage = new Image();
        defaultImage.setId(100L);
        defaultImage.setName("default-product");

        when(imageRepository.findByName("default-product")).thenReturn(Optional.of(defaultImage));

        Long defaultImageId = imageService.getDefaultImageId();

        assertNotNull(defaultImageId);
        assertEquals(100L, defaultImageId);
        verify(imageRepository, times(1)).findByName("default-product");
    }


}
