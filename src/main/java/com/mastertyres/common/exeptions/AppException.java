package com.mastertyres.common.exeptions;

//Clase para manejo de excepciones
//Se lanzan excepciones a proposito a metodos como guardar inventario en dado caso de que no se guarde
//Lanzar una excepcion significa que no se guardo en BD el objeto

public class AppException extends RuntimeException{

    public AppException(String exception){
        super(exception);
    }

    public AppException(String exception, Throwable cause ) {
        super(exception,cause);
    }

}//clase
