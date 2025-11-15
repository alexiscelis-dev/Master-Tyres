package com.mastertyres.fxControllers.cliente;

import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.cliente.model.StatusCliente;
import com.mastertyres.cliente.service.ClienteService;
import com.mastertyres.common.ApplicationContextProvider;
import com.mastertyres.fxControllers.EditarControllers.EditarClienteController;
import com.mastertyres.fxControllers.ventanaPrincipal.VentanaPrincipalController;
import com.mastertyres.vehiculo.model.Vehiculo;
import com.mastertyres.vehiculo.model.StatusVehiculo;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, String> colTipoCliente;
    @FXML private TableColumn<Cliente, String> colGenero;
    @FXML private TableColumn<Cliente, String> colCumpleanos;
    @FXML private TableColumn<Cliente, String> colRegistro;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colRFC;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colEstado;
    @FXML private TableColumn<Cliente, String> colCiudad;
    @FXML private TableColumn<Cliente, String> colDomicilio;
    @FXML private TableColumn<Cliente, String> colHobbie;
    @FXML private TableColumn<Cliente, String> colVehiculo;
    @FXML private TextField buscarClienteBuscador;
    @FXML private ChoiceBox<String> atributoBusquedaClientes;
    @FXML private Label statusLabel;
    @FXML private HBox limpiarChoiceBox;
    @FXML private Button refrescar;

    private PauseTransition delayQuery = new PauseTransition(Duration.millis(300)); //evita que se ejecuta una query cada vez que el usuario
    //presiona una tecla hace un delay

    @Autowired
    private ClienteService clienteService;



    @FXML
    private Pagination paginadorClientes;

    private static final int CLIENTES_POR_PAGINA = 20;

    private List<Cliente> todosLosClientes;
    private String terminoBusquedaActual = "";
    private boolean modoBusqueda = false;

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
                    listaOpciones.getItems().addAll("Ver informacion", "Copiar", "Copiar fila completa", "Editar", "Eliminar");
                    listaOpciones.setPrefSize(200, 150);
                    listaOpciones.getStyleClass().add("popup-table");
                    listaOpciones.getStylesheets().add(
                            getClass().getResource("/styles_css/Lista.css").toExternalForm()
                    );

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

                                        clienteService.eliminarCliente(StatusVehiculo.INACTIVE.toString(), clienteId);


                                        cargarClientes(); //metodo que cargar los datos en la tabla

                                    } else {
                                        mostrarInformacion("Accion cancelada", "", "Accion cancelada");

                                    }//else


                                }

                                case "Editar" -> {
                                    try {
                                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/cliente/EditarCliente.fxml"));
                                        loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
                                        Parent root = loader.load();

                                        EditarClienteController controller = loader.getController();
                                        controller.editarCliente(clienteSeleccionado); // pasa el cliente actual

                                        Stage stage = new Stage(StageStyle.UTILITY);



                                        stage.setTitle("Editar Cliente");
                                        stage.setScene(new Scene(root));
                                        stage.setResizable(false);

                                        stage.initModality(Modality.APPLICATION_MODAL);

                                        stage.showAndWait();


                                        cargarClientes();
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                }

                                case "Ver informacion" -> {
                                    try {
                                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/cliente/DetalleCliente.fxml"));
                                        loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
                                        Parent root = loader.load();

                                        DetalleCliente controller = loader.getController();
                                        controller.informacionCliente(clienteSeleccionado); // pasa el cliente actual

                                        Stage stage = new Stage(StageStyle.UTILITY);
                                        stage.setTitle("Informacion Cliente");
                                        stage.setScene(new Scene(root));
                                        stage.initModality(Modality.APPLICATION_MODAL);
                                        stage.showAndWait();
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
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

            if (event.getCode() == KeyCode.ENTER) {
                String seleccion = atributoBusquedaClientes.getValue(), busqueda = buscarClienteBuscador.getText();

                if (seleccion != null && !seleccion.isEmpty() && busqueda != null && !busqueda.isEmpty()) {
                    buscarCliente(seleccion.toLowerCase(), busqueda);
                }
            }


        });

        //buscar mientras escribes

        buscarClienteBuscador.setOnKeyReleased(event -> {

            if (event.getCode() != KeyCode.ENTER) {
                delayQuery.setOnFinished(e -> {
                    String seleccion = atributoBusquedaClientes.getValue();
                    String busqueda = buscarClienteBuscador.getText();

                    if (seleccion == null && busqueda != null && !busqueda.isEmpty()) {
                        buscarCliente(busqueda);
                    } else {
                        // Si no hay búsqueda, salir del modo búsqueda y recargar tabla
                        modoBusqueda = false;
                        terminoBusquedaActual = "";
                        paginadorClientes.setPageFactory(this::crearPaginaClientes);
                        paginadorClientes.setCurrentPageIndex(0);
                    }

                });
                delayQuery.playFromStart();
            }
        });


        //pone en null la lista de ChoiceBox
        limpiarChoiceBox.setOnMouseClicked(event -> {

            if ((event.getButton() == MouseButton.PRIMARY || event.getButton() == MouseButton.MIDDLE) && event.getClickCount() == 2)
                atributoBusquedaClientes.setValue(null); // pone el valor en null para que vuelva a buscar dinamicamente

        });

    }// initialize



    @FXML
    private void agregarCliente(ActionEvent event) {
        ventanaPrincipalController.viewContent(
                null, // no se requiere el MouseEvent
                "/fxmlViews/cliente/AgregarCliente.fxml",
                "Agregar cliente"
        );
        ventanaPrincipalController.cambiarPaginaEtiqueta.setText("Agregar cliente");
    }

    private VBox crearPaginaClientes(int indicePagina) {
        Page<Cliente> paginaClientes;

        if (modoBusqueda) {
            paginaClientes = clienteService.buscadorClientesPaginado(
                    StatusCliente.ACTIVE.toString(),
                    terminoBusquedaActual,
                    indicePagina,
                    CLIENTES_POR_PAGINA
            );
        } else {
            paginaClientes = clienteService.listarClientesConVehiculosPaginado(
                    StatusCliente.ACTIVE.toString(),
                    indicePagina,
                    CLIENTES_POR_PAGINA
            );
        }

        tablaClientes.setItems(FXCollections.observableArrayList(paginaClientes.getContent()));

        VBox contenedor = new VBox(tablaClientes);
        contenedor.setMinHeight(500);
        contenedor.setPrefHeight(500);
        contenedor.setStyle("-fx-background-color: transparent;");
        return contenedor;
    }


    private VBox crearPaginaClientesconFiltro(int indicePagina) {
        Page<Cliente> paginaClientes;

        if (modoBusqueda) {
            // Dependiendo del atributo de búsqueda
            switch (atributoBusquedaClientes.getValue()) {
                case "nombre" -> paginaClientes = clienteService.buscarClientePorNombrePaginado(
                        StatusCliente.ACTIVE.toString(), terminoBusquedaActual, indicePagina, CLIENTES_POR_PAGINA);
                case "telefono" -> paginaClientes = clienteService.buscarClientePorNumTelefonoPaginado(
                        StatusCliente.ACTIVE.toString(), terminoBusquedaActual, indicePagina, CLIENTES_POR_PAGINA);
                case "estado" -> paginaClientes = clienteService.buscarClientePorEstadoPaginado(
                        StatusCliente.ACTIVE.toString(), terminoBusquedaActual, indicePagina, CLIENTES_POR_PAGINA);
                case "ciudad" -> paginaClientes = clienteService.buscarClientePorCiudadPaginado(
                        StatusCliente.ACTIVE.toString(), terminoBusquedaActual, indicePagina, CLIENTES_POR_PAGINA);
                case "domicilio" -> paginaClientes = clienteService.buscarClientePorDomicilioPaginado(
                        StatusCliente.ACTIVE.toString(), terminoBusquedaActual, indicePagina, CLIENTES_POR_PAGINA);
                case "hobbie" -> paginaClientes = clienteService.buscarClientePorHobbiePaginado(
                        StatusCliente.ACTIVE.toString(), terminoBusquedaActual, indicePagina, CLIENTES_POR_PAGINA);
                case "rfc" -> paginaClientes = clienteService.buscarClientePorRfcPaginado(
                        StatusCliente.ACTIVE.toString(), terminoBusquedaActual, indicePagina, CLIENTES_POR_PAGINA);
                default -> paginaClientes = clienteService.listarClientesConVehiculosPaginado(StatusCliente.ACTIVE.toString(), indicePagina, CLIENTES_POR_PAGINA);
            }
        } else {
            paginaClientes = clienteService.listarClientesConVehiculosPaginado(
                    StatusCliente.ACTIVE.toString(), indicePagina, CLIENTES_POR_PAGINA);
        }

        tablaClientes.setItems(FXCollections.observableArrayList(paginaClientes.getContent()));

        VBox contenedor = new VBox(tablaClientes);
        contenedor.setMinHeight(500);
        contenedor.setPrefHeight(500);
        contenedor.setStyle("-fx-background-color: transparent;");

        return contenedor;
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
            long totalClientes = clienteService.contarClientesActivos(StatusCliente.ACTIVE.toString());
            int totalPaginas = (int) Math.ceil((double) totalClientes / CLIENTES_POR_PAGINA);
            paginadorClientes.setPageCount(Math.max(totalPaginas, 1));
            paginadorClientes.setPageFactory(this::crearPaginaClientes);

            // Muestra la primera página
            paginadorClientes.setCurrentPageIndex(0);

        } catch (Exception e) {
            mostrarError("Error al mostrar datos", "", "No se pudieron cargar los datos. Por favor, inténtalo de nuevo más tarde.");
        }
    }

