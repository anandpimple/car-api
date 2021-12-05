package com.test.car.api.dao;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.test.car.api.entity.Car;

public interface CarRepository extends PagingAndSortingRepository<Car, Long> {

    Optional<Car> getCarByBusinessId(final String businessId);
}
