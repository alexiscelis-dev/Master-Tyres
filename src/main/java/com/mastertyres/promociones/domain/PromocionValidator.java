package com.mastertyres.promociones.domain;

import com.mastertyres.common.exeptions.PromocionExcepcion;
import com.mastertyres.promociones.model.Promocion;
import org.springframework.stereotype.Component;

@Component
public class PromocionValidator {

    public void validarGuardar(Promocion promocion){

        if (promocion == null   ){
            throw new PromocionExcepcion("Error interno: No se pudo recuperar la promocion creada");
        }

        if (promocion.getNombre() == null || promocion.getNombre().isEmpty()){
            throw new PromocionExcepcion("El nombre de la promocion no puede estar vacio");
        }

        if(promocion.getNombre().length() > 50){
            throw new PromocionExcepcion("El nombre de la promocion no puede tener mas de 50 caracteres");
        }

        if (promocion.getDescripcion() == null || promocion.getDescripcion().isEmpty()){
            throw new PromocionExcepcion("La descripcion de la promocion no puede estar vacia");
        }

        if (promocion.getTipoDescuento() == null || promocion.getTipoDescuento().isEmpty()){
            throw new PromocionExcepcion("El tipo de descuento es un campo requerido");
        }


        if (promocion.getPorcentaje() < 0 || promocion.getPorcentaje() > 100){
            throw new PromocionExcepcion("El porcentaje ingresado no es válido. Debe ser un valor entre 0 y 100.");
        }

        if (promocion.getPrecio() < 0){
            throw new PromocionExcepcion("El precio no puede ser un valor negativo");
        }

        if (promocion.getFechaInicio() == null  || promocion.getFechaInicio().isBlank()){
            throw new PromocionExcepcion("La fecha de inicio es un campo requerido");
        }

        if (promocion.getFechaFin() == null  || promocion.getFechaFin().isBlank()){
            throw new PromocionExcepcion("La fecha de fin es un campo requerido");
        }


    }//validarGuardar

    public void validarEliminar(Promocion promocion){
        if(promocion == null){
            throw new PromocionExcepcion("Error interno: No se pudo recuperar la promocion a eliminar");
        }

        if (promocion.getPromocionId() == null){
            throw new PromocionExcepcion("Error interno: No se pudo recuperar el id de la promocion a eliminar");
        }

        if (promocion.getPromocionId() < 0){
            throw new PromocionExcepcion("Id invalido");
        }

    }

}//class
