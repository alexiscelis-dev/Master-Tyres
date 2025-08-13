package com.mastertyres.fxControllers.AgregarVehiculo;


import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.categoria.services.CategoriaService;
import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.cliente.model.StatusCliente;
import com.mastertyres.cliente.model.TipoCliente;
import com.mastertyres.cliente.service.ClienteService;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.marca.services.MarcaService;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.modelo.services.ModeloService;
import com.mastertyres.vehiculo.model.Vehiculo;
import com.mastertyres.vehiculo.repository.VehiculoRepository;
import com.mastertyres.vehiculo.service.VehiculoService;
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

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

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
    @FXML private ListView<Cliente> listaClientes;
    private ObservableList<Vehiculo> listaVehiculos = FXCollections.observableArrayList();

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

        //Listar Clientes
        listaClientes.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Cliente cliente, boolean empty) {
                super.updateItem(cliente, empty);
                if (empty || cliente == null) {
                    setText(null);
                } else {
                    setText(
                            cliente.getNombre() + " " +
                                    cliente.getApellido() + " " +
                                    cliente.getSegundoApellido() +
                                    "    |   " + cliente.getTipoCliente() +
                                    "   |  RFC: " + cliente.getRfc() +
                                    "   |  Tel: " + cliente.getNumTelefono()
                    );
                }
            }
        });

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

        //Cliente Seleccionado
        listaClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
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
        // Deshabilitar botón "Agregar Vehículo" hasta que se llenen los campos requeridos
        btnAgregarVehiculo.disableProperty().bind(
                choiceMarca.valueProperty().isNull()
                        .or(choiceModelo.valueProperty().isNull())
                        .or(txtColor.textProperty().isEmpty())
                        .or(spinnerAnio.valueProperty().isNull())
        );
        // Deshabilitar botón "Guardar" cuando NO haya cliente o la lista de vehículos esté vacía
        btnGuardar.disableProperty().bind(
                Bindings.isEmpty(listaVehiculos)
                        .or(listaClientes.getSelectionModel().selectedItemProperty().isNull())
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


        //LLenar lista
        List<Cliente> clientes = clienteService.listarClientesConVehiculos(StatusCliente.ACTIVE.toString());
        listaClientes.setItems(FXCollections.observableArrayList(clientes));
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
        Cliente clienteSeleccionado = listaClientes.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Cliente no seleccionado", "Por favor selecciona un cliente.");
            return;
        }

        if (listaVehiculos.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin vehículos", "Agrega al menos un vehículo antes de guardar.");
            return;
        }

        try {
            vehiculoService.guardarVehiculos(clienteSeleccionado, listaVehiculos);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Vehículos guardados correctamente.");
            listaVehiculos.clear();
            limpiarCamposVehiculo();
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron guardar los vehículos: " + e.getMessage());
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

}
