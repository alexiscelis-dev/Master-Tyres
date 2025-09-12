package com.mastertyres.common;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class MensajesAlert {

    public static void mostrarError(String title, String header, String error) {

        Alert ventana = new Alert(Alert.AlertType.ERROR);
        ventana.setTitle(title);
        ventana.setHeaderText(header);
        ventana.setContentText(error);
        ventana.showAndWait();

    }

    public static void mostrarInformacion(String title, String header, String informacion) {

        Alert ventana = new Alert(Alert.AlertType.INFORMATION);
        ventana.setTitle(title);
        ventana.setHeaderText(header);
        ventana.setContentText(informacion);
        ventana.showAndWait();

    }

    public static void mostrarWarning(String title, String header, String warning) {

        Alert ventana = new Alert(Alert.AlertType.WARNING);
        ventana.setTitle(title);
        ventana.setHeaderText(header);
        ventana.setContentText(warning);
        ventana.showAndWait();

    }

    public static  boolean mostrarConfirmacion(String title, String header, String mensaje, String txtButton1, String txtButton2){

        Alert ventana = new Alert(Alert.AlertType.CONFIRMATION);
        ventana.setTitle(title);
        ventana.setHeaderText(header);
        ventana.setContentText(mensaje);

        ButtonType button1 = new ButtonType(txtButton1, ButtonBar.ButtonData.OK_DONE);
        ButtonType button2 = new ButtonType(txtButton2, ButtonBar.ButtonData.CANCEL_CLOSE);

        ventana.getButtonTypes().setAll(button1,button2);
        Optional<ButtonType> resultado = ventana.showAndWait();

        return resultado.isPresent() && resultado.get() == button1; // devuelve true si preciona la primera opcion
    }


}//clase
