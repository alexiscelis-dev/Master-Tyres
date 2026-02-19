package com.mastertyres.common.exeptions;

public class RespaldoException extends AppException {
    public RespaldoException(String exception) {
        super(exception);
    }

    public RespaldoException(String exception, Throwable cause) {
        super(exception, cause);
    }
}
