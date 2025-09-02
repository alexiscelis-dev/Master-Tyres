package com.mastertyres.fxControllers.cliente;

import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.cliente.model.ClienteStatus;
import com.mastertyres.cliente.service.ClienteService;
import com.mastertyres.common.ApplicationContextProvider;
import com.mastertyres.fxControllers.ventanaPrincipal.VentanaPrincipalController;
import com.mastertyres.vehiculo.model.Vehiculo;
import com.mastertyres.vehiculo.model.VehiculoStatus;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.mastertyres.common.MensajesAlert.mostrarError;
import static com.mastertyres.common.MensajesAlert.mostrarInformacion;


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
    private TableColumn<Cliente, String> colGenero;

    @FXML
    private TableColumn<Cliente, String> colCumpleanos;

    @FXML
    private TableColumn<Cliente, String> colRegistro;

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
    private TableColumn<Cliente, String> colHobbie;

    @FXML
    private TableColumn<Cliente, String> colVehiculo;

    @FXML
    private TextField buscarClienteBuscador;

    @FXML
    private ChoiceBox<String> atributoBusquedaClientes;
    @FXML
    private Label statusLabel;
    @FXML
    private Label choiceBoxLabel;

    private PauseTransition delayQuery = new PauseTransition(Duration.millis(300)); //evita que se ejecuta una query cada vez que el usuario
    //presiona una tecla hace un delay

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

                    ListView<String> listaOpciones = new ListView<>();
                    listaOpciones.getItems().addAll("Copiar", "Copiar fila completa", "Editar", "Eliminar");
                    listaOpciones.setPrefSize(200, 150);
                    listaOpciones.getStyleClass().add("popup-table");

                    Popup listViewPopup = new Popup();
                    listViewPopup.getContent().add(listaOpciones);
                    listViewPopup.setAutoHide(true);


                    listaOpciones.setOnMouseClicked(e -> {
                        String seleccion = listaOpciones.getSelectionModel().getSelectedItem();


                        if (seleccion != null) {
                            switch (seleccion) {

                                case "Eliminar" -> {


                                    Alert ventanaEliminar = new Alert(Alert.AlertType.WARNING);
                                    ventanaEliminar.setTitle("Eliminar vehiculo");

                                    String cliente, informacionCliente;

                                    //verificar que no contenga null  mostrar mensaje
                                    cliente = (clienteSeleccionado.getNombre() != null ? clienteSeleccionado.getNombre() : "") + " " +
                                            (clienteSeleccionado.getApellido() != null ? clienteSeleccionado.getApellido() : "") + " " +
                                            (clienteSeleccionado.getSegundoApellido() != null ? clienteSeleccionado.getSegundoApellido() : "");

                                    informacionCliente = "Tipo de cliente " + clienteSeleccionado.getTipoCliente() + " " + cliente + " " + clienteSeleccionado.getCiudad() + ", " +
                                            clienteSeleccionado.getEstado() + ", " + clienteSeleccionado.getDomicilio();

                                    String clienteEliminar = cliente + " " + informacionCliente;

                                    ventanaEliminar.setHeaderText("¿Estas seguro que quieres eliminar el siguiente vehiculo? \n\n " + clienteEliminar);

                                    ventanaEliminar.setContentText("Esta accion no se podra deshacer");


                                    ButtonType buttonEliminar = new ButtonType("Elimnar", ButtonBar.ButtonData.YES);
                                    ButtonType buttonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

                                    ventanaEliminar.getButtonTypes().setAll(buttonEliminar, buttonCancelar);

                                    Optional<ButtonType> resultado = ventanaEliminar.showAndWait();

                                    if (resultado.isPresent() && resultado.get() == buttonEliminar) {

                                        System.out.println("vehiculoId = " + clienteId);

                                        clienteService.eliminarCliente(VehiculoStatus.INACTIVE.toString(), clienteId);


                                        cargarClientes(); //metodo que cargar los datos en la tabla

                                    } else {
                                        mostrarInformacion("Accion cancelada", "", "Accion cancelada");

                                    }//else


                                }

                                case "Editar" -> {
                                    System.out.println("Editado");

                                }

                                case "Copiar" -> {
                                    var selectedCell = tablaClientes.getSelectionModel().getSelectedCells();

                                    if (!selectedCell.isEmpty()) {
                                        TablePosition<?, ?> position = selectedCell.get(0);
                                        int row = position.getRow();
                                        TableColumn<?, ?> columna = position.getTableColumn();

                                        Object value = columna.getCellData(row);

                                        if (value != null) {
                                            ClipboardContent content = new ClipboardContent();
                                            content.putString(value.toString());
                                            Clipboard.getSystemClipboard().setContent(content);

                                            statusLabel.setVisible(true);
                                            statusLabel.setText("Texto copiado");

                                            new Thread(() -> {
                                                try {
                                                    Thread.sleep(2500);

                                                } catch (InterruptedException exception) {
                                                    exception.printStackTrace();
                                                }
                                                javafx.application.Platform.runLater(() -> statusLabel.setText(""));

                                            }).start();


                                        } else {
                                            System.out.println("No hay celda seleccionada");
                                        }

                                    }

                                }
                                case "Copiar fila completa" -> {
                                    Cliente item = tablaClientes.getSelectionModel().getSelectedItem();

                                    if (item != null) {

                                        StringBuilder vehiculosStr = new StringBuilder();

                                        if (item.getVehiculos() != null && !item.getVehiculos().isEmpty()) {
                                            for (Vehiculo v : item.getVehiculos()) {
                                                vehiculosStr.append(v.getMarca().getNombreMarca())
                                                        .append(" ")
                                                        .append(v.getModelo().getNombreModelo())
                                                        .append(" ")
                                                        .append(v.getAnio())
                                                        .append(" ")
                                                        .append("| ");

                                            }
                                            if (vehiculosStr.length() > 1) {
                                                vehiculosStr.setLength(vehiculosStr.length() - 1);
                                            }
                                        }
                                        String filaCopiada = item.getTipoCliente() + " " + (item.getNombre() != null ? item.getNombre() : "") + " " + (item.getApellido() != null ? item.getApellido() : "") + " " +
                                                (item.getSegundoApellido() != null ? item.getSegundoApellido() : "") + " " + (item.getNumTelefono() != null ? item.getNumTelefono() : "") + " " +
                                                (item.getEstado() != null ? item.getEstado() : "") + " " + (item.getCiudad() != null ? item.getCiudad() : "") + " " +
                                                (item.getDomicilio() != null ? item.getDomicilio() : "") + " " + (item.getHobbie() != null ? item.getHobbie() : "") + " " +
                                                vehiculosStr + " " + (item.getRfc() != null ? item.getRfc() : "");

                                        ClipboardContent content = new ClipboardContent();
                                        content.putString(filaCopiada);
                                        Clipboard.getSystemClipboard().setContent(content);

                                        statusLabel.setVisible(true);
                                        statusLabel.setText("Fila copiada");
                                        new Thread(() -> {
                                            try {
                                                Thread.sleep(2500);

                                            } catch (InterruptedException exception) {
                                                exception.printStackTrace();
                                            }
                                            javafx.application.Platform.runLater(() -> {
                                                statusLabel.setText("");
                                                statusLabel.setVisible(false);
                                            });

                                        }).start();
                                    }
                                }

                            }//switch
                            listViewPopup.hide();
                        }
                    });
                    Point2D coordenadasPantalla = fila.localToScreen(event.getX(), event.getY()); //convertir las coordenadas relativas a las obsolutas de la pantalla para que la lista aparezca justo donde click


                    listViewPopup.show(fila.getScene().getWindow(), coordenadasPantalla.getX(), coordenadasPantalla.getY());

                }
            });

            return fila;
        });

        //Enter buscar
        buscarClienteBuscador.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.ENTER){
                String seleccion = atributoBusquedaClientes.getValue(), busqueda = buscarClienteBuscador.getText();

                if (seleccion != null && !seleccion.isEmpty() && busqueda != null && !busqueda.isEmpty()) {
                    buscarCliente(seleccion.toLowerCase(),busqueda);
                }
            }


        });

        //buscar mientras escribes

        buscarClienteBuscador.setOnKeyReleased(event -> {

            if (event.getCode() != KeyCode.ENTER) {
                delayQuery.setOnFinished(e -> {
                    String seleccion = atributoBusquedaClientes.getValue();
                    String busqueda = buscarClienteBuscador.getText();

                    if (seleccion == null && busqueda != null && !busqueda.isEmpty())
                        buscarCliente(busqueda);
                     else if (seleccion == null)
                        cargarClientes();

                });
                delayQuery.playFromStart();
            }
        });

        //pone en null la lista de ChoiceBox
        choiceBoxLabel.setOnMouseClicked(event -> {

            if ((event.getButton() == MouseButton.PRIMARY || event.getButton() == MouseButton.MIDDLE) && event.getClickCount() == 2)
                atributoBusquedaClientes.setValue(null); // pone el valor en null para que vuelva a buscar dinamicamente

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
        colHobbie.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHobbie() != null ? data.getValue().getHobbie() : "N/A"));
        colRFC.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRfc() != null ? data.getValue().getRfc() : "N/A"));
        colGenero.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGenero() != null ? data.getValue().getGenero() : "N/A"));
        colRegistro.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCreated_at() != null ? data.getValue().getCreated_at() : "N/A"));
        colCumpleanos.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFechaCumple() != null ? data.getValue().getFechaCumple() : "N/A"));

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
        cargarDatosClientes();


    }//cargarClientes

    private void cargarDatosClientes() {
        try {
            // Listado de los clientes
            tablaClientes.getItems().setAll(clienteService.listarClientesConVehiculos(ClienteStatus.ACTIVE.toString()));

        } catch (Exception e) {
            mostrarError("Error al mostrar datos", "", "No se pudieron cargar los datos. Por favor, inténtalo de nuevo más tarde.");
        }

    }//cargarDatosClientes

    private void buscarCliente( String seleccion, String busqueda) {


            switch (seleccion) {
                case "nombre" -> {

                    String nombre = busqueda;

                    List<Cliente> clientesPorNombre = clienteService.buscarClientePorNombre(ClienteStatus.ACTIVE.toString(), nombre);
                    tablaClientes.setItems(FXCollections.observableList(clientesPorNombre));

                }
                case "telefono" -> {
                            String numTelefono = busqueda;

                            List<Cliente> clientesPorNumero = clienteService.buscarClientePorNumTelefono(ClienteStatus.ACTIVE.toString(), numTelefono);
                            tablaClientes.setItems(FXCollections.observableList(clientesPorNumero));

                }
                case "estado" -> {
                    String estado = busqueda;

                    List<Cliente> clientesPorEstado = clienteService.buscarClientePorEstado(ClienteStatus.ACTIVE.toString(),estado);
                    tablaClientes.setItems(FXCollections.observableList(clientesPorEstado));

                }
                case "ciudad" -> {
                    String ciudad = busqueda;

                    List<Cliente> clientesPorCiudad = clienteService.buscarClientePorCiudad(ClienteStatus.ACTIVE.toString(),ciudad);
                    tablaClientes.setItems(FXCollections.observableList(clientesPorCiudad));


                }
                case "domicilio" -> {
                    String domicilio = busqueda;

                    List<Cliente> clientesPorDomicilio = clienteService.buscarClientePorDomicilio(ClienteStatus.ACTIVE.toString(),domicilio);
                    tablaClientes.setItems(FXCollections.observableList(clientesPorDomicilio));

                }
                case "hobbie" -> {
                    String hobbie = busqueda;

                    List<Cliente> clientesPorHobbie = clienteService.buscarClientePorHobbie(ClienteStatus.ACTIVE.toString(),hobbie);
                    tablaClientes.setItems(FXCollections.observableList(clientesPorHobbie));
                }
                case "rfc" -> {
                    String rfc = busqueda;

                    List<Cliente> clientesPorRfc = clienteService.buscarClientePorRfc(ClienteStatus.ACTIVE.toString(),rfc);
                    tablaClientes.setItems(FXCollections.observableList(clientesPorRfc));
                }

            }//switch

    }//buscarCliente


    public void buscarCliente(String busqueda){

        List<Cliente> clientes = clienteService.buscadorClientes(ClienteStatus.ACTIVE.toString(),busqueda);

        tablaClientes.setItems(FXCollections.observableList(clientes));


    }//buscarCliente




}//clase
