package com.test.car.api.controller;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.car.api.model.CarResponse;
import com.test.car.api.model.CreateCarRequest;
import com.test.car.api.model.PageResponse;
import com.test.car.api.service.CarServiceImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Validated
@RestController
public class CarApiController {
    private final CarServiceImpl carServiceImpl;

    public CarApiController(CarServiceImpl carServiceImpl) {
        this.carServiceImpl = carServiceImpl;
    }

    @GetMapping(path = "/cars/{businessId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("To get Car by businessId. Will return 404, if car not found")
    public CarResponse getCar(@PathVariable(value = "businessId") final String businessId) {
        return carServiceImpl.getCarByBusinessId(businessId);
    }

    @GetMapping(path = "/cars", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("To get all cars")
    public PageResponse<CarResponse> getCars(@ApiParam(required = true, example = "500", defaultValue = "500") @RequestParam(defaultValue = "500") @Max(500) int size,
                                             @ApiParam(required = true, example = "0", defaultValue = "0") @RequestParam(defaultValue = "0") @Min(0) int pageNo) {
        return carServiceImpl.getCars(size, pageNo);
    }

    @PostMapping(path = "/cars", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("To add new car in system")
    public CarResponse addCar(@Valid @NotNull @RequestBody final CreateCarRequest carRequest) {
        return carServiceImpl.addCar(carRequest);
    }

    @DeleteMapping(path = "/cars/{businessId}")
    @ApiOperation("To delete car from the system")
    public void deleteCar(@PathVariable(value = "businessId") final String businessId) {
        carServiceImpl.delete(businessId);
    }
}
