package com.heisenberg.pan.server.modules.user.context;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;


@Data
public class CheckUsernameContext implements Serializable {


    private static final long serialVersionUID = -7805822942948667638L;

    private String username;
}
