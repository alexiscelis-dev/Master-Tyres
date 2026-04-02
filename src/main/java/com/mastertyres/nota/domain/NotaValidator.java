package com.mastertyres.nota.domain;

import com.mastertyres.common.exeptions.NotaException;
import com.mastertyres.nota.entity.Nota;
import com.mastertyres.nota.entity.StatusSiNo;
import com.mastertyres.notaClienteDetalle.entity.NotaClienteDetalle;
import com.mastertyres.notaDetalle.entity.NotaDetalle;
import org.springframework.stereotype.Component;

@Component
public class NotaValidator {

    public void validarStatusPagos(Integer notaId) {
        if (notaId == null) {
            throw new NotaException("Error interno: No se pudo obtener la nota");
        }

        if (notaId <= 0) {
            throw new NotaException("Error interno: Se se esta asignando un identificador invalido");
        }


    }

    public void validarAdeudo(float adeudo, String fechaVencimiento, Integer notaId) {

        if (notaId == null) {
            throw new NotaException("Error interno: No se pudo obtener la nota");
        }

        if (fechaVencimiento == null || fechaVencimiento.trim().isEmpty()) {
            throw new NotaException("No se pudo obtener la fecha de vencimiento");
        }

        if (adeudo < 0) {
            throw new NotaException("Monto incorrecto: Adeudo no puede ser una cantidad negativa");
        }


    }//validar adeudo

    public void validadUpdatedAt(Integer notaId) {
        if (notaId == null) {
            throw new NotaException("Error interno: No se pudo obtener el identificador de la nota");
        }
        if (notaId <= 0) {
            throw new NotaException("Error interno: Se se esta asignando un identificador invalido");
        }
    }

    public void validarSaldoFavor(Integer notaId) {

        if (notaId == null) {
            throw new NotaException("Error interno: No se pudo obtener la nota");
        }

        if (notaId <= 0) {
            throw new NotaException("Error interno: Se se esta asignando un identificador invalido");
        }
    }

