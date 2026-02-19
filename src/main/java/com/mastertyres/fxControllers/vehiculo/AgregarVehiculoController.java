package com.mastertyres.fxControllers.vehiculo;

import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.categoria.service.CategoriaService;
import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.cliente.model.StatusCliente;
import com.mastertyres.cliente.service.ClienteService;
import com.mastertyres.common.exeptions.VehiculoException;
import com.mastertyres.common.interfaces.ICleanable;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.interfaces.ILoader;
import com.mastertyres.common.interfaces.IVentanaPrincipal;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.MenuContextSetting;
import com.mastertyres.detalleCategoria.service.DetalleCategoriaService;
import com.mastertyres.fxComponents.LoadingComponentController;
import com.mastertyres.fxControllers.ventanaPrincipal.VentanaPrincipalController;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.marca.service.MarcaService;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.modelo.service.ModeloService;
import com.mastertyres.vehiculo.model.Vehiculo;
import com.mastertyres.vehiculo.repository.VehiculoRepository;
import com.mastertyres.vehiculo.service.VehiculoService;
import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Year;
import java.util.*;

import static com.mastertyres.common.utils.MensajesAlert.*;

@Component
public class AgregarVehiculoController implements IVentanaPrincipal, IFxController, ILoader, ICleanable {

    @Override
    public void cleanup() {
        // 1. Limpiar tablas
        if (tablaVehiculos != null) {
            tablaVehiculos.getItems().clear();
            tablaVehiculos.setItems(null);
        }

//        if (tablaClientes != null) {
//            tablaClientes.getItems().clear();
//            tablaClientes.setItems(null);
//        }

        // 2. Limpiar colección
        if (listaVehiculos != null) {
            listaVehiculos.clear();
            listaVehiculos = null;
        }

        // 3. Limpiar mapa
        if (modeloPorCategoriaId != null) {
            modeloPorCategoriaId.clear();
        }

        // 4. Nullificar controladores
        ventanaPrincipalController = null;
        loadingOverlayController = null;

        // 5. Nullificar referencias FXML principales
        tablaVehiculos = null;
        //tablaClientes = null;
        rootPane = null;
        ventanaAgregarCliente = null;
    }

    @Autowired
    private MarcaService marcaService;

    @Autowired
    private ModeloService modeloService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private DetalleCategoriaService detalleCategoriaService;

    @Autowired
    private TaskService taskService;


    // Columnas vehiculo
    @FXML
    private TableView<Vehiculo> tablaVehiculos;
    @FXML
    private TableColumn<Vehiculo, String> colMarca;
    @FXML
    private TableColumn<Vehiculo, String> colCategoria;
    @FXML
    private TableColumn<Vehiculo, String> colColor;
    @FXML
    private TableColumn<Vehiculo, String> colPlacas;
    @FXML
    private TableColumn<Vehiculo, String> colAnnio;
    @FXML
    private TableColumn<Vehiculo, String> colModelo;
    @FXML
    private TableColumn<Vehiculo, String> colNumSerie;
    @FXML
    private TableColumn<Vehiculo, String> colKilometros;
    @FXML
    private TableColumn<Vehiculo, String> colultimoServicio;
    @FXML
    private TableColumn<Vehiculo, String> colObservaciones;
    @FXML
    private TableColumn<Vehiculo, Void> colEliminar;

    // Campos vehiculo
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ChoiceBox<Marca> choiceMarca;
    @FXML
    private ChoiceBox<Modelo> choiceModelo;
    @FXML
    private ChoiceBox<Categoria> choiceCategoria;
    @FXML
    private TextField txtColor;
    @FXML
    private TextField txtPlacas;
    @FXML
    private Spinner<Integer> spinnerAnio;
    @FXML
    private TextField txtSerie;
    @FXML
    private TextField txtKilometros;
    @FXML
    private DatePicker pickerUltimoServicio;
    @FXML
    private TextField txtObservaciones;
    @FXML
    private Button btnAgregarVehiculo;
    @FXML
    private Button btnGuardar;

