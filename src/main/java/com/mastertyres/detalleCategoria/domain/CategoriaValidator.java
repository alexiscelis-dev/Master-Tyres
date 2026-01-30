package com.mastertyres.detalleCategoria.domain;

import com.mastertyres.common.exeptions.CategoriaException;
import com.mastertyres.detalleCategoria.model.DetalleCategoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaValidator {

    public void guardarValidator(DetalleCategoria categoria){

        if (categoria == null || categoria.getCategoria().getCategoriaId() < 0){
            throw new CategoriaException("Debe seleccionar una categoría válida.");
        }

    }

}//class
