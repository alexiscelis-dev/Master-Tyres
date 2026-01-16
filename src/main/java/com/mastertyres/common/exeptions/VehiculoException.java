package com.mastertyres.common.exeptions;

public class VehiculoException extends AppException{
    public VehiculoException(String exception) {
        super(exception);
    }

    public VehiculoException(String exception, Throwable cause) {
        super(exception, cause);
    }
}
