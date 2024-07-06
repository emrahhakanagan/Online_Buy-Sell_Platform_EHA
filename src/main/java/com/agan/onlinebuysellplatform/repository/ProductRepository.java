package com.agan.onlinebuysellplatform.repository;

import com.agan.onlinebuysellplatform.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByTitle(String title);

}
