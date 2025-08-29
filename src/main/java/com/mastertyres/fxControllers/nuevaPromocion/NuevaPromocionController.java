package com.mastertyres.fxControllers.nuevaPromocion;

import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.promociones.service.PromocionService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mastertyres.common.MensajesAlert.mostrarConfirmacion;
import static com.mastertyres.common.MensajesAlert.mostrarWarning;

@Component
public class NuevaPromocionController {
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
    private Button btnLimpiar;
    @FXML
    private ChoiceBox<String> tipoDescuento;
    @FXML
    private TextField nombrePromocion;
    @FXML
    private TextField descripcion;
    @FXML
    private ChoiceBox<Marca> choiceMarca;
    @FXML
    private ChoiceBox<Modelo> choiceModelo;
    @FXML
    private ChoiceBox<Categoria> choiceCategoria;
    @FXML
    private ChoiceBox<String> choiceAnio;
    @Autowired
    private PromocionService promocionService;


    @FXML
    public void initialize() {

        //Abre y cierra el popup del choiceBox para que aparezca en la posicion correcta y no debajo de la tabla
        choiceAnio.setOnMousePressed(event -> {
            if(!choiceAnio.isShowing()){
                choiceAnio.show();
                choiceAnio.hide();
            }
        });

        //Inicializar marca, modelo, año
        vehiculosParticipantesInitialize();

        precioSinDescuento.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*(\\.\\d{0,2})?")) {
                return change;
            }
            return null;
        }));

        porcentajeDescuento.valueProperty().addListener((observable, valAnterior, valNuevo) -> {

            obtenerPorcentaje(valNuevo.doubleValue());

        });

        tipoDescuento.getSelectionModel().selectedItemProperty().addListener((observable, valorAnterior, nuevoValor) -> {


            if (nuevoValor != null && !nuevoValor.isEmpty())
                tipoDescuento(nuevoValor.toLowerCase());

        });

        btnLimpiar.setOnAction(event -> clean());

        btnRegistrar.setOnAction(event -> registrarPromocion());


    }//initialize

    private void clean() {
        nombrePromocion.setText("");
        descripcion.setText("");
        precioSinDescuento.setText("");
        porcentajeDescuento.setValue(0);
        descuentoLabel.setText("Descuento 0%");
        fechaInicio.setValue(null);
        fechaFin.setValue(null);
        tipoDescuento.setValue(null);
        vehiculosParticipantesInitialize();
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

    private void tipoDescuento(String tipo) {

        switch (tipo) {

            case "porcentaje" -> {
                precioSinDescuento.setDisable(false);
                porcentajeDescuento.setDisable(false);
                LocalDate fecha = LocalDate.now();
                fechaInicio.setValue(fecha);
                fechaFin.setDisable(false);

            }
            case "otro" -> {
                precioSinDescuento.setDisable(false);
                porcentajeDescuento.setDisable(false);
                LocalDate fecha = LocalDate.now();
                fechaInicio.setValue(fecha);
                fechaFin.setDisable(false);

            }
            default -> {
            }

        }//switch

    }

    private void vehiculosParticipantesInitialize() {

        choiceMarca.setItems(FXCollections.observableList(promocionService.listarMarcas()));
        choiceModelo.setItems(FXCollections.observableList(promocionService.listarModelos()));
        choiceCategoria.setItems(FXCollections.observableList(promocionService.listarCategorias()));
        List<String> anios = new ArrayList<>();

        int anioActual = LocalDate.now().getYear();

        for (int i = anioActual; i >= 1950 ; i--) {
            anios.add(i+"");
        }
        choiceAnio.getItems().setAll(anios);


    }//vehiculosParticipantesInitialize

    private boolean empty() {
        boolean empty = false;
        if (nombrePromocion.getText() == null || descripcion.getText() == null || tipoDescuento.getValue() == null || precioSinDescuento.getText() == null ||
                fechaInicio.getValue() == null || fechaFin.getValue() == null || choiceMarca.getValue() == null || choiceModelo.getValue() == null || choiceCategoria.getValue() == null ||
                nombrePromocion.getText().isEmpty() || descripcion.getText().isEmpty() || precioSinDescuento.getText().isEmpty())

            empty = true;

        else {
            empty = false;
        }

        return empty;
    }

    private boolean registrarPromocion() {

        if (!empty() && validarFecha(fechaInicio.getValue(),fechaFin.getValue())) {

            String nombre,descripcion,tipoDescuento,precio,porcentaje,inicioPromo,finPromo;

        } else if (empty())
            mostrarWarning("Campos vacios", "", "Favor de completar cada uno de los campos solicitados.");

        return true;
    }

    private boolean validarFecha(LocalDate fechaInicioLD, LocalDate fechaFinLD) {
        boolean fechaValida = false;

        if (fechaFinLD.isAfter(fechaInicioLD)) {
            fechaValida = true;

        } else if (fechaFinLD.isBefore(fechaInicioLD)) {
            mostrarWarning("Fecha incorrecta","","Le fecha de fin no puede ser antes que la fecha de inicio, vuelva a intentarlo");

            fechaValida = false;

        } else if (fechaFinLD.equals(fechaInicioLD)) {
            fechaValida = false;
            mostrarConfirmacion("Fechas iguales", "Ha ingresado la misma fecha de inicio y de fin para la promocion.", "¿Desea continuar?", "Continuar", "Cancelar");

        }
        return fechaValida;
    }//validarFecha

}//clase
