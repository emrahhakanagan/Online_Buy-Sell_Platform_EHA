package com.agan.onlinebuysellplatform.repository;

import com.agan.onlinebuysellplatform.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByTitle(String title);

    @Query("SELECT p FROM Product p WHERE p.title LIKE CONCAT('%', :keyword, '%') ")
    List<Product> searchProductByKeywordTitle(@Param("keyword") String keyword);

}
