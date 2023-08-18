package com.heisenberg.pan.server.modules.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heisenberg.pan.server.modules.user.entity.RPanUser;
import com.heisenberg.pan.server.modules.user.service.RPanUserService;
import com.heisenberg.pan.server.modules.user.mapper.RPanUserMapper;
import org.springframework.stereotype.Service;

/**
* @author 2Bug
* @description 针对表【r_pan_user(用户信息表)】的数据库操作Service实现
* @createDate 2023-08-17 15:36:18
*/
@Service
public class RPanUserServiceImpl extends ServiceImpl<RPanUserMapper, RPanUser>
    implements RPanUserService{

}




