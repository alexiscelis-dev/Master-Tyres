package com.mastertyres.fxControllers.EditarMarcasModelosCategorias;

import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.categoria.services.CategoriaService;
import com.mastertyres.detalleCategoria.model.DetalleCategoria;
import com.mastertyres.detalleCategoria.service.DetalleCategoriaService;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.marca.services.MarcaService;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.modelo.services.ModeloService;
import com.mastertyres.vehiculo.repository.VehiculoRepository;
import com.mastertyres.vehiculo.service.VehiculoService;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.mastertyres.common.MensajesAlert.mostrarError;

@Component
public class EditarMarcaController {

    @FXML private  Button btnAgregarVehiculo;
    @FXML private  Button btnAgregar;
    @FXML private TextField txtMarca, txtModelo;
    @FXML private ChoiceBox<Categoria> choiceCategoria;
    @FXML private TableView<DetalleCategoria> tablaVehiculos;
    @FXML private TableColumn<DetalleCategoria, String> colModelo;
    @FXML private TableColumn<DetalleCategoria, String> colCategoria;
    @FXML private TableColumn<DetalleCategoria, Void> colEliminar;

    private Marca marcaSeleccionada;

    @Autowired private MarcaService marcaService;
    @Autowired private ModeloService modeloService;
    @Autowired private CategoriaService categoriaService;
    @Autowired private VehiculoService vehiculoService;
    @Autowired private DetalleCategoriaService detalleCategoriaService;
    @Autowired private VehiculoRepository vehiculoRepository;

    private BooleanProperty ModeloValido = new SimpleBooleanProperty(true);
    private BooleanProperty MarcaValido = new SimpleBooleanProperty(true);

    private ObservableList<DetalleCategoria> listaVehiculos = FXCollections.observableArrayList();

    @FXML
    private void initialize(){

        cargarOpciones();
        configurarValidaciones();
    }

    private void cargarOpciones() {

        List<Categoria> categorias = categoriaService.listarCategorias();
        choiceCategoria.setItems(FXCollections.observableArrayList(categorias));
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

    private void configurarValidaciones() {

        txtModelo.textProperty().addListener((obs, oldText, newText) -> {
            String texto = newText.toUpperCase();
            txtModelo.setText(texto);
            if (texto.isEmpty()) {
                ModeloValido.set(false);
                txtModelo.setStyle("-fx-border-color: red;");
            } else if (!texto.matches("^[A-Z]{1,20}")) {
                ModeloValido.set(false);
                txtModelo.setStyle("-fx-border-color: red;");
            } else {
                ModeloValido.set(true);
                txtModelo.setStyle("");
            }
        });

        txtMarca.textProperty().addListener((obs, oldText, newText) -> {
            String texto = newText.toUpperCase();
            txtMarca.setText(texto);
            if (texto.isEmpty()) {
                MarcaValido.set(false);
                txtMarca.setStyle("-fx-border-color: red;");
            } else if (!texto.matches("^[A-Z]{1,20}")) {
                MarcaValido.set(false);
                txtMarca.setStyle("-fx-border-color: red;");
            } else {
                MarcaValido.set(true);
                txtMarca.setStyle("");
            }
        });

        // Deshabilitar botón "Agregar Vehículo" hasta que se llenen los campos requeridos
        btnAgregarVehiculo.disableProperty().bind(
                choiceCategoria.valueProperty().isNull()
                        .or(txtModelo.textProperty().isEmpty())
        );

        //Deshabilitar botón "Guardar" cuando NO haya cliente o la lista de vehículos esté vacía
        btnAgregar.disableProperty().bind(
                Bindings.isEmpty(listaVehiculos)
                        .or(txtMarca.textProperty().isEmpty())
        );
    }


    public void setMarcaModelos(Marca m) {
        this.marcaSeleccionada = m;
        txtMarca.setText(m.getNombreMarca());
        cargarVehiculos();
    }

    private void cargarVehiculos() {
        listaVehiculos.clear();
        listaVehiculos.addAll(detalleCategoriaService.getByMarca(marcaSeleccionada.getMarcaId()));
        tablaVehiculos.setItems(listaVehiculos);

        colModelo.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getModelo().getNombreModelo()));
        colCategoria.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCategoria().getNombreCategoria()));

        // Botón eliminar
        colEliminar.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button();
            {
                Image img = new Image(getClass().getResourceAsStream("/icons/delete.png"));
                ImageView iv = new ImageView(img);
                iv.setFitWidth(18);
                iv.setFitHeight(18);
                btn.setGraphic(iv);
                btn.setStyle("-fx-background-color: red;");
                btn.setOnAction(e -> {
                    DetalleCategoria d = getTableView().getItems().get(getIndex());
                    listaVehiculos.remove(d);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

    }

    @FXML
    private void agregarVehiculo() {
        String modeloNombre = txtModelo.getText();
        Categoria categoria = choiceCategoria.getValue();

        if (modeloNombre.isEmpty() || categoria == null) {
            mostrarError("Error", "Campos Vacíos", "Debes ingresar un modelo y seleccionar una categoría.");
            return;
        }

        // 🔹 Validación de duplicados
        boolean existe = listaVehiculos.stream().anyMatch(
                v -> v.getModelo().getNombreModelo().equalsIgnoreCase(modeloNombre) &&
                        v.getCategoria().getCategoriaId().equals(categoria.getCategoriaId())
        );

        if (existe) {
            mostrarError("Duplicado", "Modelo y Categoría repetidos",
                    "Ya existe este modelo con la misma categoría en la tabla.");
            return;
        }

        Modelo modelo = new Modelo();
        modelo.setNombreModelo(modeloNombre);

        DetalleCategoria detalle = new DetalleCategoria();
        detalle.setMarca(marcaSeleccionada);
        detalle.setModelo(modelo);
        detalle.setCategoria(categoria);

        listaVehiculos.add(detalle);

        txtModelo.clear();
        choiceCategoria.setValue(null);
    }

    @FXML
    private void GuardarCambios() {

        cerrarVentana();
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) txtMarca.getScene().getWindow();
        stage.close();
    }
}

