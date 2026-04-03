package com.mastertyres.common.utils;

import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FechaUtils {

    public static String getFechaFormateada(String fechaSinFormato) {

        DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatterOutput = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String fechaFormateada = LocalDate.parse(fechaSinFormato, formatterInput).format(formatterOutput);

        return fechaFormateada;

    }

    public static String getFechaFormateadaSegundos(String fechaSinFormato) {

        DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatterOutput = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String fechaFormateada = LocalDate.parse(fechaSinFormato, formatterInput).format(formatterOutput);

        return fechaFormateada;

    }


    public static String formatearFecha(String fecha) {
        if (fecha == null || fecha.isBlank()) return "N/D";

        try {
            LocalDate f = LocalDate.parse(fecha);  // yyyy-MM-dd
            return f.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (Exception e) {
            return "N/D";
        }
    }

    public static String formatearFechaHora(String fechaHora) {
        if (fechaHora == null || fechaHora.isBlank()) return "N/D";

        try {
            LocalDateTime f = LocalDateTime.parse(
                    fechaHora.replace(" ", "T")  // convierte yyyy-MM-dd HH:mm:ss → yyyy-MM-ddTHH:mm:ss
            );
            return f.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (Exception e) {
            return "N/D";
        }
    }

    public static String getFechaActual(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static void mostrarFechayHora(TextField txtDia, TextField txtMes, TextField txtAnio, TextField txtHoraEntrega) {
        LocalDate hoy = LocalDate.now();
        txtDia.setText(String.format("%02d", hoy.getDayOfMonth()));
        txtMes.setText(String.format("%02d", hoy.getMonthValue()));
        txtAnio.setText(String.valueOf(hoy.getYear()));

        LocalDateTime fechayHora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String hora = fechayHora.format(formatter);
        txtHoraEntrega.setText(hora);



    }//mostrarFecha



}//class
