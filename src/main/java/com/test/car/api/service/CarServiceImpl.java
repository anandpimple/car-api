package com.test.car.api.service;

import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.test.car.api.dao.CarRepository;
import com.test.car.api.entity.Car;
import com.test.car.api.exception.DataNotFoundException;
import com.test.car.api.model.CarResponse;
import com.test.car.api.model.CreateCarRequest;
import com.test.car.api.model.PageResponse;

@Service
public class CarServiceImpl implements CarService {
    private static final Logger logger = LoggerFactory.getLogger(CarServiceImpl.class);

    private final CarRepository carRepository;

    public CarServiceImpl(final CarRepository carRepository) {
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


    private static CarResponse mapToResponse(final Car entity) {
        return CarResponse.builder().colour(entity.getColour()).id(entity.getBusinessId()).make(entity.getMake()).model(entity.getModel()).year(entity.getYear()).build();
    }

    private static String generateBusinessId() {
        return RandomStringUtils.randomAlphanumeric(12);
    }

    @Override
    public CarResponse addCar(final CreateCarRequest carRequest) {
        logger.info("Adding car details to system");
        final Car savedEntity = carRepository.save(convertToCarEntity(carRequest));
        return mapToResponse(savedEntity);
    }

    @Override
    public CarResponse getCarByBusinessId(final String businessId) {
        logger.info("Retrieving car details for business id '{}'", businessId);
        final Car carEntity = carRepository.getCarByBusinessId(businessId).orElseThrow(() -> new DataNotFoundException("BusinessId", String.format("Car not found with business id '%s'", businessId)));
        return mapToResponse(carEntity);
    }

    @Override
    public PageResponse getCars(int size, int pageNo) {
        logger.info("Retrieving cars for page no {} with max size {}", pageNo, size);
        final Page<Car> page = carRepository.findAll(Pageable.ofSize(size).withPage(pageNo));
        return PageResponse.builder().content(page.getContent().stream().map(CarServiceImpl::mapToResponse).collect(Collectors.toList())).page(page.getNumber()).totalPages(page.getTotalPages()).totalSize(page.getTotalElements()).size(page.getSize()).build();
    }
}
