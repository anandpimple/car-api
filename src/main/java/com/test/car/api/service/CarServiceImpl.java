package com.test.car.api.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.test.car.api.dao.CarRepository;
import com.test.car.api.entity.Car;
import com.test.car.api.model.CarResponse;
import com.test.car.api.model.CreateCarRequest;

@Service
public class CarServiceImpl implements CarService {
    private static final Logger logger = LoggerFactory.getLogger(CarServiceImpl.class);

    private final CarRepository carRepository;

    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    private static Car convertToCarEntity(CreateCarRequest carRequest) {
        Car car = new Car();
        car.setColour(carRequest.getColour());
        car.setModel(carRequest.getModel());
        car.setYear(carRequest.getYear());
        car.setBusinessId(generateBusinessId());
        car.setMake(carRequest.getMake());
        return car;
    }

    private static String generateBusinessId() {
        return RandomStringUtils.randomAlphanumeric(12);
    }

    @Override
    public CarResponse addCar(final CreateCarRequest carRequest) {
        logger.info("Adding car details to system");
        final Car savedEntity = carRepository.save(convertToCarEntity(carRequest));
        return CarResponse.builder().colour(savedEntity.getColour()).id(savedEntity.getBusinessId()).make(savedEntity.getMake()).model(savedEntity.getModel()).year(savedEntity.getYear()).build();
    }
}
