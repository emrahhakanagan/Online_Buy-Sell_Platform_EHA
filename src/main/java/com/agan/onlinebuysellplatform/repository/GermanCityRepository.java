package com.agan.onlinebuysellplatform.repository;

import com.agan.onlinebuysellplatform.model.GermanCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GermanCityRepository extends JpaRepository<GermanCity, Long> {
}
