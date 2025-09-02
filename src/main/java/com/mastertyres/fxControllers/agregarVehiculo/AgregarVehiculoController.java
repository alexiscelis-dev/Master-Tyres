package com.mastertyres.fxControllers.agregarVehiculo;

import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.categoria.services.CategoriaService;
import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.cliente.model.ClienteStatus;
import com.mastertyres.cliente.service.ClienteService;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.marca.services.MarcaService;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.modelo.services.ModeloService;
import com.mastertyres.vehiculo.model.StatusCliente;
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
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mastertyres.common.MensajesAlert.mostrarInformacion;
import static com.mastertyres.common.MensajesAlert.mostrarWarning;

import static com.mastertyres.common.MensajesAlert.mostrarError;

@Component
public class AgregarVehiculoController {
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


// Columnas vehiculo
    @FXML private TableView<Vehiculo> tablaVehiculos;
    @FXML private TableColumn<Vehiculo, String> colMarca;
    @FXML private TableColumn<Vehiculo, String> colCategoria;
    @FXML private TableColumn<Vehiculo, String> colColor;
    @FXML private TableColumn<Vehiculo, String> colPlacas;
    @FXML private TableColumn<Vehiculo, String> colAnnio;
    @FXML private TableColumn<Vehiculo, String> colModelo;
    @FXML private TableColumn<Vehiculo, String> colNumSerie;
    @FXML private TableColumn<Vehiculo, String> colKilometros;
    @FXML private TableColumn<Vehiculo, String> colultimoServicio;
    @FXML private TableColumn<Vehiculo, String> colObservaciones;
    @FXML private TableColumn<Vehiculo, Void> colEliminar;

// Campos vehiculo
    @FXML private ChoiceBox<Marca> choiceMarca;
    @FXML private ChoiceBox<Modelo> choiceModelo;
    @FXML private ChoiceBox<Categoria> choiceCategoria;
    @FXML private TextField txtColor;
    @FXML private TextField txtPlacas;
    @FXML private Spinner<Integer> spinnerAnio;
    @FXML private TextField txtSerie;
    @FXML private TextField txtKilometros;
    @FXML private DatePicker pickerUltimoServicio;
    @FXML private TextField txtObservaciones;
    @FXML private Button btnAgregarVehiculo;
    @FXML private Button btnGuardar;

// Campos Cliente
    @FXML private TextField txtClienteNombre;
    @FXML private TextField txtClienteTipo;
    @FXML private TextField txtBuscarCliente;
    //@FXML private ListView<Cliente> listaClientes;
    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, String> colNombreCompleto;
    @FXML private TableColumn<Cliente, String> colTipoCliente;
    @FXML private TableColumn<Cliente, String> colRfc;
    @FXML private TableColumn<Cliente, String> colTelefono;
    private ObservableList<Vehiculo> listaVehiculos = FXCollections.observableArrayList();
    @FXML private Button btnBuscarCliente;

    private BooleanProperty serieValido = new SimpleBooleanProperty(true);
    private BooleanProperty placasValido = new SimpleBooleanProperty(true);
    private BooleanProperty kilometrosValido = new SimpleBooleanProperty(true);
    private BooleanProperty ColorValido = new SimpleBooleanProperty(true);



    @FXML
    private void initialize() {

        cargarOpciones();
        configurarValidaciones();

        int currentYear = Year.now().getValue();
        SpinnerValueFactory<Integer> yearFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, currentYear , currentYear);
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
                ImageView iv = new ImageView(img);
                iv.setFitWidth(18);   // tamaño icono
                iv.setFitHeight(18);
                btn.setGraphic(iv);

                btn.setOnAction(e -> {
                    Vehiculo v = getTableView().getItems().get(getIndex());
                    listaVehiculos.remove(v);
                });

                // (opcional) estilo para que sea redondo o plano
                btn.setStyle("-fx-background-color: white;");

                btn.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
                    if (isNowHovered) {
                        btn.setStyle("-fx-scale-x: 1.1;\n" +
                                "    -fx-scale-y: 1.1;");
                    } else {
                        btn.setStyle("-fx-background-color: white;");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
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



    }

