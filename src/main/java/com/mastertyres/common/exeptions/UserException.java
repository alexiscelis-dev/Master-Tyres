package com.mastertyres.common.exeptions;

public class UserException extends AppException{
    public UserException(String exception) {
        super(exception);
    }

    public UserException(String exception, Throwable cause) {
        super(exception, cause);
    }
}
