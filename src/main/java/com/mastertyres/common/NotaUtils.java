package com.mastertyres.common;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PopupControl;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class NotaUtils {


    public static float toFloatSafe(String text) {
        text = text.replaceFirst("^\\$", "");

        try {

            // Normaliza comas a puntos
            text = text.replace(",", ".");

            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return 0f;
        }
    }//toFloatSafe

    public static int toIntSafe(String texto) {
        try {
            if (texto == null || texto.trim().isEmpty()) {
                return 0;
            }
            return (int) Double.parseDouble(texto.trim());

        } catch (NumberFormatException e) {
            return 0;

        }
    }//toFloatSafe

    public static String eliminarCero(float cantidad) {
        if (cantidad != 0.0)
            return "$" + cantidad;
        else
            return "";

    }

    public static String eliminarCero(int cantidad) {
        if (cantidad != 0)
            return cantidad + "";
        else
            return "";
    }

    public static void mostrarPopupHora(TextField txtTarget) {

        PopupControl popup = new PopupControl();
        popup.setAutoHide(true);

        ComboBox<Integer> cmbHora = new ComboBox<>();
        ComboBox<Integer> cmbMinuto = new ComboBox<>();

        for (int h = 0; h < 24; h++) cmbHora.getItems().add(h);
        for (int m = 0; m < 60; m++) cmbMinuto.getItems().add(m);

        Button btnAceptar = new Button("Aceptar");
        Button btnCancelar = new Button("Cancelar");

        btnAceptar.setOnAction(e -> {
            if (cmbHora.getValue() != null && cmbMinuto.getValue() != null) {
                txtTarget.setText(String.format("%02d:%02d",
                        cmbHora.getValue(),
                        cmbMinuto.getValue()));
            }
            popup.hide();
        });

        btnCancelar.setOnAction(e -> popup.hide());

        HBox selectors = new HBox(10, cmbHora, cmbMinuto);
        selectors.setAlignment(Pos.CENTER);

        HBox buttons = new HBox(10, btnAceptar, btnCancelar);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10, selectors, buttons);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: #333;");

        popup.getScene().setRoot(layout);

        txtTarget.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                popup.show(txtTarget, e.getScreenX(), e.getScreenY());
            }
        });
    }

    public static void showIcons(GridPane gridPaneIcons) {
        final Duration TIEMPO = Duration.millis(500);

        if (!gridPaneIcons.isVisible()) {
            gridPaneIcons.setVisible(true);
            TranslateTransition slideIN = new TranslateTransition(TIEMPO, gridPaneIcons);
            slideIN.setFromY(-50); //Mueve nodo
            slideIN.setToY(0); // Termina en su posición normal

            FadeTransition fadeIn = new FadeTransition(TIEMPO, gridPaneIcons);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            ParallelTransition showTransition = new ParallelTransition(slideIN, fadeIn);
            showTransition.play();

        } else {

            TranslateTransition slideOut = new TranslateTransition(TIEMPO, gridPaneIcons);
            slideOut.setFromY(0); //Mueve nodo
            slideOut.setToY(-50); // Termina en su posición normal

            FadeTransition fadeOut = new FadeTransition(TIEMPO, gridPaneIcons);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);

            ParallelTransition showTransition = new ParallelTransition(slideOut, fadeOut);
            showTransition.setOnFinished(event -> gridPaneIcons.setVisible(false));
            showTransition.play();


        }


    }//showIcons







}//class
