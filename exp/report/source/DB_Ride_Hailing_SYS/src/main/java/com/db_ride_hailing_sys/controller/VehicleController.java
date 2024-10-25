package com.db_ride_hailing_sys.controller;

import com.db_ride_hailing_sys.dto.Result;
import com.db_ride_hailing_sys.dto.VehicleDTO;
import com.db_ride_hailing_sys.service.IVehicleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {
    @Resource
    private IVehicleService vehicleService;

    @PostMapping("/register")
    public Result registerForVehicle(@RequestBody VehicleDTO vehicleDTO){
        return vehicleService.registerForVehicle(vehicleDTO);
    }
}
