package com.heisenberg.pan.server.modules.user.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 注册用户参数实体对象
 */
@Data
@ApiModel(value = "用户注册参数")
public class UserRegisterPO implements Serializable {

    private static final long serialVersionUID = -5521427813609988931L;

    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名",required = true)
    @Pattern(regexp = "^[0-9A-Za-z]{6,16}$",message = "请输入6-16位只包含数字与字母的用户名")
    private String username;

    @NotBlank(message = "密码不为空")
    @ApiModelProperty(value = "密码",required = true)
    @Length(min = 8,max = 16,message = "请输出8-16位密码")
    private String password;

    @ApiModelProperty(value = "密保问题",required = true)
    @NotBlank(message = "密保不能为空")
    @Length(max = 100,message = "密保不能超过100字符")
    private String question;

    @ApiModelProperty(value = "密保答案",required = true)
    @NotBlank(message = "密保问题不能为空")
    @Length(max = 100,message = "密保答案不能超过100字符")
    private String answer;
}
