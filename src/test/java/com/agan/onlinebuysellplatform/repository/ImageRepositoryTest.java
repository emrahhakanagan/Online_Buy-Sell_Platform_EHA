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
        image.setName("testImage.jpg");
        image.setContentType("image/jpeg");
        image.setSize(1024L);
        image.setBytes(new byte[]{1, 2, 3});

        savedImage = imageRepository.save(image);
    }
    @Test
    @DisplayName("Should save and find image by ID")
    public void testSaveAndFindById() {
        Image foundImage = imageRepository.findById(savedImage.getId()).orElse(null);

        assertNotNull(foundImage, "Image should be found by ID");
        assertEquals("testImage.jpg", foundImage.getName(), "Image name should match");
    }

    @Test
    @DisplayName("Should delete image by ID")
    public void testDeleteById() {
        Long imageId = savedImage.getId();

        imageRepository.deleteById(imageId);

        Image foundImage = entityManager.find(Image.class, imageId);
        assertNull(foundImage, "Image should be deleted and not found");
    }
}
