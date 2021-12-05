package com.test.car.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.car.api.CarApplication;
import com.test.car.api.dao.CarRepository;
import com.test.car.api.entity.Car;
import com.test.car.api.model.CreateCarRequest;

@SpringBootTest(classes = CarApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class CarApiControllerITTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarRepository carRepository;

    private List<Long> carData;

    private static ObjectMapper mapper() {
        return new ObjectMapper();
    }

    @BeforeEach
    void setUp() {
        carData = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        carRepository.deleteAllById(carData);
    }

    @Test
    void givenProperCarData_whenPostCar_thenSuccess() throws Exception {

        CreateCarRequest carRequest = CreateCarRequest.builder().make("Make").colour("Red").model("Model").year(1999).build();
        mockMvc.perform(post("/cars").contentType(MediaType.APPLICATION_JSON).content(mapper().writeValueAsString(carRequest)))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("id").isNotEmpty())
            .andExpect(jsonPath("make").value("Make"))
            .andExpect(jsonPath("model").value("Model"))
            .andExpect(jsonPath("colour").value("Red"))
            .andExpect(jsonPath("year").value("1999"));
    }

    @Test
    void givenCarColourMissing_whenPostCar_thenSuccess() throws Exception {
        testValidationIssue("Make", "Model", null, 1998);
    }

    @Test
    void givenCarModelMissing_whenPostCar_thenSuccess() throws Exception {
        testValidationIssue("Make", "", "Red", 1998);
    }

    @Test
    void givenCarMakeMissing_whenPostCar_thenSuccess() throws Exception {
        testValidationIssue(" ", "Model", "Red", 1998);
    }

    @Test
    void givenCarYearIncorrect_whenPostCar_thenSuccess() throws Exception {
        testValidationIssue("Make", "Model", "Red", 1799);
    }

    @Test
    void givenCarAbsentForBusinessId_whenGetGarByBusinessId_then404() throws Exception {
        mockMvc.perform(get("/cars/2323232"))
            .andExpect(status().isNotFound());
    }

    @Test
    void givenCarPresentForBusinessId_whenGetGarByBusinessId_thenSuccess() throws Exception {
        addCar("businessId1", "colour", "make", "model", 1983);
        addCar("businessId2", "colour", "make", "model", 1983);
        addCar("businessId3", "colour", "make", "model", 1983);
        addCar("businessId4", "colour", "make", "model", 1983);
        mockMvc.perform(get("/cars/businessId4"))
            .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("id").value("businessId4"))
            .andExpect(jsonPath("make").value("make"));
    }

    @Test
    void givenNoCars_whenGetCars_thenSuccessWithEmptyData() throws Exception {
        mockMvc.perform(get("/cars"))
            .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().string("{\"content\":[],\"size\":500,\"totalSize\":0,\"page\":0,\"totalPages\":0}"));
    }

    @Test
    void givenMultipleCars_whenGetCars_thenSuccessWithData() throws Exception {
        addCar("businessId1", "colour", "make", "model", 1983);
        addCar("businessId2", "colour", "make", "model", 1983);
        mockMvc.perform(get("/cars"))
            .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().string("{\"content\":[{\"id\":\"businessId1\",\"model\":\"model\",\"make\":\"make\",\"colour\":\"colour\",\"year\":1983},{\"id\":\"businessId2\",\"model\":\"model\",\"make\":\"make\",\"colour\":\"colour\",\"year\":1983}],\"size\":500,\"totalSize\":2,\"page\":0,\"totalPages\":1}"));
    }

    private void testValidationIssue(String make, String model, String colour, int year) throws Exception {
        CreateCarRequest carRequest = CreateCarRequest.builder().make(make).colour(colour).model(model).year(year).build();
        mockMvc.perform(post("/cars").contentType(MediaType.APPLICATION_JSON).content(mapper().writeValueAsString(carRequest)))
            .andExpect(status().is4xxClientError());
    }

    private void addCar(String businessId, String colour, String make, String model, int year) {
        Car car = new Car();
        car.setMake(make);
        car.setYear(year);
        car.setColour(colour);
        car.setBusinessId(businessId);
        car.setModel(model);

        carData.add(carRepository.save(car).getId());
    }
}