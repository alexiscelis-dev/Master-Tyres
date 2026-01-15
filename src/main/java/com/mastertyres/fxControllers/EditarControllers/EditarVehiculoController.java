package com.mastertyres.fxControllers.EditarControllers;

import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.categoria.service.CategoriaService;
import com.mastertyres.common.utils.MensajesAlert;
import com.mastertyres.detalleCategoria.service.DetalleCategoriaService;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.marca.service.MarcaService;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.modelo.services.ModeloService;
import com.mastertyres.vehiculo.model.Vehiculo;
import com.mastertyres.vehiculo.model.VehiculoDTO;
import com.mastertyres.vehiculo.service.VehiculoService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class EditarVehiculoController {

    @FXML private ChoiceBox<Marca> choiceMarca;
    @FXML private ChoiceBox<Modelo> choiceModelo;
    @FXML private ChoiceBox<Categoria> choiceCategoria;
    @FXML private Spinner<Integer> spinnerAnio;
    @FXML private TextField txtKilometros;
    @FXML private TextField txtColor;
    @FXML private TextField txtPlacas;
    @FXML private TextField txtNumSerie;
    @FXML private TextField txtObservaciones;
    @FXML private DatePicker dateUltimoServicio;
    @FXML private Button btnCambiar;

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

    private List<Modelo> modelos;

    private BooleanProperty serieValido = new SimpleBooleanProperty(true);
    private BooleanProperty placasValido = new SimpleBooleanProperty(true);
    private BooleanProperty kilometrosValido = new SimpleBooleanProperty(true);
    private BooleanProperty ColorValido = new SimpleBooleanProperty(true);



    @FXML
    public void initialize() {
        // Configurar spinner para año
        int anioActual = LocalDate.now().getYear();
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, anioActual, anioActual);
        spinnerAnio.setValueFactory(valueFactory);

        // Cargar marcas, modelos y categorías
        cargarOpciones();

        // Cargar Validaciones
        configurarValidaciones();

        // Acción de cambiar
        btnCambiar.setOnAction(event -> actualizarVehiculo());
    }

    private void configurarValidaciones() {

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
                List<Modelo> modelosFiltrados = modelos.stream()
                        .filter(m -> m.getMarca_id().getMarcaId().equals(newMarca.getMarcaId()))
                        .toList();
                choiceModelo.setItems(FXCollections.observableArrayList(modelosFiltrados));
            } else {
                choiceModelo.getItems().clear();
            }
        });

        choiceModelo.getSelectionModel().selectedItemProperty().addListener((obs, oldModelo, newModelo) -> {
            Marca marcaSeleccionada = choiceMarca.getValue();

            if (marcaSeleccionada != null && newModelo != null) {

                List<Categoria> categoriasFiltradas =
                        detalleCategoriaService.listarCategoriasPorMarcaYModelo(
                                marcaSeleccionada.getMarcaId(),
                                newModelo.getModeloId()
                        );

                choiceCategoria.setItems(FXCollections.observableArrayList(categoriasFiltradas));

            } else {
                choiceCategoria.getItems().clear();
            }
        });
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
            MensajesAlert.mostrarError(
                    "Error",
                    "No hay vehiculo seleccionado",
                    "Debe seleccionar un vehiculo antes de poder modificarlo."
            );
            return;
        }

        if (txtColor.getText() == null || txtColor.getText().trim().isEmpty()) {
            MensajesAlert.mostrarWarning("Validación", "Campo requerido", "El color del vehiculo no puede estar vacío.");
            return;
        }

        if (choiceMarca.getValue() == null) {
            MensajesAlert.mostrarWarning("Validación", "Campo requerido", "Debe seleccionar una marca.");
            return;
        }

        if (choiceModelo.getValue() == null) {
            MensajesAlert.mostrarWarning("Validación", "Campo requerido", "Debe seleccionar un modelo.");
            return;
        }

        boolean confirmar = MensajesAlert.mostrarConfirmacion(
                "Confirmar actualización",
                "¿Desea guardar los cambios en este vehiculo?",
                "Se actualizarán los datos del vehiculo seleccionado.",
                "Sí, guardar",
                "Cancelar"
        );

        if (dateUltimoServicio.getValue() != null) {
            vehiculo.setUltimoServicio(dateUltimoServicio.getValue().toString()); // yyyy-MM-dd
        }

        if (!confirmar) {
            return; // Usuario canceló
        }

        String placas = txtPlacas.getText();
        String numSerie = txtNumSerie.getText();
        Integer id = vehiculo.getId();

        if (placas != null && !placas.isBlank()){
            if (vehiculoService.existeVehiculoPlacas_Editar(placas, id)){
                MensajesAlert.mostrarError( "Error al actualizar","Vehículo duplicado",
                        "Ya existe un vehículo activo con las mismas placas.");
                return;
            }
        }

        if (numSerie != null && !numSerie.isBlank()){
            if (vehiculoService.existeVehiculoNumeroSerie_Editar(numSerie, id)){
                MensajesAlert.mostrarError( "Error al actualizar","Vehículo duplicado",
                        "Ya existe un vehículo activo con el mismo numero de serie.");
                return;
            }
        }

        try {
            Vehiculo v = new Vehiculo();
            v.setVehiculoId(vehiculo.getId());
            v.setMarca(choiceMarca.getValue());
            v.setModelo(choiceModelo.getValue());
            v.setCategoria(choiceCategoria.getValue());
            v.setAnio(spinnerAnio.getValue());
            v.setKilometros(Integer.parseInt(txtKilometros.getText()));
            v.setColor(txtColor.getText());
            v.setPlacas(txtPlacas.getText());
            v.setNumSerie(txtNumSerie.getText());
            v.setObservaciones(txtObservaciones.getText());
            if (dateUltimoServicio.getValue() != null) {
                v.setUltimoServicio(dateUltimoServicio.getValue().toString());
            }


            // 🔹 Guardar en la BD
            vehiculoService.actualizarVehiculo(vehiculo.getId(), v);

            MensajesAlert.mostrarInformacion("Éxito", "Vehículo actualizado", "El vehículo se actualizó correctamente.");
            cerrarVentana();

        } catch (Exception e) {
            MensajesAlert.mostrarError("Error al actualizar", "No se pudo actualizar el vehículo", "Detalles: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) btnCambiar.getScene().getWindow();
        stage.close();
    }
}