//    private void buscarCliente(String seleccion, String busqueda) {
////
////
////        switch (seleccion) {
////            case "nombre" -> {
////
////                String nombre = busqueda;
////
////                List<Cliente> clientesPorNombre = clienteService.buscarClientePorNombre(ClienteStatus.ACTIVE.toString(), nombre);
////                tablaClientes.setItems(FXCollections.observableList(clientesPorNombre));
////
////            }
////            case "telefono" -> {
////                String numTelefono = busqueda;
////
////                List<Cliente> clientesPorNumero = clienteService.buscarClientePorNumTelefono(ClienteStatus.ACTIVE.toString(), numTelefono);
////                tablaClientes.setItems(FXCollections.observableList(clientesPorNumero));
////
////            }
////            case "estado" -> {
////                String estado = busqueda;
////
////                List<Cliente> clientesPorEstado = clienteService.buscarClientePorEstado(ClienteStatus.ACTIVE.toString(), estado);
////                tablaClientes.setItems(FXCollections.observableList(clientesPorEstado));
////
////            }
////            case "ciudad" -> {
////                String ciudad = busqueda;
////
////                List<Cliente> clientesPorCiudad = clienteService.buscarClientePorCiudad(ClienteStatus.ACTIVE.toString(), ciudad);
////                tablaClientes.setItems(FXCollections.observableList(clientesPorCiudad));
////
////
////            }
////            case "domicilio" -> {
////                String domicilio = busqueda;
////
////                List<Cliente> clientesPorDomicilio = clienteService.buscarClientePorDomicilio(ClienteStatus.ACTIVE.toString(), domicilio);
////                tablaClientes.setItems(FXCollections.observableList(clientesPorDomicilio));
////
////            }
////            case "hobbie" -> {
////                String hobbie = busqueda;
////
////                List<Cliente> clientesPorHobbie = clienteService.buscarClientePorHobbie(ClienteStatus.ACTIVE.toString(), hobbie);
////                tablaClientes.setItems(FXCollections.observableList(clientesPorHobbie));
////            }
////            case "rfc" -> {
////                String rfc = busqueda;
////
////                List<Cliente> clientesPorRfc = clienteService.buscarClientePorRfc(ClienteStatus.ACTIVE.toString(), rfc);
////                tablaClientes.setItems(FXCollections.observableList(clientesPorRfc));
////            }
////
////        }//switch
//
//        terminoBusquedaActual = busqueda;
//        modoBusqueda = !busqueda.trim().isEmpty();
//        atributoBusquedaClientes.setValue(seleccion);
//
//        // Reinicia el paginador para la búsqueda
//        paginadorClientes.setPageFactory(this::crearPaginaClientesconFiltro);
//        paginadorClientes.setCurrentPageIndex(0);
//
//    }//buscarCliente

    private void buscarCliente(String seleccion, String busqueda) {

        terminoBusquedaActual = busqueda;
        modoBusqueda = !busqueda.trim().isEmpty();
        atributoBusquedaClientes.setValue(seleccion);

        // Obtener primera página filtrada
        Page<Cliente> paginaFiltrada;

        switch (seleccion) {
            case "nombre" -> paginaFiltrada =
                    clienteService.buscarClientePorNombrePaginado(
                            StatusCliente.ACTIVE.toString(), busqueda, 0, CLIENTES_POR_PAGINA);

            case "telefono" -> paginaFiltrada =
                    clienteService.buscarClientePorNumTelefonoPaginado(
                            StatusCliente.ACTIVE.toString(), busqueda, 0, CLIENTES_POR_PAGINA);

            case "estado" -> paginaFiltrada =
                    clienteService.buscarClientePorEstadoPaginado(
                            StatusCliente.ACTIVE.toString(), busqueda, 0, CLIENTES_POR_PAGINA);

            case "ciudad" -> paginaFiltrada =
                    clienteService.buscarClientePorCiudadPaginado(
                            StatusCliente.ACTIVE.toString(), busqueda, 0, CLIENTES_POR_PAGINA);

            case "domicilio" -> paginaFiltrada =
                    clienteService.buscarClientePorDomicilioPaginado(
                            StatusCliente.ACTIVE.toString(), busqueda, 0, CLIENTES_POR_PAGINA);

            case "hobbie" -> paginaFiltrada =
                    clienteService.buscarClientePorHobbiePaginado(
                            StatusCliente.ACTIVE.toString(), busqueda, 0, CLIENTES_POR_PAGINA);

            case "rfc" -> paginaFiltrada =
                    clienteService.buscarClientePorRfcPaginado(
                            StatusCliente.ACTIVE.toString(), busqueda, 0, CLIENTES_POR_PAGINA);

            default -> paginaFiltrada =
                    clienteService.listarClientesConVehiculosPaginado(
                            StatusCliente.ACTIVE.toString(), 0, CLIENTES_POR_PAGINA);
        }

        // ACTUALIZA EL PAGECOUNT CON EL TOTAL DE COINCIDENCIAS
        int totalPaginas = paginaFiltrada.getTotalPages();
        paginadorClientes.setPageCount(Math.max(totalPaginas, 1));

        // Cambia el PageFactory
        paginadorClientes.setPageFactory(this::crearPaginaClientesconFiltro);

        // Ir a la primera página
        paginadorClientes.setCurrentPageIndex(0);
    }


    public void buscarCliente(String busqueda) {
        terminoBusquedaActual = busqueda;
        modoBusqueda = !busqueda.trim().isEmpty();

        paginadorClientes.setCurrentPageIndex(0); // Reinicia la paginación
        paginadorClientes.setPageFactory(this::crearPaginaClientes);
    }

    @FXML
    public void actualizarTabla(ActionEvent actionEvent) {
        buscarClienteBuscador.setText("");
        atributoBusquedaClientes.setValue(null);

        // Salir del modo búsqueda
        modoBusqueda = false;
        terminoBusquedaActual = "";

        // Reinicia paginador y carga los clientes
        paginadorClientes.setPageFactory(this::crearPaginaClientes);
        paginadorClientes.setCurrentPageIndex(0);
    }


}//clase
