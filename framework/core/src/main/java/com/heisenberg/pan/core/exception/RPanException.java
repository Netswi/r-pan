package com.heisenberg.pan.core.exception;

import com.heisenberg.pan.core.response.ResponseCode;
import lombok.Data;

/**
 *  自定义全局异常处理
 */
@Data
public class RPanException extends RuntimeException{
    private Integer code;

    private String message;

    public RPanException(ResponseCode responseCode){
        this.code = responseCode.getCode();
        this.message = responseCode.getDesc();
    }

    public RPanException(Integer code,String message){
        this.code = code;
        this.message = message;
    }

    public RPanException(String message){
        this.code = ResponseCode.ERROR_PARAM.getCode();
        this.message = message;
    }

    public RPanException(){
        this.code = ResponseCode.ERROR_PARAM.getCode();
        this.message = ResponseCode.ERROR_PARAM.getDesc();
    }
}
