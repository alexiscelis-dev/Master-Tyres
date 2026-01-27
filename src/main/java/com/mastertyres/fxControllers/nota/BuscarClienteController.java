package com.mastertyres.fxControllers.nota;

import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.cliente.model.StatusCliente;
import com.mastertyres.cliente.service.ClienteService;
import com.mastertyres.common.utils.MenuContextSetting;
import com.mastertyres.nota.model.NotaDTO;
import com.mastertyres.vehiculo.model.StatusVehiculo;
import com.mastertyres.vehiculo.model.VehiculoDTO;
import com.mastertyres.vehiculo.service.VehiculoService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.mastertyres.common.utils.MensajesAlert.mostrarError;


@Component
public class BuscarClienteController {

    @FXML
    private AnchorPane root;
    @FXML
    private TableView<Cliente> tablaClientes;
    @FXML
    private TableColumn<Cliente, String> colNombreCompleto;
    @FXML
    private TableColumn<Cliente, String> colTipoCliente;
    @FXML
    private TableColumn<Cliente, String> colRfc;
    @FXML
    private TableColumn<Cliente, String> colTelefono;
    @FXML
    private TextField txtBuscador;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnAceptar;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtTipoCliente;
    @FXML
    private TextField txtRfc;
    @FXML
    private TextField txtNumTelefono;

    @FXML
    private TableView<VehiculoDTO> tablaVehiculos;
    @FXML
    private TableColumn<VehiculoDTO, String> colMarca;
    @FXML
    private TableColumn<VehiculoDTO, String> colModelo;
    @FXML
    private TableColumn<VehiculoDTO, String> colCategoria;
    @FXML
    private TableColumn<VehiculoDTO, Integer> colAnio;


    private Cliente clienteSeleccionado;
    private VehiculoDTO vehiculoSeleccionado;

    //getters

    public Cliente getClienteSeleccionado() {
        return clienteSeleccionado;
    }

    public VehiculoDTO getVehiculoSeleccionado() {
        return vehiculoSeleccionado;
    }

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private VehiculoService vehiculoService;

    @FXML
    private void initialize() {

        configuracones();

        btnBuscar.setOnAction(event -> cargarDatosClientes(txtBuscador.getText()));


    }//initialize

    private void configuracones(){

        cargarClientesInicio();

        MenuContextSetting.disableMenu(root);

        txtBuscador.setOnKeyPressed(event -> {
            if (txtBuscador.getText() != null && !txtBuscador.getText().isEmpty()) {
                if (event.getCode() == KeyCode.ENTER)
                    cargarDatosClientes(txtBuscador.getText());
            }


        });

        tablaClientes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {


            if (newValue != null) {
                txtNombre.setText((newValue.getNombre() != null ? newValue.getNombre() : "") + "" + " " +
                        (newValue.getApellido() != null ? newValue.getApellido() : "") + "" + " " +
                        (newValue.getSegundoApellido() != null ? newValue.getSegundoApellido() : "")
                );

                txtTipoCliente.setText(newValue.getTipoCliente());
                txtNumTelefono.setText(newValue.getNumTelefono() != null ? newValue.getNumTelefono() : "N/A");
                txtRfc.setText(newValue.getRfc() != null ? newValue.getRfc() : "N/A");


                String busqueda = newValue.getNombre() + " " + (newValue.getApellido() != null ? newValue.getApellido() : "") + " " + (newValue.getSegundoApellido() != null ? newValue.getSegundoApellido() : "");
                cargarVehiculos(busqueda);


            }


        });

