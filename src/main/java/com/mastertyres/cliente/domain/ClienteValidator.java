package com.mastertyres.cliente.domain;

import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.common.exeptions.ClienteException;

public class ClienteValidator {

    public void validarGuardar(Cliente cliente){
        if (cliente == null){
            throw new ClienteException("Error interno: No se pudo recuperar la informacion del cliente");
        }

    }

}//class
