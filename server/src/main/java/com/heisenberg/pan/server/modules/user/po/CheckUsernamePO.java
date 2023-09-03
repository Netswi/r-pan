package com.heisenberg.pan.server.modules.user.po;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@ApiModel(value = "用户忘记密码校验用户名参数")
@Data
public class CheckUsernamePO implements Serializable {

    private static final long serialVersionUID = 1895528109702191824L;

    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[0-9A-Za-z]{6,16}$",message = "请输入6-16位只包含数字与字母的用户名")
    private String username;
}
