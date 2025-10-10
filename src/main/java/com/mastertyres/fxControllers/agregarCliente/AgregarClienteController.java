package com.mastertyres.fxControllers.agregarCliente;

import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.categoria.services.CategoriaService;
import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.cliente.service.ClienteService;
import com.mastertyres.common.MenuContextSetting;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.marca.services.MarcaService;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.modelo.services.ModeloService;
import com.mastertyres.vehiculo.model.Vehiculo;
import com.mastertyres.vehiculo.service.VehiculoService;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javafx.fxml.FXML;

import java.time.LocalDate;
import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mastertyres.common.MensajesAlert.*;

@Component
public class AgregarClienteController {

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

    // Columnas de tabla vehiculos
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

    //Campos Cliente

    @FXML
    private AnchorPane ventanaAgregarCliente;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtSegundoApellido;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtDomicilio;
    @FXML
    private TextField txtCiudad;
    @FXML
    private TextField txtEstado;
    @FXML
    private TextField txtHobbie;
    @FXML
    private TextField txtRFC;
    @FXML
    private DatePicker pickerCumpleanos;
    @FXML
    private ChoiceBox<String> choiceTipoCliente;
    @FXML
    private ChoiceBox<String> choiceGenero;

    // Campos Vehiculo
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
    private ObservableList<Vehiculo> listaVehiculos = FXCollections.observableArrayList();

    // Botones Principales
    @FXML
    private Button btnAgregarVehiculo;
    @FXML
    private Button btnGuardar;

    //Validaciones:
    private BooleanProperty rfcValido = new SimpleBooleanProperty(true);
    private BooleanProperty serieValido = new SimpleBooleanProperty(true);
    private BooleanProperty placasValido = new SimpleBooleanProperty(true);
    private BooleanProperty telefonoValido = new SimpleBooleanProperty(true);
    private BooleanProperty kilometrosValido = new SimpleBooleanProperty(true);
    private BooleanProperty nombreValido = new SimpleBooleanProperty(true);
    private BooleanProperty apellidoValido = new SimpleBooleanProperty(true);
    private BooleanProperty ColorValido = new SimpleBooleanProperty(true);


    @FXML
    private void initialize() {
        MenuContextSetting.disableMenu(ventanaAgregarCliente);

        configurarValidaciones();

        cargarOpciones();
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
                ImageView iv = new ImageView(img);
                iv.setFitWidth(18);   // tamaño icono
                iv.setFitHeight(18);
                btn.setGraphic(iv);

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

// APELLIDO
        txtApellido.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.isBlank()) {
                apellidoValido.set(false);
                txtApellido.setStyle("-fx-border-color: red;");
            } else {
                apellidoValido.set(true);
                txtApellido.setStyle("");
            }
        });
        txtApellido.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                txtApellido.setText(formatoOracion(txtApellido.getText()));
            }
        });

