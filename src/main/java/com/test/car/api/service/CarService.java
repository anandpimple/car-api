package com.test.car.api.service;

import com.test.car.api.model.CarResponse;
import com.test.car.api.model.CreateCarRequest;

public interface CarService {
    CarResponse addCar(CreateCarRequest carRequest);
}
