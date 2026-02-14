package com.mastertyres.inventario.domain;

import com.mastertyres.common.exeptions.InventarioException;
import com.mastertyres.inventario.model.Inventario;
import org.springframework.stereotype.Component;

@Component
public class InventarioValidator {

    public void validarGuardar(Inventario llanta) {

         String excedio = "Excedio el limite de caracteres para el campo ";

        if (llanta == null) {
            throw new InventarioException("Error interno: No se pudo obtener la informacion de la llanta. ");
        }

        if (llanta.getIdentificadorLlanta() == null || llanta.getIdentificadorLlanta().isEmpty()) {
            throw new InventarioException("Error interno: No se encontro el identificador de llanta.");

        }

        if ( llanta.getMarca() == null || llanta.getMarca().isEmpty()) {

            throw  new InventarioException("El campo 'Marca' es un campo obligatorio. ");
        }

        if (llanta.getModelo() == null){
            throw  new InventarioException("El campo 'Modelo' es un campo obligatorio. ");
        }

        if (llanta.getMedida() == null || llanta.getMedida().isEmpty()){
            throw  new InventarioException("La 'Medida' de la llanta son campos obligatorios. ");
        }

        if (llanta.getStock() == null){
            throw new InventarioException("El campo 'Stock' es un campo obligatorio. ");
        }

        if (llanta.getPrecioVenta() == null){
            throw new InventarioException("El campo 'Precio de compra' es un campo obligatorio. ");
        }

        if (llanta.getPrecioVenta() == null){
            throw new InventarioException("El campo 'Precio de venta' es un campo obligatorio. ");
        }

        verificarLongitud(llanta.getIdentificadorLlanta(), 105, "EL identificador de la llanta es un campo generado auntomaticamente y no puede ser modificado de su longitud.");

        verificarLongitud(llanta.getMarca(), 30, excedio + "'Marca'. ");

        verificarLongitud(llanta.getModelo(), 50, excedio + "'Modelo'. ");

        verificarLongitud(llanta.getMedida(), 20, "Se tienen problemas al guardar la medida de la llanta. ");

        verificarLongitud(llanta.getIndiceCarga(), 3, "El indice de carga supero el numero de caracteres permitidos. ");

        verificarLongitud(llanta.getIndiceVelocidad(), 2, "El indice de velocidad supero el numero de caracteres permitidos. ");

        verificarLongitud(llanta.getDot(), 13, excedio + "'Dot' ");

        verificarLongitud(llanta.getCodigoBarras(), 13, excedio + "'Codigo de barras'. ");

        verificarLongitud(llanta.getImagen(), 1024, "La ruta del archivo es demasiado grande para ser almacenada. Intente guardarla en otra ubicacion. ");



    }

    private void verificarLongitud(String inventarioCampo, int longitud, String exception) {
        if (inventarioCampo != null && inventarioCampo.length() > longitud) {
            throw new InventarioException(exception);
        }
    }//verificarLongitud

}//class
