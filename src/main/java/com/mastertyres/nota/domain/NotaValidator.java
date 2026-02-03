package com.mastertyres.nota.domain;

import com.mastertyres.common.exeptions.NotaException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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

        if (fechaVencimiento != null && !fechaVencimiento.trim().isEmpty()) {

            try {
                LocalDate fecha = LocalDate.parse(fechaVencimiento);
                if (fecha.isBefore(LocalDate.now()))
                    throw new NotaException("Fecha de vencmiento incorrecta. La fecha de vencimiento no puede ser anterior a la fecha actual.");

            } catch (DateTimeParseException e) {
                throw new NotaException("El formato de la fecha de vencimiento no es valido");

            }

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


}//class
