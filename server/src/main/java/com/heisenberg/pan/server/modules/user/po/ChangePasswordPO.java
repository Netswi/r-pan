package com.heisenberg.pan.server.modules.user.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel(value = "用户在线修改密码参数")
public class ChangePasswordPO implements Serializable {
    private static final long serialVersionUID = -699465286714660916L;

    @NotBlank(message = "旧密码不为空")
    @ApiModelProperty(value = "旧密码",required = true)
    @Length(min = 8,max = 16,message = "请输出8-16位密码")
    private String oldpassword;

    @NotBlank(message = "新密码不为空")
    @ApiModelProperty(value = "新密码",required = true)
    @Length(min = 8,max = 16,message = "请输出8-16位密码")
    private String newPassword;


}
