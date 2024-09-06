package com.agan.onlinebuysellplatform.repository;

import com.agan.onlinebuysellplatform.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByTitle(String title);

    @Query("SELECT p FROM Product p JOIN p.cities c WHERE c.id = :cityId")
    List<Product> searchProductByCity(@Param("cityId") Long cityId);

    @Query("SELECT p FROM Product p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) ")
    List<Product> searchProductByKeywordTitle(@Param("keyword") String keyword);

    @Query("SELECT p FROM Product p JOIN p.cities c WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) AND c.id = :cityId")
    List<Product> searchProductByKeywordTitleAndCities(@Param("keyword") String keyword, @Param("cityId") Long cityId);

    @Modifying
    @Query("DELETE FROM Product p WHERE p.id = :id AND p.user.id = :userId")
    void deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

}
