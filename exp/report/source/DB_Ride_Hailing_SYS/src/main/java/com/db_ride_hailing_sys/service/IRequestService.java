package com.db_ride_hailing_sys.service;

import com.db_ride_hailing_sys.EntityInfo.RequestInfo;
import com.db_ride_hailing_sys.dto.CancelRequestDTO;
import com.db_ride_hailing_sys.dto.RequestDTO;
import com.db_ride_hailing_sys.dto.Result;
import com.db_ride_hailing_sys.entity.Request;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gilfoyle
 * @since 2023-07-22
 */
public interface IRequestService extends IService<Request> {

    Result createCustomerRequest(RequestDTO requestDTO);

    Result cancelRequest(CancelRequestDTO cancelRequestDTO);
}
