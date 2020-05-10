package com.cc.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{
    QUESTION_NULL_FOUND(2001,"你找到的问题不在了，要不要换个试试?"),
    TARGET_PARAM_NULL_FOUND(2002,"未选中任何问题或评论进行回复"),
    NO_LOGIN(2003, "请先登录，再进行评论！"),
    SYS_ERROR(2004, "服务过热，请稍后再试！"),
    COM_TYPE_NOT_FOUND(2005, "评论类型不存在！" ),
    COM_NOT_FOUND(2006, "评论不在了啊！" ),
    QUES_NOT_FOUND(2007,"问题不在了啊！" ),
    CONTENT_IS_EMPTY(2008,"评论为空" ),
    QUES_COULD_NOT_UPDATE(2009,"您没有权利修改哦！" );

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    private String message;
    private Integer code;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

}
