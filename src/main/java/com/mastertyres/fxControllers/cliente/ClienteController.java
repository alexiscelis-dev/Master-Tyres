package com.mastertyres.fxControllers.cliente;

import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.cliente.service.ClienteService;
import com.mastertyres.fxControllers.ventanaPrincipal.VentanaPrincipalController;
import com.mastertyres.vehiculo.service.VehiculoService;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import lombok.*;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.IOException;
import java.util.Optional;


@Component
public class ClienteController {


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


    @Autowired
    private ClienteService clienteService;

    @FXML
    private void initialize() {
        //Ajuste de tamaño de las columnas
        tablaClientes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //LLenado de las columnas
        colTipoCliente.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTipoCliente().toString()));
        colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre() + " " + data.getValue().getApellido() + " " + data.getValue().getSegundoApellido()));
        //colApellido.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getApellido() + " " + data.getValue().getSegundoApellido()));
        colTelefono.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNumTelefono()));
        colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEstado()));
        colCiudad.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCiudad()));
        colDomicilio.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDomicilio()));
        colCurp.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCurp()));
        colRFC.setCellValueFactory(data-> new SimpleStringProperty(data.getValue().getRfc()));
        //colVehiculo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVehiculo().getMarca().toString() + data.getValue().getVehiculo().getModelo().toString() + data.getValue().getVehiculo().getAnio().toString()));
        // Validacion de si hay o no hay vehiculos
        colVehiculo.setCellValueFactory(data -> {
            var vehiculos = data.getValue().getVehiculos(); // lista de Vehiculo

            if (vehiculos == null || vehiculos.isEmpty()) {
                return new SimpleStringProperty("Sin vehículos");
            }

            StringBuilder descripcion = new StringBuilder();

            for (var v : vehiculos) {
                String marca = v.getMarca() != null ? v.getMarca().getNombreMarca(): "Sin marca";
                String modelo = v.getModelo() != null ? v.getModelo().getNombreModelo() : "Sin modelo";
                String anio = v.getAnio() != null ? v.getAnio().toString() : "Sin año";
                descripcion.append(marca).append(" ").append(modelo).append(" ").append(anio).append(" | ");
            }

            // Quitar el último " | "
            if (descripcion.length() > 3) {
                descripcion.setLength(descripcion.length() - 3);
            }

            return new SimpleStringProperty(descripcion.toString());
        });

        // Listado de los clientes
        tablaClientes.getItems().setAll(clienteService.listarCliente());

    }





    @FXML
    private void eliminarCliente() {
        // Obtener el cliente seleccionado
        Cliente clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado != null) {
            // Confirmar la eliminación con el usuario
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("Confirmar eliminación");
            alerta.setHeaderText("¿Estás seguro de eliminar este cliente?");
            alerta.setContentText("Cliente: " + clienteSeleccionado.getNombre());

            Optional<ButtonType> resultado = alerta.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                // Llamar a tu servicio/repositorio para eliminar de la BD
                clienteService.eliminar(clienteSeleccionado.getClienteId());

                // Eliminar de la tabla
                tablaClientes.getItems().remove(clienteSeleccionado);
            }
        } else {
            // No hay cliente seleccionado
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Aviso");
            alerta.setHeaderText("Ningún cliente seleccionado");
            alerta.setContentText("Por favor selecciona un cliente para eliminar.");
            alerta.showAndWait();
        }
    }





}//clase
