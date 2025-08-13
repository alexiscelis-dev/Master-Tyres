package com.mastertyres.fxControllers.AgregarCliente;

import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.categoria.services.CategoriaService;
import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.cliente.model.TipoCliente;
import com.mastertyres.cliente.service.ClienteService;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.marca.services.MarcaService;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.modelo.services.ModeloService;
import com.mastertyres.vehiculo.model.Vehiculo;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javafx.fxml.FXML;
import com.mastertyres.cliente.model.TipoCliente;
import javafx.util.StringConverter;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;

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

    // Columnas de tabla vehiculos
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

    //Campos Cliente
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtSegundoApellido;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDomicilio;
    @FXML private TextField txtCiudad;
    @FXML private TextField txtEstado;
    @FXML private TextField txtCURP;
    @FXML private TextField txtRFC;
    @FXML private DatePicker pickerCumpleanos;
    @FXML private ChoiceBox<String> choiceTipoCliente;

    // Campos Vehiculo
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
    private ObservableList<Vehiculo> listaVehiculos = FXCollections.observableArrayList();

    // Botones Principales
    @FXML private Button btnAgregarVehiculo;
    @FXML private Button btnGuardar;


    @FXML
    private void initialize() {

        configurarValidaciones();

        cargarOpciones();
        int currentYear = Year.now().getValue();
        SpinnerValueFactory<Integer> yearFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1950, currentYear , currentYear);
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
            private final Button btn = new Button("Eliminar");

            {
                btn.setOnAction(e -> {
                    Vehiculo v = getTableView().getItems().get(getIndex());
                    listaVehiculos.remove(v);
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
        // Deshabilitar botón "Agregar Vehículo" hasta que se llenen los campos requeridos
        btnAgregarVehiculo.disableProperty().bind(
                choiceMarca.valueProperty().isNull()
                        .or(choiceModelo.valueProperty().isNull())
                        .or(txtColor.textProperty().isEmpty())
                        .or(spinnerAnio.valueProperty().isNull())
        );

        // Deshabilitar botón "Guardar" hasta que se llenen los campos requeridos y haya al menos un vehículo
        btnGuardar.disableProperty().bind(
                Bindings.isEmpty(listaVehiculos)
                        .or(txtNombre.textProperty().isEmpty())
                        .or(txtApellido.textProperty().isEmpty())
                        .or(txtTelefono.textProperty().isEmpty())
                        .or(choiceTipoCliente.valueProperty().isNull())
        );

        txtTelefono.setTextFormatter(new TextFormatter<>(c -> {
            if (c.getControlNewText().matches("\\d{0,13}")) {
                return c;
            }
            return null;
        }));

        txtKilometros.setTextFormatter(new TextFormatter<>(c -> {
            if (c.getControlNewText().matches("\\d*")) {
                return c;
            }
            return null;
        }));

        // Validación RFC (4 letras, 6 números de fecha, 3 caracteres alfanuméricos)
        txtRFC.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // cuando pierde el foco
                if (!txtRFC.getText().matches("^[A-ZÑ&]{3,4}\\d{6}[A-Z0-9]{3}$")) {
                    mostrarAlerta("RFC inválido", "El RFC debe tener el formato correcto.");
                    txtRFC.clear();
                }
            }
        });

        // Número de serie (VIN - 17 caracteres alfanuméricos, sin O/I/Q)
        txtSerie.setTextFormatter(new TextFormatter<>(c -> {
            if (c.getControlNewText().matches("[A-HJ-NPR-Z0-9]{0,17}")) {
                return c;
            }
            return null;
        }));

        // Placas (ejemplo: 3 letras + 4 números o similar)
        txtPlacas.setTextFormatter(new TextFormatter<>(c -> {
            if (c.getControlNewText().matches("[A-Z0-9]{0,8}")) { // hasta 8 caracteres
                c.setText(c.getText().toUpperCase()); // forzar mayúsculas
                return c;
            }
            return null;
        }));

    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void cargarOpciones() {
        List<Marca> marcas = marcaService.listarMarcas(); // no nombres
        List<Modelo> modelos = modeloService.listarModelos();
        List<Categoria> categorias = categoriaService.listarCategorias();

        choiceMarca.setItems(FXCollections.observableArrayList(marcas));
        choiceModelo.setItems(FXCollections.observableArrayList(modelos));
        choiceCategoria.setItems(FXCollections.observableArrayList(categorias));

        // Muestra los nombres en el ChoiceBox (para Marca, Modelo y Categoría)
        choiceMarca.setConverter(new StringConverter<>() {
            @Override
            public String toString(Marca marca) {
                return marca != null ? marca.getNombreMarca() : "";
            }

            @Override
            public Marca fromString(String string) {
                return null; // no necesario si no permites edición
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

    private void LimpiarCamposClientes(){
        txtNombre.clear();
        txtApellido.clear();
        txtSegundoApellido.clear();
        txtDomicilio.clear();
        txtEstado.clear();
        txtCiudad.clear();
        txtTelefono.clear();
        txtCURP.clear();
        txtRFC.clear();
        pickerCumpleanos.setValue(null);
        choiceTipoCliente.setValue(null);
    }

    @FXML
    private void GuardarCliente(ActionEvent event) {
        // Crear Cliente
        Cliente cliente = new Cliente();
        cliente.setUpdated_at(LocalDate.now().toString());  // <<< AGREGADO
        cliente.setNombre(txtNombre.getText());
        cliente.setApellido(txtApellido.getText());
        cliente.setSegundoApellido(txtSegundoApellido.getText());
        cliente.setCurp(txtCURP.getText());
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

        // Asignar los vehículos
        for (Vehiculo v : listaVehiculos) {
            v.setCliente(cliente); // relación bidireccional
            if (v.getActive() == null) {
                v.setActive("ACTIVE");
            }
            if (v.getCreated_at() == null) {
                v.setCreated_at(LocalDate.now().toString());  // Asignar fecha creación aquí
            }
            if (v.getUpdated_at() == null){
                v.setUpdated_at(LocalDate.now().toString());
            }
            if (v.getFechaRegistro() == null){
                v.setFechaRegistro(LocalDate.now().toString());
            }
        }
        cliente.setVehiculos(listaVehiculos);

        // Guardar usando el servicio
        clienteService.guardarCliente(cliente);

        // Limpiar formulario
        limpiarCamposVehiculo();
        LimpiarCamposClientes();
        listaVehiculos.clear();

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cliente guardado con éxito", ButtonType.OK);
        alert.showAndWait();
    }

}
