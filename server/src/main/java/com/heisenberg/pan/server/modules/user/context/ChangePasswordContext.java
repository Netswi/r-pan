package com.heisenberg.pan.server.modules.user.context;


import com.heisenberg.pan.server.modules.user.entity.RPanUser;
import lombok.Data;

import java.io.Serializable;

@Data
public class ChangePasswordContext implements Serializable {

    private static final long serialVersionUID = -6820035530819055280L;

    private String oldPassword;

    private String newPassword;

    private Long userId;
    /**
     * 当前登录用户的实体信息
     */
    private RPanUser entity;
}
