package com.mastertyres.modelo.domain;

import com.mastertyres.common.exeptions.ModeloException;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.modelo.repository.ModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModeloValidator {
    private static final Integer MODELO_GENERICO = 1;
    @Autowired
    private ModeloRepository modeloRepository;

    public void validarEliminar(Integer modeloId){

        if (modeloId == null || modeloId == 0){
            throw new ModeloException("Ocurrió un problema al seleccionar el modelo. Intente nuevamente.");
        }

        if (modeloId == MODELO_GENERICO ){
            throw new ModeloException("No se puede eliminar el modelo genérico");
        }

    }

    public void validarGuardar(Modelo modelo){

        if (modelo == null) {
            throw new ModeloException("No se pudo obtener la información del modelo. Intente nuevamente.");
        }
        if (modelo.getNombreModelo() == null || modelo.getNombreModelo().isBlank()) {
            throw new ModeloException("El nombre del modelo no puede estar vacío.");
        }
        if (modelo.getNombreModelo().length() > 30) {
            throw new ModeloException("El nombre del modelo no puede tener más de 30 caracteres.");
        }



    }




}//class
