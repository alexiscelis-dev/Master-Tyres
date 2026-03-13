package com.mastertyres.controllers.fxControllers.Promociones;


import com.mastertyres.ClientesPromocion.model.ClientesPromocion;
import com.mastertyres.ClientesPromocion.service.ClientePromocionService;
import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.cliente.model.StatusCliente;
import com.mastertyres.cliente.service.ClienteService;
import com.mastertyres.common.interfaces.ICleanable;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.utils.FechaUtils;
import com.mastertyres.common.utils.MensajesAlert;
import com.mastertyres.promociones.model.Promocion;
import com.mastertyres.promociones.model.TipoDescuento;
import com.mastertyres.promociones.service.PromocionService;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Component
public class EditarPromocionClienteController implements  IFxController, ICleanable {

    @FXML
    private TextField txtNombre;
    @FXML private TextArea txtDescripcion;
    @FXML private TextField txtPrecio;
    @FXML private TextField precioConDescuento;
    //@FXML private ChoiceBox<String> txtTipoDescuento;

    @FXML private Slider porcentajeDescuento;
    @FXML private Label porcentaje; //  el Label donde mostramos el valor
    @FXML private DatePicker dateInicio;
    @FXML private DatePicker dateFin;
    @FXML private TextField txtRutaImagen;
    @FXML private Button btnSeleccionarImagen;
    @FXML private Button btnCambiar;
    private Promocion promocionSeleccionada;


    @FXML private TableView<Cliente> Tabla_Clientes;
    @FXML private ListView<Cliente> ClientesAgregados;
    @FXML private TableColumn<Cliente, Boolean> colSeleccionCliente;
    @FXML private TextField txtBuscador;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colRFC;
    @FXML private TableColumn<Cliente, String> colCumpleanos;
    @FXML private TableColumn<Cliente, String> colFechaRegistro;
    @FXML private TableColumn<Cliente, String> colNombreEmpresa;
    @FXML private TableColumn<Cliente, String> colTipoCliente;
    @FXML private TableColumn<Cliente, String> colHobbie;
    @FXML private Button btnBuscar;

    private static final int CLIENTES_POR_PAGINA = 20;
    @FXML private Pagination PaginadorClientes;
    private String terminoBusquedaActual = "";
    private String terminoBusqueda = "";
    private boolean modoBusqueda = false;

    private final ObservableSet<Cliente> clientesSeleccionados =
            FXCollections.observableSet();

    @Autowired
    private ClientePromocionService clientePromocionService;

    @Autowired
    private ClienteService clienteService;


    @Autowired
    private PromocionService promocionService;

    //Validaciones:
    private BooleanProperty precioValido = new SimpleBooleanProperty(true);
    private BooleanProperty nombreValido = new SimpleBooleanProperty(true);
    private BooleanProperty descripcionValido = new SimpleBooleanProperty(true);


    // Inicializa el controlador
    public void initialize() {

        configuraciones();
        listeners();
        configurarColumnas();
        configurarPaginador();
        ClientesParticipantesInitialize();

    }//initialize


    private void ClientesParticipantesInitialize() {

    }

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

