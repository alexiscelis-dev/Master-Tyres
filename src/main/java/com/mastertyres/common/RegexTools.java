package com.mastertyres.common;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.regex.Pattern;

public class RegexTools {

    //acepta enteros y dos decimales
    public static void aplicarNumerosDecimal(TextField textField) {
        Pattern pattern = Pattern.compile("\\d*(\\.\\d{0,2})?");

        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty()) {
                return change;
            }

            if (pattern.matcher(newText).matches()) {
                return change;
            }

            return null;
        });

        textField.setTextFormatter(formatter);
    }

    public static void aplicarNumerosDecimalNota(TextField textField) {

        Pattern pattern = Pattern.compile("^$|^\\$?\\s*\\d*(?:[.,]\\d*)?$");

        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty()) {
                return change;
            }

            if (pattern.matcher(newText).matches()) {
                return change;
            }

            return null;
        });

        textField.setTextFormatter(formatter);

    }


    //acepta solo enteros y vacio
    public static void aplicarNumeroEntero(TextField textField) {


        Pattern pattern = Pattern.compile("\\d*"); // solo números enteros

        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty()) {
                return change;
            }

            if (pattern.matcher(newText).matches()) {
                return change;
            }

            return null;
        });

        textField.setTextFormatter(formatter);

    }

    //formato hora 24 horas
    public static void aplicar24Horas(TextField textField) {
        Pattern pattern = Pattern.compile("([01]\\d|2[0-3]):[0-5]\\d");

        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String nuevo = change.getControlNewText();

            if (nuevo.isEmpty()) return change; // permite borrar
            return pattern.matcher(nuevo).matches() ? change : null;
        });

        textField.setTextFormatter(formatter);
    }


    //acepta dos enteros 11,22 se usa para la fecha

    public static void aplicar2Enteros(TextField textField) {
        Pattern pattern = Pattern.compile("\\d{0,2}");

        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty()) {
                return change;
            }

            if (pattern.matcher(newText).matches()) {
                return change;
            }

            return null;
        });

        textField.setTextFormatter(formatter);
    }


    //acepta dos enteros entre 0 y 6
    public static void aplicar6Enteros(TextField textField) {
        Pattern pattern = Pattern.compile("\\d{0,6}");

        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty()) {
                return change;
            }

            if (pattern.matcher(newText).matches()) {
                return change;
            }

            return null;
        });

        textField.setTextFormatter(formatter);

    }

    //acepta de 0 a 4 enteros se usa para validar año
    public static void aplicar4Enteros(TextField textField) {
        Pattern pattern = Pattern.compile("\\d{0,4}");

        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty()) {
                return change;
            }

            if (pattern.matcher(newText).matches()) {
                return change;
            }

            return null;
        });

        textField.setTextFormatter(formatter);

    }



}//class
