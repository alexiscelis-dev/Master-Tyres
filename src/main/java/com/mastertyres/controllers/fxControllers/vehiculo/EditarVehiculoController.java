package com.mastertyres.controllers.fxControllers.vehiculo;

import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.categoria.service.CategoriaService;
import com.mastertyres.common.exeptions.VehiculoException;
import com.mastertyres.common.interfaces.ICleanable;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.interfaces.ILoader;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.MensajesAlert;
import com.mastertyres.common.utils.MenuContextSetting;
import com.mastertyres.common.utils.RegexTools;
import com.mastertyres.detalleCategoria.service.DetalleCategoriaService;
import com.mastertyres.components.fxComponents.LoadingComponentController;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.marca.service.MarcaService;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.modelo.service.ModeloService;
import com.mastertyres.vehiculo.model.Vehiculo;
import com.mastertyres.vehiculo.model.VehiculoDTO;
import com.mastertyres.vehiculo.service.VehiculoService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class EditarVehiculoController implements IFxController, ILoader, ICleanable {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ChoiceBox<Marca> choiceMarca;
    @FXML
    private ChoiceBox<Modelo> choiceModelo;
    @FXML
    private ChoiceBox<Categoria> choiceCategoria;
    @FXML
    private Spinner<Integer> spinnerAnio;
    @FXML
    private TextField txtKilometros;
    @FXML
    private TextField txtColor;
    @FXML
    private TextField txtPlacas;
    @FXML
    private TextField txtNumSerie;
    @FXML
    private TextField txtObservaciones;
    @FXML
    private DatePicker dateUltimoServicio;
    @FXML
    private Button btnCambiar;

    private VehiculoDTO vehiculo;

    @Autowired
    private MarcaService marcaService;

    @Autowired
    private ModeloService modeloService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private DetalleCategoriaService detalleCategoriaService;

    @Autowired
    private TaskService taskService;

    private List<Modelo> modelos;
    private LoadingComponentController loadingOverlayController;


    private BooleanProperty serieValido = new SimpleBooleanProperty(true);
    private BooleanProperty placasValido = new SimpleBooleanProperty(true);
    private BooleanProperty kilometrosValido = new SimpleBooleanProperty(true);
    private BooleanProperty ColorValido = new SimpleBooleanProperty(true);

    private final Map<Integer, Modelo> modeloPorCategoriaId = new HashMap<>();

    @FXML
    public void initialize() {

        configuraciones();
        listeners();

        // Cargar marcas, modelos y categorías
        cargarOpciones();

    }//initialize

    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        loadingOverlayController = loading;
    }

    @Override
    public void configuraciones() {

        MenuContextSetting.disableMenuDatePicker(rootPane);
        MenuContextSetting.disableMenu(rootPane);
        RegexTools.aplicarNumeroEntero(spinnerAnio.getEditor());

        // Configurar spinner para año
        int anioActual = LocalDate.now().getYear();
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, anioActual, anioActual);
        spinnerAnio.setValueFactory(valueFactory);

    }//configuraciones

    @Override
    public void listeners() {

        // Cargar Validaciones
        configurarValidaciones();

        dateUltimoServicio.getEditor().setOnKeyPressed(event ->{
            switch (event.getCode()){
                case BACK_SPACE, DELETE -> {
                    dateUltimoServicio.setValue(null);
                }
            }
        });

        btnCambiar.setOnAction(event -> actualizarVehiculo());

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
        txtNumSerie.textProperty().addListener((obs, oldText, newText) -> {
            String texto = newText.toUpperCase();
            txtNumSerie.setText(texto);
            if (texto.isEmpty()) {
                serieValido.set(true);
                txtNumSerie.setStyle("");
            } else if (!texto.matches("^[A-HJ-NPR-Z0-9]{17}$")) {
                serieValido.set(false);
                txtNumSerie.setStyle("-fx-border-color: red;");
            } else {
                serieValido.set(true);
                txtNumSerie.setStyle("");
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
        btnCambiar.disableProperty().bind(
                choiceMarca.valueProperty().isNull()
                        .or(choiceModelo.valueProperty().isNull())
                        .or(txtColor.textProperty().isEmpty())
                        .or(txtColor.textProperty().isNull())
                        .or(spinnerAnio.valueProperty().isNull())
                        .or(serieValido.not())
                        .or(placasValido.not())
                        .or(kilometrosValido.not())
                        .or(choiceMarca.valueProperty().isNull())
                        .or(choiceModelo.valueProperty().isNull())
                        .or(choiceCategoria.valueProperty().isNull())
                        .or(serieValido.not())

        );

    }

    private void cargarOpciones() {
        List<Marca> marcas = marcaService.listarMarcas();
        modelos = modeloService.listarModelos();
        List<Categoria> categorias = categoriaService.listarCategorias();
        Map<String, List<Modelo>> modelosPorNombre = new HashMap<>();

        choiceMarca.setItems(FXCollections.observableArrayList(marcas));
        choiceModelo.setItems(FXCollections.observableArrayList(modelos)); // se filtrará después
        choiceCategoria.setItems(FXCollections.observableArrayList(categorias));

        // Mostrar nombres en los ChoiceBox
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

        // Listener para filtrar modelos según la marca seleccionada
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

//                List<Categoria> categoriasFiltradas =
//                        detalleCategoriaService.listarCategoriasPorMarcaYModelo(
//                                marcaSeleccionada.getMarcaId(),
//                                newModelo.getModeloId()
//                        );
//
//                choiceCategoria.setItems(FXCollections.observableArrayList(categoriasFiltradas));

            } else {
                choiceCategoria.getItems().clear();
            }
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

    public void setVehiculo(VehiculoDTO vehiculo) {
        this.vehiculo = vehiculo;

        if (vehiculo != null) {
            // Seleccionar marca
            choiceMarca.getItems().stream()
                    .filter(m -> m.getNombreMarca().equals(vehiculo.getNombreMarca()))
                    .findFirst()
                    .ifPresent(choiceMarca::setValue);

            // Seleccionar modelo
            choiceModelo.getItems().stream()
                    .filter(m -> m.getNombreModelo().equals(vehiculo.getNombreModelo()))
                    .findFirst()
                    .ifPresent(choiceModelo::setValue);

            // Seleccionar categoría
            choiceCategoria.getItems().stream()
                    .filter(c -> c.getNombreCategoria().equals(vehiculo.getNombreCategoria()))
                    .findFirst()
                    .ifPresent(choiceCategoria::setValue);

            spinnerAnio.getValueFactory().setValue(vehiculo.getAnio());
            txtKilometros.setText(String.valueOf(vehiculo.getKilometros()));
            txtColor.setText(vehiculo.getColor());
            txtPlacas.setText(vehiculo.getPlacas());
            txtNumSerie.setText(vehiculo.getNumSerie());
            txtObservaciones.setText(vehiculo.getObservaciones());

            if (vehiculo.getUltimoServicio() != null && !vehiculo.getUltimoServicio().isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                dateUltimoServicio.setValue(LocalDate.parse(vehiculo.getUltimoServicio(), formatter));
            }
        }
    }

    private void actualizarVehiculo() {
        if (vehiculo == null) {
            MensajesAlert.mostrarWarning(
                    "Sin selección",
                    "Vehículo no seleccionado",
                    "Debe seleccionar un vehículo de la lista antes de poder modificarlo."
            );
            return;
        }

        if (txtColor.getText() == null || txtColor.getText().trim().isEmpty()) {
            MensajesAlert.mostrarWarning(
                    "Datos incompletos",
                    "Color del vehículo obligatorio",
                    "El campo de color del vehículo no puede estar vacío."
            );
            return;
        }

        if (choiceMarca.getValue() == null) {
            MensajesAlert.mostrarWarning(
                    "Datos incompletos",
                    "Marca no seleccionada",
                    "Debe seleccionar una marca para el vehículo antes de continuar."
            );
            return;
        }

        if (choiceModelo.getValue() == null) {
            MensajesAlert.mostrarWarning(
                    "Datos incompletos",
                    "Modelo no seleccionado",
                    "Debe seleccionar un modelo para el vehículo antes de continuar."
            );
            return;
        }

        boolean confirmar = MensajesAlert.mostrarConfirmacion(
                "Confirmar actualización",
                "Guardar cambios",
                "¿Está seguro de que desea guardar los cambios en este vehículo? Se actualizarán los datos del registro seleccionado.",
                "Sí, guardar",
                "Cancelar"
        );

        if (dateUltimoServicio.getValue() != null) {
            vehiculo.setUltimoServicio(dateUltimoServicio.getValue().toString()); // yyyy-MM-dd
        }

        if (!confirmar) {
            return; // Usuario canceló
        }

        taskService.runTask(
                loadingOverlayController,
                () -> {

                    String placas = txtPlacas.getText();
                    String numSerie = txtNumSerie.getText();
                    Integer id = vehiculo.getId();

                    if (placas != null && !placas.isBlank()) {
                        if (vehiculoService.existeVehiculoPlacasEditar(placas, id)) {
                            throw new VehiculoException("Ya existe un vehículo regstrado con la misma placa.");
                        }
                    }

                    if (numSerie != null && !numSerie.isBlank()) {
                        if (vehiculoService.existeVehiculoNumeroSerieEditar(numSerie, id)) {
                            throw new VehiculoException("Ya existe un vehículo regstrado con el mismo número de serie.");
                        }
                    }


                    Vehiculo vehiculo = new Vehiculo();
                    vehiculo.setVehiculoId(this.vehiculo.getId());
                    vehiculo.setMarca(choiceMarca.getValue());
                    vehiculo.setModelo(obtenerModeloSeleccionado());
                    //vehiculo.setModelo(choiceModelo.getValue());
                    vehiculo.setCategoria(choiceCategoria.getValue());
                    vehiculo.setAnio(spinnerAnio.getValue());
                    vehiculo.setKilometros(Integer.parseInt(txtKilometros.getText()));
                    vehiculo.setColor(txtColor.getText());
                    vehiculo.setPlacas(txtPlacas.getText());
                    vehiculo.setNumSerie(txtNumSerie.getText());
                    vehiculo.setObservaciones(txtObservaciones.getText());

                    if (dateUltimoServicio.getValue() != null) {
                        vehiculo.setUltimoServicio(dateUltimoServicio.getValue().toString());
                    }


                    //  Guardar en la BD
                    vehiculoService.actualizarVehiculo(this.vehiculo.getId(), vehiculo);

                    return null;

                }, (resultado) -> {

                    MensajesAlert.mostrarInformacion(
                            "Operación completada",
                            "Vehículo actualizado",
                            "Los cambios en la información del vehículo se han guardado correctamente."
                    );
                    cerrarVentana();

                }, (ex) -> {

                    if (ex instanceof VehiculoException) {
                        MensajesAlert.mostrarExcepcionThrowable(
                                "Error al actualizar",
                                "Problema al guardar cambios",
                                "Ocurrió un problema al intentar guardar los cambios del vehículo",
                                ex
                        );
                    } else {
                        MensajesAlert.mostrarExcepcionThrowable(
                                "Error inesperado",
                                "Se produjo una excepción durante la operación",
                                "Ocurrió un error inesperado al intentar guardar los cambios. Por favor, inténtelo de nuevo más tarde.",
                                ex
                        );
                    }

                }, null
        );


    }

    @Override
    public void cleanup() {
        vehiculo = null;

        if (modeloPorCategoriaId != null) {
            modeloPorCategoriaId.clear();
        }

        if (modelos != null) {
            modelos.clear();
        }
    }

    @FXML
    private void cerrarVentana() {
        cleanup();
        Stage stage = (Stage) btnCambiar.getScene().getWindow();
        stage.close();
    }


}
