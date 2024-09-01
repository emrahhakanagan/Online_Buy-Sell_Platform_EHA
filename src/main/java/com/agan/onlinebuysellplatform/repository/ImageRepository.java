package com.agan.onlinebuysellplatform.repository;

import com.agan.onlinebuysellplatform.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.net.ssl.SSLSession;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByName(String defaultImageName);
}
