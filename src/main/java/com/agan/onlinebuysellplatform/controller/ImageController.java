package com.agan.onlinebuysellplatform.controller;

import com.agan.onlinebuysellplatform.model.Image;
import com.agan.onlinebuysellplatform.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/images/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getImageById(@PathVariable Long id) {
        try {
            Image image = imageService.getImageById(id);

            if (image == null || image.getSize() == 0) {
                byte[] defaultImageBytes = Files.readAllBytes(Paths.get("src/main/resources/static/images/default-product.png"));

                return ResponseEntity.ok()
                        .header("filename", "default-product.png")
                        .contentType(MediaType.IMAGE_PNG)
                        .contentLength(defaultImageBytes.length)
                        .body(new InputStreamResource(new ByteArrayInputStream(defaultImageBytes)));
            }

            return ResponseEntity.ok()
                    .header("filename", image.getOriginalFileName())
                    .contentType(MediaType.valueOf(image.getContentType()))
                    .contentLength(image.getSize())
                    .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
        } catch (RuntimeException | IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found");
        }
    }
}
