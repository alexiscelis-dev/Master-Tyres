package com.mastertyres.vehiculo.domain;

import com.mastertyres.common.exeptions.VehiculoException;
import com.mastertyres.vehiculo.model.Vehiculo;
import com.mastertyres.vehiculo.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VehiculoValidator {
    @Autowired
    private VehiculoRepository vehiculoRepository;

    public void validarGuardar(List<Vehiculo> vehiculos) {

        if (vehiculos == null) {
            throw new VehiculoException("Error interno: no se pudo recuperar la información de los vehículos.");
        }


        for (Vehiculo v : vehiculos) {
            String placas = v.getPlacas();
            String numSerie = v.getNumSerie();

            if (placas != null && !placas.isBlank()) {
                if (vehiculoRepository.existeVehiculoPorPlacas(placas)) {

                    throw new VehiculoException("Vehiculo duplicado: Ya existe un vehiculo registrado con las mismas placas.");

                }
            }

            if (numSerie != null && !numSerie.isBlank()) {
                if (vehiculoRepository.existeVehiculoPorNumeroSerie(numSerie)) {
                    throw new VehiculoException("Vehiculo duplicado: Ya existe un vehiculo registrado con el mismo numero de serie.");

                }
            }

        }//for


    }

    public void validarActualizar(Vehiculo vehiculo) {

        if (vehiculo == null) {
            throw new VehiculoException("Error interno: no se pudo recuperar la información del vehiculo.");
        }

        if (vehiculo.getVehiculoId() == null || vehiculo.getMarca() == null
                || vehiculo.getModelo() == null || vehiculo.getCategoria() == null){
            throw new VehiculoException("Error interno: no se pudo recuperar la información del vehiculo.");
        }

        if (vehiculo.getAnio() == null){
            throw new VehiculoException("El año del vehiculo e un campo obligatorio.");
        }
        if (vehiculo.getAnio() < 0){
            throw new VehiculoException("Valor no permitido");

        }

        if (vehiculo.getKilometros() < 0 || vehiculo.getKilometros() > 999999){
            throw new VehiculoException("Rango de kilometros no permitido, el rango de kilometros permitido es de 0 a 999,999");
        }

        if (vehiculo.getColor() == null || vehiculo.getColor().isBlank()){
            throw new VehiculoException("El color del vehiculo es un campo obligatorio.");
        }

        if (vehiculo.getColor().length() > 20){
            throw new VehiculoException("Excedio el limite de caracteres para el campo Color.");
        }

        if (vehiculo.getPlacas().length() > 15){
            throw new VehiculoException("Las placas no pueden tener un valor mayor a 15 caracteres. Verifique y vuelva a intentarlo.");

        }

        if (vehiculo.getNumSerie().length() > 17){
            throw new VehiculoException("El numero de serie no puede ser mayor a 17 caracteres. Verifique y vuelva a intentarlo.");
        }

    }


}//class
