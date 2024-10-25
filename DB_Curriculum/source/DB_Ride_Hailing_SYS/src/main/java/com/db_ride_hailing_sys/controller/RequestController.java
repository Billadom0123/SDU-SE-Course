package com.db_ride_hailing_sys.controller;


import com.db_ride_hailing_sys.EntityInfo.RequestInfo;
import com.db_ride_hailing_sys.dto.CancelRequestDTO;
import com.db_ride_hailing_sys.dto.RequestDTO;
import com.db_ride_hailing_sys.dto.Result;
import com.db_ride_hailing_sys.service.IRequestService;
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
@RequestMapping("/request")
public class RequestController {

    @Resource
    private IRequestService requestService;

    @PostMapping("customerRequest")
    public Result  customerRequest(@RequestBody RequestDTO requestDTO){
        return requestService.createCustomerRequest(requestDTO);
    }

    @PostMapping("cancelRequest")
    public Result cancelRequest(@RequestBody CancelRequestDTO cancelRequestDTO){
        return requestService.cancelRequest(cancelRequestDTO);
    }
}

