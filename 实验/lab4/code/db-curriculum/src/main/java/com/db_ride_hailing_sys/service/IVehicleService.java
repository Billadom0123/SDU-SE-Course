package com.db_ride_hailing_sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.db_ride_hailing_sys.dto.Result;
import com.db_ride_hailing_sys.dto.VehicleDTO;
import com.db_ride_hailing_sys.entity.Vehicle;

public interface IVehicleService extends IService<Vehicle> {
    Result registerForVehicle(VehicleDTO vehicleDTO);
}
