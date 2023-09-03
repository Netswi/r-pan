package com.heisenberg.pan.server.modules.user.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.heisenberg.pan.web.serializer.IdEncryptSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.io.Serializable;

@ApiModel(value = "用户基本信息实体")
@Data
public class UserInfoVO implements Serializable {


    private static final long serialVersionUID = 5575050155970486229L;

    @ApiModelProperty(value = "用户名称")
    private String username;
    @ApiModelProperty(value = "用户根目录加密ID")
    @JsonSerialize(using = IdEncryptSerializer.class)//long型传给前端会精度丢失
    private Long rootFileId;
    @ApiModelProperty(value = "用户根目录名称")
    private String rootFileName;
}
