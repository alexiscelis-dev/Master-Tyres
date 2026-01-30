package com.mastertyres.marca.domain;

import com.mastertyres.common.exeptions.MarcaException;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.marca.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MarcaValidator {


    @Autowired
    private MarcaRepository marcaRepository;

    private static final Integer MARCA_GENERICA = 1;

    public void validarEliminacion(Integer marcaId) {

        if (marcaId == null || marcaId == 0) {
            throw new MarcaException("Ocurrió un problema al seleccionar la marca. Intente nuevamente.");
        }

        if (marcaId == MARCA_GENERICA) {
            throw new MarcaException("No se puede eliminar la marca genérica");
        }

    }

    public void validarGuardar(Marca marca) {

        if (marca == null) {
            throw new MarcaException("No se pudo obtener la información de la marca. Intente nuevamente.");
        }

        if (marcaExiste(marca.getNombreMarca())) {
            throw new MarcaException("Ya existe una marca con el mismo nombre.");
        }

        if (marca.getNombreMarca() == null || marca.getNombreMarca().isEmpty()) {
            throw new MarcaException("El nombre de la marca no puede estar vacío.");
        }
        if (marca.getNombreMarca().length() > 40) {
            throw new MarcaException("El nombre de la marca no puede tener más de 40 caracteres.");
        }
    }

    private boolean marcaExiste(String nombreMarca) {

        nombreMarca = nombreMarca.trim().toUpperCase();

        Optional<Marca> marca = marcaRepository.findByNombreMarca(nombreMarca);

        if(marca.isPresent()){
            System.out.println("Si encontro la marca");
            return true;

        }

        else{
            return false;
        }

    }


}//class
