package com.db_ride_hailing_sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.db_ride_hailing_sys.service.IBelongService;
import com.db_ride_hailing_sys.dao.BelongDao;
import com.db_ride_hailing_sys.dao.VehicleDao;
import com.db_ride_hailing_sys.entity.Belong;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BelongServiceImpl extends ServiceImpl<BelongDao, Belong> implements IBelongService {
    @Resource
    private VehicleDao vehicleDao;


}
