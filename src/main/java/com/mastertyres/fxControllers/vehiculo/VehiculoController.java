package com.mastertyres.fxControllers.vehiculo;


import com.mastertyres.vehiculo.model.VehiculoDTO;
import com.mastertyres.vehiculo.model.VehiculoStatus;
import com.mastertyres.vehiculo.service.VehiculoService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.stage.Popup;


@Component
public class VehiculoController {
    @FXML
    private TableView<VehiculoDTO> tablaVehiculos;
    @FXML
    private TableColumn<VehiculoDTO, String> colMarca;
    @FXML
    private TableColumn<VehiculoDTO, String> colModelo;
    @FXML
    private TableColumn<VehiculoDTO, Integer> colAnio;
    @FXML
    private TableColumn<VehiculoDTO, String> colPlacas;
    @FXML
    private TableColumn<VehiculoDTO, String> colNumeroSerie;
    @FXML
    private TableColumn<VehiculoDTO, Integer> colKilometraje;
    @FXML
    private TableColumn<VehiculoDTO, String> colUltimoServicio;
    @FXML
    private TableColumn<VehiculoDTO, String> colFechaRegistro;
    @FXML
    private TextField buscarVehiculoBuscador;
    @FXML
    private ChoiceBox atributoBusquedaVehiculos;

    @Autowired
    private VehiculoService vehiculoService;


    @FXML
    public void initialize() {


        buscarVehiculoBuscador.setOnAction(event -> {
            String seleccion = (String) atributoBusquedaVehiculos.getValue();
            //   eliminarVehiculo(buscarVehiculoBuscador.getText(), seleccion);

        });


        colMarca.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNombreMarca())
        );

        colModelo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNombreModelo())

        );
        colAnio.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getAnio()).asObject()

        );
        colPlacas.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPlacas())

        );
        colNumeroSerie.setCellValueFactory(
                new PropertyValueFactory<>("numSerie")
        );
        colKilometraje.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getKilometros()).asObject()
        );

        DateTimeFormatter input = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter output = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        colUltimoServicio.setCellValueFactory(data -> {
                    String fechaStr = data.getValue().getUltimoServicio();

                    if (fechaStr == null || fechaStr.isEmpty()) {
                        return new SimpleStringProperty("N/A");
                    } else {
                        LocalDate fecha = LocalDate.parse(fechaStr, input);
                        String texto = fecha.format(output);
                        return new SimpleStringProperty(texto);

                    }
                }

        );


        colFechaRegistro.setCellValueFactory(data ->

                new SimpleStringProperty(data.getValue().getFechaRegistro())
        );

        //Llenar tabla con registros

        tablaVehiculos.getItems().setAll(vehiculoService.listarVehiculos(VehiculoStatus.ACTIVE.toString()));


        //Click derecho borrar

        tablaVehiculos.setRowFactory(tabla -> {
            TableRow<VehiculoDTO> fila = new TableRow<>();

            fila.setOnContextMenuRequested(event -> {
                if (!fila.isEmpty()) {
                    VehiculoDTO vehiculoSeleccionado = fila.getItem();

                    // Crear el botón "Eliminar"
                    Button eliminarBtn = new Button("Eliminar");
                    eliminarBtn.setStyle("-fx-font-size: 15px; -fx-padding: 2 5;");

                    // Crear el popup
                    Popup popup = new Popup();

                    popup.getContent().add(eliminarBtn);

                    popup.setAutoHide(true); // Se cierra solo al hacer clic fuera

                    eliminarBtn.setOnAction(e -> {
                        eliminarVehiculo(buscarVehiculoBuscador.getText(),atributoBusquedaVehiculos.getValue().toString());


                        popup.hide();
                    });

                    popup.show(fila.getScene().getWindow(), event.getScreenX(), event.getScreenY());
                }
            });

            return fila;
        });

        //Enter buscar
        buscarVehiculoBuscador.setOnKeyPressed(event -> {
            eliminarVehiculo(buscarVehiculoBuscador.getText(),atributoBusquedaVehiculos.getValue().toString());
        });

    }//initialize


    private void eliminarVehiculo(String vehiculo, String seleccion) {

        Alert ventana = new Alert(Alert.AlertType.INFORMATION);
        ventana.setTitle("Vehiculo");
        ventana.setHeaderText(null);
        ventana.setContentText("Elemento a buscar: " + vehiculo + " " + "Buscar por: " + seleccion);
        ventana.showAndWait();



    }


}//NotasController
