package com.heisenberg.pan.server.modules.user.context;

import com.heisenberg.pan.server.modules.user.entity.RPanUser;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册业务实体对象
 */
@Data
public class UserRegiseterContext implements Serializable {

    private static final long serialVersionUID = 68390484031606610L;

    private String username;

    private String password;

    private String question;

    private String answer;

    private RPanUser entity;
}
