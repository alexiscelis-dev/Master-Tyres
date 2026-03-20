package com.mastertyres.controllers.fxControllers.vehiculo;


import com.mastertyres.cliente.model.StatusCliente;
import com.mastertyres.common.exeptions.VehiculoException;
import com.mastertyres.common.interfaces.*;
import com.mastertyres.common.service.NotaUtils;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.ApplicationContextProvider;
import com.mastertyres.common.utils.MensajesAlert;
import com.mastertyres.components.fxComponents.loader.LoadingComponentController;
import com.mastertyres.controllers.fxControllers.ventanaPrincipal.VentanaPrincipalController;
import com.mastertyres.vehiculo.model.StatusVehiculo;
import com.mastertyres.vehiculo.model.VehiculoDTO;
import com.mastertyres.vehiculo.service.VehiculoService;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleObjectProperty;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static com.mastertyres.common.utils.FechaUtils.getFechaFormateada;
import static com.mastertyres.common.utils.FechaUtils.getFechaFormateadaSegundos;
import static com.mastertyres.common.utils.MensajesAlert.*;
import static com.mastertyres.common.utils.MenuContextSetting.disableMenu;

@Component
public class VehiculoController implements IVentanaPrincipal, IFxController, ILoader, ICleanable, IRestaurableDatos {

    @FXML
    private AnchorPane rootPane;
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
    private TableColumn<VehiculoDTO, String> colObservaciones;
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
    @FXML
    private Label statusLabel;
    @FXML
    private HBox limpiarChoiceBox;
    @FXML
    private DatePicker dpVehiculoInicio, dpVehiculoFin;
    @FXML
    private CheckBox chkRangoFechas;
    @FXML
    private Label lblHasta;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnRefrescar;
    @FXML
    private Button btnModificarMarcaModeloCategoria;
    @FXML
    private Button btnAgregarVehiculo;


    private PauseTransition delayQuery = new PauseTransition(Duration.millis(300)); //evita que se ejecuta una query cada vez que el usuario
    private PauseTransition pauseTransition;

    //presiona una tecla hace un delay
    private VentanaPrincipalController ventanaPrincipalController;

    @Override
    public void setVentanaPrincipalController(VentanaPrincipalController controller) {
        this.ventanaPrincipalController = controller;

    }

    @Autowired
    private VehiculoService vehiculoService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private NotaUtils notaUtils;

