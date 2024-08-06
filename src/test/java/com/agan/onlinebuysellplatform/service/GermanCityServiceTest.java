package com.agan.onlinebuysellplatform.service;

import com.agan.onlinebuysellplatform.model.GermanCity;
import com.agan.onlinebuysellplatform.repository.GermanCityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GermanCityServiceTest {

    @Mock
    private GermanCityRepository germanCityRepository;

    @InjectMocks
    private GermanCityService germanCityService;

    private GermanCity city;

    @BeforeEach
    void setUpCity() {
        city = new GermanCity();
        city.setId(1L);
        city.setCity_name("Berlin");
    }

    @Test
    @DisplayName("Should return cities sorted by name")
    public void testGetAllCities_ShouldReturnCitiesSortedByName() {
        GermanCity city2 = new GermanCity();
        city2.setCity_name("Munich");
        GermanCity city3 = new GermanCity();
        city3.setCity_name("Cologne");

        when(germanCityRepository.findAll()).thenReturn(Arrays.asList(city, city2, city3));

        List<GermanCity> sortedCities = germanCityService.getAllCities();

        assertEquals("Berlin", sortedCities.get(0).getCity_name());
        assertEquals("Cologne", sortedCities.get(1).getCity_name());
        assertEquals("Munich", sortedCities.get(2).getCity_name());
    }

    @Test
    @DisplayName("Should return city when city exists")
    public void testGetCityById_ShouldReturnCity_WhenCityExists() {
        when(germanCityRepository.findById(1L)).thenReturn(Optional.of(city));

        GermanCity foundCity = germanCityService.getCityById(1L);

        assertNotNull(foundCity);
        assertEquals("Berlin", foundCity.getCity_name());
    }

    @Test
    @DisplayName("Should return null when city does not exist")
    public void testGetCityById_ShouldReturnNull_WhenCityDoesNotExist() {
        when(germanCityRepository.findById(1L)).thenReturn(Optional.empty());

        GermanCity foundCity = germanCityService.getCityById(1L);

        assertNull(foundCity);
    }

    @Test
    @DisplayName("Should return saved city")
    public void testSaveCity_ShouldReturnSavedCity() {
        when(germanCityRepository.save(city)).thenReturn(city);

        GermanCity savedCity = germanCityService.saveCity(city);

        assertNotNull(savedCity);
        assertEquals("Berlin", savedCity.getCity_name());
    }

    @Test
    @DisplayName("Should call deleteById")
    public void testDeleteCity_ShouldCallDeleteById() {
        germanCityService.deleteCity(1L);

        verify(germanCityRepository, times(1)).deleteById(1L);
    }
}