    private void configurarValidaciones() {

        // Placas
        txtPlacas.textProperty().addListener((obs, oldText, newText) -> {
            String texto = newText.toUpperCase();
            txtPlacas.setText(texto);
            if (texto.isEmpty()) {
                placasValido.set(true);
                txtPlacas.setStyle("");
            } else if (!texto.matches("^[A-Z0-9]{1,7}(-[A-Z0-9]{1,4})?$")) {
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
        v.setModelo(choiceModelo.getValue());
        v.setCategoria(choiceCategoria.getValue());

        listaVehiculos.add(v);
        limpiarCamposVehiculo();
    }

    private void cargarOpciones() {
        List<Marca> marcas = marcaService.listarMarcas();
        List<Modelo> modelos = modeloService.listarModelos();
        List<Categoria> categorias = categoriaService.listarCategorias();


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

        // 🔹 Listener para filtrar modelos según la marca seleccionada
        choiceMarca.getSelectionModel().selectedItemProperty().addListener((obs, oldMarca, newMarca) -> {
            if (newMarca != null) {
                List<Modelo> modelosFiltrados = modelos.stream()
                        .filter(m -> m.getMarca_id().getMarcaId().equals(newMarca.getMarcaId()))
                        .toList();
                choiceModelo.setItems(FXCollections.observableArrayList(modelosFiltrados));
            } else {
                choiceModelo.getItems().clear();
            }
        });

       /* choiceModelo.getSelectionModel().selectedItemProperty().addListener((obs, oldModelo, newModelo) -> {
            Marca marcaSeleccionada = choiceMarca.getSelectionModel().getSelectedItem();
            if (marcaSeleccionada != null && newModelo != null) {
                List<Categoria> categoriasFiltradas = vehiculoService.listarCategoriasPorMarcaYModelo(
                        "ACTIVE",
                        marcaSeleccionada.getMarcaId(),
                        newModelo.getModeloId()
                );
                choiceCategoria.setItems(FXCollections.observableArrayList(categoriasFiltradas));
            } else {
                choiceCategoria.getItems().clear();
            }
        });*/

        // Configurar columnas del TableView
        colNombreCompleto.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getNombre() + " " +
                                cellData.getValue().getApellido() + " " +
                                cellData.getValue().getSegundoApellido()
                )
        );
        colTipoCliente.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTipoCliente())
        );
        colRfc.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRfc())
        );
        colTelefono.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNumTelefono())
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

        //  Listener del botón buscar
        btnBuscarCliente.setOnAction(e -> {
            buscarCliente();
        });
    }

    private void buscarCliente(){
        // Llenar tabla
        List<Cliente> clientes = clienteService.listarClientesConVehiculos(StatusCliente.ACTIVE.toString());
        //tablaClientes.setItems(FXCollections.observableArrayList(clientes));


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
    }

    @FXML
    private void guardarVehiculos(ActionEvent event) {
        for (Vehiculo v : listaVehiculos) {
            String placas = v.getPlacas();
            String numSerie = v.getNumSerie();

            if (placas != null && !placas.isBlank()){
                if (vehiculoService.existeVehiculoPorPlacas(placas)){
                    mostrarWarning( "Vehículo duplicado","",
                            "Ya existe un vehículo activo con las mismas placas.");
                    return;
                }
            }

            if (numSerie != null && !numSerie.isBlank()){
                if (vehiculoService.existeVehiculoPorNumeroSerie(numSerie)){
                    mostrarWarning("Vehículo duplicado","",
                            "Ya existe un vehículo activo con el mismo numero de serie.");
                    return;
                }
            }

        }

        Cliente clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado == null) {
            mostrarWarning("Cliente no seleccionado","","Por favor selecciona un cliente.");
            return;
        }

        if (listaVehiculos.isEmpty()) {
            mostrarWarning("Sin vehículos","","Agrega al menos un vehículo antes de guardar.");
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


        try {
            vehiculoService.guardarVehiculos(clienteSeleccionado, listaVehiculos);

            mostrarInformacion("Éxito","","Vehículos guardados correctamente.");
            listaVehiculos.clear();
            limpiarCamposVehiculo();
            tablaClientes.getSelectionModel().clearSelection();
        } catch (Exception e) {
            mostrarWarning("Error","","No se pudieron guardar los vehículos");

        }
    }

}
