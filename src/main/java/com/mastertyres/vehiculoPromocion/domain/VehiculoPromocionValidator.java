package com.mastertyres.vehiculoPromocion.domain;

import com.mastertyres.common.exeptions.PromocionException;
import com.mastertyres.vehiculoPromocion.model.VehiculoPromocion;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class VehiculoPromocionValidator {

    public void validarGuardar(VehiculoPromocion vehiculosCompatibles){
        if (vehiculosCompatibles == null){
            throw new PromocionException("Error interno: No se pudieron recuperar los vehiculos compatibles a la promocion");
        }
        // verifica que nnguna de las relaciones ni el año
        //No se verifica id promocion porque en el front se hace despues de que se inserto ademas de que el mismo metodo es usado para editar y
        //en editar se forza a ser null por lo que siempre tendria exepcion
        if (vehiculosCompatibles.getMarca() == null ||
                vehiculosCompatibles.getModelo() == null || vehiculosCompatibles.getAnnio() == null ){
            throw new PromocionException("Error interno: No se pudo recuperar los vehiculos compatibles a la promocion");
        }

        int anio = LocalDate.now().getYear() + 1;

        if (vehiculosCompatibles.getAnnio() < 0 || vehiculosCompatibles.getAnnio() > anio){
            throw new PromocionException("Año invalido");
        }
    }

}//class
