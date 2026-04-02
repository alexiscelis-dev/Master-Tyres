package com.mastertyres.detalleCategoria.domain;

import com.mastertyres.common.exeptions.CategoriaException;
import com.mastertyres.common.exeptions.ModeloException;
import com.mastertyres.detalleCategoria.entity.DetalleCategoria;
import com.mastertyres.detalleCategoria.repository.DetalleCategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CategoriaValidator {
    @Autowired
    private DetalleCategoriaRepository detalleCategoriaRepository;

    public void guardarValidator(DetalleCategoria categoria){

        if (categoria == null || categoria.getCategoria().getCategoriaId() < 0){
            throw new CategoriaException("Debe seleccionar una categoría válida.");
        }
        if(modeloExiste(categoria)){
             throw new ModeloException("Modelo ya esta registrado en la misma marca");
        }

    }

    private boolean modeloExiste(DetalleCategoria detalles){
        Optional<DetalleCategoria> detalleCategoria = detalleCategoriaRepository.existeModelo(detalles);

        if (detalleCategoria.isPresent()){
           return true;
        }else {
           return false;
        }

    }//modeloExiste



}//class