                    if (checkBox.isSelected()) {
                        clientesSeleccionados.add(cliente);
                    } else {
                        clientesSeleccionados.remove(cliente);
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

                checkBox.setSelected(clientesSeleccionados.contains(cliente));

                setGraphic(checkBox);
            }
        });
    }

    private void configurarListaClientesAgregados() {

        ClientesAgregados.setItems(
                FXCollections.observableArrayList(clientesSeleccionados)
        );

        clientesSeleccionados.addListener((SetChangeListener<Cliente>) change -> {
            ClientesAgregados.getItems().setAll(clientesSeleccionados);
        });

        ClientesAgregados.setCellFactory(list -> new ListCell<>() {
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

        Tabla_Clientes.setItems(
                FXCollections.observableArrayList(resultado.getContent())
        );
        Tabla_Clientes.refresh();
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


    @Override
    public void configuraciones(){

        //txtTipoDescuento.getItems().addAll("Porcentaje", "Otro");

        txtPrecio.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*(\\.\\d{0,2})?")) {
                return change;
            }
            return null;
        }));

        // Configuración del slider
        porcentajeDescuento.setMin(0);
        porcentajeDescuento.setMax(100);
        porcentajeDescuento.setBlockIncrement(1);


    }//configuraciones

    @Override
    public void listeners() {

        configurarValidaciones();

        // Mostrar el valor del slider en el Label en tiempo real
        porcentajeDescuento.valueProperty().addListener((obs, oldVal, newVal) -> {
            porcentaje.setText(String.format("%d%%", newVal.intValue()));
        });


        // Acción para abrir el FileChooser
        btnSeleccionarImagen.setOnAction(e -> seleccionarImagen());

        //Actualizar valores
        btnCambiar.setOnAction(event -> cambiarPromocion() );

        btnBuscar.setOnAction(event -> buscarClientes());

        txtBuscador.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                buscarClientes();
            }
        });

        porcentajeDescuento.valueProperty().addListener((observable, valAnterior, valNuevo) -> {
            //obtenerPorcentaje(valNuevo.doubleValue());
            calcularPrecioConDescuento();
        });


        txtPrecio.textProperty().addListener((observable, valAnterior, nuevoValor) -> {
            calcularPrecioConDescuento();
        });

    }//listeners

    private void calcularPrecioConDescuento() {
        String precioTexto = txtPrecio.getText();

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

    /* ================= HELPERS ================= */

    private String valorONull(String valor) {
        return (valor == null || valor.isBlank()) ? "N/A" : valor;
    }

    private String nombreCompleto(Cliente c) {
        String n = (c.getNombre() == null ? "" : c.getNombre().trim());
        String a1 = (c.getApellido() == null ? "" : c.getApellido().trim());
        String a2 = (c.getSegundoApellido() == null ? "" : c.getSegundoApellido().trim());

        String full = String.join(" ", n, a1, a2).trim();

        return full.isBlank() ? "N/A" : full;
    }

    private void configurarValidaciones() {

        // NOMBRE
        txtNombre.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.isBlank()) {
                nombreValido.set(false);
                txtNombre.setStyle("-fx-border-color: red;");
            } else {
                nombreValido.set(true);
                txtNombre.setStyle("");
            }
        });
        txtNombre.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // pierde foco
                txtNombre.setText(formatoOracion(txtNombre.getText()));
            }
        });

        // DESCRIPCION
        txtDescripcion.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.isBlank()) {
                descripcionValido.set(false);
                txtDescripcion.setStyle("-fx-border-color: red;");
            } else {
                descripcionValido.set(true);
                txtDescripcion.setStyle("");
            }
        });
        txtDescripcion.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // pierde foco
                txtDescripcion.setText(formatoOracion(txtDescripcion.getText()));
            }
        });


        // Precio (solo números decimales)
        txtPrecio.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.isEmpty()) {
                precioValido.set(true);
                txtPrecio.setStyle(""); // estilo normal
            } else if (!newText.matches("\\d*(\\.\\d{0,2})?")) {
                // 🔹 Permite enteros o con hasta 2 decimales
                precioValido.set(false);
                txtPrecio.setStyle("-fx-border-color: red;");
            } else {
                precioValido.set(true);
                txtPrecio.setStyle("");
            }
        });

        // TextFormatter para bloquear entradas inválidas
        txtPrecio.setTextFormatter(new TextFormatter<>(c -> {
            if (c.getControlNewText().matches("\\d*(\\.\\d{0,2})?")) {
                return c;
            }
            return null;
        }));

        // Deshabilitar botón "Agregar Vehículo" hasta que se llenen los campos requeridos


        //Deshabilitar botón "Guardar" cuando NO haya cliente o la lista de vehículos esté vacía
        btnCambiar.disableProperty().bind(
                Bindings.isEmpty(clientesSeleccionados)
                        //.or(txtTipoDescuento.valueProperty().isNull())
                        .or(txtNombre.textProperty().isEmpty())
                        .or(txtDescripcion.textProperty().isEmpty())
                        .or(precioValido.not())
        );

    }

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
                    "Confirmar rango",
                    "Fechas de inicio y fin idénticas",
                    "Ha ingresado la misma fecha para el inicio y el fin de la promoción. ¿Desea continuar con esta configuración?",
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

    public void setPromocion(Promocion promocion) {
        this.promocionSeleccionada = promocion;

        if (promocion != null) {
            txtNombre.setText(promocion.getNombre());
            txtDescripcion.setText(promocion.getDescripcion());
            txtPrecio.setText(String.valueOf(promocion.getPrecio()));
            //txtTipoDescuento.setValue(promocion.getTipoDescuento());

            // Configurar el valor del slider y actualizar el label
            porcentajeDescuento.setValue(promocion.getPorcentaje());
            porcentaje.setText(promocion.getPorcentaje() + "%");
            calcularPrecioConDescuento();

            dateInicio.setValue(LocalDate.parse(promocion.getFechaInicio()));
            dateFin.setValue(LocalDate.parse(promocion.getFechaFin()));
            txtRutaImagen.setText(promocion.getImg());
        }
        cargarClientes();
    }

    private String formatoOracion(String texto) {
        if (texto == null || texto.isBlank()) return "";
        String[] palabras = texto.trim().split("\\s+");
        StringBuilder resultado = new StringBuilder();
        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                resultado.append(palabra.substring(0, 1).toUpperCase())
                        .append(palabra.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return resultado.toString().trim();
    }

    private void cargarClientes() {
        if (promocionSeleccionada == null || promocionSeleccionada.getPromocionId() == null) {
            return;
        }

        try {
            // Obtener los clientes asociados a esta promoción
            List<ClientesPromocion> clientesPromocion =
                    clientePromocionService.listarClientesPorPromocion(promocionSeleccionada.getPromocionId());

            // Limpiar selecciones previas
            clientesSeleccionados.clear();

            // Agregar cada cliente a la lista de seleccionados
            for (ClientesPromocion cp : clientesPromocion) {
                if (cp.getCliente() != null) {
                    clientesSeleccionados.add(cp.getCliente());
                }
            }

            // Refrescar la tabla para que se marquen los checkboxes
            Tabla_Clientes.refresh();

        } catch (Exception e) {
            e.printStackTrace();
            MensajesAlert.mostrarExcepcion(
                    "Error de carga",
                    "Fallo al cargar registros",
                    "Ocurrió un problema técnico al intentar cargar los clientes de la promoción.",
                    e
            );
        }
    }

    // Selección de imagen con FileChooser
    @FXML
    private void seleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            txtRutaImagen.setText(file.getAbsolutePath());
        }
    }

    // Acción para guardar cambios
    @FXML
    private void cambiarPromocion() {

        if (promocionSeleccionada == null) {
            MensajesAlert.mostrarWarning(
                    "Sin selección",
                    "Ninguna promoción seleccionada",
                    "Debe seleccionar una promoción de la lista antes de intentar modificarla."
            );
            return;
        }

        // Validar que haya al menos un cliente seleccionado
        MensajesAlert.mostrarWarning(
                "Datos incompletos",
                "Asignación de clientes obligatoria",
                "Debe asignar al menos un cliente a la promoción antes de continuar."
        );

        //  Validaciones básicas
        if (txtNombre.getText() == null || txtNombre.getText().trim().isEmpty()) {
            MensajesAlert.mostrarWarning(
                    "Datos incompletos",
                    "Nombre de promoción obligatorio",
                    "El campo del nombre de la promoción no puede estar vacío."
            );
            return;
        }

        if (txtDescripcion.getText() == null || txtDescripcion.getText().trim().isEmpty()) {
            MensajesAlert.mostrarWarning(
                    "Datos incompletos",
                    "Descripción obligatoria",
                    "La descripción de la promoción no puede estar vacía."
            );
            return;
        }

        try {
            Float.parseFloat(txtPrecio.getText());
        } catch (NumberFormatException e) {
            MensajesAlert.mostrarWarning(
                    "Advertencia",
                    "Formato de precio incorrecto",
                    "Por favor, ingrese un valor numérico válido para el precio."
            );
            return;
        }

        if (dateInicio.getValue() == null || dateFin.getValue() == null) {
            MensajesAlert.mostrarWarning(
                    "Datos incompletos",
                    "Fechas requeridas",
                    "Debe ingresar tanto la fecha de inicio como la de fin para continuar."
            );
            return;
        }

        if (dateInicio.getValue().isAfter(dateFin.getValue())) {
            MensajesAlert.mostrarWarning(
                    "Advertencia",
                    "Rango de fechas no válido",
                    "La fecha de inicio no puede ser posterior a la fecha de fin."
            );
            return;
        }

        boolean confirmar = MensajesAlert.mostrarConfirmacion(
                "Confirmar actualización",
                "Guardar cambios en la promoción",
                "¿Está seguro de que desea guardar los cambios? Se actualizarán los datos de la promoción seleccionada.",
                "Sí, guardar",
                "Cancelar"
        );

        if (!confirmar) {
            return; // Usuario canceló
        }

        try {
            //  Actualizar datos generales
            promocionSeleccionada.setNombre(txtNombre.getText().trim());
            promocionSeleccionada.setDescripcion(txtDescripcion.getText().trim());
            promocionSeleccionada.setPrecio(Float.parseFloat(txtPrecio.getText().trim()));
            promocionSeleccionada.setTipoDescuento(TipoDescuento.PORCENTAJE.toString());
            promocionSeleccionada.setPorcentaje((int) porcentajeDescuento.getValue());
            promocionSeleccionada.setFechaInicio(dateInicio.getValue().toString());
            promocionSeleccionada.setFechaFin(dateFin.getValue().toString());
            promocionSeleccionada.setImg(txtRutaImagen.getText());
            promocionSeleccionada.setUpdated_at(LocalDateTime.now());

            promocionService.actualizarPromocionConClientes(
                    promocionSeleccionada,
                    new ArrayList<>(clientesSeleccionados)
            );


            MensajesAlert.mostrarInformacion(
                    "Operación completada",
                    "Promoción actualizada",
                    "La información de la promoción se ha actualizado correctamente en el sistema."
            );

            cerrarVentana();

        } catch (Exception e) {

            MensajesAlert.mostrarExcepcion(
                    "Error al actualizar",
                    "No se pudieron guardar los cambios",
                    "Ocurrió un problema técnico al intentar actualizar la promoción: " + e.getMessage(),
                    e
            );

            e.printStackTrace();
        }
    }

    @Override
    public void cleanup() {
        promocionSeleccionada = null;

        if (clientesSeleccionados != null) {
            clientesSeleccionados.clear();
        }
    }

    // Acción de cancelar -> cerrar ventana
    @FXML
    private void cerrarVentana() {
        cleanup();
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

}
