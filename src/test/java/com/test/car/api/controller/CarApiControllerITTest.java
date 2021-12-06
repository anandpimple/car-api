package com.test.car.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
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
    private static final Logger logger = LoggerFactory.getLogger(CarApiControllerITTest.class);
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarRepository carRepository;

    private List<Long> carData;

    private static ObjectMapper mapper() {
        return new ObjectMapper();
    }

    private static final String BASIC_AUTH_DETAILS = "Basic Y2FyLWFwaTpwYXNzdzByZA==";

    private static final String SECURITY_HEADER = "Authorization";

    @BeforeEach
    void setUp() {
        carData = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        try {
            carRepository.deleteAllById(carData);
        } catch (final EmptyResultDataAccessException exception) {
            //Ignoring the empty data exception as its in teardown method
            logger.info("Exception while deleting cars in tear-down", exception);
        }

    }

    @Test
    void givenProperCarData_whenPostCar_thenSuccess() throws Exception {

        CreateCarRequest carRequest = CreateCarRequest.builder().make("Make").colour("Red").model("Model").year(1999).build();
        mockMvc.perform(post("/cars").contentType(MediaType.APPLICATION_JSON).header(SECURITY_HEADER, BASIC_AUTH_DETAILS).content(mapper().writeValueAsString(carRequest)))
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
    void givenCarAbsentForBusinessId_whenGetCarByBusinessId_then404() throws Exception {
        mockMvc.perform(get("/cars/2323232").header(SECURITY_HEADER, BASIC_AUTH_DETAILS))
            .andExpect(status().isNotFound());
    }

    @Test
    void givenCarPresentForBusinessId_whenGetGarByBusinessId_thenSuccess() throws Exception {
        addCar("businessId1", "colour", "make", "model", 1983);
        addCar("businessId2", "colour", "make", "model", 1983);
        addCar("businessId3", "colour", "make", "model", 1983);
        addCar("businessId4", "colour", "make", "model", 1983);
        mockMvc.perform(get("/cars/businessId4").header(SECURITY_HEADER, BASIC_AUTH_DETAILS))
            .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("id").value("businessId4"))
            .andExpect(jsonPath("make").value("make"));
    }

    @Test
    void givenNoCars_whenGetCars_thenSuccessWithEmptyData() throws Exception {
        mockMvc.perform(get("/cars").header(SECURITY_HEADER, BASIC_AUTH_DETAILS))
            .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().string("{\"content\":[],\"size\":500,\"totalSize\":0,\"page\":0,\"totalPages\":0}"));
    }

    @Test
    void givenMultipleCars_whenGetCars_thenSuccessWithData() throws Exception {
        final String bId1 = RandomStringUtils.randomAlphanumeric(12);
        final String bId2 = RandomStringUtils.randomAlphanumeric(12);
        addCar(bId1, "colour", "make", "model", 1983);
        addCar(bId2, "colour", "make", "model", 1983);
        mockMvc.perform(get("/cars").header(SECURITY_HEADER, BASIC_AUTH_DETAILS))
            .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().string("{\"content\":[{\"id\":\"" + bId1 + "\",\"model\":\"model\",\"make\":\"make\",\"colour\":\"colour\",\"year\":1983},{\"id\":\"" + bId2 + "\",\"model\":\"model\",\"make\":\"make\",\"colour\":\"colour\",\"year\":1983}],\"size\":500,\"totalSize\":2,\"page\":0,\"totalPages\":1}"));
    }

    @Test
    void givenCarAbsentForBusinessId_whenDeleteCarByBusinessId_then404() throws Exception {
        mockMvc.perform(delete("/cars/2323232").header(SECURITY_HEADER, BASIC_AUTH_DETAILS))
            .andExpect(status().isNotFound());
    }

    @Test
    void givenCarPresentForBusinessId_whenDeleteCarByBusinessId_thenSuccess() throws Exception {
        final String bId1 = RandomStringUtils.randomAlphanumeric(12);
        addCar(bId1, "colour", "make", "model", 1983);
        mockMvc.perform(delete("/cars/" + bId1).header(SECURITY_HEADER, BASIC_AUTH_DETAILS))
            .andExpect(status().isOk());
    }

    private void testValidationIssue(String make, String model, String colour, int year) throws Exception {
        CreateCarRequest carRequest = CreateCarRequest.builder().make(make).colour(colour).model(model).year(year).build();
        mockMvc.perform(post("/cars").header(SECURITY_HEADER, BASIC_AUTH_DETAILS).contentType(MediaType.APPLICATION_JSON).content(mapper().writeValueAsString(carRequest)))
            .andExpect(status().is4xxClientError());
    }

    private void addCar(String businessId, String colour, String make, String model, int year) {
        Car car = new Car();
        car.setMake(make);
        car.setYear(year);
        car.setColour(colour);
        car.setBusinessId(businessId);
        car.setModel(model);
        car.setDeletedOn(null);

        carData.add(carRepository.save(car).getId());
    }
}