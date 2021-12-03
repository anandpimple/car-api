package com.test.car.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarApiController {
    @GetMapping(path = "/cars")
    public String getCars(){
        return "Cars";
    }
}
