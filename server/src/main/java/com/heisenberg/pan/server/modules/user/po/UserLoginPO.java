package com.heisenberg.pan.server.modules.user.po;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 用户登录参数实体对象
 */
@Data
@ApiModel(value = "用户登录参数")
public class UserLoginPO implements Serializable {


    private static final long serialVersionUID = -6074800354225886992L;

    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名",required = true)
    @Pattern(regexp = "^[0-9A-Za-z]{6,16}$",message = "请输入6-16位只包含数字与字母的用户名")
    private String username;

    @NotBlank(message = "密码不为空")
    @ApiModelProperty(value = "密码",required = true)
    @Length(min = 8,max = 16,message = "请输出8-16位密码")
    private String password;


}