    public void validarAgregarNota(Nota nota, NotaDetalle notaDetalle, NotaClienteDetalle clienteDetalle) {

        //nota

        if (nota == null || notaDetalle == null || clienteDetalle == null) {
            throw new NotaException("Error interno: No se pudo obtener la informacion de la nota.");
        }

        if (nota.getNumNota() == null || nota.getNumNota().trim().isEmpty()) {
            throw new NotaException("El campo 'Numero de nota' no puede estar vacio.");
        }

        if (nota.getTotal() == null) {
            throw new NotaException("El campo 'Total' no puede estar vacio.");
        }

        if (nota.getTotal() < 1) {
            throw new NotaException("Monto incorrecto.");
        }

        if (nota.getFechaYhora() == null || nota.getFechaYhora().trim().isEmpty()) {
            throw new NotaException("Error interno: No se pudo obtener la fecha y hora de la nota.");

        }

        if (nota.getStatusNota() == null || nota.getStatusNota().trim().isEmpty()) {
            throw new NotaException("Error interno: Ocurrio un problema al obtener estado de la nota.");
        }


        validarLongitud(nota.getNumFactura(), 32, "Excedio el limite de caracteres permitidos para el campo 'Numero de nota'. ");

        validarLongitud(nota.getNumNota(), 8, "Excedio el limite de caracteres permitidos para el campo 'Numero de Nota'. ");


        //nota detalles

        // SI este falla es porque esta fallando el metodo de notaUtils que sirve para cambiar de renglon cuando se llena (Nunca debe de fallar)

        validarLongitud(notaDetalle.getObservaciones(), 120, "Error interno: Ocurrio un problema al guardar las Observaciones. ");

        validarLongitud(notaDetalle.getObservaciones2(), 120, "Excedio el limite de caracteres permitidos para el campo 'Observaciones renglon 2'. ");


        enumValido(notaDetalle.getRayones());
        enumValido(notaDetalle.getGolpes());
        enumValido(notaDetalle.getTapones());
        enumValido(notaDetalle.getTapetes());
        enumValido(notaDetalle.getRadio());
        enumValido(notaDetalle.getGato());
        enumValido(notaDetalle.getLlanta());
        enumValido(notaDetalle.getLlave());

        validarLongitud(notaDetalle.getAlineacion(), 56, "Excedio el limite de caracteres permitidos para el campo 'Alineacion'. ");

        validarLongitud(notaDetalle.getBalanceo(), 56, "Excedio el limite de caracteres permitidos para el campo 'Balanceo'. ");

        validarLongitud(notaDetalle.getAmorDelanteros(), 90, "Excedio el limite de caracteres permitidos para el campo 'Amortiguadores Delanteros'. ");

        validarLongitud(notaDetalle.getAmorTraseros(), 90, "Excedio el limite de caracteres permitidos para el campo 'Amortiguadores Traseros'. ");

        validarLongitud(notaDetalle.getSuspension(), 90, "Excedio el limite de caracteres permitidos para el campo 'Suspension'. ");

        validarLongitud(notaDetalle.getSuspension2(), 90, "Excedio el limite de caracteres permitidos para el campo 'Suspension renglon 2'. ");

        validarLongitud(notaDetalle.getMecanica(), 90, "Excedio el limite de caracteres permitidos para el campo 'Mecanica'. ");

        validarLongitud(notaDetalle.getMecanica2(), 90, "Excedio el limite de caracteres permitidos para el campo 'Mecanica renglon 2'. ");

        validarLongitud(notaDetalle.getFrenos(), 90, "Excedio el limite de caracteres permitidos para el campo 'Frenos'. ");

        validarLongitud(notaDetalle.getFrenos2(), 90, "Excedio el limite de caracteres permitidos para el campo 'Frenos renglon 2'. ");


        validarLongitud(notaDetalle.getOtros(), 90, "Excedio el limite de caracteres permitidos para el campo 'Otros'. ");

        validarLongitud(notaDetalle.getOtros2(), 90, "Excedio el limite de caracteres permitidos para el campo 'Otros renglon 2'. ");

        validarLongitud(notaDetalle.getLlantaCampo(), 90, "Excedio el limite de caracteres permitidos para el campo 'Llantas'.");


        //Nota cliente detalle

        validarLongitud(clienteDetalle.getDireccion1Nota(), 100, "Excedio el limite de caracteres permitidos para el campo 'Direccion 1'. ");

        validarLongitud(clienteDetalle.getDireccion2Nota(), 100, "Excedio el limite de caracteres permitidos para el campo 'Direccion renglon 2'. ");

        validarLongitud(clienteDetalle.getRfcNota(), 13, "Excedio el limite de caracteres permitidos para el campo 'RFC'. ");

        validarLongitud(clienteDetalle.getCorreoNota(), 320, "Excedio el limite de caracteres permitidos para el campo 'Correo'. ");

        validarLongitud(clienteDetalle.getMarcaNota(), 40, "Excedio el limite de caracteres permitidos para el campo 'Marca'. ");

        validarLongitud(clienteDetalle.getModeloNota(), 40, "Excedio el limite de caracteres permitidos para el campo 'Modelo'. ");

        validarLongitud(clienteDetalle.getPlacasNota(), 15, "Excedio el limite de caracteres permitidos para el campo 'Placas'. ");


        if (clienteDetalle.getMarcaNota() == null || clienteDetalle.getMarcaNota().trim().isEmpty()) {
            throw new NotaException("Error interno: No se pudo obtener la marca de vehiculo del cliente.");
        }

        if (clienteDetalle.getModeloNota() == null || clienteDetalle.getModeloNota().trim().isEmpty()) {
            throw new NotaException("Error interno: No se pudo obtener el modelo de vehiculo del cliente");
        }

        if (clienteDetalle.getAnioNota() == null) {
            throw new NotaException("Error interno: No se pudo obtener el año de vehiculo del cliente");
        }


        if (clienteDetalle.getKilometrosNota() < 0 || clienteDetalle.getKilometrosNota() > 999999) {
            throw new NotaException("Kilometros incorrectos, el rango de kilometros debe estar entre 0 y 999,999");
        }


    }//validarAgregarNota

    public void enumValido(String en) {
        System.out.println(en);

        if (en == null) {
            throw new NotaException("Error interno: Ocurrio un error al guardar el estado de los checkbox.");
        }

        if (!en.equals(StatusSiNo.SI.toString()) && !en.equals(StatusSiNo.NO.toString()) && !en.equals(StatusSiNo.DESELECCIONADO.toString())) {
            throw new NotaException("Error interno: Ocurrio un error al guardar el estado de los checkbox. ");
        }
    }

    private void validarLongitud(String valor, int max, String exception) {

        if (valor != null && valor.length() > max) {
            throw new NotaException(exception);
        }
    }


}//class
