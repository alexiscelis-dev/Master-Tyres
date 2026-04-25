package com.mastertyres.controllers.fxControllers.cliente;

import com.mastertyres.cliente.entity.Cliente;
import com.mastertyres.cliente.entity.StatusCliente;
import com.mastertyres.cliente.service.ClienteService;
import com.mastertyres.common.exeptions.ClienteException;
import com.mastertyres.common.interfaces.*;
import com.mastertyres.common.service.NotaUtils;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.ApplicationContextProvider;
import com.mastertyres.common.utils.FechaUtils;
import com.mastertyres.components.fxComponents.loader.LoadingComponentController;
import com.mastertyres.controllers.fxControllers.ventanaPrincipal.VentanaPrincipalController;
import com.mastertyres.vehiculo.entity.Vehiculo;
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
import java.time.LocalDate;
import java.util.List;

import static com.mastertyres.common.utils.FechaUtils.getFechaFormateadaSegundos;
import static com.mastertyres.common.utils.MensajesAlert.*;
import static com.mastertyres.common.utils.MenuContextSetting.disableMenu;


@Component
public class ClienteController implements IVentanaPrincipal, IFxController, ILoader, ICleanable, IRestaurableDatos {

    private VentanaPrincipalController ventanaPrincipalController;


    @Override
    public void setVentanaPrincipalController(VentanaPrincipalController controller) {
        this.ventanaPrincipalController = controller;
    }


    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Cliente> tablaClientes;
    @FXML
    private TableColumn<Cliente, String> colTipoCliente;
    @FXML
    private TableColumn<Cliente, String> colNombreEmpresa;
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
    private TableColumn<Cliente, String> colCorreo;
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
    private DatePicker dpBuscarCliente, dpClienteFin;
    @FXML
    private CheckBox chkRangoCliente;
    @FXML
    private Label lblHastaCliente;
    @FXML
    private Label statusLabel;
    @FXML
    private HBox limpiarChoiceBox;
    @FXML
    private Button refrescar;
    @FXML
    private Button btnBuscar;
    @FXML
    private Pagination paginadorClientes;

    private PauseTransition delayQuery = new PauseTransition(Duration.millis(300)); //evita que se ejecuta una query cada vez que el usuario
    //presiona una tecla hace un delay
    private PauseTransition pauseTransition;

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private NotaUtils notaUtils;

    private static final int CLIENTES_POR_PAGINA = 20;
    private List<Cliente> todosLosClientes;
    private String terminoBusquedaActual = "";
    private String terminoBusquedaFechaActual = "";
    private boolean modoBusqueda = false;

