package com.test.car.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.test.car.api.dao.CarRepository;
import com.test.car.api.entity.Car;
import com.test.car.api.exception.DataNotFoundException;
import com.test.car.api.model.CarResponse;
import com.test.car.api.model.CreateCarRequest;
import com.test.car.api.model.PageResponse;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {
    @Mock
    private CarRepository mockCarRepository;

    @Mock
    private CreateCarRequest carRequest;

    @Mock
    private Car savedEntity, car1, car2;

    @Mock
    private Page<Car> mockPage;

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

    @Test
    void givenCarWithRequestedIdNotFound_whenGetCarByBusinessId_thenException() {
        when(mockCarRepository.getCarByBusinessId("businessId")).thenReturn(Optional.empty());

        assertThatExceptionOfType(DataNotFoundException.class)
            .isThrownBy(() -> underTest.getCarByBusinessId("businessId"))
            .withMessage("Car not found with business id 'businessId'");
    }

    @Test
    void givenCarWithRequestedIdFound_whenGetCarByBusinessId_thenSuccess() {
        when(savedEntity.getColour()).thenReturn("Red");
        when(savedEntity.getMake()).thenReturn("Make");
        when(savedEntity.getModel()).thenReturn("Model");
        when(savedEntity.getYear()).thenReturn(1983);
        when(savedEntity.getBusinessId()).thenReturn("businessId");
        when(mockCarRepository.getCarByBusinessId("businessId")).thenReturn(Optional.of(savedEntity));

        CarResponse response = underTest.getCarByBusinessId("businessId");

        assertThat(response.getColour()).isEqualTo("Red");
        assertThat(response.getMake()).isEqualTo("Make");
        assertThat(response.getModel()).isEqualTo("Model");
        assertThat(response.getId()).isEqualTo("businessId");
        assertThat(response.getYear()).isEqualTo(1983);
    }

    @Test
    void givenEmptyCars_whenGetCars_thenSuccess() {
        when(mockCarRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        PageResponse response = underTest.getCars(10, 0);

        assertThat(response.getContent()).isEmpty();
        assertThat(response.getSize()).isZero();
        assertThat(response.getPage()).isZero();
        assertThat(response.getTotalPages()).isOne();
        assertThat(response.getTotalSize()).isZero();
    }

    @Test
    void givenSomeCars_whenGetCars_thenSuccess() {
        when(mockCarRepository.findAll(any(Pageable.class))).thenReturn(mockPage);
        when(mockPage.getContent()).thenReturn(List.of(car1, car2));
        when(car1.getBusinessId()).thenReturn("businessId1");
        when(car2.getBusinessId()).thenReturn("businessId2");
        PageResponse response = underTest.getCars(10, 0);

        assertThat(response.getContent()).hasSize(2);
        assertThat(((CarResponse) response.getContent().get(0)).getId()).isEqualTo("businessId1");
        assertThat(((CarResponse) response.getContent().get(1)).getId()).isEqualTo("businessId2");
    }
}