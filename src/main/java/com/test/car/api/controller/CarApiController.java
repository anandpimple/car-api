package com.test.car.api.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.test.car.api.model.CarResponse;
import com.test.car.api.model.CreateCarRequest;
import com.test.car.api.service.CarServiceImpl;

import io.swagger.annotations.ApiOperation;

@Validated
@RestController
public class CarApiController {
    private final CarServiceImpl carServiceImpl;

    public CarApiController(CarServiceImpl carServiceImpl) {
        this.carServiceImpl = carServiceImpl;
    }

    @GetMapping(path = "/cars")
    public String getCars() {
        return "Cars";
    }

    @PostMapping(path = "/cars", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("To add new car in system")
    public CarResponse addCar(@Valid @NotNull @RequestBody final CreateCarRequest carRequest) {
        return carServiceImpl.addCar(carRequest);
    }
}
