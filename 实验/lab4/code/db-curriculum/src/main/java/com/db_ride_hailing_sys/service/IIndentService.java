package com.db_ride_hailing_sys.service;

import com.db_ride_hailing_sys.dto.DeleteCompletedOrderDTO;
import com.db_ride_hailing_sys.dto.Result;
import com.db_ride_hailing_sys.entity.Indent;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gilfoyle
 * @since 2023-07-22
 */
public interface IIndentService extends IService<Indent> {

    Result showOrder(Long id,Long role);

    Result deleterOrder(DeleteCompletedOrderDTO deleteCompletedOrderDTO);
}
