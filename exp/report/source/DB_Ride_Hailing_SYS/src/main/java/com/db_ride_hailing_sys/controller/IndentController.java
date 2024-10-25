package com.db_ride_hailing_sys.controller;


import com.db_ride_hailing_sys.dto.DeleteCompletedOrderDTO;
import com.db_ride_hailing_sys.dto.Result;
import com.db_ride_hailing_sys.service.IIndentService;
import com.db_ride_hailing_sys.service.impl.IndentServiceImpl;
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
@RequestMapping("/indent")
public class IndentController {

    @Resource
    private IIndentService iIndentService;

    @GetMapping("showOrder")
    public Result showOrder(@RequestParam("id")Long id,@RequestParam("role")Long role){
        return iIndentService.showOrder(id,role);
    }

    @PostMapping("deleteOrder")
    public Result deleteOrder(@RequestBody DeleteCompletedOrderDTO deleteCompletedOrderDTO){
        return iIndentService.deleterOrder(deleteCompletedOrderDTO);
    }

}

