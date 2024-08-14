package com.agan.onlinebuysellplatform.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;

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

    @Test
    @DisplayName("Should handle null or negative size")
    public void testSetSize_WithNullOrNegativeValues() {
        image.setSize(null);
        assertNull(image.getSize(), "Size should be null when set to null");

        image.setSize(-1024L);
        assertEquals(-1024L, image.getSize(), "Size should be negative when set to a negative value");
    }

    @Test
    @DisplayName("Should handle empty bytes array")
    public void testSetBytes_WithEmptyArray() {
        byte[] emptyBytes = new byte[0];
        image.setBytes(emptyBytes);
        assertArrayEquals(emptyBytes, image.getBytes(), "Bytes array should be empty");
    }

    @Test
    @DisplayName("Should consider two identical images as equal")
    public void testEqualsAndHashCode_WithIdenticalImages() {
        Image image1 = new Image();
        image1.setId(1L);
        image1.setName("Test Image");
        image1.setOriginalFileName("test_image.jpg");

        Image image2 = new Image();
        image2.setId(1L);
        image2.setName("Test Image");
        image2.setOriginalFileName("test_image.jpg");

        assertEquals(image1, image2, "Images with the same properties should be equal");
        assertEquals(image1.hashCode(), image2.hashCode(), "Hash codes of equal images should be the same");
    }

    @Test
    @DisplayName("Should consider two different images as not equal")
    public void testEqualsAndHashCode_WithDifferentImages() {
        Image image1 = new Image();
        image1.setId(1L);
        image1.setName("Test Image 1");

        Image image2 = new Image();
        image2.setId(2L);
        image2.setName("Test Image 2");

        assertNotEquals(image1, image2, "Images with different properties should not be equal");
        assertNotEquals(image1.hashCode(), image2.hashCode(), "Hash codes of different images should not be the same");
    }

    @Test
    @DisplayName("Should serialize and deserialize image")
    public void testSerialization() throws IOException, ClassNotFoundException {
        image.setId(1L);
        image.setName("Serialized Image");

        // Serialize the image object to a byte array
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(image);
        out.flush();
        byte[] imageData = byteOut.toByteArray();

        // Deserialize the byte array back to an image object
        ByteArrayInputStream byteIn = new ByteArrayInputStream(imageData);
        ObjectInputStream in = new ObjectInputStream(byteIn);
        Image deserializedImage = (Image) in.readObject();

        assertEquals(image, deserializedImage, "Deserialized image should be equal to the original");
    }
}
