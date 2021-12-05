package com.test.car.api.model;


import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel("Details about car")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CarResponse {
    @ApiModelProperty(value = "Identifier for the car. Used to retrieve specific car in system", example = "CA1")
    private final String id;
    @ApiModelProperty(value = "Name of the model", example = "X3")
    private final String model;
    @ApiModelProperty(value = "Name of the make", example = "BMW")
    private final String make;
    @ApiModelProperty(value = "Colour of the car", example = "RED")
    private final String colour;
    @ApiModelProperty(value = "Year of manufacturing", example = "1983")
    private final Integer year;
}
