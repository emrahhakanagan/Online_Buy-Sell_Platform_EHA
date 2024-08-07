package com.agan.onlinebuysellplatform.controller;


import com.agan.onlinebuysellplatform.model.Image;
import com.agan.onlinebuysellplatform.service.ImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ImageControllerTest {

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ImageController imageController;

    private MockMvc mockMvc;

    @Test
    @DisplayName("Should return image when image exists")
    public void testGetImageById_WhenImageExists() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();

        Image image = new Image();
        image.setId(1L);
        image.setOriginalFileName("test.jpg");
        image.setContentType("image/jpeg");
        image.setSize(1000L);
        image.setBytes("test image data".getBytes());

        when(imageService.getImageById(1L)).thenReturn(image);

        mockMvc.perform(get("/images/1"))
                .andExpect(status().isOk())
                .andExpect(header().string("filename", "test.jpg"))
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(image.getBytes()));
    }

    @Test
    @DisplayName("Should return 404 when image not found")
    public void testGetImageById_WhenImageNotFound() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();

        when(imageService.getImageById(1L)).thenThrow(new RuntimeException("Image not found"));

        mockMvc.perform(get("/images/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Image not found"));
    }
}
