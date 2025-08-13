package com.mastertyres.fxControllers.cliente;

import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.cliente.model.StatusCliente;
import com.mastertyres.cliente.service.ClienteService;
import com.mastertyres.common.ApplicationContextProvider;
import com.mastertyres.fxControllers.ventanaPrincipal.VentanaPrincipalController;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.Optional;


@Component
public class ClienteController {


    private VentanaPrincipalController ventanaPrincipalController;

    public void setVentanaPrincipalController(VentanaPrincipalController controller) {
        this.ventanaPrincipalController = controller;
    }

    @FXML
    private TableView<Cliente> tablaClientes;

    @FXML
    private TableColumn<Cliente, String> colTipoCliente;

    @FXML
    private TableColumn<Cliente, String> colNombre;

    @FXML
    private TableColumn<Cliente, String> colRFC;

    @FXML
    private TableColumn<Cliente, String> colTelefono;

    @FXML
    private TableColumn<Cliente, String> colEstado;

    @FXML
    private TableColumn<Cliente, String> colCiudad;

    @FXML
    private TableColumn<Cliente, String> colDomicilio;

    @FXML
    private TableColumn<Cliente, String> colCurp;

    @FXML
    private TableColumn<Cliente, String> colVehiculo;

    @FXML
    private TextField buscarClienteBuscador;

    @FXML
    private ChoiceBox<String> atributoBusquedaClientes;

    @Autowired
    private ClienteService clienteService;


    @FXML
    private void initialize() {
        cargarClientes();

        buscarClienteBuscador.setOnAction(event -> {
            String seleccion = atributoBusquedaClientes.getValue();

        });


        //Click derecho borrar

        tablaClientes.setRowFactory(tabla -> {
            TableRow<Cliente> fila = new TableRow<>();

            fila.setOnContextMenuRequested(event -> {
                if (!fila.isEmpty()) {
                    Cliente clienteSeleccionado = fila.getItem();

                    Integer clienteId = clienteSeleccionado.getClienteId();

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

                        String clientEliminar;

                        //verificar que no contenga null  mostrar mensaje
                        clientEliminar = clienteSeleccionado.getNombre() != null ? clienteSeleccionado.getNombre() : "" +
                                clienteSeleccionado.getApellido() != null ? clienteSeleccionado.getApellido() : "" +
                                clienteSeleccionado.getSegundoApellido() != null ? clienteSeleccionado.getSegundoApellido() : "" + " Tipo de cliente " +
                                clienteSeleccionado.getTipoCliente() != null ? clienteSeleccionado.getTipoCliente() : "";


                        ventanaEliminar.setHeaderText("¿Estas seguro que quieres eliminar el siguiente cliente? \n\n " + clientEliminar);

                        ventanaEliminar.setContentText("Esta accion no se podra deshacer");


                        ButtonType buttonEliminar = new ButtonType("Elimnar", ButtonBar.ButtonData.YES);
                        ButtonType buttonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

                        ventanaEliminar.getButtonTypes().setAll(buttonEliminar, buttonCancelar);

                        Optional<ButtonType> resultado = ventanaEliminar.showAndWait();

                        if (resultado.isPresent() && resultado.get() == buttonEliminar) {
                            System.out.println(clienteId);

                            clienteService.eliminarCliente(StatusCliente.INACTIVE.toString(), clienteId);
                            popup.hide();
                            cargarClientes(); //metodo que cargar los datos en la tabla


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
        buscarClienteBuscador.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.ENTER)
                buscarCliente();

        });


    }// initialize


    @FXML
    private void agregarCliente(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml_views/AgregarCliente.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent root = loader.load();

            Pane panel = ventanaPrincipalController.getPanelMenu();
            panel.getChildren().setAll(root);
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarClientes() {

        //Ajuste de tamaño de las columnas
        //  tablaClientes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //LLenado de las columnas
        colTipoCliente.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTipoCliente().toString() != null ? data.getValue().getTipoCliente().toString() : "N/A"));
        colNombre.setCellValueFactory(data -> new SimpleStringProperty(


                (data.getValue().getNombre() != null ? data.getValue().getNombre() : "") + " " +
                        (data.getValue().getApellido() != null ? data.getValue().getApellido() : "") + " " +
                        (data.getValue().getSegundoApellido() != null ? data.getValue().getSegundoApellido() : "")


        ));



        //colApellido.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getApellido() + " " + data.getValue().getSegundoApellido()));
        colTelefono.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNumTelefono() != null ? data.getValue().getNumTelefono() : "N/A"));
        colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEstado() != null ? data.getValue().getEstado() : "N/A"));
        colCiudad.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCiudad() != null ? data.getValue().getCiudad() : "N/A"));
        colDomicilio.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDomicilio() != null ? data.getValue().getDomicilio() : "N/A"));
        colCurp.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCurp() != null ? data.getValue().getCurp() : "N/A"));
        colRFC.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRfc() != null ? data.getValue().getRfc() : "N/A"));
        //colVehiculo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVehiculo().getMarca().toString() + data.getValue().getVehiculo().getModelo().toString() + data.getValue().getVehiculo().getAnio().toString()));
        // Validacion de si hay o no hay vehiculos
        /*colVehiculo.setCellValueFactory(data -> {
            var vehiculos = data.getValue().getVehiculos(); // lista de Vehiculo

            if (vehiculos == null || vehiculos.isEmpty()) {
                return new SimpleStringProperty("Sin vehículos");
            }

            StringBuilder descripcion = new StringBuilder();

            for (var v : vehiculos) {
                String marca = v.getMarca().getNombreMarca() != null ? v.getMarca().getNombreMarca() : "Sin marca";
                String modelo = v.getModelo().getNombreModelo() != null ? v.getModelo().getNombreModelo() : "Sin modelo";
                String anio = v.getAnio() != null ? v.getAnio().toString() : "Sin año";
                descripcion.append(marca).append(" ").append(modelo).append(" ").append(anio).append(" | ");
            }

            // Quitar el último " | "
            if (descripcion.length() > 3) {
                descripcion.setLength(descripcion.length() - 3);
            }

            return new SimpleStringProperty(descripcion.toString());
        });*/
        colVehiculo.setCellValueFactory(data -> {
            var vehiculos = data.getValue().getVehiculos();

            if (vehiculos == null || vehiculos.isEmpty()) {
                return new SimpleStringProperty("Sin vehículos");
            }

            StringBuilder descripcion = new StringBuilder();

            for (var v : vehiculos) {
                String marca = v.getMarca() != null && v.getMarca().getNombreMarca() != null ? v.getMarca().getNombreMarca() : "Sin marca";
                String modelo = v.getModelo() != null && v.getModelo().getNombreModelo() != null ? v.getModelo().getNombreModelo() : "Sin modelo";
                String anio = v.getAnio() != null ? v.getAnio().toString() : "Sin año";
                descripcion.append(marca).append(" ").append(modelo).append(" ").append(anio).append(" | ");
            }

            if (descripcion.length() > 3) {
                descripcion.setLength(descripcion.length() - 3);
            }

            return new SimpleStringProperty(descripcion.toString());
        });

        // Listado de los clientes
        tablaClientes.getItems().setAll(clienteService.listarClientesConVehiculos(StatusCliente.ACTIVE.toString()));
    }

    private void buscarCliente() {

    }


}//clase
