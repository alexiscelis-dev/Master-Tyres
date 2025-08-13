package com.mastertyres.common;

import javafx.scene.control.Alert;

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


}//clase
