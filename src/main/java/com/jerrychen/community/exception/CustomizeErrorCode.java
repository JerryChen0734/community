package com.jerrychen.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND(2001,"你找的问题不在了，你问问🐔脑壳是不是他吃掉了！"),
    TARGET_PARAM_WRONG(2002,"先选择一个正确的问题或者评论才能来黑下🐔脑壳！"),
    NO_LOGIN(2003,"骂🐔脑壳前先走下流程，登陆先。"),
    SYS_ERROR(2004,"🐔脑壳你不要搞事！"),
    COMMENT_NOT_FOUND(2005,"🐔脑壳偷偷的吃掉了你想要回复的内容！"),
    CONTENT_IS_EMPTY(2006,"你是不是对🐔脑壳无语了？"),
    READ_NOTIFICATION(2007,"🐔脑壳来不能传递给你信息了。"),
    PASSWORD_WRONG(2007,"密码要一样！！！。"),
    ;
    private String message;
    private Integer code;
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
    CustomizeErrorCode(Integer code, String message){
        this.code=code;
        this.message=message;}


}
