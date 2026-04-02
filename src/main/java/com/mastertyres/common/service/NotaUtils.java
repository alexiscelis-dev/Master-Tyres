package com.mastertyres.common.service;

import com.mastertyres.nota.entity.CampoNota;
import jakarta.annotation.PostConstruct;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class NotaUtils {

    @Value("${mastertyres.ui.nota.direccion.campoNombre.maxlength}")
    private int campoNombre;
    @Value("${mastertyres.ui.nota.direccion.campoDireccion1.maxlength}")
    private int campoDireccion1;
    @Value("${mastertyres.ui.nota.direccion.campoDireccion2.maxlength}")
    private int campoDireccion2;
    @Value("${mastertyres.ui.nota.direccion.campoRfc.maxlength}")
    private int campoRfc;
    @Value("${mastertyres.ui.nota.direccion.campoCorreo.maxlength}")
    private int campoCorreo;
    @Value("${mastertyres.ui.nota.direccion.campoMarca.maxlength}")
    private int campoMarca;
    @Value("${mastertyres.ui.nota.direccion.campoModelo.maxlength}")
    private int campoModelo;
    @Value("${mastertyres.ui.nota.direccion.campoKilometros.maxlength}")
    private int campoKilometros;

    private Map<CampoNota, Integer> longitudes;

    @PostConstruct
    public void init() {
        longitudes = new EnumMap<>(CampoNota.class);
        longitudes.put(CampoNota.NOMBRE, campoNombre);
        longitudes.put(CampoNota.DIRECCION1, campoDireccion1);
        longitudes.put(CampoNota.DIRECCION2, campoDireccion2);
        longitudes.put(CampoNota.RFC, campoRfc);
        longitudes.put(CampoNota.CORREO, campoCorreo);
        longitudes.put(CampoNota.MARCA, campoMarca);
        longitudes.put(CampoNota.MODELO, campoModelo);
        longitudes.put(CampoNota.KILOMETROS, campoKilometros);

    }


    public float toFloatSafe(String text) {
        if (text == null || text.isBlank()) {
            return 0f;
        }

        text = text.replaceFirst("^\\$", "");

        try {

            // Normaliza comas a puntos
            text = text.replace(",", ".");

            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return 0f;
        }
    }//toFloatSafe

    public int toIntSafe(String texto) {
        if (texto == null || texto.isBlank()) {
            return 0;
        }

        try {
            if (texto == null || texto.trim().isEmpty()) {
                return 0;
            }
            return (int) Double.parseDouble(texto.trim());

        } catch (NumberFormatException e) {
            return 0;

        }
    }//toFloatSafe

    public String eliminarCero(float cantidad) {
        if (cantidad != 0.0)
            return "$" + cantidad;
        else
            return "";

    }

    public String eliminarCero(int cantidad) {
        if (cantidad != 0)
            return cantidad + "";
        else return "";
    }

    public void mostrarPopupHora(TextField txtTarget) {

        Popup popup = new Popup();
        popup.setAutoHide(true);

        ComboBox<Integer> cmbHora = new ComboBox<>();
        ComboBox<Integer> cmbMinuto = new ComboBox<>();
        cmbHora.setValue(12);
        cmbMinuto.setValue(0);

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

            if (e.getButton() == MouseButton.SECONDARY){
                popup.show(txtTarget, e.getScreenX(), e.getScreenY());
            }

        });
    }

    public void showIcons(GridPane gridPaneIcons, double[] sliderFromTo, Duration TIEMPO) {


        if (!gridPaneIcons.isVisible()) {
            gridPaneIcons.setVisible(true);
            TranslateTransition slideIN = new TranslateTransition(TIEMPO, gridPaneIcons);
            slideIN.setFromY(sliderFromTo[0]); //Mueve nodo
            slideIN.setToY(sliderFromTo[1]); // Termina en su posición normal

            FadeTransition fadeIn = new FadeTransition(TIEMPO, gridPaneIcons);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            ParallelTransition showTransition = new ParallelTransition(slideIN, fadeIn);
            showTransition.play();

        } else {

            TranslateTransition slideOut = new TranslateTransition(TIEMPO, gridPaneIcons);
            slideOut.setFromY(sliderFromTo[1]); //Mueve nodo
            slideOut.setToY(sliderFromTo[0]); // Termina en su posición normal

            FadeTransition fadeOut = new FadeTransition(TIEMPO, gridPaneIcons);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);

            ParallelTransition showTransition = new ParallelTransition(slideOut, fadeOut);
            showTransition.setOnFinished(event -> gridPaneIcons.setVisible(false));
            showTransition.play();


        }


    }//showIcons


    //metodo que separa la direccion en los dos campos en caso de ser necesario(direccion1 y 2)
    public void campoFormatter(String campoTxt, TextField txtcampo, TextField txtcampo2) {
        if (campoTxt == null || campoTxt.isBlank()) {
            txtcampo.setText("");
            txtcampo2.setText("");
            return;
        }


        String palabras[] = campoTxt.split(" ");
        StringBuilder strCampo1 = new StringBuilder();
        StringBuilder strCampo2 = new StringBuilder();
        StringBuilder resultado = new StringBuilder();
        int longitudMaxDir1 = longitudes.getOrDefault(CampoNota.DIRECCION1, 58);
        int longitudMaxDir2 = longitudes.getOrDefault(CampoNota.DIRECCION2, 70);


        for (String palabra : palabras) {
            if (strCampo1.length() + palabra.length() + 1 <= longitudMaxDir1) {
                strCampo1.append(palabra).append(" ");
            } else {
                if (strCampo2.length() + palabra.length() + 1 <= longitudMaxDir2 - 3) {
                    strCampo2.append(palabra).append(" ");
                } else {
                    strCampo2.append(palabra).append("...");
                    break;
                }
            }

        }
        txtcampo.setText(strCampo1.toString().trim());
        txtcampo2.setText(strCampo2.toString().trim());


    }//campoFormatter

    public void campoFormatter(String campoTxt, TextField txtcampo, CampoNota tipoCampo) {
        if (campoTxt == null || campoTxt.isBlank()) {
            txtcampo.setText("");
            return;
        }

        int longitudMaxima = longitudes.getOrDefault(tipoCampo, 255);
        StringBuilder resultado = new StringBuilder();
        String[] palabras = campoTxt.split(" ");


        for (String palabra : palabras) {
            if (resultado.length() + palabra.length() + 1 <= longitudMaxima - 3) {
                resultado.append(palabra).append(" ");
            } else {
                resultado.append("...");
                break;

            }

        }
        txtcampo.setText(resultado.toString().trim());

    }//campoFormatter

    public void campoFormatter(String campoTxt, TextField txtcampo) {
        if (campoTxt == null || campoTxt.isBlank()) {
            txtcampo.setText("");
            return;
        }

        char letras[] = campoTxt.toCharArray();
        StringBuilder palabra = new StringBuilder();
        int limite = longitudes.getOrDefault(CampoNota.CORREO, 68);

        for (int i = 0; i < campoTxt.length(); i++) {
            if (campoTxt.length() <= limite - 3) {
                if (palabra.length() <= limite - 3)
                    palabra.append(campoTxt.charAt(i));

                else {
                    palabra.append("...");
                    break;
                }
            }
        }

        txtcampo.setText(palabra.toString().trim());
    }

    private boolean eliminarPuntosBool(String texto) {
        if (texto == null) {
            return false;
        }
        return texto.endsWith("...");
    }

    public String eliminarPuntos(String texto) {
        if (eliminarPuntosBool(texto)) {
            return texto.substring(0, texto.length() - 3).trim();
        }
        return texto;

    }

    public void descripcionComponent(Node node, String texto) {
        Tooltip tooltip = new Tooltip(texto);
        tooltip.setShowDelay(Duration.millis(200));

        if (node instanceof Control)
            ((Control) node).setTooltip(tooltip);
        else
            Tooltip.install(node, tooltip);

    }


}//class
