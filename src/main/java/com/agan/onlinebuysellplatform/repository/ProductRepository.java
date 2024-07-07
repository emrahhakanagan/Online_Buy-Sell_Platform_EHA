package com.agan.onlinebuysellplatform.repository;

import com.agan.onlinebuysellplatform.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Transactional(readOnly = true)
    List<Product> findByTitle(String title);

}
