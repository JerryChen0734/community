package com.jerrychen.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND("你找的问题不在了，你问问鸡脑壳是不是他吃掉了！");

    private String message;
    @Override
    public String getMessage() {
        return message;
    }
    CustomizeErrorCode(String message){this.message=message;}


}
