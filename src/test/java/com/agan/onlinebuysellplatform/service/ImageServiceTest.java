package com.agan.onlinebuysellplatform.service;

import com.agan.onlinebuysellplatform.model.Image;
import com.agan.onlinebuysellplatform.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageService imageService;

    private Image image;

    @BeforeEach
    void setUpImage() {
        image = new Image();
        image.setId(1L);
        image.setName("Test Image");
    }

    @Test
    public void testGetImageByIdSuccess() {
        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));

        Image result = imageService.getImageById(1L);

        assertEquals(image, result);
        verify(imageRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetImageByIdNotFound() {
        when(imageRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            imageService.getImageById(2L);
        });

        assertEquals("Image not found", exception.getMessage());
        verify(imageRepository, times(1)).findById(2L);
    }

}
