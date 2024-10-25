package com.db_ride_hailing_sys.service.impl;

import com.db_ride_hailing_sys.service.IRoleService;
import com.db_ride_hailing_sys.entity.Role;
import com.db_ride_hailing_sys.dao.RoleDao;
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
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements IRoleService {

}