    @FXML
    private Pagination paginadorVehiculos;
    private static final int VEHICULO_POR_PAGINA = 20;
    private List<VehiculoDTO> todosLosVehiculos;
    private String terminoBusquedaActual = "";
    private boolean modoBusqueda = false;
    private LoadingComponentController loadingOverlayController;

    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingOverlayController = loading;
    }

    @FXML
    public void initialize() {

        configuraciones();
        listeners();
        cargarVehiculos();


    }//initialize

    private void ConfigurarMostarDatePicker1(String criterio) {
        boolean esFecha = "Ultimo Servicio".equals(criterio) || "Fecha Registro".equals(criterio);

        // Alternar TextField vs Controles de Fecha
        buscarVehiculoBuscador.setVisible(!esFecha);
        buscarVehiculoBuscador.setManaged(!esFecha);

        dpVehiculoInicio.setVisible(esFecha);
        dpVehiculoInicio.setManaged(esFecha);
        chkRangoFechas.setVisible(esFecha);
        chkRangoFechas.setManaged(esFecha);

        // Si cambiamos a texto, ocultamos el resto del rango forzosamente
        if (!esFecha) {
            ocultarRangoSegundoNivel();
            chkRangoFechas.setSelected(false);
        }
    }

    private void ConfigurarMostrarDatepicker2(Boolean criterio) {
        lblHasta.setVisible(criterio);
        lblHasta.setManaged(criterio);
        dpVehiculoFin.setVisible(criterio);
        dpVehiculoFin.setManaged(criterio);
    }

    @Override
    public void configuraciones() {

        disableMenu(rootPane);
        notaUtils.descripcionComponent(btnBuscar,"Buscar");
        notaUtils.descripcionComponent(btnRefrescar,"Refrescar");
        notaUtils.descripcionComponent(atributoBusquedaVehiculos,"Filtrar búsqueda");
        notaUtils.descripcionComponent(btnModificarMarcaModeloCategoria,"Agregar o editar marcas");
        notaUtils.descripcionComponent(btnAgregarVehiculo,"Asociar nuevo vehiculo a un cliente existente");

        //Click derecho borrar
        tablaVehiculos.setRowFactory(tabla -> {
            TableRow<VehiculoDTO> fila = new TableRow<>();

            fila.setOnContextMenuRequested(event -> {
                if (!fila.isEmpty()) {
                    VehiculoDTO vehiculoSeleccionado = fila.getItem();
                    Integer vehiculoId = vehiculoSeleccionado.getId();

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

                                    String propietario, vehiculo;

                                    //verificar que no contenga null  mostrar mensaje
                                    propietario = (vehiculoSeleccionado.getNombreCliente() != null ? vehiculoSeleccionado.getNombreCliente() : "") + " " +
                                            (vehiculoSeleccionado.getApellido() != null ? vehiculoSeleccionado.getApellido() : "") + " " +
                                            (vehiculoSeleccionado.getSegundoApellido() != null ? vehiculoSeleccionado.getSegundoApellido() : "");

                                    String vehiculoEliminar = propietario + " " + vehiculoSeleccionado.getNombreMarca() +
                                            " " + vehiculoSeleccionado.getNombreModelo() + " " + vehiculoSeleccionado.getAnio();


                                    boolean eliminar = MensajesAlert.mostrarConfirmacion(
                                            "Confirmar eliminación",
                                            "Eliminar vehículo",
                                            "¿Está seguro de que desea eliminar este vehículo? Esta acción no se puede deshacer.",
                                            "Eliminar",
                                            "Cancelar"
                                    );

                                    if (eliminar) {
                                        taskService.runTask(
                                                loadingOverlayController,
                                                () -> {
                                                    vehiculoService.eliminarVehiculo(StatusVehiculo.INACTIVE.toString(), vehiculoSeleccionado.getId());
                                                    return null;
                                                },
                                                (resultado) -> {
                                                    cargarVehiculos(); //metodo que cargar los datos en la tabla
                                                    mostrarInformacion(
                                                            "Operación completada",
                                                            "Vehículo eliminado",
                                                            "El vehículo ha sido eliminado del sistema exitosamente."
                                                    );
                                                },
                                                (ex) -> {
                                                    if (ex.getCause() instanceof InterruptedException || ex.getCause() instanceof java.util.concurrent.CancellationException) {
                                                        mostrarWarning(
                                                                "Operación cancelada",
                                                                "Acción interrumpida",
                                                                "La acción fue cancelada por el usuario."
                                                        );
                                                    } else if (ex.getCause() instanceof VehiculoException) {
                                                        mostrarExcepcion(
                                                                "Error al eliminar",
                                                                "Fallo en la eliminación del vehículo",
                                                                "No se pudo completar la operación debido a un error de validación.",
                                                                (Exception) ex.getCause()
                                                        );
                                                    } else {
                                                        mostrarExcepcionThrowable(
                                                                "Error inesperado",
                                                                "Fallo al eliminar vehículo",
                                                                "Ocurrió un error inesperado al intentar eliminar el vehículo seleccionado.",
                                                                ex
                                                        );
                                                    }
                                                }, null
                                        );
                                    } else {
                                        mostrarInformacion(
                                                "Información",
                                                "Operación cancelada",
                                                "La acción ha sido cancelada correctamente."
                                        );
                                    }

                                }//case eliminar

                                case "Editar" -> {
                                    try {
                                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/vehiculo/EditarVehiculo.fxml"));
                                        loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
                                        Parent root = loader.load();

                                        EditarVehiculoController controller = loader.getController();
                                        controller.setVehiculo(vehiculoSeleccionado);
                                        controller.setInitializeLoading(loadingOverlayController);

                                        Stage stage = new Stage(StageStyle.UNDECORATED);
                                        stage.setTitle("Editar Cliente");
                                        stage.setScene(new Scene(root));
                                        stage.setResizable(false);

                                        stage.initModality(Modality.APPLICATION_MODAL);


                                        stage.showAndWait();
                                        cargarVehiculos();
                                    } catch (IOException ex) {
                                        mostrarExcepcion(
                                                "Error de carga",
                                                "No se pudo inicializar la interfaz",
                                                "Ocurrió un error al intentar cargar la vista. Por favor, inténtelo de nuevo más tarde.",
                                                ex
                                        );
                                    }
                                }

                                case "Ver informacion" -> {
                                    try {
                                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/vehiculo/DetalleVehiculo.fxml"));
                                        loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
                                        Parent root = loader.load();

                                        detalleVehiculo controller = loader.getController();
                                        controller.setVehiculo(vehiculoSeleccionado);

                                        Stage stage = new Stage(StageStyle.UNDECORATED);
                                        stage.setTitle("Informacion Vehiculo");
                                        stage.setScene(new Scene(root));
                                        stage.initModality(Modality.APPLICATION_MODAL);
                                        stage.showAndWait();
                                    } catch (IOException ex) {
                                        mostrarExcepcion(
                                                "Error de carga",
                                                "No se pudo inicializar la interfaz",
                                                "Ocurrió un error al intentar cargar la vista. Por favor, inténtelo de nuevo más tarde.",
                                                ex
                                        );
                                    }
                                }

                                case "Copiar" -> {
                                    var selectedCell = tablaVehiculos.getSelectionModel().getSelectedCells();


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
                                            statusLabel.setText("Texto copiado ");

                                            if (pauseTransition != null) {
                                                pauseTransition.stop();
                                            }

                                            pauseTransition = new PauseTransition(Duration.seconds(2.5));

                                            pauseTransition.setOnFinished(e1 -> {
                                                statusLabel.setVisible(false);
                                                statusLabel.setText("");
                                            });
                                            pauseTransition.play();


                                        }

                                    }

                                }
                                case "Copiar fila completa" -> {

                                    VehiculoDTO item = tablaVehiculos.getSelectionModel().getSelectedItem();

                                    if (item != null) {

                                        String ultimoServicio = "", fechaRegistro = "";


                                        if (item.getUltimoServicio() != null && !item.getUltimoServicio().isEmpty()) {
                                            fechaRegistro = getFechaFormateadaSegundos(item.getFechaRegistro());
                                            ultimoServicio = getFechaFormateada(item.getUltimoServicio());


                                        } else {

                                            fechaRegistro = getFechaFormateadaSegundos(item.getFechaRegistro());
                                            ultimoServicio = "";

                                        }


                                        String filaCopiada = item.getNombreCliente() + " " + (item.getApellido() != null ? item.getApellido() : "") + " " +
                                                (item.getSegundoApellido() != null ? item.getSegundoApellido() : "") + " " +
                                                (item.getNombreMarca() != null ? item.getNombreMarca() : "") + " " +
                                                (item.getNombreModelo() != null ? item.getNombreModelo() : "") + " " +
                                                (item.getNombreCategoria() != null ? item.getNombreCategoria() : "") + " " +
                                                (item.getColor() != null ? item.getColor() : "") + " " +
                                                (item.getAnio() != null ? item.getAnio() : "") + " " +
                                                (item.getPlacas() != null ? item.getPlacas() : "") + " " +
                                                (item.getNumSerie() != null ? item.getNumSerie() : "") + " " +
                                                (item.getKilometros() != null ? item.getKilometros() : "") + " " +
                                                ultimoServicio + " " + fechaRegistro;


                                        ClipboardContent content = new ClipboardContent();
                                        content.putString(filaCopiada);
                                        Clipboard.getSystemClipboard().setContent(content);

                                        statusLabel.setVisible(true);
                                        statusLabel.setText("Fila copiada");

                                        if (pauseTransition != null) {
                                            pauseTransition.stop();
                                        }

                                        pauseTransition = new PauseTransition(Duration.seconds(2.5));
                                        pauseTransition.setOnFinished(event1 -> {
                                            statusLabel.setVisible(false);
                                            statusLabel.setText("");
                                        });
                                        pauseTransition.play();

                                    }
                                }
                            }//switch
                            listViewPopup.hide();
                        }


                    });
                    Point2D coordenadasPantalla = fila.localToScreen(event.getX(), event.getY()); //convertir las coordenadas relativas a las obsolutas de la pantalla para que la lista aparezca justo donde click


                    listViewPopup.show(fila.getScene().getWindow(), coordenadasPantalla.getX(), coordenadasPantalla.getY());

                }//if if empty
            });

            return fila;
        });


    }//configuraciones

    @Override
    public void listeners() {

        //pone en null la lista de ChoiceBox
        limpiarChoiceBox.setOnMouseClicked(event -> {

            if ((event.getButton() == MouseButton.PRIMARY || event.getButton() == MouseButton.MIDDLE) && event.getClickCount() == 2)
                atributoBusquedaVehiculos.setValue(null); // pone el valor en null para que vuelva a buscar dinamicamente

        });

        buscarVehiculoBuscador.setOnAction(event -> {
            String seleccion = atributoBusquedaVehiculos.getValue();

        });

        //Enter buscar

        buscarVehiculoBuscador.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {

                String seleccion = atributoBusquedaVehiculos.getValue();
                String busqueda = buscarVehiculoBuscador.getText();

                // SOLO funciona si hay un filtro seleccionado
                if (seleccion != null && !seleccion.isEmpty()) {

                    // Si el texto está vacío, resetea y detén
                    if (busqueda == null || busqueda.isEmpty() && seleccion == null) {
                        resetBusqueda();
                        return;
                    }

                    // Ejecutar búsqueda específica
                    buscarVehiculo(seleccion.toLowerCase(), busqueda);
                }
            }
        });

        atributoBusquedaVehiculos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            ConfigurarMostarDatePicker1(newVal);
        });

        // 2. Listener para el CheckBox (Mostrar/Ocultar segunda fecha)
        chkRangoFechas.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            ConfigurarMostrarDatepicker2(isSelected);
        });

        //Buscar mientras escribes

        buscarVehiculoBuscador.setOnKeyReleased(event -> {

            if (event.getCode() == KeyCode.ENTER) return; // ignorar enter aquí

            delayQuery.setOnFinished(e -> {

                String seleccion = atributoBusquedaVehiculos.getValue();
                String busqueda = buscarVehiculoBuscador.getText();


                if (seleccion == null || seleccion.isEmpty()) {

                    if (busqueda == null || busqueda.isEmpty()) {
                        resetBusqueda();
                        return;
                    }

                    // Búsqueda general mientras escribe
                    buscarVehiculo(busqueda);
                }
                // Si sí hay filtro → no hace nada, porque solo se buscan al presionar ENTER
            });

            delayQuery.playFromStart();
        });


    }//listeners

    private void ocultarRangoSegundoNivel() {
        lblHasta.setVisible(false);
        lblHasta.setManaged(false);
        dpVehiculoFin.setVisible(false);
        dpVehiculoFin.setManaged(false);
    }

    @FXML
    public void actualizarTabla(ActionEvent actionEvent) {

        buscarVehiculoBuscador.setText("");
        atributoBusquedaVehiculos.setValue(null);

        // Salir del modo búsqueda
        modoBusqueda = false;
        terminoBusquedaActual = "";

        // Reinicia paginador y carga los clientes
        paginadorVehiculos.setPageFactory(this::crearPaginaVehiculo);
        paginadorVehiculos.setCurrentPageIndex(0);
    }

    private VBox crearPaginaVehiculo(int indicePagina) {
        Page<VehiculoDTO> paginaVehiculo;

        if (modoBusqueda) {
            paginaVehiculo = vehiculoService.buscadorVehiculosPaginado(
                    StatusVehiculo.ACTIVE.toString(),
                    terminoBusquedaActual,
                    indicePagina,
                    VEHICULO_POR_PAGINA
            );
        } else {
            paginaVehiculo = vehiculoService.listarVehiculosPaginado(
                    StatusVehiculo.ACTIVE.toString(),
                    indicePagina,
                    VEHICULO_POR_PAGINA
            );
        }

        tablaVehiculos.setItems(FXCollections.observableArrayList(paginaVehiculo.getContent()));

        VBox contenedor = new VBox(tablaVehiculos);
        contenedor.setMinHeight(500);
        contenedor.setPrefHeight(500);
        contenedor.setStyle("-fx-background-color: transparent;");
        return contenedor;
    }

    private VBox crearPaginaVehiculoconFiltro(int indicePagina) {
        Page<VehiculoDTO> paginaVehiculo = Page.empty();

        try {
            if (modoBusqueda) {
                String busqueda = terminoBusquedaActual.trim();
                String activo = StatusVehiculo.ACTIVE.toString();
                String key = atributoBusquedaVehiculos.getValue();
                switch (key.toLowerCase()) {
                    case "propietario" -> paginaVehiculo =
                            vehiculoService.buscarPorPropietario(activo, busqueda, indicePagina, VEHICULO_POR_PAGINA);

                    case "marca" -> paginaVehiculo =
                            vehiculoService.buscarPorMarca(activo, busqueda, indicePagina, VEHICULO_POR_PAGINA);

                    case "modelo" -> paginaVehiculo =
                            vehiculoService.buscarPorModelo(activo, busqueda, indicePagina, VEHICULO_POR_PAGINA);

                    case "categoria" -> paginaVehiculo =
                            vehiculoService.buscarPorCategoria(activo, busqueda, indicePagina, VEHICULO_POR_PAGINA);

                    case "color" -> paginaVehiculo =
                            vehiculoService.buscarPorColor(activo, busqueda, indicePagina, VEHICULO_POR_PAGINA);

                    case "placas" -> paginaVehiculo =
                            vehiculoService.buscarPorPlacas(activo, busqueda.toUpperCase(), indicePagina, VEHICULO_POR_PAGINA);

                    case "numero serie" -> paginaVehiculo =
                            vehiculoService.buscarVehiculoPorNumSeriePaginado(activo, busqueda.toUpperCase(), indicePagina, VEHICULO_POR_PAGINA);


                    case "año" -> {
                        if (busqueda.matches("\\d{4}")) {
                            int anio = Integer.parseInt(busqueda);
                            paginaVehiculo = vehiculoService.buscarPorAnio(activo, anio, indicePagina, VEHICULO_POR_PAGINA);

                        } else if (busqueda.matches("\\d{4},\\d{4}")) {
                            String[] anios = busqueda.split(",");
                            String[] aniosOrdenados = Arrays.stream(anios).sorted().toArray(String[]::new);
                            int anioInicio = Integer.parseInt(aniosOrdenados[0]);
                            int anioFin = Integer.parseInt(aniosOrdenados[1]);
                            paginaVehiculo = vehiculoService.buscarPorAnioRango(activo, anioInicio, anioFin, indicePagina, VEHICULO_POR_PAGINA);

                        } else {
                            mostrarWarning(
                                    "Advertencia",
                                    "Formato de año no válido",
                                    "Por favor, use el formato yyyy o yyyy,yyyy para establecer un rango de años."
                            );
                        }
                    }

                    //  KILOMETRAJE (ej: 10000 o 0,50000) ====
                    case "kilometraje" -> {

                        if (busqueda.matches("\\d+")) {
                            int km = Integer.parseInt(busqueda);
                            paginaVehiculo = vehiculoService.buscarPorKilometros(activo, km, indicePagina, VEHICULO_POR_PAGINA);

                        } else if (busqueda.matches("\\d+,\\d+")) {
                            String[] kms = busqueda.split(",");
                            String[] ordenado = Arrays.stream(kms).sorted().toArray(String[]::new);
                            int kmInicio = Integer.parseInt(ordenado[0]);
                            int kmFin = Integer.parseInt(ordenado[1]);
                            paginaVehiculo = vehiculoService.buscarPorKilometrosRango(activo, kmInicio, kmFin, indicePagina, VEHICULO_POR_PAGINA);

                        } else {
                            mostrarWarning(
                                    "Advertencia",
                                    "Formato de kilometraje no válido",
                                    "Por favor, use el formato 10000 o 0,50000 para establecer un rango de kilómetros."
                            );
                        }
                    }

                    case "fecha registro" -> {
                        LocalDate fechaInicio = dpVehiculoInicio.getValue();

                        if (chkRangoFechas.isSelected()) {
                            // LÓGICA DE RANGO
                            LocalDate fechaFin = dpVehiculoFin.getValue();

                            if (fechaInicio == null || fechaFin == null) {
                                mostrarWarning(
                                        "Datos incompletos",
                                        "Rango de fechas no válido",
                                        "Por favor, seleccione ambas fechas para realizar la búsqueda por rango correctamente."
                                );
                                paginaVehiculo = vehiculoService.listarVehiculosPaginado(StatusVehiculo.ACTIVE.toString(), indicePagina, VEHICULO_POR_PAGINA);


                                tablaVehiculos.setItems(FXCollections.observableArrayList(paginaVehiculo.getContent()));

                                VBox contenedor = new VBox(tablaVehiculos);
                                contenedor.setMinHeight(500);
                                contenedor.setPrefHeight(500);
                                contenedor.setStyle("-fx-background-color: transparent;");
                                return contenedor;

                            }

                            // Validar y ordenar si el usuario puso la fecha mayor al inicio
                            if (fechaInicio.isAfter(fechaFin)) {
                                LocalDate aux = fechaInicio;
                                fechaInicio = fechaFin;
                                fechaFin = aux;
                            }

                            paginaVehiculo = vehiculoService.buscarPorFechaRegistroRango(
                                    StatusVehiculo.ACTIVE.toString(),
                                    fechaInicio,
                                    fechaFin,
                                    indicePagina,
                                    VEHICULO_POR_PAGINA
                            );

                        } else {
                            // LÓGICA DE FECHA ÚNICA
                            if (fechaInicio == null) {
                                mostrarWarning(
                                        "Datos incompletos",
                                        "Fecha no seleccionada",
                                        "Por favor, seleccione una fecha en el calendario para continuar."
                                );
                                paginaVehiculo = vehiculoService.listarVehiculosPaginado(StatusVehiculo.ACTIVE.toString(), indicePagina, VEHICULO_POR_PAGINA);


                                tablaVehiculos.setItems(FXCollections.observableArrayList(paginaVehiculo.getContent()));

                                VBox contenedor = new VBox(tablaVehiculos);
                                contenedor.setMinHeight(500);
                                contenedor.setPrefHeight(500);
                                contenedor.setStyle("-fx-background-color: transparent;");
                                return contenedor;
                            }

                            paginaVehiculo = vehiculoService.buscarPorFechaRegistro(
                                    StatusVehiculo.ACTIVE.toString(),
                                    fechaInicio,
                                    indicePagina,
                                    VEHICULO_POR_PAGINA
                            );
                        }
                    }

                    case "ultimo servicio" -> {
                        LocalDate fechaInicio = dpVehiculoInicio.getValue();

                        if (chkRangoFechas.isSelected()) {
                            LocalDate fechaFin = dpVehiculoFin.getValue();
                            if (fechaInicio == null || fechaFin == null) {
                                mostrarWarning(
                                        "Datos incompletos",
                                        "Rango de fechas no válido",
                                        "Por favor, seleccione ambas fechas para establecer el rango del último servicio correctamente."
                                );
                                paginaVehiculo = vehiculoService.listarVehiculosPaginado(StatusVehiculo.ACTIVE.toString(), indicePagina, VEHICULO_POR_PAGINA);


                                tablaVehiculos.setItems(FXCollections.observableArrayList(paginaVehiculo.getContent()));

                                VBox contenedor = new VBox(tablaVehiculos);
                                contenedor.setMinHeight(500);
                                contenedor.setPrefHeight(500);
                                contenedor.setStyle("-fx-background-color: transparent;");
                                return contenedor;
                            }

                            if (fechaInicio.isAfter(fechaFin)) {
                                LocalDate aux = fechaInicio;
                                fechaInicio = fechaFin;
                                fechaFin = aux;
                            }

                            // Asumiendo que tienes este método en tu servicio
                            paginaVehiculo = vehiculoService.buscarVehiculoPorUltimoServicioPaginadoRango(
                                    StatusVehiculo.ACTIVE.toString(), fechaInicio, fechaFin, indicePagina, VEHICULO_POR_PAGINA);
                        } else {
                            if (fechaInicio == null) {
                                mostrarWarning(
                                        "Datos incompletos",
                                        "Fecha de servicio obligatoria",
                                        "Por favor, seleccione la fecha del último servicio antes de continuar."
                                );
                                paginaVehiculo = vehiculoService.listarVehiculosPaginado(StatusVehiculo.ACTIVE.toString(), indicePagina, VEHICULO_POR_PAGINA);


                                tablaVehiculos.setItems(FXCollections.observableArrayList(paginaVehiculo.getContent()));

                                VBox contenedor = new VBox(tablaVehiculos);
                                contenedor.setMinHeight(500);
                                contenedor.setPrefHeight(500);
                                contenedor.setStyle("-fx-background-color: transparent;");
                                return contenedor;
                            }

                            paginaVehiculo = vehiculoService.buscarVehiculoPorUltimoServicioPaginado(
                                    StatusVehiculo.ACTIVE.toString(), fechaInicio, indicePagina, VEHICULO_POR_PAGINA);
                        }
                    }

                    default -> mostrarWarning(
                            "Advertencia",
                            "Criterio de búsqueda incorrecto",
                            "Por favor, seleccione un campo de búsqueda válido de la lista."
                    );
                }
                int totalPaginas = paginaVehiculo.getTotalPages();
                paginadorVehiculos.setPageCount(Math.max(totalPaginas, 1));
            } else {
                paginaVehiculo = vehiculoService.listarVehiculosPaginado(
                        StatusVehiculo.ACTIVE.toString(), indicePagina, VEHICULO_POR_PAGINA);
            }

        } catch (Exception e) {
            mostrarExcepcion(
                    "Error del sistema",
                    "Fallo al realizar la búsqueda",
                    "Ocurrió un error inesperado al intentar buscar los vehículos: " + e.getMessage(),
                    e
            );

        }

        tablaVehiculos.setItems(FXCollections.observableArrayList(paginaVehiculo.getContent()));

        VBox contenedor = new VBox(tablaVehiculos);
        contenedor.setMinHeight(500);
        contenedor.setPrefHeight(500);
        contenedor.setStyle("-fx-background-color: transparent;");
        return contenedor;
    }

    @FXML
    private void agregarVehiculo(ActionEvent event) {

        ventanaPrincipalController.viewContent(
                null, // no se requiere el MouseEvent
                "/fxmlViews/vehiculo/AgregarVehiculo.fxml",
                "Agregar vehiculo"
        );
        ventanaPrincipalController.cambiarPaginaEtiqueta.setText("AGREGAR VEHICULO");

    }

    public void cargarVehiculos() {

        colCliente.setCellValueFactory(data -> {
            String propietario = nombreCompleto(
                    data.getValue().getNombreCliente(),
                    data.getValue().getApellido(),
                    data.getValue().getSegundoApellido()
            );
            return new SimpleStringProperty(propietario);
        });

        colMarca.setCellValueFactory(data ->
                new SimpleStringProperty(valorONull(data.getValue().getNombreMarca()))
        );

        colModelo.setCellValueFactory(data ->
                new SimpleStringProperty(valorONull(data.getValue().getNombreModelo()))
        );

        colColor.setCellValueFactory(data ->
                new SimpleStringProperty(valorONull(data.getValue().getColor()))
        );

        colCategoria.setCellValueFactory(data ->
                new SimpleStringProperty(valorONull(data.getValue().getNombreCategoria()))
        );


        colAnio.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getAnio())
        );
        colAnio.setCellFactory(column -> new TableCell<VehiculoDTO, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item == null ? "N/D" : item.toString());
                }
            }
        });

        colPlacas.setCellValueFactory(data ->
                new SimpleStringProperty(valorONull(data.getValue().getPlacas()))
        );

        colNumeroSerie.setCellValueFactory(data ->
                new SimpleStringProperty(valorONull(data.getValue().getNumSerie()))
        );

        colObservaciones.setCellValueFactory(data ->
                new SimpleStringProperty(valorONull(data.getValue().getObservaciones()))
        );

        colKilometraje.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getKilometros())
        );
        colKilometraje.setCellFactory(column -> new TableCell<VehiculoDTO, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item == null ? "N/D" : item.toString());
                }
            }
        });

        colUltimoServicio.setCellValueFactory(data -> {
            String fechaStr = data.getValue().getUltimoServicio();

            if (fechaStr == null || fechaStr.isBlank()) {
                return new SimpleStringProperty("N/D");
            }

            DateTimeFormatter input = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter output = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            LocalDate fecha = LocalDate.parse(fechaStr, input);
            return new SimpleStringProperty(fecha.format(output));
        });

        colFechaRegistro.setCellValueFactory(data -> {
            String fechaStr = data.getValue().getFechaRegistro();

            if (fechaStr == null || fechaStr.isBlank()) {
                return new SimpleStringProperty("N/D");
            }

            try {
                DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime fecha = LocalDateTime.parse(fechaStr, formatterEntrada);

                return new SimpleStringProperty(
                        fecha.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                );
            } catch (Exception e) {
                return new SimpleStringProperty("N/D");
            }
        });

        cargarDatosVehiculo();
    }//cargarVehiculos

    private String valorONull(String valor) {
        return (valor == null || valor.isBlank()) ? "N/D" : valor;
    }

    private String nombreCompleto(String n, String a1, String a2) {
        n = (n == null ? "" : n.trim());
        a1 = (a1 == null ? "" : a1.trim());
        a2 = (a2 == null ? "" : a2.trim());

        String full = (n + " " + a1 + " " + a2).trim();
        return full.isBlank() ? "N/D" : full;
    }

    private void cargarDatosVehiculo() {

        try {
            long totalvehiculos = vehiculoService.contarVehiculosActivos(StatusVehiculo.ACTIVE.toString());
            int totalPaginas = (int) Math.ceil((double) totalvehiculos / VEHICULO_POR_PAGINA);
            paginadorVehiculos.setPageCount(Math.max(totalPaginas, 1));
            paginadorVehiculos.setPageFactory(this::crearPaginaVehiculo);

            // Muestra la primera página
            paginadorVehiculos.setCurrentPageIndex(0);

        } catch (Exception e) {
            mostrarExcepcion(
                    "Error de visualización",
                    "No se pudieron cargar los registros",
                    "No se pudieron cargar los datos solicitados. Por favor, inténtelo de nuevo más tarde.",
                    e
            );
        }


    }//cargarDatosVehiculo

    private void buscarVehiculo(String seleccion, String busqueda) { // porque se va a buscar, vehiculo a buscar

        taskService.runTask(
                loadingOverlayController,
                () -> {

                    terminoBusquedaActual = busqueda;
                    modoBusqueda = !busqueda.trim().isEmpty();
                    //atributoBusquedaVehiculos.setValue(seleccion);


                    String activo = StatusVehiculo.ACTIVE.toString();

                    Page<VehiculoDTO> paginaFiltrada = null;

                    switch (seleccion) {
                        case "propietario" -> paginaFiltrada =
                                vehiculoService.buscarPorPropietario(activo, busqueda, 0, VEHICULO_POR_PAGINA);

                        case "marca" -> paginaFiltrada =
                                vehiculoService.buscarPorMarca(activo, busqueda, 0, VEHICULO_POR_PAGINA);

                        case "modelo" -> paginaFiltrada =
                                vehiculoService.buscarPorModelo(activo, busqueda, 0, VEHICULO_POR_PAGINA);

                        case "categoria" -> paginaFiltrada =
                                vehiculoService.buscarPorCategoria(activo, busqueda, 0, VEHICULO_POR_PAGINA);

                        case "color" -> paginaFiltrada =
                                vehiculoService.buscarPorColor(activo, busqueda, 0, VEHICULO_POR_PAGINA);

                        case "placas" -> paginaFiltrada =
                                vehiculoService.buscarPorPlacas(activo, busqueda.toUpperCase(), 0, VEHICULO_POR_PAGINA);

                        case "numero serie" -> paginaFiltrada =
                                vehiculoService.buscarVehiculoPorNumSeriePaginado(activo, busqueda.toUpperCase(), 0, VEHICULO_POR_PAGINA);


                        case "año" -> {
                            if (busqueda.matches("\\d{4}")) {
                                int anio = Integer.parseInt(busqueda);
                                paginaFiltrada = vehiculoService.buscarPorAnio(activo, anio, 0, VEHICULO_POR_PAGINA);

                            } else if (busqueda.matches("\\d{4},\\d{4}")) {
                                String[] anios = busqueda.split(",");
                                String[] aniosOrdenados = Arrays.stream(anios).sorted().toArray(String[]::new);
                                int anioInicio = Integer.parseInt(aniosOrdenados[0]);
                                int anioFin = Integer.parseInt(aniosOrdenados[1]);
                                paginaFiltrada = vehiculoService.buscarPorAnioRango(activo, anioInicio, anioFin, 0, VEHICULO_POR_PAGINA);

                            } else {
                                mostrarWarning(
                                        "Advertencia",
                                        "Formato de año no válido",
                                        "Por favor, utilice el formato yyyy o yyyy,yyyy para definir un rango de años."
                                );
                            }
                        }

                        // ====  KILOMETRAJE (ej: 10000 o 0,50000) ====
                        case "kilometraje" -> {

                            if (busqueda.matches("\\d+")) {
                                int km = Integer.parseInt(busqueda);
                                paginaFiltrada = vehiculoService.buscarPorKilometros(activo, km, 0, VEHICULO_POR_PAGINA);

                            } else if (busqueda.matches("\\d+,\\d+")) {
                                String[] kms = busqueda.split(",");
                                String[] ordenado = Arrays.stream(kms).sorted().toArray(String[]::new);
                                int kmInicio = Integer.parseInt(ordenado[0]);
                                int kmFin = Integer.parseInt(ordenado[1]);
                                paginaFiltrada = vehiculoService.buscarPorKilometrosRango(activo, kmInicio, kmFin, 0, VEHICULO_POR_PAGINA);

                            } else {
                                throw new VehiculoException("Formato de kilometraje no valido: Ej. 10000 o 0,50000");

                            }
                        }

                        // ====  ÚLTIMO SERVICIO (dd-MM-yyyy o rango) ====
                        case "fecha registro" -> {
                            LocalDate fechaInicio = dpVehiculoInicio.getValue();

                            if (chkRangoFechas.isSelected()) {
                                // LÓGICA DE RANGO
                                LocalDate fechaFin = dpVehiculoFin.getValue();

                                if (fechaInicio == null || fechaFin == null) {
                                    throw new VehiculoException("Fechas incompletas. Seleccione ambas fechas para realizar la búsqueda por rango.");

                                }

                                // Validar y ordenar si el usuario puso la fecha mayor al inicio
                                if (fechaInicio.isAfter(fechaFin)) {
                                    LocalDate aux = fechaInicio;
                                    fechaInicio = fechaFin;
                                    fechaFin = aux;
                                }

                                paginaFiltrada = vehiculoService.buscarPorFechaRegistroRango(
                                        StatusVehiculo.ACTIVE.toString(),
                                        fechaInicio,
                                        fechaFin,
                                        0,
                                        VEHICULO_POR_PAGINA
                                );

                            } else {
                                // LÓGICA DE FECHA ÚNICA
                                if (fechaInicio == null) {
                                    throw new VehiculoException("Fecha no seleccionada. Debe seleccionar una fecha en el calendario.");

                                }

                                paginaFiltrada = vehiculoService.buscarPorFechaRegistro(
                                        StatusVehiculo.ACTIVE.toString(),
                                        fechaInicio,
                                        0,
                                        VEHICULO_POR_PAGINA
                                );
                            }
                        }

                        case "ultimo servicio" -> {
                            LocalDate fechaInicio = dpVehiculoInicio.getValue();

                            if (chkRangoFechas.isSelected()) {
                                LocalDate fechaFin = dpVehiculoFin.getValue();
                                if (fechaInicio == null || fechaFin == null) {
                                    throw new VehiculoException ("Fechas incompletas. Seleccione ambas fechas para el último servicio. ");

                                }

                                if (fechaInicio.isAfter(fechaFin)) {
                                    LocalDate aux = fechaInicio;
                                    fechaInicio = fechaFin;
                                    fechaFin = aux;
                                }

                                // Asumiendo que tienes este método en tu servicio
                                paginaFiltrada = vehiculoService.buscarVehiculoPorUltimoServicioPaginadoRango(
                                        StatusVehiculo.ACTIVE.toString(), fechaInicio, fechaFin, 0, VEHICULO_POR_PAGINA);
                            } else {
                                if (fechaInicio == null) {
                                    throw new VehiculoException("Fecha no seleccionada. Seleccione una fecha de último servicio.");

                                }

                                paginaFiltrada = vehiculoService.buscarVehiculoPorUltimoServicioPaginado(
                                        StatusVehiculo.ACTIVE.toString(), fechaInicio, 0, VEHICULO_POR_PAGINA);
                            }
                        }

                        default -> paginaFiltrada =
                                vehiculoService.listarVehiculosPaginado(
                                        StatusCliente.ACTIVE.toString(), 0, VEHICULO_POR_PAGINA);
                    }

                    return paginaFiltrada;

                }, (paginaFiltrada) -> {


                    // ACTUALIZA EL PAGECOUNT CON EL TOTAL DE COINCIDENCIAS
                    int totalPaginas = paginaFiltrada.getTotalPages();
                    paginadorVehiculos.setPageCount(Math.max(totalPaginas, 1));


                    // Reinicia el paginador para la búsqueda
                    paginadorVehiculos.setPageFactory(this::crearPaginaVehiculoconFiltro);
                    paginadorVehiculos.setCurrentPageIndex(0);

                }, (ex) -> {

                    if (ex instanceof VehiculoException) {
                        mostrarWarning(
                                "Advertencia",
                                "Problema en la búsqueda",
                                ex.getMessage()
                        );
                    } else {
                        MensajesAlert.mostrarExcepcionThrowable(
                                "Error inesperado",
                                "Fallo al cargar vehículos",
                                "Ocurrió un error inesperado al intentar cargar la información de los vehículos.",
                                ex
                        );
                    }

                }, null
        );


    }//buscarVehiculo

    private void buscarVehiculo(String busqueda) {
        terminoBusquedaActual = busqueda;
        modoBusqueda = !busqueda.trim().isEmpty();

        if (modoBusqueda) {
            //  total de resultados para el buscador general
            long totalResultados = vehiculoService.contarVehiculosPorBusquedaGeneral(
                    StatusVehiculo.ACTIVE.toString(),
                    terminoBusquedaActual
            );

            int totalPaginas = (int) Math.ceil((double) totalResultados / VEHICULO_POR_PAGINA);

            paginadorVehiculos.setPageCount(Math.max(totalPaginas, 1));
        } else {
            // Si no hay búsqueda, restaurar paginación normal
            long totalClientes = vehiculoService.contarVehiculosActivos(StatusVehiculo.ACTIVE.toString());
            int totalPaginas = (int) Math.ceil((double) totalClientes / VEHICULO_POR_PAGINA);
            paginadorVehiculos.setPageCount(Math.max(totalPaginas, 1));
        }

        paginadorVehiculos.setPageFactory(this::crearPaginaVehiculo);
        paginadorVehiculos.setCurrentPageIndex(0); // Reinicia la paginación

    }

    public void ModificarMarcaModeloCategoria(ActionEvent actionEvent) {

        ventanaPrincipalController.viewContent(
                null, // no se requiere el MouseEvent
                "/fxmlViews/marca/EditarMarcas_Modelos_Categorias.fxml",
                "Agregar o editar marcas"
        );
        ventanaPrincipalController.cambiarPaginaEtiqueta.setText("AGREGAR O EDITAR MARCAS");

    }

    private void resetBusqueda() {
        modoBusqueda = false;
        terminoBusquedaActual = "";
        atributoBusquedaVehiculos.setValue(null);
        buscarVehiculoBuscador.clear();

        paginadorVehiculos.setPageFactory(this::crearPaginaVehiculo);
        paginadorVehiculos.setCurrentPageIndex(0);

        cargarDatosVehiculo();  // vuelve a cargar conteo y items normales
    }

    public void accionBuscarVehiculo(ActionEvent actionEvent) {
        String seleccion = atributoBusquedaVehiculos.getValue();

        // 1. Verificación inicial del ChoiceBox
        if (seleccion == null || seleccion.isEmpty()) {
            return;
        }

        // Determinamos si la búsqueda es por alguna de las opciones de fecha
        boolean esFecha = seleccion.equals("Fecha Registro") || seleccion.equals("Ultimo Servicio");

        if (esFecha) {
            // 2. VALIDACIÓN DE FECHAS (Evitamos .toString() antes de tiempo)
            LocalDate inicio = dpVehiculoInicio.getValue();

            if (inicio == null) {
                mostrarWarning(
                        "Datos incompletos",
                        "Fecha de inicio obligatoria",
                        "Por favor, seleccione la fecha inicial para realizar la búsqueda."
                );
                return;
            }

            // 3. VALIDACIÓN DE RANGO (Si el checkbox está marcado)
            if (chkRangoFechas.isSelected()) {
                if (dpVehiculoFin.getValue() == null) {
                    mostrarWarning(
                            "Datos incompletos",
                            "Fecha final faltante",
                            "Ha activado la búsqueda por rango. Por favor, seleccione la fecha final para procesar la solicitud."
                    );
                    return;
                }
            }

            // Si todo es correcto, disparamos la búsqueda.
            // Pasamos el toString() solo ahora que sabemos que 'inicio' NO es null.
            buscarVehiculo(seleccion.toLowerCase(), inicio.toString());

        } else {
            // 4. VALIDACIÓN DE TEXTO NORMAL
            String texto = buscarVehiculoBuscador.getText();

            if (texto == null || texto.isBlank()) {
                resetBusqueda();
                return;
            }

            buscarVehiculo(seleccion.toLowerCase(), texto);
        }
    }

    @Override
    public void cleanup() {
        // 1. Detener ambos PauseTransitions
        if (delayQuery != null) {
            delayQuery.stop();
        }

        if (pauseTransition != null) {
            pauseTransition.stop();
        }

        // 3. Limpiar lista
        if (todosLosVehiculos != null) {
            todosLosVehiculos.clear();
        }
    }

    @Override
    public void restaurarEstadoInicial() {
        // 1. Resetear búsqueda
        if (buscarVehiculoBuscador != null) {
            buscarVehiculoBuscador.clear();
        }

        // 2. Resetear ChoiceBox
        if (atributoBusquedaVehiculos != null) {
            atributoBusquedaVehiculos.setValue(null);
        }

        // 3. Limpiar DatePickers
        if (dpVehiculoInicio != null) {
            dpVehiculoInicio.setValue(null);
        }
        if (dpVehiculoFin != null) {
            dpVehiculoFin.setValue(null);
        }

        // 4. Resetear CheckBox
        if (chkRangoFechas != null) {
            chkRangoFechas.setSelected(false);
        }

        // 5. Resetear variables de estado
        modoBusqueda = false;
        terminoBusquedaActual = "";

        // 6. Recargar datos
        cargarDatosVehiculo();
    }

}//clase
