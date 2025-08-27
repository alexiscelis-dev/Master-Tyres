package com.mastertyres.fxControllers.nuevaPromocion;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

@Component
public class NuevaPromocion {
    @FXML
    private Slider porcentajeDescuento;
    @FXML
    private Label descuentoLabel;
    @FXML
    private TextField precioSinDescuento;
    @FXML
    private DatePicker fechaInicio;
    @FXML
    private DatePicker fechaFin;
    @FXML
    private Button btnRegistrar;
    @FXML
    private Button btnCancelar;
    @FXML
    private ChoiceBox<String>tipoDescuento;
    @FXML
    private TextField nombrePromocion;
    @FXML
    private TextField descripcion;



    @FXML
    public void initialize() {
        porcentajeDescuento.valueProperty().addListener((observable, valAnterior, valNuevo) -> {

            obtenerPorcentaje(valNuevo.doubleValue());

        });

       tipoDescuento.getSelectionModel().selectedItemProperty().addListener((observable ,valorAnterior,nuevoValor) ->{

           String tipo = nuevoValor.toLowerCase();
           if (tipo != null && !tipo.isEmpty())
           tipoDescuento(tipo);

       });


    }//initialize

    private void limpiar(){
        nombrePromocion.setText("");
        descripcion.setText("");
        precioSinDescuento.setText("");
        porcentajeDescuento.setValue(0);
        descuentoLabel.setText("Descuento 0%");
        fechaInicio.setValue(null);
        fechaFin.setValue(null);
    }

    private void obtenerPorcentaje(Double valNuevo) {

        double porcentaje = (valNuevo.doubleValue() - porcentajeDescuento.getMin()) /
                (porcentajeDescuento.getMax() - porcentajeDescuento.getMin());

        String styleTrack = String.format(
                "-fx-background-color: linear-gradient(to right, #8EB83D %.0f%%, #cccccc %.0f%%);",
                porcentaje * 100, porcentaje * 100

        );
        int porcentajeInt = (int) (porcentaje * 100);

        if (porcentajeDescuento.lookup(".track") != null) {
            porcentajeDescuento.lookup(".track").setStyle(styleTrack);
            descuentoLabel.setText("Descuento " + porcentajeInt + "%");

        }
    }

    private void tipoDescuento(String tipo){

        switch (tipo){

            case "porcentaje" -> {
            limpiar();
            precioSinDescuento.setDisable(false);
            porcentajeDescuento.setDisable(false);
            fechaInicio.setDisable(false);
            fechaFin.setDisable(false);

            }
            case "precio fijo" -> {
                limpiar();
                precioSinDescuento.setDisable(false);
                porcentajeDescuento.setDisable(true);
                fechaInicio.setDisable(false);
                fechaFin.setDisable(false);

            }
            case "2x1" -> {
                limpiar();
                precioSinDescuento.setDisable(true);
                porcentajeDescuento.setDisable(true);
                fechaInicio.setDisable(false);
                fechaFin.setDisable(false);


            }
            case "gratis" -> {
                limpiar();
                porcentajeDescuento.setDisable(true);
                precioSinDescuento.setDisable(true);
                fechaInicio.setDisable(false);
                fechaFin.setDisable(false);
                descuentoLabel.setText("Descuento 100%");
                porcentajeDescuento.setValue(100);

            }
            case "otro" -> {
                limpiar();
                precioSinDescuento.setDisable(false);
                porcentajeDescuento.setDisable(false);
                fechaInicio.setDisable(false);
                fechaFin.setDisable(false);

            }
            default -> {}

        }//switch

    }

}//clase
