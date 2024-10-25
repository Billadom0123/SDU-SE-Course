package com.db_ride_hailing_sys.service;

import com.db_ride_hailing_sys.algorithm.DBSCAN.Exception.ARGS.DBSCAN_ARGS_EXCEPTION;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Exception.Entity.DBSCAN_POINT_EXCEPTION;
import com.db_ride_hailing_sys.dto.*;
import com.db_ride_hailing_sys.entity.Driver;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gilfoyle
 * @since 2023-07-22
 */
public interface IDriverService extends IService<Driver> {

    Result startBusiness(DriverDTO driverDTO) throws DBSCAN_ARGS_EXCEPTION, DBSCAN_POINT_EXCEPTION;

    Result registerLicense(LicenseDTO licenseDTO);

    Result endBusiness(Long id);

    Result confirmOrder(Long id, Long requestId, Boolean accept);

    Result commitOrder(CommitOrderDTO commitOrderDTO);

    Result deleteOrder(DeleteOrderDTO deleteOrderDTO);

    Result receivePassenger(Long id,Long reqId);

    Result arriveStart(Long id, Long reqId, Double curX, Double curY);

}