// SEGUNDO APELLIDO (no obligatorio, no borde rojo)
        txtSegundoApellido.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                txtSegundoApellido.setText(formatoOracion(txtSegundoApellido.getText()));
            }
        });

        //Ciudad
        txtCiudad.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                txtCiudad.setText(formatoOracion(txtCiudad.getText()));
            }
        });

        //Estado
        txtEstado.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                txtEstado.setText(formatoOracion(txtEstado.getText()));
            }
        });

        //Domicilio
        txtDomicilio.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                txtDomicilio.setText(formatoOracion(txtDomicilio.getText()));
            }
        });

        // Teléfono
        txtTelefono.textProperty().addListener((obs, oldText, newText) -> {
            if (newText != null && !newText.isBlank() && newText.matches("^\\+?[\\d\\s\\-()]{7,20}$")) {
                // Válido: no vacío y cumple formato
                telefonoValido.set(true);
                txtTelefono.setStyle("");
            } else {
                telefonoValido.set(false);
                txtTelefono.setStyle("-fx-border-color: red;");
            }
        });
        txtTelefono.setTextFormatter(new TextFormatter<>(c -> {
            if (c.getControlNewText().matches("[\\d\\s\\-()+]*")) {
                return c;
            }
            return null;
        }));

        // RFC: opcional, acepta minúsculas
        txtRFC.textProperty().addListener((obs, oldText, newText) -> {
            String texto = newText.toUpperCase(); // convertir a mayúsculas para la validación
            txtRFC.setText(texto); // actualiza el campo visualmente en mayúsculas
            if (texto.isEmpty()) {
                rfcValido.set(true);
                txtRFC.setStyle("");
            } else if (!texto.matches("^([A-Z,Ñ,&]{3,4}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[A-Z\\d]{3})$")) {
                rfcValido.set(false);
                txtRFC.setStyle("-fx-border-color: red;");
            } else {
                rfcValido.set(true);
                txtRFC.setStyle("");
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

        // Deshabilitar botón "Guardar" hasta que se llenen los campos requeridos y haya al menos un vehículo
        btnGuardar.disableProperty().bind(
                Bindings.isEmpty(listaVehiculos)
                        .or(txtNombre.textProperty().isEmpty())
                        .or(txtApellido.textProperty().isEmpty())
                        .or(txtTelefono.textProperty().isEmpty())
                        .or(choiceTipoCliente.valueProperty().isNull())
                        .or(rfcValido.not())
                        .or(telefonoValido.not())

        );

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

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
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
    }

    @FXML
    private void agregarVehiculo(ActionEvent event) {

        if (!serieValido.get() || !kilometrosValido.get() || !placasValido.get()) {
            mostrarAlerta("Error en campos", "Por favor, corrige los campos marcados en rojo antes de agregar vehiculo");
            return;
        }
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

    private void LimpiarCamposClientes() {
        txtNombre.clear();
        txtApellido.clear();
        txtSegundoApellido.clear();
        txtDomicilio.clear();
        txtEstado.clear();
        txtCiudad.clear();
        txtTelefono.clear();
        txtHobbie.clear();
        txtRFC.clear();
        pickerCumpleanos.setValue(null);
        choiceTipoCliente.setValue(null);
    }

    @FXML
    private void GuardarCliente(ActionEvent event) {

        if (!rfcValido.get() || !telefonoValido.get()) {
            mostrarError("Error en campos", "", "Por favor, corrige los campos marcados en rojo antes de guardar.");
            return;
        }

        // Validar RFC duplicado
        String rfc = txtRFC.getText();
        if (rfc != null && !rfc.isBlank()) {
            if (clienteService.existeClientePorRFC(rfc)) {
                mostrarWarning("RFC duplicado", "", "Ya existe un cliente activo con este RFC.");
                return;
            }
        }

        // Validar vehículos duplicados contra la base
        for (Vehiculo v : listaVehiculos) {
            String placas = v.getPlacas();
            String numSerie = v.getNumSerie();

            if (placas != null && !placas.isBlank()) {
                if (vehiculoService.existeVehiculoPorPlacas(placas)) {
                    mostrarWarning("Vehículo duplicado", "",
                            "Ya existe un vehículo activo con las mismas placas.");
                    return;
                }
            }

            if (numSerie != null && !numSerie.isBlank()) {
                if (vehiculoService.existeVehiculoPorNumeroSerie(numSerie)) {
                    mostrarWarning("Vehículo duplicado", "",
                            "Ya existe un vehículo activo con el mismo numero de serie.");
                    return;
                }
            }
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


        // Crear Cliente
        Cliente cliente = new Cliente();
        cliente.setUpdated_at(LocalDate.now().toString());  // <<< AGREGADO
        cliente.setNombre(txtNombre.getText());
        cliente.setApellido(txtApellido.getText());
        cliente.setSegundoApellido(txtSegundoApellido.getText());
        cliente.setHobbie(txtHobbie.getText());
        cliente.setRfc(txtRFC.getText());
        cliente.setNumTelefono(txtTelefono.getText());
        cliente.setEstado(txtEstado.getText());
        cliente.setCiudad(txtCiudad.getText());
        cliente.setDomicilio(txtDomicilio.getText());
        cliente.setTipoCliente(choiceTipoCliente.getValue() != null ? choiceTipoCliente.getValue().toString() : null);
        if (pickerCumpleanos.getValue() != null) {
            cliente.setFechaCumple(pickerCumpleanos.getValue().toString());
        }
        cliente.setActive("ACTIVE");
        cliente.setCreated_at(LocalDate.now().toString());

        if (choiceGenero.getValue() != null) {
            switch (choiceGenero.getValue()) {
                case "Masculino" -> cliente.setGenero("M");
                case "Femenino" -> cliente.setGenero("F");
                case "Otro" -> cliente.setGenero("O");
            }
        }

        // Asignar los vehículos
        for (Vehiculo v : listaVehiculos) {
            v.setContador_mensaje(0);
            v.setCliente(cliente); // relación bidireccional
            if (v.getActive() == null) {
                v.setActive("ACTIVE");
            }
            if (v.getCreated_at() == null) {
                v.setCreated_at(LocalDate.now().toString());  // Asignar fecha creación aquí
            }
            if (v.getUpdated_at() == null) {
                v.setUpdated_at(LocalDate.now().toString());
            }
            if (v.getFechaRegistro() == null) {
                v.setFechaRegistro(LocalDate.now().toString());
            }
        }
        cliente.setVehiculos(listaVehiculos);

        try {
            // Guardar usando el servicio
            clienteService.guardarCliente(cliente);

            // Limpiar formulario
            limpiarCamposVehiculo();
            LimpiarCamposClientes();
            listaVehiculos.clear();

            mostrarInformacion("Cliente guardado con éxito", "", "El cliente guardado con éxito");
        } catch (Exception e) {
            mostrarError("Error", "Cliente NO guardado", "El cliente NO pudo ser guardado");
        }

    }

}
