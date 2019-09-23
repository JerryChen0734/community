package com.jerrychen.community.exception;

import io.swagger.models.auth.In;

public interface ICustomizeErrorCode {
    String getMessage() ;
    Integer getCode();
}

