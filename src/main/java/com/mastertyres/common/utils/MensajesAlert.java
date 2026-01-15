package com.mastertyres.common.utils;

import javafx.scene.control.*;
import javafx.util.converter.IntegerStringConverter;

import java.util.Optional;
import java.util.function.UnaryOperator;

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

    public static boolean mostrarConfirmacion(String title, String header, String mensaje, String txtButton1, String txtButton2) {

        Alert ventana = new Alert(Alert.AlertType.CONFIRMATION);
        ventana.setTitle(title);
        ventana.setHeaderText(header);
        ventana.setContentText(mensaje);

        ButtonType button1 = new ButtonType(txtButton1, ButtonBar.ButtonData.OK_DONE);
        ButtonType button2 = new ButtonType(txtButton2, ButtonBar.ButtonData.CANCEL_CLOSE);

        ventana.getButtonTypes().setAll(button1, button2);
        Optional<ButtonType> resultado = ventana.showAndWait();

        return resultado.isPresent() && resultado.get() == button1; // devuelve true si preciona la primera opcion
    }

    public static String mostrarInput(String tittle, String header, String content) {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(tittle);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        var textField = dialog.getEditor();

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d")) {
                return change;
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), null, filter));

        Optional<String> resultado = dialog.showAndWait();


        return resultado.orElse("");
    }




}//clase