    // Campos Cliente
    @FXML
    private AnchorPane ventanaAgregarCliente;
    @FXML
    private TextField txtClienteNombre;
    @FXML
    private TextField txtClienteTipo;
    @FXML
    private TextField txtBuscarCliente;
    //@FXML private ListView<Cliente> listaClientes;
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
    private ObservableList<Vehiculo> listaVehiculos = FXCollections.observableArrayList();
    @FXML
    private Button btnBuscarCliente;
    @FXML
    private Button btnLimpiar;

    private static final String STYLE_FIELD = "";

    private LoadingComponentController loadingOverlayController;

    private BooleanProperty serieValido = new SimpleBooleanProperty(true);
    private BooleanProperty placasValido = new SimpleBooleanProperty(true);
    private BooleanProperty kilometrosValido = new SimpleBooleanProperty(true);
    private BooleanProperty ColorValido = new SimpleBooleanProperty(true);
    private final Map<Integer, Modelo> modeloPorCategoriaId = new HashMap<>();

    private VentanaPrincipalController ventanaPrincipalController;

    @Override
    public void setVentanaPrincipalController(VentanaPrincipalController controller) {
        this.ventanaPrincipalController = controller;
    }

    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingOverlayController = loading;
    }


    @FXML
    private void initialize() {

        cargarOpciones();
        configuraciones();
        listeners();

    }//initialize

    @Override
    public void configuraciones() {

        MenuContextSetting.disableMenu(rootPane);

        //Deshabilitar mediante el nodo interno (textField Interno)
        MenuContextSetting.disableMenu(spinnerAnio.getEditor());
        MenuContextSetting.disableMenu(pickerUltimoServicio.getEditor());


        int currentYear = Year.now().getValue();
        SpinnerValueFactory<Integer> yearFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, currentYear, currentYear);
        spinnerAnio.setValueFactory(yearFactory);

        tablaVehiculos.setItems(listaVehiculos);

        colMarca.setCellValueFactory(data -> {
            Marca marca = data.getValue().getMarca();
            return new SimpleStringProperty(marca != null ? marca.getNombreMarca() : "");
        });
        colModelo.setCellValueFactory(data -> {
            Modelo modelo = data.getValue().getModelo();
            return new SimpleStringProperty(modelo != null ? modelo.getNombreModelo() : "");
        });
        colCategoria.setCellValueFactory(data -> {
            Categoria categoria = data.getValue().getCategoria();
            return new SimpleStringProperty(categoria != null ? categoria.getNombreCategoria() : "");
        });

        colAnnio.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getAnio())));
        colColor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getColor()));
        colPlacas.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPlacas()));
        colNumSerie.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNumSerie()));
        colKilometros.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getKilometros())));
        colultimoServicio.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUltimoServicio()));
        colObservaciones.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getObservaciones()));

        // Botón eliminar
        colEliminar.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button();

            {
                // Quitar texto y agregar imagen
                Image img = new Image(getClass().getResourceAsStream("/icons/delete.png"));
                ImageView imgv = new ImageView(img);
                imgv.setFitWidth(18);   // tamaño icono
                imgv.setFitHeight(18);
                btn.setGraphic(imgv);

                btn.setOnAction(e -> {
                    Vehiculo v = getTableView().getItems().get(getIndex());
                    listaVehiculos.remove(v);
                });

                // (opcional) estilo para que sea redondo o plano
                btn.setStyle("-fx-background-color: red;");

                btn.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
                    if (isNowHovered) {
                        btn.setStyle("-fx-scale-x: 1.1;\n" +
                                "    -fx-scale-y: 1.1;");
                    } else {
                        btn.setStyle("-fx-background-color: red;");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });


        colObservaciones.setPrefWidth(400);
        colObservaciones.setMinWidth(100);


    }//configuraciones

    @Override
    public void listeners() {

        btnLimpiar.setOnAction(event -> {
            limpiarCamposVehiculo();
            tablaClientes.setItems(FXCollections.observableArrayList());
            tablaVehiculos.setItems(FXCollections.observableArrayList());
        });

        //  Listener del botón buscar
        btnBuscarCliente.setOnAction(e -> {
            if (!txtBuscarCliente.getText().isEmpty())
                buscarCliente();
        });

        configurarValidaciones();

        //Hacer que se pueda borrar en los DatePicker
        pickerUltimoServicio.getEditor().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case BACK_SPACE, DELETE -> {
                    pickerUltimoServicio.setValue(null);
                }
            }
        });

        //Cliente Seleccionado
        tablaClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtClienteNombre.setText(
                        newSelection.getNombre() + " " +
                                newSelection.getApellido() + " " +
                                newSelection.getSegundoApellido()
                );
                txtClienteTipo.setText(newSelection.getTipoCliente());
            } else {
                txtClienteNombre.clear();
                txtClienteTipo.clear();
            }
        });

        //evitar que los choiceBox aparezcan en otros lados al momento de abrirlos

        choiceMarca.setOnMousePressed(event -> {
            if (!choiceMarca.isShowing()) {
                choiceMarca.show();
                choiceMarca.hide();
            }
        });

        choiceModelo.setOnMousePressed(event -> {
            if (!choiceModelo.isShowing()) {
                if (!choiceModelo.isShowing()) {
                    choiceModelo.show();
                    choiceModelo.hide();
                }
            }
        });

        choiceCategoria.setOnMousePressed(event -> {
            if (!choiceCategoria.isShowing()) {
                choiceCategoria.show();
                choiceCategoria.hide();
            }
        });

    }//listeners

    private void configurarValidaciones() {

        //VALIDACIONES DE CHOICESBOXS
        choiceModelo.disableProperty().bind(choiceMarca.valueProperty().isNull());
        choiceCategoria.disableProperty().bind(choiceModelo.valueProperty().isNull());

        txtKilometros.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,6}")) {
                txtKilometros.setText(oldValue);
            }
        });

        // Placas
        txtPlacas.textProperty().addListener((obs, oldText, newText) -> {
            String texto = newText.toUpperCase();
            txtPlacas.setText(texto);
            if (texto.isEmpty()) {
                placasValido.set(true);
                txtPlacas.setStyle("");
            } else if (!texto.matches("^[A-Z0-9]+(-[A-Z0-9]+)*$")) {
                placasValido.set(false);
                txtPlacas.setStyle("-fx-border-color: red;");
            } else {
                placasValido.set(true);
                txtPlacas.setStyle("");
            }
        });


        // Número de serie (VIN)
        txtSerie.textProperty().addListener((obs, oldText, newText) -> {
            String texto = newText.toUpperCase();
            txtSerie.setText(texto);
            if (texto.isEmpty()) {
                serieValido.set(true);
                txtSerie.setStyle("");
            } else if (!texto.matches("^[A-HJ-NPR-Z0-9]{17}$")) {
                serieValido.set(false);
                txtSerie.setStyle("-fx-border-color: red;");
            } else {
                serieValido.set(true);
                txtSerie.setStyle("");
            }
        });

        // Kilómetros
        txtKilometros.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.isEmpty()) {
                kilometrosValido.set(true);
                txtKilometros.setStyle(""); // estilo normal
            } else if (!newText.matches("\\d*")) {
                kilometrosValido.set(false);
                txtKilometros.setStyle("-fx-border-color: red;"); // borde rojo si es inválido
            } else {
                kilometrosValido.set(true);
                txtKilometros.setStyle("");
            }
        });
        txtKilometros.setTextFormatter(new TextFormatter<>(c -> {
            if (c.getControlNewText().matches("\\d*")) {
                return c;
            }
            return null;
        }));

        //Color
        txtColor.textProperty().addListener((obs, oldText, newText) -> {
            String texto = newText.toUpperCase();
            txtColor.setText(texto);
            if (texto.isEmpty()) {
                ColorValido.set(false);
                txtColor.setStyle("-fx-border-color: red;");
            } else if (!texto.matches("^[A-Z]{1,20}")) {
                ColorValido.set(false);
                txtColor.setStyle("-fx-border-color: red;");
            } else {
                ColorValido.set(true);
                txtColor.setStyle("");
            }
        });

        // Deshabilitar botón "Agregar Vehículo" hasta que se llenen los campos requeridos
        btnAgregarVehiculo.disableProperty().bind(
                choiceMarca.valueProperty().isNull()
                        .or(choiceModelo.valueProperty().isNull())
                        .or(txtColor.textProperty().isEmpty())
                        .or(spinnerAnio.valueProperty().isNull())
                        .or(serieValido.not())
                        .or(placasValido.not())
                        .or(kilometrosValido.not())
        );

        //Deshabilitar botón "Guardar" cuando NO haya cliente o la lista de vehículos esté vacía
        btnGuardar.disableProperty().bind(
                Bindings.isEmpty(listaVehiculos)
                        .or(tablaClientes.getSelectionModel().selectedItemProperty().isNull())
        );
    }

    @FXML
    private void agregarVehiculo(ActionEvent event) {
        Vehiculo v = new Vehiculo();

        v.setColor(txtColor.getText());
        v.setPlacas(txtPlacas.getText());
        v.setNumSerie(txtSerie.getText());
        v.setObservaciones(txtObservaciones.getText());

        if (pickerUltimoServicio.getValue() != null) {
            v.setUltimoServicio(pickerUltimoServicio.getValue().toString());
        }


        v.setAnio(spinnerAnio.getValue());


        try {
            v.setKilometros(Integer.parseInt(txtKilometros.getText()));
        } catch (NumberFormatException e) {
            v.setKilometros(0);
        }


        v.setMarca(choiceMarca.getValue());
        v.setModelo(obtenerModeloSeleccionado());
        //v.setModelo(choiceModelo.getValue());
        v.setCategoria(choiceCategoria.getValue());

        listaVehiculos.add(v);
        limpiarCamposVehiculo();
    }

    private void cargarOpciones() {
        List<Marca> marcas = marcaService.listarMarcas();
        List<Modelo> modelos = modeloService.listarModelos();
        List<Categoria> categorias = categoriaService.listarCategorias();

        Map<String, List<Modelo>> modelosPorNombre = new HashMap<>();


        choiceMarca.setItems(FXCollections.observableArrayList(marcas));
        choiceModelo.setItems(FXCollections.observableArrayList(modelos)); // se llenará luego con el filtro
        choiceCategoria.setItems(FXCollections.observableArrayList(categorias));

        // Mostrar nombres
        choiceMarca.setConverter(new StringConverter<>() {
            @Override
            public String toString(Marca marca) {
                return marca != null ? marca.getNombreMarca() : "";
            }

            @Override
            public Marca fromString(String string) {
                return null;
            }
        });

        choiceModelo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Modelo modelo) {
                return modelo != null ? modelo.getNombreModelo() : "";
            }

            @Override
            public Modelo fromString(String string) {
                return null;
            }
        });

        choiceCategoria.setConverter(new StringConverter<>() {
            @Override
            public String toString(Categoria categoria) {
                return categoria != null ? categoria.getNombreCategoria() : "";
            }

            @Override
            public Categoria fromString(String string) {
                return null;
            }
        });

        //  Listener para filtrar modelos según la marca seleccionada
        choiceMarca.getSelectionModel().selectedItemProperty().addListener((obs, oldMarca, newMarca) -> {
            if (newMarca != null) {
//                List<Modelo> modelosFiltrados = modelos.stream()
//                        .filter(m -> m.getMarca_id().getMarcaId().equals(newMarca.getMarcaId()))
//                        .toList();
//                choiceModelo.setItems(FXCollections.observableArrayList(modelosFiltrados));
                modelosPorNombre.clear();
                modelos.stream()
                        .filter(m -> m.getMarca_id().getMarcaId().equals(newMarca.getMarcaId()))
                        .forEach(modelo -> modelosPorNombre
                                .computeIfAbsent(normalizarModelo(modelo.getNombreModelo()), key -> new ArrayList<>())
                                .add(modelo));
                List<Modelo> modelosFiltrados = modelosPorNombre.values().stream()
                        .map(modelosConNombre -> modelosConNombre.get(0))
                        .sorted(Comparator.comparing(Modelo::getNombreModelo))
                        .toList();


                choiceModelo.setItems(FXCollections.observableArrayList(modelosFiltrados));
                choiceModelo.getSelectionModel().clearSelection();
                choiceCategoria.getItems().clear();
            } else {
                choiceModelo.getItems().clear();
            }
        });

        choiceModelo.getSelectionModel().selectedItemProperty().addListener((obs, oldModelo, newModelo) -> {
            Marca marcaSeleccionada = choiceMarca.getValue();

            if (marcaSeleccionada != null && newModelo != null) {
                Map<Integer, Categoria> categoriasPorId = new LinkedHashMap<>();
                modeloPorCategoriaId.clear();
                List<Modelo> modelosConNombre = modelosPorNombre
                        .getOrDefault(normalizarModelo(newModelo.getNombreModelo()), List.of());
                for (Modelo modelo : modelosConNombre) {
                    List<Categoria> categoriasFiltradas = detalleCategoriaService.listarCategoriasPorMarcaYModelo(
                            marcaSeleccionada.getMarcaId(),
                            modelo.getModeloId()
                    );
                    for (Categoria categoria : categoriasFiltradas) {
                        categoriasPorId.putIfAbsent(categoria.getCategoriaId(), categoria);
                        modeloPorCategoriaId.putIfAbsent(categoria.getCategoriaId(), modelo);
                    }
                }
                choiceCategoria.setItems(FXCollections.observableArrayList(categoriasPorId.values()));
                choiceCategoria.getSelectionModel().clearSelection();


            } else {
                choiceCategoria.getItems().clear();
            }
        });


        // Configurar columnas del TableView
        colNombreCompleto.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        emptyIfNull(cellData.getValue().getNombre()) + " " +
                                emptyIfNull(cellData.getValue().getApellido()) + " " +
                                emptyIfNull(cellData.getValue().getSegundoApellido())
                )
        );

        colTipoCliente.setCellValueFactory(cellData ->
                new SimpleStringProperty(naIfNullOrEmpty(cellData.getValue().getTipoCliente()))
        );

        colRfc.setCellValueFactory(cellData ->
                new SimpleStringProperty(naIfNullOrEmpty(cellData.getValue().getRfc()))
        );

        colTelefono.setCellValueFactory(cellData ->
                new SimpleStringProperty(naIfNullOrEmpty(cellData.getValue().getNumTelefono()))
        );


        txtBuscarCliente.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                buscarCliente();
            }
        });

        PauseTransition pause = new PauseTransition(Duration.millis(400));

        txtBuscarCliente.textProperty().addListener((obs, oldValue, newValue) -> {
            pause.setOnFinished(e -> buscarCliente());
            pause.playFromStart();
        });

    }

    private String normalizarModelo(String nombre) {
        return nombre == null ? "" : nombre.trim().toLowerCase();
    }

    private Modelo obtenerModeloSeleccionado() {
        Categoria categoria = choiceCategoria.getValue();
        if (categoria != null) {
            Modelo modelo = modeloPorCategoriaId.get(categoria.getCategoriaId());
            if (modelo != null) {
                return modelo;
            }
        }
        return choiceModelo.getValue();
    }

    private void buscarCliente() {
        // Llenar tabla
        List<Cliente> clientes = clienteService.listarClientesConVehiculos(StatusCliente.ACTIVE.toString());


        ObservableList<Cliente> listaClientesOriginal = FXCollections.observableArrayList(clientes);
        String filtro = txtBuscarCliente.getText().trim().toLowerCase();

        if (filtro.isEmpty()) {
            tablaClientes.setItems(listaClientesOriginal);
        } else {
            ObservableList<Cliente> filtrados = listaClientesOriginal.filtered(cliente ->
                    (cliente.getNombre() != null && cliente.getNombre().toLowerCase().contains(filtro)) ||
                            (cliente.getApellido() != null && cliente.getApellido().toLowerCase().contains(filtro)) ||
                            (cliente.getSegundoApellido() != null && cliente.getSegundoApellido().toLowerCase().contains(filtro)) ||
                            (cliente.getRfc() != null && cliente.getRfc().toLowerCase().contains(filtro)) ||
                            (cliente.getNumTelefono() != null && cliente.getNumTelefono().toLowerCase().contains(filtro)) ||
                            (cliente.getTipoCliente() != null && cliente.getTipoCliente().toLowerCase().contains(filtro)) ||
                            (cliente.getCiudad() != null && cliente.getCiudad().toLowerCase().contains(filtro)) ||
                            (cliente.getEstado() != null && cliente.getEstado().toLowerCase().contains(filtro)) ||
                            (cliente.getHobbie() != null && cliente.getHobbie().toLowerCase().contains(filtro)) ||
                            (cliente.getDomicilio() != null && cliente.getDomicilio().toLowerCase().contains(filtro)) ||
                            (cliente.getCreated_at() != null && cliente.getCreated_at().toLowerCase().contains(filtro)) ||
                            (cliente.getUpdated_at() != null && cliente.getUpdated_at().toLowerCase().contains(filtro)) ||
                            (cliente.getFechaCumple() != null && cliente.getFechaCumple().toLowerCase().contains(filtro))
            );
            tablaClientes.setItems(filtrados);
        }
    }

    private String emptyIfNull(String value) {
        return value == null ? "" : value;
    }

    private String naIfNullOrEmpty(String value) {
        return (value == null || value.trim().isEmpty()) ? "N/A" : value;
    }

    private void limpiarCamposVehiculo() {


        choiceMarca.setValue(null);
        choiceModelo.setValue(null);
        choiceCategoria.setValue(null);
        spinnerAnio.getValueFactory().setValue(LocalDate.now().getYear());

        txtColor.clear();
        txtPlacas.clear();
        txtSerie.clear();
        txtKilometros.clear();
        pickerUltimoServicio.setValue(null);
        txtObservaciones.clear();

        txtSerie.setStyle(STYLE_FIELD);
        txtColor.setStyle(STYLE_FIELD);
        txtPlacas.setStyle(STYLE_FIELD);
        txtKilometros.setStyle(STYLE_FIELD);
        spinnerAnio.setStyle(STYLE_FIELD);
        pickerUltimoServicio.setStyle(STYLE_FIELD);
        txtObservaciones.setStyle(STYLE_FIELD);
        txtBuscarCliente.setStyle(STYLE_FIELD);


    }

    @FXML
    private void guardarVehiculos(ActionEvent event) {


        Cliente clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado == null) {
            mostrarWarning("Cliente no seleccionado", "", "Por favor selecciona un cliente.");
            return;
        }

        if (listaVehiculos.isEmpty()) {
            mostrarWarning("Sin vehículos", "", "Agrega al menos un vehículo antes de guardar.");
            return;
        }

        // Validar que no haya duplicados en la lista antes de guardar
        Set<String> placasSerieSet = new HashSet<>();

        for (Vehiculo v : listaVehiculos) {
            String placas = v.getPlacas();
            String numSerie = v.getNumSerie();

            // Si ambos están vacíos o nulos, lo dejamos pasar
            if ((placas == null || placas.isBlank()) && (numSerie == null || numSerie.isBlank())) {
                continue;
            }

            // Normalizamos valores nulos a vacío
            placas = placas != null ? placas.trim() : "";
            numSerie = numSerie != null ? numSerie.trim() : "";

            String key = placas + "|" + numSerie;

            if (!placasSerieSet.add(key)) {
                mostrarWarning(
                        "Vehículos repetidos", "",
                        "Hay vehículos con placas o número de serie repetidos en la lista."
                );
                return;
            }
        }
        taskService.runTask(
                loadingOverlayController,
                () -> {
                    vehiculoService.guardarVehiculos(clienteSeleccionado, listaVehiculos);


                    return null;

                }, (resultado) -> {
                    mostrarInformacion("Éxito", "", "Vehículos guardados correctamente.");
                    listaVehiculos.clear();
                    limpiarCamposVehiculo();
                    tablaClientes.getSelectionModel().clearSelection();

                }, (ex) -> {

                    if (ex instanceof VehiculoException) {
                        mostrarWarning("No se pudo guardar", "Ocurrio un problema al guardar el/los vehiclos", ex.getMessage());

                    } else {
                        mostrarError("Error interno",
                                "Error inesperado",
                                "Ocurrio un error al intentar registrar el/los vehiculos");
                    }

                }, null
        );


    }//guardarVehiculos

    public void Cancelar(ActionEvent actionEvent) {

        ventanaPrincipalController.irAtras();

    }

}//class
