package com.heisenberg.pan.server.modules.user.context;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class ResetPasswordContext implements Serializable {

    private static final long serialVersionUID = -1161700196851036701L;


    private String username;


    private String password;


    private String token;

}
