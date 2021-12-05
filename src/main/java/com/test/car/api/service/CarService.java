package com.test.car.api.service;

import com.test.car.api.model.CarResponse;
import com.test.car.api.model.CreateCarRequest;
import com.test.car.api.model.PageResponse;

public interface CarService {
    CarResponse addCar(final CreateCarRequest carRequest);

    CarResponse getCarByBusinessId(final String businessId);

    PageResponse<CarResponse> getCars(int size, int pageNo);

    void delete(final String businessId);
}
