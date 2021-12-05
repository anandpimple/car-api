package com.test.car.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.test.car.api.dao.CarRepository;
import com.test.car.api.entity.Car;
import com.test.car.api.model.CarResponse;
import com.test.car.api.model.CreateCarRequest;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {
    @Mock
    private CarRepository mockCarRepository;

    @Mock
    private CreateCarRequest carRequest;

    @Mock
    private Car savedEntity;

    @Captor
    private ArgumentCaptor<Car> carArgumentCaptor;

    @InjectMocks
    private CarServiceImpl underTest;

    @Test
    void givenCar_whenAddCar_thenSuccess() {
        when(carRequest.getColour()).thenReturn("Red");
        when(carRequest.getMake()).thenReturn("Make");
        when(carRequest.getModel()).thenReturn("Model");
        when(carRequest.getYear()).thenReturn(1983);

        when(mockCarRepository.save(carArgumentCaptor.capture())).thenReturn(savedEntity);

        final CarResponse carResponse = underTest.addCar(carRequest);

        assertThat(carArgumentCaptor.getValue().getColour()).isEqualTo("Red");
        assertThat(carArgumentCaptor.getValue().getMake()).isEqualTo("Make");
        assertThat(carArgumentCaptor.getValue().getBusinessId()).isNotBlank();
        assertThat(carArgumentCaptor.getValue().getModel()).isEqualTo("Model");
        assertThat(carArgumentCaptor.getValue().getYear()).isEqualTo(1983);
        assertThat(carResponse).isNotNull();
    }
}