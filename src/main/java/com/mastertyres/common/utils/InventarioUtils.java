package com.mastertyres.common.utils;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class InventarioUtils {

    public static void indicesChoiceBox(ChoiceBox<String> choiceIvelocidad, ChoiceBox<String> choiceIcarga) {
        String[] indicesVelocidad = {
                "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8",
                "B", "C", "D", "E", "F", "G",
                "J", "K", "L", "M", "N",
                "P", "Q", "R", "S", "T",
                "U", "H", "V", "W", "Y",
                "(Y)", "ZR"
        };
        for (String indice : indicesVelocidad)
            choiceIvelocidad.getItems().addAll(indice);


        for (int i = 60; i <= 138; i++)
            choiceIcarga.getItems().addAll(i + "");


    }//indicesChoiceBox

    public static void indicesChoiceBox(ChoiceBox<String> choiceIvelocidad, ChoiceBox<String> choiceIcarga,
                                  ChoiceBox<String> choiceAncho,ChoiceBox<String> choicePerfil,ChoiceBox<String> choiceRin) {
        String[] indicesVelocidad = {
                "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8",
                "B", "C", "D", "E", "F", "G",
                "J", "K", "L", "M", "N",
                "P", "Q", "R", "S", "T",
                "U", "H", "V", "W", "Y",
                "(Y)", "ZR"
        };
        for (String indice : indicesVelocidad)
            choiceIvelocidad.getItems().addAll(indice);


        for (int i = 60; i <= 138; i++)
            choiceIcarga.getItems().addAll(i + "");


        //Choice Ancho
        for (int i = 145; i <= 355 ; i = i + 5)
            choiceAncho.getItems().add(i + "");

        //Choice Perfil
        for (int i = 30; i <= 85 ; i = i + 5)
            choicePerfil.getItems().add(i + "");

        //Choice Rin
        for (int i = 13; i < 25 ; i++)
            choiceRin.getItems().add( i + "");

    }//indicesChoiceBox


    //metodo que genera identificador unico para poder saber si las llantas existen
    public static String generarIdentificador(TextField txtMarca,TextField txtModelo,ChoiceBox choiceAncho,
                                         ChoiceBox choicePerfil,ChoiceBox choiceRin, ChoiceBox choiceIcarga, ChoiceBox<String> choiceIvelocidad ){

        return txtMarca.getText().toLowerCase().replace(" ","").trim() + "-" +txtModelo.getText().toLowerCase().replace(" ", "").trim() + "-" +
                choiceAncho.getValue() + "/" + choicePerfil.getValue() + "r" + choiceRin.getValue() + "-" +
                choiceIcarga.getValue() + "-" + choiceIvelocidad.getValue().toLowerCase();

    }//generarIdentificador





}//class
