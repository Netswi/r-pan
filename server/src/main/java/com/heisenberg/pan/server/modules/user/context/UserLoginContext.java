package com.heisenberg.pan.server.modules.user.context;

import com.heisenberg.pan.server.modules.user.entity.RPanUser;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录业务实体对象
 */
@Data
public class UserLoginContext implements Serializable {


    private static final long serialVersionUID = -6414926661811635108L;

    private String username;

    private String password;

    private RPanUser entity;
    /**
     * 登录成功凭证
     */
    private String accessToken;
}
