package com.cc.community.exception;

public class CustomizeException extends RuntimeException{

    private String message;
    private Integer code;

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public CustomizeException(ICustomizeErrorCode errorCode){
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
    }

}
