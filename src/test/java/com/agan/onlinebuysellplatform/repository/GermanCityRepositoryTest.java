package com.agan.onlinebuysellplatform.repository;

import com.agan.onlinebuysellplatform.config.TestConfig;
import com.agan.onlinebuysellplatform.model.GermanCity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestConfig.class)
public class GermanCityRepositoryTest {

    @Autowired
    private GermanCityRepository germanCityRepository;

    @Autowired
    private TestEntityManager entityManager;

    private GermanCity savedCity;

    @BeforeEach
    public void setup() {
        GermanCity city = new GermanCity();
        city.setCity_name("Berlin");

        savedCity = germanCityRepository.save(city);
    }

    @Test
    @DisplayName("Should save and find city by ID")
    public void testSaveAndFindById() {
        Optional<GermanCity> foundCity = germanCityRepository.findById(savedCity.getId());

        assertTrue(foundCity.isPresent(), "City should be found by ID");
        assertEquals("Berlin", foundCity.get().getCity_name(), "City name should match");
    }

    @Test
    @DisplayName("Should update city name")
    public void testUpdateCityName() {
        savedCity.setCity_name("Munich");
        GermanCity updatedCity = germanCityRepository.save(savedCity);

        Optional<GermanCity> foundCity = germanCityRepository.findById(updatedCity.getId());

        assertTrue(foundCity.isPresent(), "City should be found by ID after update");
        assertEquals("Munich", foundCity.get().getCity_name(), "City name should be updated");
    }

    @Test
    @DisplayName("Should delete city by ID")
    public void testDeleteById() {
        Long cityId = savedCity.getId();

        germanCityRepository.deleteById(cityId);

        Optional<GermanCity> foundCity = germanCityRepository.findById(cityId);
        assertFalse(foundCity.isPresent(), "City should be deleted and not found");
    }
}
