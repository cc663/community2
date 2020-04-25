package com.cc.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{
    QUESTION_NULL_FOUND("你找到的问题不在了，要不要换个试试?");

    @Override
    public String getMessage() {
        return message;
    }

    private String message;

    CustomizeErrorCode(String message) {
        this.message = message;
    }
}
