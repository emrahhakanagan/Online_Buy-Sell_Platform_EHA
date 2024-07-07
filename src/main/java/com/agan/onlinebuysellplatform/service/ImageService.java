package com.agan.onlinebuysellplatform.service;

import com.agan.onlinebuysellplatform.model.Image;
import com.agan.onlinebuysellplatform.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional(readOnly = true)
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElse(null);
    }
}
