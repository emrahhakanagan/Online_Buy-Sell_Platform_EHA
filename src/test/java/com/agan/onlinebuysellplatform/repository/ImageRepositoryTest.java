package com.agan.onlinebuysellplatform.repository;

import com.agan.onlinebuysellplatform.config.TestConfig;
import com.agan.onlinebuysellplatform.model.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestConfig.class)
public class ImageRepositoryTest {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Image savedImage;

    @BeforeEach
    public void setup() {
        Image image = new Image();
        image.setName("testImage");
        image.setContentType("image/jpeg");
        image.setSize(1024L);
        image.setBytes(new byte[]{1, 2, 3});

        savedImage = imageRepository.save(image);
    }

    @Test
    @DisplayName("Should save image and return saved entity")
    public void testSaveImage() {
        Image image = new Image();
        image.setName("Test Image");

        Image savedImage = imageRepository.save(image);

        assertNotNull(savedImage.getId());
        assertEquals("Test Image", savedImage.getName());
    }

    @Test
    @DisplayName("Should delete image by ID")
    public void testDeleteById() {
        Long imageId = savedImage.getId();

        imageRepository.deleteById(imageId);

        Image foundImage = entityManager.find(Image.class, imageId);
        assertNull(foundImage, "Image should be deleted and not found");
    }

    @Test
    @DisplayName("Should return Optional with Image when image with given name exists")
    public void testFindByName_WhenImageExists() {
        Optional<Image> foundImage = imageRepository.findByName("testImage");

        assertTrue(foundImage.isPresent());
        assertEquals("testImage", foundImage.get().getName());
    }

    @Test
    @DisplayName("Should return empty Optional when image with given name does not exist")
    public void testFindByName_WhenImageDoesNotExist() {
        Optional<Image> foundImage = imageRepository.findByName("non-existent-image");

        assertFalse(foundImage.isPresent());
    }

}
