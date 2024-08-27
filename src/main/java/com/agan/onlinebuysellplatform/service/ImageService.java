package com.agan.onlinebuysellplatform.service;

import com.agan.onlinebuysellplatform.model.Image;
import com.agan.onlinebuysellplatform.repository.ImageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional(readOnly = true)
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));
    }

    public void deleteImageById(Long id) {
        // Проверяем, существует ли изображение с таким ID
        Optional<Image> imageOptional = imageRepository.findById(id);
        if (imageOptional.isPresent()) {
            // Если изображение найдено, удаляем его
            imageRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Image with ID " + id + " not found");
        }
    }
}
