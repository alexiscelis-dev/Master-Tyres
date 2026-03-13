package com.mastertyres.controllers.fxControllers.Promociones;


import com.mastertyres.ClientesPromocion.service.ClientePromocionService;
import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.cliente.model.StatusCliente;
import com.mastertyres.cliente.service.ClienteService;
import com.mastertyres.common.exeptions.PromocionException;
import com.mastertyres.common.interfaces.ICleanable;
import com.mastertyres.common.interfaces.ILoader;
import com.mastertyres.common.interfaces.IRestaurableDatos;
import com.mastertyres.common.interfaces.IVentanaPrincipal;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.ApplicationContextProvider;
import com.mastertyres.common.utils.FechaUtils;
import com.mastertyres.common.utils.MensajesAlert;
import com.mastertyres.common.utils.MenuContextSetting;
import com.mastertyres.components.fxComponents.LoadingComponentController;
import com.mastertyres.controllers.fxControllers.ventanaPrincipal.VentanaPrincipalController;
import com.mastertyres.promociones.model.Promocion;
import com.mastertyres.promociones.model.StatusPromocion;
import com.mastertyres.promociones.model.TipoDescuento;
import com.mastertyres.promociones.model.TipoPromocion;
import com.mastertyres.promociones.service.PromocionService;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class NuevaPromocionClienteController implements IVentanaPrincipal, ILoader,  ICleanable, IRestaurableDatos {

    @FXML private AnchorPane rootPane;
    @FXML private Slider porcentajeDescuento;
    @FXML private Label descuentoLabel;
    @FXML private TextField precioSinDescuento;
    @FXML private DatePicker fechaInicio;
    @FXML private DatePicker fechaFin;
    @FXML private Button btnRegistrar;
    @FXML private Button btnLimpiar;
    //@FXML private ChoiceBox<String> tipoDescuento;
    @FXML private TextField precioConDescuento;
    @FXML private TextField nombrePromocion;
    @FXML private TextArea descripcion;
    @FXML private Button btnImagen;
    @FXML private TextField textFieldImg;


    @FXML
    private TableView<Cliente> tablaClientes;
    @FXML
    private ListView<Cliente> clientesAgregados;
    @FXML
    private TableColumn<Cliente, Boolean> colSeleccionCliente;
    @FXML
    private TextField txtBuscador;
    @FXML
    private TableColumn<Cliente, String> colNombre;
    @FXML
    private TableColumn<Cliente, String> colRFC;
    @FXML
    private TableColumn<Cliente, String> colCumpleanos;
    @FXML
    private TableColumn<Cliente, String> colFechaRegistro;
    @FXML
    private TableColumn<Cliente, String> colNombreEmpresa;
    @FXML
    private TableColumn<Cliente, String> colTipoCliente;
    @FXML
    private TableColumn<Cliente, String> colHobbie;
    @FXML
    private Pagination PaginadorClientes;
    @FXML
    private Label statusLabel;
    @FXML
    private Button btnBuscar;

    private static final int CLIENTES_POR_PAGINA = 20;
    private String terminoBusquedaActual = "";
    private String terminoBusqueda = "";
    private boolean modoBusqueda = false;
    private PauseTransition pauseTransition;

    private final ObservableSet<Cliente> clientesSeleccionados =
            FXCollections.observableSet();

    @Autowired
    private ClientePromocionService clientePromocionService;

    @Autowired
    private PromocionService promocionService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ClienteService clienteService;

    private VentanaPrincipalController ventanaPrincipalController;

    private LoadingComponentController loadingComponentController;

    @Override
    public void setVentanaPrincipalController(VentanaPrincipalController controller) {
        this.ventanaPrincipalController = controller;
    }

    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingComponentController = loading;
    }

    @FXML
    private void initialize() {
        configuraciones();
        listeners();
        configurarColumnas();
        configurarPaginador();
    }

    /* ======= LISTENERS Y CONFIGURACIONES ======= */

    public void configuraciones() {

        //cargarPorcentaje();

        precioSinDescuento.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*(\\.\\d{0,2})?")) {
                return change;
            }
            return null;
        }));

        MenuContextSetting.disableMenu(rootPane);
        MenuContextSetting.disableMenuDatePicker(rootPane);

    }//configuraciones

    public void listeners() {

        btnLimpiar.setOnAction(event -> {
            clean();
            clientesAgregados.getItems().clear();
            clientesSeleccionados.clear();
            tablaClientes.refresh();
        });

        btnBuscar.setOnAction(event -> buscarClientes());

        btnRegistrar.setOnAction(event -> registrarPromocion());

        btnImagen.setOnAction(event -> seleccionarImg());

        porcentajeDescuento.valueProperty().addListener((observable, valAnterior, valNuevo) -> {

            obtenerPorcentaje(valNuevo.doubleValue());

        });

//        tipoDescuento.getSelectionModel().selectedItemProperty().addListener((observable, valorAnterior, nuevoValor) -> {
//
//
//            if (nuevoValor != null && !nuevoValor.isEmpty())
//                tipoDescuento(nuevoValor.toLowerCase());
//
//        });


        porcentajeDescuento.valueProperty().addListener((observable, valAnterior, valNuevo) -> {
            obtenerPorcentaje(valNuevo.doubleValue());
            calcularPrecioConDescuento();
        });

        precioSinDescuento.textProperty().addListener((observable, valAnterior, nuevoValor) -> {
            calcularPrecioConDescuento();
        });

        txtBuscador.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                buscarClientes();
            }
        });
    }//listeners

    /* ================= TABLA ================= */

    private void configurarColumnas() {

        configurarColumnaSeleccion();

        colNombre.setCellValueFactory(data ->
                new SimpleStringProperty(nombreCompleto(data.getValue()))
        );

        colNombreEmpresa.setCellValueFactory(data ->
                new SimpleStringProperty(valorONull(data.getValue().getNombreEmpresa()))
        );

        colTipoCliente.setCellValueFactory(data ->
                new SimpleStringProperty(valorONull(data.getValue().getTipoCliente()))
        );

        colRFC.setCellValueFactory(data ->
                new SimpleStringProperty(valorONull(data.getValue().getRfc()))
        );

        colCumpleanos.setCellValueFactory(data ->
                new SimpleStringProperty(
                        FechaUtils.formatearFecha(data.getValue().getFechaCumple())
                )
        );

        colFechaRegistro.setCellValueFactory(data ->
                new SimpleStringProperty(
                        FechaUtils.formatearFechaHora(data.getValue().getCreated_at())
                )
        );

        colHobbie.setCellValueFactory(data ->
                new SimpleStringProperty(valorONull(data.getValue().getHobbie()))
        );

        configurarListaClientesAgregados();
    }

    /* ============= COLUMNA SELECCION ============== */

    private void configurarColumnaSeleccion() {

        colSeleccionCliente.setCellFactory(col -> new TableCell<>() {

            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setOnAction(e -> {
                    Cliente cliente = getTableView()
                            .getItems()
                            .get(getIndex());
                    String mensaje = "";

                    if (checkBox.isSelected()) {

                        clientesSeleccionados.add(cliente);
                        mensaje = nombreCompleto(cliente);

                        showLabel(mensaje, "agregado");

                    } else {
                        clientesSeleccionados.remove(cliente);
                        mensaje = nombreCompleto(cliente);
                        showLabel(mensaje, "eliminado");
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                    return;
                }

                Cliente cliente = getTableView().getItems().get(getIndex());

                checkBox.setSelected(clientesSeleccionados.contains(cliente));

                setGraphic(checkBox);

            }
        });
    }

    private void configurarListaClientesAgregados() {

        clientesAgregados.setItems(
                FXCollections.observableArrayList(clientesSeleccionados)
        );

        clientesSeleccionados.addListener((SetChangeListener<Cliente>) change -> {
            clientesAgregados.getItems().setAll(clientesSeleccionados);
        });

        clientesAgregados.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Cliente c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null ? null : nombreCompleto(c));
            }
        });
    }

    /* ================= PAGINACIÓN ================= */

    private void configurarPaginador() {
        long total = clienteService.contarClientesActivos(StatusCliente.ACTIVE.toString());
        int paginas = (int) Math.ceil((double) total / CLIENTES_POR_PAGINA);

        PaginadorClientes.setPageCount(Math.max(paginas, 1));

        PaginadorClientes.currentPageIndexProperty().addListener(
                (obs, oldVal, newVal) -> cargarPagina(newVal.intValue())
        );

        cargarPagina(0);
    }

    private void cargarPagina(int pagina) {
        Page<Cliente> resultado;

        if (modoBusqueda) {
            resultado = clienteService.buscadorClientesPaginado(
                    StatusCliente.ACTIVE.toString(),
                    terminoBusqueda,
                    pagina,
                    CLIENTES_POR_PAGINA
            );
        } else {
            resultado = clienteService.listarClientesConVehiculosPaginado(
                    StatusCliente.ACTIVE.toString(),
                    pagina,
                    CLIENTES_POR_PAGINA
            );
        }

        tablaClientes.setItems(
                FXCollections.observableArrayList(resultado.getContent())
        );
        tablaClientes.refresh();
    }

    /* ================= BUSCADOR ================= */

    @FXML
    private void buscarClientes() {

        String texto = txtBuscador.getText().trim();

        if (texto.isEmpty()) {
            // Salir de modo búsqueda
            modoBusqueda = false;
            terminoBusqueda = "";

            configurarPaginador();
            return;
        }

        // Activar búsqueda
        modoBusqueda = true;
        terminoBusqueda = texto;

        long total = clienteService.contarClientesPorBusquedaGeneral(
                StatusCliente.ACTIVE.toString(),
                terminoBusqueda
        );

        int paginas = (int) Math.ceil((double) total / CLIENTES_POR_PAGINA);

        PaginadorClientes.setPageCount(Math.max(paginas, 1));
        PaginadorClientes.setCurrentPageIndex(0);

        cargarPagina(0);
    }


    /* =========== REGISTRO PROMOCION =========== */

    private boolean registrarPromocion() {

        btnRegistrar.setDisable(true);

        try {
            if (!empty() && validarFecha(fechaInicio.getValue(), fechaFin.getValue())) {
                insertarPromocion();
            } else if (empty()) {
                MensajesAlert.mostrarWarning(
                        "Datos incompletos",
                        "Campo obligatorio vacío",
                        "Por favor, complete todos los campos obligatorios antes de continuar."
                );
            }
        } finally {
            btnRegistrar.setDisable(false);
        }

        return true;
    }

    private void insertarPromocion() {



        // 2. Validar clientes
        if (clientesAgregados.getItems().isEmpty()) {
            MensajesAlert.mostrarWarning(
                    "Datos incompletos",
                    "Cliente requerido",
                    "Debe agregar al menos un cliente a la promoción antes de continuar."
            );
            return;
        }


        taskService.runTask(
                loadingComponentController,
                () -> {

                    Promocion promocion = Promocion.builder()
                            .nombre(nombrePromocion.getText())
                            .descripcion(descripcion.getText())
                            .tipoDescuento(TipoDescuento.PORCENTAJE.toString())
                            //.tipoDescuento(tipoDescuento.getValue())
                            .precio(Float.parseFloat(precioSinDescuento.getText()))
                            .porcentaje((int) porcentajeDescuento.getValue())
                            .fechaInicio(String.valueOf(fechaInicio.getValue()))
                            .fechaFin(String.valueOf(fechaFin.getValue()))
                            .active(StatusPromocion.ACTIVE.toString())
                            .img(textFieldImg.getText() != null ? textFieldImg.getText() : "")
                            .TipoPromocion(TipoPromocion.CLIENTE.toString())
                            .build();

                    // 3. Extraer IDs de clientes del ListView
                    List<Integer> clientesIds = clientesAgregados.getItems()
                            .stream()
                            .map(Cliente::getClienteId)
                            .toList();

                    //System.out.println(clientesIds);
                    promocionService.crearPromocionConClientes(promocion, clientesIds);


                    return null;
                }, (resultado) -> {
                    MensajesAlert.mostrarInformacion(
                            "Operación completada",
                            "Promoción registrada",
                            "La promoción ha sido registrada en el sistema exitosamente."
                    );
                    clean();

                }, (ex) -> {

                    if (ex instanceof PromocionException) {
                        MensajesAlert.mostrarExcepcionThrowable(
                                "Error al crear promoción",
                                "No se pudo completar la operación",
                                ex.getMessage(),
                                ex
                        );
                    } else {
                        MensajesAlert.mostrarExcepcionThrowable(
                                "Error inesperado",
                                "Se produjo una excepción durante la operación",
                                "Ocurrió un error inesperado al intentar crear la promoción. Por favor, inténtelo de nuevo más tarde.",
                                ex
                        );
                    }

                }, null
        );
    }//insertarPromocion

    private void actualizarPromocionesActivas() {
        try {
            PromocionesActivasController controller = ApplicationContextProvider
                    .getApplicationContext()
                    .getBean(PromocionesActivasController.class);
            controller.actualizar(null);
        } catch (Exception ignored) {
            // Si no existe contexto disponible en este momento, la vista se recargará al regresar.
        }
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
    }//obtenerPorcentaje

    private void tipoDescuento(String tipo) {

        switch (tipo) {

            case "porcentaje" -> {
                precioSinDescuento.setDisable(false);
                porcentajeDescuento.setDisable(false);
                LocalDate fecha = LocalDate.now();
                fechaInicio.setValue(fecha);
                fechaFin.setValue(fecha);
                fechaFin.setDisable(false);
                fechaInicio.setDisable(false);
                btnImagen.setDisable(false);
                textFieldImg.setDisable(false);


            }
            case "otro" -> {
                precioSinDescuento.setDisable(false);
                porcentajeDescuento.setDisable(false);
                LocalDate fecha = LocalDate.now();
                fechaInicio.setValue(fecha);
                fechaFin.setValue(fecha);
                fechaFin.setDisable(false);
                fechaInicio.setDisable(false);
                btnImagen.setDisable(false);
                textFieldImg.setDisable(false);

            }
        }//switch

    }

    private void calcularPrecioConDescuento() {
        String precioTexto = precioSinDescuento.getText();

        if (precioTexto == null || precioTexto.isEmpty()) {
            precioConDescuento.setText("");
            return;
        }

        try {
            double precio = Double.parseDouble(precioTexto);
            double porcentaje = porcentajeDescuento.getValue();
            double descuento = precio * (porcentaje / 100.0);
            double precioFinal = precio - descuento;

            precioConDescuento.setText(String.format("%.2f", precioFinal));
        } catch (NumberFormatException e) {
            precioConDescuento.setText("");
        }
    }//calcularPrecioConDescuento

    private boolean empty() {
        boolean empty = false;
        if (nombrePromocion.getText() == null || descripcion.getText() == null  || precioSinDescuento.getText() == null ||
                fechaInicio.getValue() == null || fechaFin.getValue() == null  || nombrePromocion.getText().isEmpty() || descripcion.getText().isEmpty() ||
                precioSinDescuento.getText().isEmpty() )

            empty = true;

        else {
            empty = false;
        }

        return empty;
    }

    private void seleccionarImg() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de imagen", "*.png", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) btnImagen.getScene().getWindow();
        File archivo = fileChooser.showOpenDialog(stage);

        if (archivo != null) {
            String url = archivo.getAbsolutePath();
            textFieldImg.setText(url);
        }

    }//seleccionarImg

    private boolean validarFecha(LocalDate fechaInicioLD, LocalDate fechaFinLD) {
        boolean fechaValida = false;

        if (fechaFinLD.isAfter(fechaInicioLD)) {
            fechaValida = true;

        } else if (fechaFinLD.isBefore(fechaInicioLD)) {
            MensajesAlert.mostrarWarning(
                    "Advertencia",
                    "Rango de fechas no válido",
                    "La fecha de fin no puede ser anterior a la fecha de inicio. Por favor, intente de nuevo."
            );
            fechaValida = false;

        } else if (fechaFinLD.equals(fechaInicioLD)) {

            fechaValida = MensajesAlert.mostrarConfirmacion(
                    "Confirmar fechas",
                    "Fechas de inicio y fin idénticas",
                    "Ha ingresado la misma fecha para el inicio y el fin de la promoción. ¿Desea continuar?",
                    "Continuar",
                    "Cancelar"
            );

            if (fechaValida)
                fechaValida = true;
            else
                fechaValida = false;

        }
        return fechaValida;
    }//validarFecha

    /* ================= HELPERS ================= */

    private String valorONull(String valor) {
        return (valor == null || valor.isBlank()) ? "N/D" : valor;
    }

    private String nombreCompleto(Cliente c) {
        String n = (c.getNombre() == null ? "" : c.getNombre().trim());
        String a1 = (c.getApellido() == null ? "" : c.getApellido().trim());
        String a2 = (c.getSegundoApellido() == null ? "" : c.getSegundoApellido().trim());

        String full = String.join(" ", n, a1, a2).trim();

        return full.isBlank() ? "N/D" : full;
    }

    private void clean() {
        nombrePromocion.setText("");
        descripcion.setText("");
        precioSinDescuento.setText("");
        precioConDescuento.setText("");
        porcentajeDescuento.setValue(0);
        descuentoLabel.setText("Descuento 0%");
        fechaInicio.setValue(null);
        fechaFin.setValue(null);
        //tipoDescuento.setValue(null);

        txtBuscador.setText("");
        clientesAgregados.getItems().clear();
        textFieldImg.setText("");

    }

    private void showLabel(String mensaje, String accion) {
        statusLabel.setText(mensaje + " " + accion);

        if (pauseTransition != null) {
            pauseTransition.stop();
        }
        pauseTransition = new PauseTransition(Duration.seconds(2.5));
        pauseTransition.setOnFinished(event -> statusLabel.setText(""));
        pauseTransition.play();


    }//showLabel

    @Override
    public void cleanup() {
        if (pauseTransition != null) {
            pauseTransition.stop();
        }

        if (clientesSeleccionados != null) {
            clientesSeleccionados.clear();
        }
    }

    @Override
    public void restaurarEstadoInicial() {
        if (txtBuscador != null) {
            txtBuscador.clear();
        }

        modoBusqueda = false;
        terminoBusqueda = "";
        terminoBusquedaActual = "";

        if (clientesSeleccionados != null) {
            clientesSeleccionados.clear();
        }

        configurarPaginador();
    }

}//class