        tablaVehiculos.getSelectionModel().selectedItemProperty().addListener((observableVehiculo, oldVehiculo, newVehiculo) -> {


            tablaClientes.getSelectionModel().selectedItemProperty().addListener((observableCliente, oldCliente, newCliente) -> {

                if (newCliente != null && newVehiculo != null) {


                    NotaDTO nota = NotaDTO
                            .builder()
                            .nombreClienteNota(newCliente.getNombre())
                            .marcaNota(newVehiculo.getNombreMarca())
                            .modeloNota(newVehiculo.getNombreMarca())
                            .categoriaNota(newVehiculo.getNombreCategoria())
                            .anioNota(newVehiculo.getAnio())
                            .build();

                }
            });

            btnAceptar.setDisable(false);

        });

    }//configuracones


    //seccion tabla clientes
    private void cargarClientes(String busqueda) {

        cargarDatosClientes(busqueda);

    }//cargarClientes

    private void cargarClientesInicio(){

        colNombreCompleto.setCellValueFactory(data -> new SimpleStringProperty(
                (data.getValue().getNombre() != null ? data.getValue().getNombre() : "") + "" + " " +
                        (data.getValue().getApellido() != null ? data.getValue().getApellido() : "") + "" + " " +
                        (data.getValue().getSegundoApellido() != null ? data.getValue().getSegundoApellido() : "")

        ));

        colTipoCliente.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getTipoCliente()
        ));

        colRfc.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getRfc() != null ? data.getValue().getRfc() : ""
        ));

        colTelefono.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getNumTelefono() != null ? data.getValue().getNumTelefono() : ""
        ));

        cargarDatosClientes();

    }//cargarClientes

    private void cargarDatosClientes(String busqueda) {
        try {
            if (!busqueda.isEmpty()) {
                List<Cliente> clientes = clienteService.buscadorClientes(StatusCliente.ACTIVE.toString(), busqueda);
                tablaClientes.getItems().setAll(FXCollections.observableList(clientes));
            } else {
                List<Cliente> clientesVacio = new ArrayList<>();
                tablaClientes.getItems().setAll(FXCollections.observableList(clientesVacio));
            }

        } catch (Exception e) {
            mostrarError("Error al mostrar datos", "", "No se pudieron cargar los datos. Por favor, inténtalo de nuevo más tarde.");
        }
    }

    private void cargarDatosClientes(){
        try {
            List<Cliente> clientes = clienteService.firs100Buscador(StatusCliente.ACTIVE.toString());
            tablaClientes.getItems().setAll(FXCollections.observableList(clientes));

        }catch (Exception e){
            mostrarError("Error al mostrar datos", "", "No se pudieron cargar los datos. Por favor, inténtalo de nuevo más tarde.");
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();

    }//cancelar

    //seccion vehiculos

    private void cargarVehiculos(String busqueda) {

        colMarca.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getNombreMarca()
        ));
        colModelo.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getNombreModelo()
        ));
        colCategoria.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getNombreCategoria()
        ));
        colAnio.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getAnio()).asObject());

        cargarDatosVehiculo(busqueda);

    }//cargarVehiculos

    private void cargarDatosVehiculo(String busqueda) {

        try {
            if (busqueda != null && !busqueda.isEmpty()) {
                List<VehiculoDTO> vehiculos = vehiculoService.buscarVehiculoPorPropietario(StatusVehiculo.ACTIVE.toString(), busqueda);
                tablaVehiculos.getItems().setAll(FXCollections.observableList(vehiculos));

            } else {
                List<VehiculoDTO> vehiculosVacio = new ArrayList<>();
                tablaVehiculos.getItems().setAll(FXCollections.observableList(vehiculosVacio));
            }


        } catch (Exception e) {
            mostrarError("", "", "");
            e.printStackTrace();
            mostrarError("Error al mostrar datos", "", "No se pudieron cargar los datos. Por favor, inténtalo de nuevo más tarde.");
        }

    }//cargarDatosVehiculo

    @FXML
    private void aceptar(ActionEvent event) {
        clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        vehiculoSeleccionado = tablaVehiculos.getSelectionModel().getSelectedItem();
        btnAceptar.getScene().getWindow().hide();

    }//aceptar


}//clase
