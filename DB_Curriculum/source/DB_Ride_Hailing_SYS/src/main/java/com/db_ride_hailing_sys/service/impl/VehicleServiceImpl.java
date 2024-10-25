package com.db_ride_hailing_sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.db_ride_hailing_sys.Util.UserUtil.UserHolder;
import com.db_ride_hailing_sys.dao.UserDao;
import com.db_ride_hailing_sys.entity.Belong;
import com.db_ride_hailing_sys.service.IBelongService;
import com.db_ride_hailing_sys.service.IVehicleService;
import com.db_ride_hailing_sys.dao.BelongDao;
import com.db_ride_hailing_sys.dao.DriverDao;
import com.db_ride_hailing_sys.dao.VehicleDao;
import com.db_ride_hailing_sys.dto.Result;
import com.db_ride_hailing_sys.dto.VehicleDTO;
import com.db_ride_hailing_sys.entity.Vehicle;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.db_ride_hailing_sys.constant.BusinessConstant.*;

@Service
public class VehicleServiceImpl extends ServiceImpl<VehicleDao, Vehicle> implements IVehicleService {

    @Resource
    private BelongDao belongDao;

    @Resource
    private VehicleDao vehicleDao;

    @Resource
    private DriverDao driverDao;

    @Resource
    private UserDao userDao;

    @Override
    public Result registerForVehicle(VehicleDTO vehicleDTO) {
        //伪造请求校验:是否为司机
        Long driverId = vehicleDTO.getId();
        Long role = userDao.queryRoleByUID(driverId);
        if(role!=Driver_Role){
            return Result.fail("当前用户不为司机类型，不能注册车辆");
        }
        //检查该账号是否已经注册过车辆
        Integer count = belongDao.queryCountByDriver(driverId);
        if(null==count||count>=Driver_Max_Vehicle){
            return Result.fail("一个司机至多注册一辆不同的车");
        }
        //检查该车辆是否已被注册过
        Integer belong = belongDao.queryVehicleBelongCount(vehicleDTO.getNumber());
        if(belong!=null&&belong>=Vehicle_Max_Driver){
            return Result.fail("一辆车最多有一个不同的司机注册");
        }
        //检查司机的驾照类型和车辆类型是否符合
        String type = vehicleDTO.getType();
        String license = driverDao.queryLicenseByUid(driverId);
        if(null==license){
            return Result.fail("该账号还没有注册驾照类型，请先前往个人中心注册");
        }
        if(!type.equals(license)&&!typeMatch.get(license).contains(type)){
            return Result.fail("该驾照类型无法匹配该车辆类型");
        }
        //检查注册车型是否是合法网约车类型
        if(!Valid_Type.contains(type)){
            return Result.fail("只接受C1或C2类驾照的车型注册");
        }
        //检查车牌号是否已经被注册过
        Integer exist = vehicleDao.queryHasExistVehicle(vehicleDTO.getNumber());
        if(exist!=null&&exist>0){
            return Result.fail("该车牌号已经注册过");
        }
        //为车辆注册
        if(!regCreateVehicle(vehicleDTO)){
            return Result.fail("注册车辆失败");
        }
        //注册属主关系
        boolean success = regCreateBelong(vehicleDTO.getNumber(), driverId);
        return success?Result.ok():Result.fail("注册属主失败");
    }

    private boolean regCreateVehicle(VehicleDTO vehicleDTO){
        String number = vehicleDTO.getNumber();
        String type = vehicleDTO.getType();
        String brand = vehicleDTO.getBrand();
        String color = vehicleDTO.getColor();
        Vehicle vehicle = new Vehicle();
        vehicle.setBrand(brand);
        vehicle.setType(type);
        vehicle.setColor(color);
        vehicle.setNumber(number);
        return save(vehicle);
    }

    private boolean regCreateBelong(String number,Long driver_id){
        Long vId = vehicleDao.queryVidByNumber(number);
        if(null==vId){
            return false;
        }
        Belong belong = new Belong();
        belong.setDriverId(driver_id);
        belong.setVehicleId(vId);
        belongDao.insert(belong);
        return true;
    }


}
