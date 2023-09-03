package com.heisenberg.pan.server.modules.user.po;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import java.io.Serializable;

@ApiModel(value = "密保答案校验")
@Data
public class CheckAnswerPO implements Serializable {


    private static final long serialVersionUID = -8300768563967248713L;
    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[0-9A-Za-z]{6,16}$",message = "请输入6-16位只包含数字与字母的用户名")
    private String username;

    @ApiModelProperty(value = "密保问题",required = true)
    @NotBlank(message = "密保不能为空")
    @Length(max = 100,message = "密保不能超过100字符")
    private String question;

    @ApiModelProperty(value = "密保答案",required = true)
    @NotBlank(message = "密保问题不能为空")
    @Length(max = 100,message = "密保答案不能超过100字符")
    private String answer;
}
