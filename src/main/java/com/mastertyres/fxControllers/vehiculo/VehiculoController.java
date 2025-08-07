package com.mastertyres.fxControllers.vehiculo;


import com.mastertyres.vehiculo.model.VehiculoDTO;
import com.mastertyres.vehiculo.model.VehiculoStatus;
import com.mastertyres.vehiculo.service.VehiculoService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.stage.Popup;

import java.util.Optional;


@Component
public class VehiculoController {
    @FXML
    private TableView<VehiculoDTO> tablaVehiculos;
    @FXML
    private TableColumn<VehiculoDTO, String> colCliente; //Columna Cliente es Columna Propietario
    @FXML
    private TableColumn<VehiculoDTO, String> colMarca;
    @FXML
    private TableColumn<VehiculoDTO, String> colModelo;
    @FXML
    private TableColumn<VehiculoDTO, String> colCategoria;
    @FXML
    private TableColumn<VehiculoDTO, String> colColor;
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
    private ChoiceBox<String> atributoBusquedaVehiculos;

    @Autowired
    private VehiculoService vehiculoService;


    @FXML
    public void initialize() {

        cargarVehiculos();

        buscarVehiculoBuscador.setOnAction(event -> {
            String seleccion = (String) atributoBusquedaVehiculos.getValue();

        });


        //Click derecho borrar

        tablaVehiculos.setRowFactory(tabla -> {
            TableRow<VehiculoDTO> fila = new TableRow<>();

            fila.setOnContextMenuRequested(event -> {
                if (!fila.isEmpty()) {
                    VehiculoDTO vehiculoSeleccionado = fila.getItem();
                    Integer vehiculoId = vehiculoSeleccionado.getId();


                    // Crear el botón "Eliminar"
                    Button eliminarBtn = new Button("Eliminar");

                    eliminarBtn.setStyle("-fx-font-size: 15px; -fx-padding: 2 5;");


                    // Crear el popup
                    Popup popup = new Popup();


                    popup.getContent().add(eliminarBtn);

                    popup.setAutoHide(true); // Se cierra solo al hacer clic fuera
                    //hacer clic despues de clic derecho
                    eliminarBtn.setOnAction(e -> {

                        Alert ventanaEliminar = new Alert(Alert.AlertType.WARNING);
                        ventanaEliminar.setTitle("Eliminar vehiculo");

                        String propietario, vehiculo;

                        //verificar que no contenga null  mostrar mensaje
                        propietario = vehiculoSeleccionado.getNombreCliente() != null ? vehiculoSeleccionado.getNombreCliente() : "" + " " +
                                vehiculoSeleccionado.getApellido() != null ? vehiculoSeleccionado.getApellido() : "" + " " +
                                vehiculoSeleccionado.getSegundoApellido() != null ? vehiculoSeleccionado.getSegundoApellido() : "";

                        vehiculo = vehiculoSeleccionado.getNombreMarca() + " " + vehiculoSeleccionado.getNombreModelo() + " " + vehiculoSeleccionado + " " + vehiculoSeleccionado.getAnio();


                        String vehiculoEliminar = "Propietario " + vehiculoSeleccionado.getNombreCliente() + " " + vehiculoSeleccionado.getApellido() + " " +
                                vehiculoSeleccionado.getSegundoApellido() + " Vehiculo " + vehiculoSeleccionado.getNombreMarca() + " " +
                                vehiculoSeleccionado.getNombreModelo() + " " + vehiculoSeleccionado.getAnio();

                        ventanaEliminar.setHeaderText("¿Estas seguro que quieres eliminar el siguiente vehiculo? \n\n " + vehiculoEliminar);

                        ventanaEliminar.setContentText("Esta accion no se podra deshacer");


                        ButtonType buttonEliminar = new ButtonType("Elimnar", ButtonBar.ButtonData.YES);
                        ButtonType buttonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

                        ventanaEliminar.getButtonTypes().setAll(buttonEliminar, buttonCancelar);

                        Optional<ButtonType> resultado = ventanaEliminar.showAndWait();

                        if (resultado.isPresent() && resultado.get() == buttonEliminar) {

                            vehiculoService.eliminarVehiculo(VehiculoStatus.INACTIVE.toString(), vehiculoId);
                            popup.hide();
                            cargarVehiculos(); //metodo que cargar los datos en la tabla


                        } else {
                            Alert ventanaCancelado = new Alert(Alert.AlertType.INFORMATION);
                            ventanaCancelado.setTitle("Accion cancelada");
                            ventanaCancelado.setHeaderText("Accion cancelada");
                            ventanaCancelado.showAndWait();

                        }//else


                    });

                    popup.show(fila.getScene().getWindow(), event.getScreenX(), event.getScreenY());
                }
            });

            return fila;
        });

        //Enter buscar
        buscarVehiculoBuscador.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.ENTER)
                buscarVehiculo();

        });

    }//initialize


    private void cargarVehiculos() {

        colCliente.setCellValueFactory(data -> {

            String nombre = data.getValue().getNombreCliente() != null ? data.getValue().getNombreCliente() : "";
            String apellido = data.getValue().getApellido() != null ? data.getValue().getApellido() : "";
            String segundoApellido = data.getValue().getSegundoApellido() != null ? data.getValue().getSegundoApellido() : "";
            String propietario = nombre + " " + " " + apellido + " " + segundoApellido;

            return new SimpleStringProperty(propietario);
        });

        colMarca.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNombreMarca())
        );

        colModelo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNombreModelo())

        );
        colColor.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getColor())
        );
        colCategoria.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNombreCategoria())
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
        try {
            tablaVehiculos.getItems().setAll(vehiculoService.listarVehiculos(VehiculoStatus.ACTIVE.toString()));

        } catch (Exception e) {
            Alert ventanaError = new Alert(Alert.AlertType.ERROR);
            ventanaError.setTitle("Error");
            ventanaError.setHeaderText("No se pudieron cargar los datos. Por favor, inténtalo de nuevo más tarde o contacta al soporte.");
            ventanaError.showAndWait();

        }

    }//cargarVehiculos

    private void buscarVehiculo() {
        Alert ventana = new Alert(Alert.AlertType.INFORMATION);
        ventana.setTitle("Buscar Vehiculo");
        ventana.setContentText("Buscando vehiculo");
        String itemSeleccionado = atributoBusquedaVehiculos.getValue();
        ventana.setContentText(buscarVehiculoBuscador.getText() + " " + itemSeleccionado);
        ventana.showAndWait();
    }


}//NotasController
