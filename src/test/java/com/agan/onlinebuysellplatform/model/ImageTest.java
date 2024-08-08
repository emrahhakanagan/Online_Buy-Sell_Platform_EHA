package com.agan.onlinebuysellplatform.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ImageTest {

    private Image image;

    @BeforeEach
    public void setUp() {
        image = new Image();
    }

    @Test
    @DisplayName("Should create image with default values")
    public void testCreateImage_WithDefaultValues() {
        assertNull(image.getId());
        assertNull(image.getName());
        assertNull(image.getOriginalFileName());
        assertNull(image.getSize());
        assertNull(image.getContentType());
        assertFalse(image.isPreviewImage());
        assertNull(image.getBytes());
        assertNull(image.getProduct());
    }

    @Test
    @DisplayName("Should set and get name")
    public void testSetAndGetName() {
        String name = "Test Image";
        image.setName(name);
        assertEquals(name, image.getName());
    }

    @Test
    @DisplayName("Should set and get original file name")
    public void testSetAndGetOriginalFileName() {
        String originalFileName = "test_image.jpg";
        image.setOriginalFileName(originalFileName);
        assertEquals(originalFileName, image.getOriginalFileName());
    }

    @Test
    @DisplayName("Should set and get size")
    public void testSetAndGetSize() {
        Long size = 1024L;
        image.setSize(size);
        assertEquals(size, image.getSize());
    }

    @Test
    @DisplayName("Should set and get content type")
    public void testSetAndGetContentType() {
        String contentType = "image/jpeg";
        image.setContentType(contentType);
        assertEquals(contentType, image.getContentType());
    }

    @Test
    @DisplayName("Should set and get preview image status")
    public void testSetAndGetPreviewImageStatus() {
        image.setPreviewImage(true);
        assertTrue(image.isPreviewImage());
        image.setPreviewImage(false);
        assertFalse(image.isPreviewImage());
    }

    @Test
    @DisplayName("Should set and get bytes")
    public void testSetAndGetBytes() {
        byte[] bytes = {1, 2, 3, 4};
        image.setBytes(bytes);
        assertArrayEquals(bytes, image.getBytes());
    }

    @Test
    @DisplayName("Should set and get product")
    public void testSetAndGetProduct() {
        Product product = new Product();
        image.setProduct(product);
        assertEquals(product, image.getProduct());
    }
}
