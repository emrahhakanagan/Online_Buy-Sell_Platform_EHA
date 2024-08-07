package com.agan.onlinebuysellplatform.controller;


import com.agan.onlinebuysellplatform.model.GermanCity;
import com.agan.onlinebuysellplatform.service.GermanCityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class GermanCityControllerTest {

    @Mock
    private GermanCityService germanCityService;

    @InjectMocks
    private GermanCityController germanCityController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(germanCityController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should return list of cities")
    public void testGetAllCities_WhenCitiesExist() throws Exception {
        List<GermanCity> cities = Arrays.asList(new GermanCity(), new GermanCity());
        when(germanCityService.getAllCities()).thenReturn(cities);

        mockMvc.perform(get("/cities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(cities.size()));
    }

    @Test
    @DisplayName("Should return empty list when no cities")
    public void testGetAllCities_WhenNoCitiesExist() throws Exception {
        when(germanCityService.getAllCities()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/cities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("Should return city when city exists")
    public void testGetCityById_WhenCityExists() throws Exception {
        GermanCity city = new GermanCity();
        city.setId(1L);
        when(germanCityService.getCityById(1L)).thenReturn(city);

        mockMvc.perform(get("/cities/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(city.getId()));
    }

    @Test
    @DisplayName("Should return 404 when city not found")
    public void testGetCityById_WhenCityNotFound() throws Exception {
        when(germanCityService.getCityById(1L)).thenReturn(null);

        mockMvc.perform(get("/cities/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should create city and return created city")
    public void testCreateCity() throws Exception {
        GermanCity city = new GermanCity();
        city.setCity_name("Berlin");
        when(germanCityService.saveCity(any(GermanCity.class))).thenReturn(city);

        mockMvc.perform(post("/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(city)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city_name").value(city.getCity_name()));
    }

    @Test
    @DisplayName("Should delete city and return status 200")
    public void testDeleteCity() throws Exception {
        doNothing().when(germanCityService).deleteCity(1L);

        mockMvc.perform(delete("/cities/1"))
                .andExpect(status().isOk());

        verify(germanCityService, times(1)).deleteCity(1L);
    }
}
