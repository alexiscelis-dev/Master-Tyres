package com.mastertyres.common;

import javafx.scene.control.TextField;

public class RegexTools {

    //acepta enteros y dos decimales
    public static void aplicarNumerosDecimal(TextField textField){
        textField.textProperty().addListener((observable, oldText, newText) -> {
            if (!newText.matches("\\d*(\\.\\d{0,2})?")){
                textField.setText(oldText);
            }
        });

    }
    //acepta solo enteros y vacio
    public static void aplicarNumeroEntero(TextField textField){
        textField.textProperty().addListener((observable, oldText, newText) -> {
            if (!newText.matches("\\d*")){
                textField.setText(oldText);
            }
        });
    }
//formato hora 24 horas
    public static void aplicar24Horas(TextField textField){
        textField.textProperty().addListener((observable, oldText, newText) -> {
            if (!newText.matches("^(?:[01]\\d|2[0-3]):[0-5]\\d$")){
                textField.setText(oldText);
            }
        });
    }

//acepta dos enteros 11,22 se usa para la fecha
    public static void aplicar2Enteros(TextField textField){
        textField.textProperty().addListener((observable, oldText, newText) -> {
            if (!newText.matches("\\d{0,2}")){
                textField.setText(oldText);
            }
        });
    }


    //acepta dos enteros entre 0 y 6
    public static void aplicar6Enteros(TextField textField){
        textField.textProperty().addListener((observable, oldText, newText) -> {
            if (!newText.matches("\\d{0,6}")){
                textField.setText(oldText);
            }
        });
    }

    //acepta de 0 a 4 enteros se usa para validar año
    public static void aplicar4Enteros(TextField textField){
        textField.textProperty().addListener((observable, oldText, newText) -> {
            if (!newText.matches("\\d{0,4}")){
                textField.setText(oldText);
            }
        });
    }



}//class
