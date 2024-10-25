package com.db_ride_hailing_sys.controller;


import com.db_ride_hailing_sys.algorithm.DBSCAN.Exception.ARGS.DBSCAN_ARGS_EXCEPTION;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Exception.Entity.DBSCAN_POINT_EXCEPTION;
import com.db_ride_hailing_sys.dto.*;
import com.db_ride_hailing_sys.service.IDriverService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gilfoyle
 * @since 2023-07-22
 */
@RestController
@RequestMapping("/driver")
public class DriverController {

    @Resource
    private IDriverService driverService;

    @PostMapping("startBusiness")
    public Result startBusiness(@RequestBody DriverDTO driverDTO) throws DBSCAN_ARGS_EXCEPTION, DBSCAN_POINT_EXCEPTION {
        return driverService.startBusiness(driverDTO);
    }

    @PostMapping("registerLicense")
    public Result registerLicense(@RequestBody LicenseDTO licenseDTO){
        return driverService.registerLicense(licenseDTO);
    }

    @GetMapping("endBusiness")
    public Result endBusiness(@RequestParam("id")Long id){
        return driverService.endBusiness(id);
    }

    @PostMapping("deleteOrder")
    public Result deleteOrder(@RequestBody DeleteOrderDTO deleteOrderDTO){
        return driverService.deleteOrder(deleteOrderDTO);
    }

    @GetMapping("confirmOrder")
    public Result confirmOrder(@RequestParam("id")Long id,@RequestParam("requestId")Long requestId,@RequestParam("accept")Boolean accept){
        return driverService.confirmOrder(id,requestId,accept);
    }

    @GetMapping("receivePassenger")
    public Result receivePassenger(@RequestParam("id")Long id,@RequestParam("reqId")Long reqId){
        return driverService.receivePassenger(id,reqId);
    }

    @PostMapping("commitOrder")
    public Result commitOrder(@RequestBody CommitOrderDTO commitOrderDTO){
        return driverService.commitOrder(commitOrderDTO);
    }

    @GetMapping("arriveStart")
    public Result arriveStart(@RequestParam("id")Long id,@RequestParam("reqId")Long reqId,@RequestParam("curX")Double curX,@RequestParam("curY")Double curY){
        return driverService.arriveStart(id,reqId,curX,curY);
    }
}

