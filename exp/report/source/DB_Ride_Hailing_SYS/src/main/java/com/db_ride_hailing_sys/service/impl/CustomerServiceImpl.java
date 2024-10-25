package com.db_ride_hailing_sys.service.impl;

import com.db_ride_hailing_sys.service.ICustomerService;
import com.db_ride_hailing_sys.entity.Customer;
import com.db_ride_hailing_sys.dao.CustomerDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gilfoyle
 * @since 2023-07-22
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerDao, Customer> implements ICustomerService {

}
