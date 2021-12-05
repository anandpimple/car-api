package com.test.car.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.car.api.CarApplication;
import com.test.car.api.model.CreateCarRequest;

@SpringBootTest(classes = CarApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class CarApiControllerITTest {
    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper mapper() {
        return new ObjectMapper();
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

    private void testValidationIssue(String make, String model, String colour, int year) throws Exception {
        CreateCarRequest carRequest = CreateCarRequest.builder().make(make).colour(colour).model(model).year(year).build();
        mockMvc.perform(post("/cars").contentType(MediaType.APPLICATION_JSON).content(mapper().writeValueAsString(carRequest)))
            .andExpect(status().is4xxClientError());
    }
}