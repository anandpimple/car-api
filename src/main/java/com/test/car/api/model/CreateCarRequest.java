package com.test.car.api.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel("Model to create a car")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CreateCarRequest {

    @ApiModelProperty(value = "Name of the model", example = "X3", required = true)
    @NotBlank
    private final String model;

    @ApiModelProperty(value = "Name of the make", example = "BMW", required = true)
    @NotBlank
    private final String make;

    @ApiModelProperty(value = "Colour of the car", example = "RED", required = true)
    @NotBlank
    private final String colour;

    @ApiModelProperty(value = "Year of manufacturing", example = "RED")
    @Min(value = 1800)
    private final Integer year;
}