    private LoadingComponentController loadingOverlayController;

    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingOverlayController = loading;

    }

    @FXML
    private void initialize() {

        configuraciones();
        listeners();
        cargarClientes();

    }// initialize

    private void actualizarVisibilidaddeDatePicker(boolean esFecha) {
        // Alternar entre Texto y Fecha
        buscarClienteBuscador.setVisible(!esFecha);
        buscarClienteBuscador.setManaged(!esFecha);

        dpBuscarCliente.setVisible(esFecha);
        dpBuscarCliente.setManaged(esFecha);
        chkRangoCliente.setVisible(esFecha);
        chkRangoCliente.setManaged(esFecha);

        if (!esFecha) {
            chkRangoCliente.setSelected(false);
            actualizarVisibilidadRango(false);
        }
    }

    private void actualizarVisibilidadRango(boolean mostrar) {
        lblHastaCliente.setVisible(mostrar);
        lblHastaCliente.setManaged(mostrar);
        dpClienteFin.setVisible(mostrar);
        dpClienteFin.setManaged(mostrar);
    }

    private void configurarBuscador(String criterio) {
        boolean esFecha = "Fecha de nacimiento".equals(criterio) || "Fecha de registro".equals(criterio);

        // Intercambiar visibilidad
        buscarClienteBuscador.setVisible(!esFecha);
        buscarClienteBuscador.setManaged(!esFecha);

        dpBuscarCliente.setVisible(esFecha);
        dpBuscarCliente.setManaged(esFecha);

        // Limpiar campos al cambiar de filtro para evitar búsquedas cruzadas
        buscarClienteBuscador.clear();
        dpBuscarCliente.setValue(null);
    }

    @Override
    public void configuraciones() {

        disableMenu(rootPane);
        atributoBusquedaClientes.setValue("Sin Filtro");

        notaUtils.descripcionComponent(refrescar,"Refrescar");
        notaUtils.descripcionComponent(btnBuscar,"Buscar");
        notaUtils.descripcionComponent(atributoBusquedaClientes,"Filtrar búsqueda");

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
                            getClass().getResource("/styles-css/Lista.css").toExternalForm()
                    );

                    Popup listViewPopup = new Popup();
                    listViewPopup.getContent().add(listaOpciones);
                    listViewPopup.setAutoHide(true);


                    listaOpciones.setOnMouseClicked(e -> {
                        String seleccion = listaOpciones.getSelectionModel().getSelectedItem();


                        if (seleccion != null) {
                            switch (seleccion) {

                                case "Eliminar" -> {
                                    String cliente = (clienteSeleccionado.getNombre() != null ? clienteSeleccionado.getNombre() : "") + " " +
                                            (clienteSeleccionado.getApellido() != null ? clienteSeleccionado.getApellido() : "") + " " +
                                            (clienteSeleccionado.getSegundoApellido() != null ? clienteSeleccionado.getSegundoApellido() : "");

                                    boolean eliminar = mostrarConfirmacion(
                                            "Confirmar eliminación",
                                            "Eliminar cliente",
                                            "¿Está seguro de que desea eliminar al cliente seleccionado? Esta acción no se puede deshacer.",
                                            "Eliminar",
                                            "Cancelar"
                                    );

                                    if (eliminar) {
                                        taskService.runTask(
                                                loadingOverlayController,
                                                () -> {
                                                    clienteService.eliminarCliente(StatusCliente.INACTIVE.toString(), clienteSeleccionado.getClienteId());
                                                    return null;
                                                },
                                                (resultado) -> {

                                                    refrescarTabla();
                                                    mostrarInformacion(
                                                            "Operación completada",
                                                            "Cliente eliminado",
                                                            "El cliente ha sido eliminado del sistema exitosamente."
                                                    );
                                                },
                                                (ex) -> {
                                                    if (ex.getCause() instanceof InterruptedException || ex.getCause() instanceof java.util.concurrent.CancellationException) {
                                                        mostrarWarning(
                                                                "Operación cancelada",
                                                                "Acción interrumpida",
                                                                "La acción fue cancelada por el usuario o el sistema."
                                                        );
                                                    } else if (ex instanceof ClienteException) {
                                                        mostrarExcepcionThrowable(
                                                                "Error al eliminar cliente",
                                                                "Problema con el registro del cliente",
                                                                "" + ex.getMessage(),
                                                                ex);
                                                    } else {
                                                        mostrarExcepcionThrowable(
                                                                "Error inesperado",
                                                                "Fallo en la operación",
                                                                "Ocurrió un error inesperado al intentar eliminar el cliente seleccionado.",
                                                                ex
                                                        );
                                                    }
                                                }, null
                                        );
                                    } else {
                                        mostrarInformacion(
                                                "Información",
                                                "Acción cancelada",
                                                "La operación ha sido cancelada."
                                        );
                                    }
                                }//Eliminar

                                case "Editar" -> {
                                    try {
                                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/cliente/EditarCliente.fxml"));
                                        loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
                                        Parent root = loader.load();

                                        EditarClienteController controller = loader.getController();
                                        controller.editarCliente(clienteSeleccionado); // pasa el cliente actual
                                        controller.setInitializeLoading(loadingOverlayController);

                                        Stage stage = new Stage(StageStyle.UNDECORATED);
                                        stage.setTitle("Editar Cliente");
                                        stage.setScene(new Scene(root));
                                        stage.setResizable(false);
                                        stage.initModality(Modality.APPLICATION_MODAL);

                                        stage.showAndWait();


                                        refrescarTabla();

                                    } catch (IOException ex) {
                                        mostrarExcepcion(
                                                "Error de carga",
                                                "No se pudo inicializar la vista",
                                                "Ocurrió un error al intentar cargar la interfaz. Por favor, inténtelo de nuevo más tarde.",
                                                ex
                                        );
                                    }
                                }

                                case "Ver informacion" -> {
                                    try {
                                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/cliente/DetalleCliente.fxml"));
                                        loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
                                        Parent root = loader.load();

                                        DetalleCliente controller = loader.getController();
                                        controller.informacionCliente(clienteSeleccionado); // pasa el cliente actual

                                        Stage stage = new Stage(StageStyle.UNDECORATED);
                                        stage.setTitle("Informacion Cliente");
                                        stage.setScene(new Scene(root));
                                        stage.initModality(Modality.APPLICATION_MODAL);
                                        stage.showAndWait();
                                    } catch (IOException ex) {
                                        mostrarExcepcion(
                                                "Error de visualización",
                                                "Fallo al cargar registros",
                                                "No se pudieron cargar los datos solicitados. Por favor, verifique su conexión e inténtelo de nuevo.",
                                                ex
                                        );
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

                                            if (pauseTransition != null) {
                                                pauseTransition.stop();
                                            }
                                            pauseTransition = new PauseTransition(Duration.seconds(2.5));
                                            pauseTransition.setOnFinished(e1 -> {
                                                statusLabel.setText("");
                                                statusLabel.setVisible(false);
                                            });
                                            pauseTransition.play();

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
                                                vehiculosStr + " " + (item.getRfc() != null ? item.getRfc() : "") + " " + getFechaFormateadaSegundos(item.getCreated_at());

                                        ClipboardContent content = new ClipboardContent();
                                        content.putString(filaCopiada);
                                        Clipboard.getSystemClipboard().setContent(content);

                                        statusLabel.setVisible(true);
                                        statusLabel.setText("Fila copiada");

                                        if (pauseTransition != null) {
                                            pauseTransition.stop();
                                        }
                                        pauseTransition = new PauseTransition(Duration.seconds(2.5));
                                        pauseTransition.setOnFinished(e1 -> {
                                                    statusLabel.setText("");
                                                    statusLabel.setVisible(false);
                                                }
                                        );
                                        pauseTransition.play();

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

    }//configuraciones

    private void refrescarTabla() {
        // Limpiar búsqueda
        modoBusqueda = false;
        terminoBusquedaActual = "";
        buscarClienteBuscador.clear();
        atributoBusquedaClientes.setValue("Sin Filtro");

        // Recalcular paginación
        long totalClientes = clienteService.contarClientesActivos(StatusCliente.ACTIVE.toString());
        int totalPaginas = (int) Math.ceil((double) totalClientes / CLIENTES_POR_PAGINA);
        paginadorClientes.setPageCount(Math.max(totalPaginas, 1));

        // Cargar primera página
        Page<Cliente> primerasPagina = clienteService.listarClientesConVehiculosPaginado(
                StatusCliente.ACTIVE.toString(),
                0,
                CLIENTES_POR_PAGINA
        );

        // CRÍTICO: Actualizar items de la tabla
        tablaClientes.setItems(FXCollections.observableArrayList(primerasPagina.getContent()));

        // Resetear el PageFactory
        paginadorClientes.setPageFactory(this::crearPaginaClientes);

        // Ir a página 0
        paginadorClientes.setCurrentPageIndex(0);

        // Forzar refresh visual
        tablaClientes.refresh();
    }

    @Override
    public void listeners() {

        //buscar mientras escribes
        buscarClienteBuscador.setOnKeyReleased(event -> {

            if (event.getCode() == KeyCode.ENTER) return; // ignorar enter aquí

            delayQuery.setOnFinished(e -> {

                String seleccion = atributoBusquedaClientes.getValue();
                String busqueda = buscarClienteBuscador.getText();

                //  SOLO ejecutar búsqueda general si NO hay filtro
                if (seleccion == null || seleccion.isEmpty()) {

                    if (busqueda == null || busqueda.isEmpty()) {
                        resetBusqueda();
                        return;
                    }

                    // Búsqueda general mientras escribe
                    buscarCliente(busqueda);
                }
                // Si sí hay filtro → no hace nada, porque solo se buscan al presionar ENTER
            });

            delayQuery.playFromStart();
        });

        //Enter buscar
        buscarClienteBuscador.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {

                String seleccion = atributoBusquedaClientes.getValue();
                String busqueda = buscarClienteBuscador.getText();

                // SOLO funciona si hay un filtro seleccionado
                if (seleccion != null && !seleccion.isEmpty()) {

                    // Si el texto está vacío, resetea y detén
                    if (busqueda == null || busqueda.isEmpty() && seleccion == null) {
                        resetBusqueda();
                        return;
                    }

                    // Ejecutar búsqueda específica
                    buscarCliente(seleccion.toLowerCase(), busqueda);
                }
            }
        });

        // Listener para detectar cambios en el ChoiceBox

        // Listener para el ChoiceBox del Inventario
        atributoBusquedaClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean esFecha = "Fecha de registro".equals(newVal);
            actualizarVisibilidaddeDatePicker(esFecha);
        });
        atributoBusquedaClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean esFecha = "Fecha de registro".equals(newVal) || "Fecha de nacimiento".equals(newVal);
            actualizarVisibilidaddeDatePicker(esFecha);
        });

        // Listener para el CheckBox de Rango
        chkRangoCliente.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            actualizarVisibilidadRango(isSelected);
        });

        buscarClienteBuscador.setOnAction(event -> {
            String seleccion = atributoBusquedaClientes.getValue();

        });


        //pone en null la lista de ChoiceBox
        limpiarChoiceBox.setOnMouseClicked(event -> {

            if ((event.getButton() == MouseButton.PRIMARY || event.getButton() == MouseButton.MIDDLE) && event.getClickCount() == 2)
                atributoBusquedaClientes.setValue(null); // pone el valor en null para que vuelva a buscar dinamicamente
            resetBusqueda();
        });

    }//listeners

    @FXML
    private void agregarCliente(ActionEvent event) {
        ventanaPrincipalController.viewContent(
                null, // no se requiere el MouseEvent
                "/fxmlViews/cliente/AgregarCliente.fxml",
                "Agregar cliente"
        );
        ventanaPrincipalController.cambiarPaginaEtiqueta.setText("AGREGAR CLIENTE");
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
            String key = atributoBusquedaClientes.getValue();
            switch (key.toLowerCase()) {
                case "sin filtro" -> paginaClientes = clienteService.buscadorClientesPaginado(
                        StatusCliente.ACTIVE.toString(), terminoBusquedaActual, indicePagina, CLIENTES_POR_PAGINA);
                case "nombre" -> paginaClientes = clienteService.buscarClientePorNombrePaginado(
                        StatusCliente.ACTIVE.toString(), terminoBusquedaActual, indicePagina, CLIENTES_POR_PAGINA);
                case "nombre de empresa" -> paginaClientes = clienteService.buscarClientePornombreEmpresaPaginado(
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
                case "correo" -> paginaClientes = clienteService.buscarClientePorCorreoPaginado(
                        StatusCliente.ACTIVE.toString(), terminoBusquedaActual, indicePagina, CLIENTES_POR_PAGINA);
//
                case "fecha de nacimiento" -> {
                    LocalDate fechaInicio = dpBuscarCliente.getValue();

                    if (chkRangoCliente.isSelected()) {
                        LocalDate fechaFin = dpClienteFin.getValue();

                        if (fechaInicio == null || fechaFin == null) {
                            mostrarWarning(
                                    "Datos incompletos",
                                    "Rango de fechas no válido",
                                    "Por favor, seleccione ambas fechas para establecer el rango de búsqueda correctamente."
                            );
                            paginaClientes = clienteService.listarClientesConVehiculosPaginado(StatusCliente.ACTIVE.toString(), indicePagina, CLIENTES_POR_PAGINA);
                        } else {
                            if (fechaInicio.isAfter(fechaFin)) {
                                LocalDate aux = fechaInicio;
                                fechaInicio = fechaFin;
                                fechaFin = aux;
                            }
                            // CORRECCIÓN AQUÍ: Agregar .toString() a ambos
                            paginaClientes = clienteService.buscarClientePorCumpleanosRangoPaginado(
                                    StatusCliente.ACTIVE.toString(),
                                    fechaInicio.toString(),
                                    fechaFin.toString(),
                                    indicePagina,
                                    CLIENTES_POR_PAGINA
                            );
                        }
                    } else {
                        if (fechaInicio == null) {
                            mostrarWarning(
                                    "Datos incompletos",
                                    "Fecha no seleccionada",
                                    "Por favor, seleccione una fecha en el calendario antes de continuar."
                            );
                            paginaClientes = clienteService.listarClientesConVehiculosPaginado(StatusCliente.ACTIVE.toString(), indicePagina, CLIENTES_POR_PAGINA);
                        } else {
                            // Aquí ya tenías el .toString(), lo cual está bien
                            paginaClientes = clienteService.buscarClientePorCumpleanosPaginado(
                                    StatusCliente.ACTIVE.toString(),
                                    fechaInicio.toString(),
                                    indicePagina,
                                    CLIENTES_POR_PAGINA
                            );
                        }
                    }
                }
//
                case "fecha de registro" -> {
                    LocalDate fechaInicio = dpBuscarCliente.getValue();

                    // 1. Verificamos si es búsqueda por RANGO
                    if (chkRangoCliente.isSelected()) {
                        LocalDate fechaFin = dpClienteFin.getValue();

                        // Validación: Si faltan fechas en el rango
                        if (fechaInicio == null || fechaFin == null) {
                            mostrarWarning(
                                    "Datos incompletos",
                                    "Rango de fechas no válido",
                                    "Por favor, seleccione ambas fechas para establecer el rango de búsqueda correctamente."
                            );
                            // Cargamos lista normal por defecto para no dejar la vista rota
                            paginaClientes = clienteService.listarClientesConVehiculosPaginado(StatusCliente.ACTIVE.toString(), indicePagina, CLIENTES_POR_PAGINA);
                        } else {
                            // Ordenar fechas si el usuario las puso al revés
                            if (fechaInicio.isAfter(fechaFin)) {
                                LocalDate aux = fechaInicio;
                                fechaInicio = fechaFin;
                                fechaFin = aux;
                            }
                            paginaClientes = clienteService.buscarClientePorRegistroRangoPaginado(StatusCliente.ACTIVE.toString(), fechaInicio, fechaFin, indicePagina, CLIENTES_POR_PAGINA);
                        }
                    }
                    // 2. Búsqueda por FECHA ÚNICA
                    else {
                        if (fechaInicio == null) {
                            mostrarWarning(
                                    "Datos incompletos",
                                    "Fecha no seleccionada",
                                    "Por favor, seleccione una fecha en el calendario antes de continuar."
                            );
                            paginaClientes = clienteService.listarClientesConVehiculosPaginado(StatusCliente.ACTIVE.toString(), indicePagina, CLIENTES_POR_PAGINA);
                        } else {
                            paginaClientes = clienteService.buscarClientePorRegistroPaginado(StatusCliente.ACTIVE.toString(), fechaInicio.toString(), indicePagina, CLIENTES_POR_PAGINA);
                        }
                    }

                }
                default ->
                        paginaClientes = clienteService.listarClientesConVehiculosPaginado(StatusCliente.ACTIVE.toString(), indicePagina, CLIENTES_POR_PAGINA);
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

        colTipoCliente.setCellValueFactory(data ->
                new SimpleStringProperty(valorONull(data.getValue().getTipoCliente()))
        );

        colNombreEmpresa.setCellValueFactory(data ->
                new SimpleStringProperty(valorONull(data.getValue().getNombreEmpresa()))
        );

        colNombre.setCellValueFactory(data ->
                new SimpleStringProperty(nombreCompleto(data.getValue()))
        );

        colTelefono.setCellValueFactory(data ->
                new SimpleStringProperty(valorONull(data.getValue().getNumTelefono()))
        );

        colCorreo.setCellValueFactory(data ->
                new SimpleStringProperty(valorONull(data.getValue().getCorreo()))
        );

        colEstado.setCellValueFactory(data ->
                new SimpleStringProperty(valorONull(data.getValue().getEstado()))
        );

        colCiudad.setCellValueFactory(data ->
                new SimpleStringProperty(valorONull(data.getValue().getCiudad()))
        );

        colDomicilio.setCellValueFactory(data ->
                new SimpleStringProperty(valorONull(data.getValue().getDomicilio()))
        );

        colHobbie.setCellValueFactory(data ->
                new SimpleStringProperty(valorONull(data.getValue().getHobbie()))
        );

        colRFC.setCellValueFactory(data ->
                new SimpleStringProperty(valorONull(data.getValue().getRfc()))
        );

        colGenero.setCellValueFactory(data ->
                new SimpleStringProperty(formatearGenero(data.getValue().getGenero()))
        );

        colRegistro.setCellValueFactory(data ->
                new SimpleStringProperty(FechaUtils.formatearFechaHora(data.getValue().getCreated_at()))
        );

        colCumpleanos.setCellValueFactory(data ->
                new SimpleStringProperty(FechaUtils.formatearFecha(data.getValue().getFechaCumple()))
        );
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

    private String valorONull(String valor) {
        return (valor == null || valor.isBlank()) ? "N/D" : valor;
    }

    private String formatearGenero(String genero) {
        if (genero == null || genero.isBlank()) return "N/D";

        switch (genero.trim().toUpperCase()) {
            case "M":
                return "Masculino";
            case "F":
                return "Femenino";
            case "O":
                return "Otro";
            default:
                return "N/D";
        }
    }

    private String nombreCompleto(Cliente c) {
        String n = (c.getNombre() == null ? "" : c.getNombre().trim());
        String a1 = (c.getApellido() == null ? "" : c.getApellido().trim());
        String a2 = (c.getSegundoApellido() == null ? "" : c.getSegundoApellido().trim());

        String full = String.join(" ", n, a1, a2).trim();

        return full.isBlank() ? "N/D" : full;
    }

    private void cargarDatosClientes() {
        try {
            long totalClientes = clienteService.contarClientesActivos(StatusCliente.ACTIVE.toString());
            int totalPaginas = (int) Math.ceil((double) totalClientes / CLIENTES_POR_PAGINA);
            paginadorClientes.setPageCount(Math.max(totalPaginas, 1));
            paginadorClientes.setPageFactory(this::crearPaginaClientes);

            // Muestra la primera página
            paginadorClientes.setCurrentPageIndex(0);

        } catch (Exception e) {
           mostrarExcepcion(
                    "Error de visualización",
                    "Fallo al cargar registros",
                    "No se pudieron cargar los datos solicitados. Por favor, verifique su conexión e inténtelo de nuevo.",
                    e
            );
        }
    }

    private void buscarCliente(String seleccion, String busqueda) {

        terminoBusquedaActual = busqueda;
        terminoBusquedaFechaActual = dpBuscarCliente.getValue() != null ? dpBuscarCliente.getValue().toString() : LocalDate.now().toString();
        modoBusqueda = !busqueda.trim().isEmpty();
        //atributoBusquedaClientes.setValue(seleccion);

        // Obtener primera página filtrada
        Page<Cliente> paginaFiltrada;

        switch (seleccion) {
            case "sin filtro" -> paginaFiltrada =
                    clienteService.buscadorClientesPaginado(
                            StatusCliente.ACTIVE.toString(), busqueda, 0, CLIENTES_POR_PAGINA);
            case "nombre" -> paginaFiltrada =
                    clienteService.buscarClientePorNombrePaginado(
                            StatusCliente.ACTIVE.toString(), busqueda, 0, CLIENTES_POR_PAGINA);

            case "nombre de empresa" -> paginaFiltrada =
                    clienteService.buscarClientePornombreEmpresaPaginado(
                            StatusCliente.ACTIVE.toString(), busqueda, 0, CLIENTES_POR_PAGINA);

            case "telefono" -> paginaFiltrada =
                    clienteService.buscarClientePorNumTelefonoPaginado(
                            StatusCliente.ACTIVE.toString(), busqueda, 0, CLIENTES_POR_PAGINA);

            case "correo" -> paginaFiltrada =
                    clienteService.buscarClientePorCorreoPaginado(
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

            case "fecha de nacimiento" -> {
                LocalDate fechaInicio = dpBuscarCliente.getValue();

                if (chkRangoCliente.isSelected()) {
                    LocalDate fechaFin = dpClienteFin.getValue();

                    if (fechaInicio == null || fechaFin == null) {
                        mostrarWarning(
                                "Datos incompletos",
                                "Rango de fechas no válido",
                                "Por favor, seleccione ambas fechas para establecer el rango de búsqueda correctamente."
                        );
                        paginaFiltrada = clienteService.listarClientesConVehiculosPaginado(StatusCliente.ACTIVE.toString(), 0, CLIENTES_POR_PAGINA);
                    } else {
                        if (fechaInicio.isAfter(fechaFin)) {
                            LocalDate aux = fechaInicio;
                            fechaInicio = fechaFin;
                            fechaFin = aux;
                        }
                        // CORRECCIÓN AQUÍ: Agregar .toString() a ambos
                        paginaFiltrada = clienteService.buscarClientePorCumpleanosRangoPaginado(
                                StatusCliente.ACTIVE.toString(),
                                fechaInicio.toString(),
                                fechaFin.toString(),
                                0,
                                CLIENTES_POR_PAGINA
                        );
                    }
                } else {
                    if (fechaInicio == null) {
                        mostrarWarning(
                                "Datos incompletos",
                                "Fecha no seleccionada",
                                "Por favor, seleccione una fecha en el calendario antes de continuar."
                        );
                        paginaFiltrada = clienteService.listarClientesConVehiculosPaginado(StatusCliente.ACTIVE.toString(), 0, CLIENTES_POR_PAGINA);
                    } else {
                        // Aquí ya tenías el .toString(), lo cual está bien
                        paginaFiltrada = clienteService.buscarClientePorCumpleanosPaginado(
                                StatusCliente.ACTIVE.toString(),
                                fechaInicio.toString(),
                                0,
                                CLIENTES_POR_PAGINA
                        );
                    }
                }
            }


            case "fecha de registro" -> {
                LocalDate fechaInicio = dpBuscarCliente.getValue();

                // 1. Verificamos si es búsqueda por RANGO
                if (chkRangoCliente.isSelected()) {
                    LocalDate fechaFin = dpClienteFin.getValue();

                    // Validación: Si faltan fechas en el rango
                    if (fechaInicio == null || fechaFin == null) {
                        mostrarWarning(
                                "Datos incompletos",
                                "Rango de fechas no válido",
                                "Por favor, seleccione ambas fechas para establecer el rango de búsqueda correctamente."
                        );
                        // Cargamos lista normal por defecto para no dejar la vista rota
                        paginaFiltrada = clienteService.listarClientesConVehiculosPaginado(StatusCliente.ACTIVE.toString(), 0, CLIENTES_POR_PAGINA);
                    } else {
                        // Ordenar fechas si el usuario las puso al revés
                        if (fechaInicio.isAfter(fechaFin)) {
                            LocalDate aux = fechaInicio;
                            fechaInicio = fechaFin;
                            fechaFin = aux;
                        }
                        paginaFiltrada = clienteService.buscarClientePorRegistroRangoPaginado(StatusCliente.ACTIVE.toString(), fechaInicio, fechaFin, 0, CLIENTES_POR_PAGINA);
                    }
                }
                // 2. Búsqueda por FECHA ÚNICA
                else {
                    if (fechaInicio == null) {
                        mostrarWarning(
                                "Datos incompletos",
                                "Fecha no seleccionada",
                                "Por favor, seleccione una fecha en el calendario antes de continuar."
                        );
                        paginaFiltrada = clienteService.listarClientesConVehiculosPaginado(StatusCliente.ACTIVE.toString(), 0, CLIENTES_POR_PAGINA);
                    } else {
                        paginaFiltrada = clienteService.buscarClientePorRegistroPaginado(StatusCliente.ACTIVE.toString(), fechaInicio.toString(), 0, CLIENTES_POR_PAGINA);
                    }
                }

            }

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
    }//buscarCliente

    public void buscarCliente(String busqueda) {
        terminoBusquedaActual = busqueda;
        modoBusqueda = !busqueda.trim().isEmpty();

        if (modoBusqueda) {
            //  total de resultados para el buscador general
            long totalResultados = clienteService.contarClientesPorBusquedaGeneral(
                    StatusCliente.ACTIVE.toString(),
                    terminoBusquedaActual
            );

            int totalPaginas = (int) Math.ceil((double) totalResultados / CLIENTES_POR_PAGINA);

            paginadorClientes.setPageCount(Math.max(totalPaginas, 1));
        } else {
            // Si no hay búsqueda, restaurar paginación normal
            long totalClientes = clienteService.contarClientesActivos(StatusCliente.ACTIVE.toString());
            int totalPaginas = (int) Math.ceil((double) totalClientes / CLIENTES_POR_PAGINA);
            paginadorClientes.setPageCount(Math.max(totalPaginas, 1));
        }

        //  Asignar PageFactory
        paginadorClientes.setPageFactory(this::crearPaginaClientes);

        //  Reiniciar a la primera página
        paginadorClientes.setCurrentPageIndex(0);
    }

    private void resetBusqueda() {
        modoBusqueda = false;
        terminoBusquedaActual = "";
        atributoBusquedaClientes.setValue(null);
        buscarClienteBuscador.clear();

        paginadorClientes.setPageFactory(this::crearPaginaClientes);
        paginadorClientes.setCurrentPageIndex(0);

        cargarDatosClientes();  // vuelve a cargar conteo y items normales
    }

    @FXML
    public void refrescar(ActionEvent actionEvent) {
        buscarClienteBuscador.setText("");
        atributoBusquedaClientes.setValue(null);

        // Salir del modo búsqueda
        modoBusqueda = false;
        terminoBusquedaActual = "";

        // Reinicia paginador y carga los clientes


        paginadorClientes.setPageFactory(this::crearPaginaClientes);
        paginadorClientes.setCurrentPageIndex(0);
        cargarDatosClientes();
        resetBusqueda();
        refrescarTabla();
    }

    public void accionBuscarCliente(ActionEvent actionEvent) {

        String seleccion = atributoBusquedaClientes.getValue();

        String busqueda;
        if (atributoBusquedaClientes.getValue().equals("Fecha de nacimiento") || atributoBusquedaClientes.getValue().equals("Fecha de registro")) {
            busqueda = dpBuscarCliente.getValue().toString();
        } else {
            busqueda = buscarClienteBuscador.getText();
        }


        // SOLO funciona si hay un filtro seleccionado
        if (seleccion != null && !seleccion.isEmpty()) {

            // Si el texto está vacío, resetea y detén
            if (busqueda == null || busqueda.isEmpty() && seleccion == null) {
                resetBusqueda();
                return;
            }

            // Ejecutar búsqueda específica
            buscarCliente(seleccion.toLowerCase(), busqueda);
        }

    }

    @Override
    public void cleanup() {
        // 1. Detener animaciones (previene fugas de CPU)
        if (delayQuery != null) {
            delayQuery.stop();
        }

        if (pauseTransition != null) {
            pauseTransition.stop();
        }

    }

    @Override
    public void restaurarEstadoInicial() {
        // 1. Limpiar campos de búsqueda
        if (buscarClienteBuscador != null) {
            buscarClienteBuscador.clear();
        }

        // 2. Resetear ChoiceBox de filtros
        if (atributoBusquedaClientes != null) {
            atributoBusquedaClientes.setValue("Sin Filtro");
        }

        // 3. Limpiar DatePickers
        if (dpBuscarCliente != null) {
            dpBuscarCliente.setValue(null);
        }
        if (dpClienteFin != null) {
            dpClienteFin.setValue(null);
        }

        // 4. Resetear CheckBox
        if (chkRangoCliente != null) {
            chkRangoCliente.setSelected(false);
        }

        // 5. Resetear variables de estado
        terminoBusquedaActual = "";
        terminoBusquedaFechaActual = "";
        modoBusqueda = false;

        // 6. Recargar datos sin filtros
        cargarDatosClientes();
    }


}//class
