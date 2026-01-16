package com.mastertyres.common.exeptions;

public class ClienteException extends AppException{
    public ClienteException(String exception, Throwable cause) {
        super(exception, cause);
    }

    public ClienteException(String exception) {
        super(exception);
    }
}
