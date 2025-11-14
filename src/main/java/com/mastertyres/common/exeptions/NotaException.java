package com.mastertyres.common.exeptions;

public class NotaException extends AppException{
    public NotaException(String exception) {
        super(exception);
    }

    public NotaException(String exception, Throwable cause) {
        super(exception, cause);
    }
}
