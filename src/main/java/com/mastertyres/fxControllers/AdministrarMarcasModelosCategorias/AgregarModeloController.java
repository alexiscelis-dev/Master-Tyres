package com.mastertyres.fxControllers.AdministrarMarcasModelosCategorias;

import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.categoria.services.CategoriaService;
import com.mastertyres.common.MensajesAlert;
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

import java.beans.Transient;
import java.util.List;

import static com.mastertyres.common.MensajesAlert.mostrarError;

@Component
public class AgregarModeloController {

    @Autowired
    private MarcaService marcaService;
    @Autowired private ModeloService modeloService;
    @Autowired private CategoriaService categoriaService;
    @Autowired private VehiculoService vehiculoService;
    @Autowired private DetalleCategoriaService detalleCategoriaService;
    @Autowired private VehiculoRepository vehiculoRepository;

    @FXML
    private TableView<DetalleCategoria> tablaVehiculos;
    @FXML
    private TableColumn<DetalleCategoria, String> colModelo;
    @FXML
    private TableColumn<DetalleCategoria, String> colCategoria;
    @FXML
    private TableColumn<DetalleCategoria, Void> colEliminar;

    @FXML private ChoiceBox<Categoria> choiceCategoria;
    @FXML private TextField txtModelo;

    @FXML private Button btnAgregarVehiculo;
    @FXML private Button btnGuardar;

    private ObservableList<DetalleCategoria> listaVehiculos = FXCollections.observableArrayList();
    private List<DetalleCategoria> listaExistentes = FXCollections.observableArrayList();
    private ObservableList<DetalleCategoria> listaNuevos = FXCollections.observableArrayList();

    private Marca marcaSeleccionada;
    //private DetalleCategoria detalleCategoriaSeleccionada;
    private BooleanProperty ModeloValido = new SimpleBooleanProperty(true);

    @FXML
    private void initialize(){

        cargarOpciones();
        configurarValidaciones();
    }

    public void setMarcaModelos(Marca m) {
        //this.detalleCategoriaSeleccionada = m;
        this.marcaSeleccionada = m;
        cargarVehiculos();
    }

//    private void cargarVehiculos() {
//        listaVehiculos.clear();
//        listaVehiculos.addAll(detalleCategoriaService.getByMarca(marcaSeleccionada.getMarcaId()));
//        tablaVehiculos.setItems(listaVehiculos);
//
//        colModelo.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getModelo().getNombreModelo()));
//        colCategoria.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCategoria().getNombreCategoria()));
//
//        // Botón eliminar
//        colEliminar.setCellFactory(col -> new TableCell<>() {
//            private final Button btn = new Button();
//            {
//                Image img = new Image(getClass().getResourceAsStream("/icons/delete.png"));
//                ImageView iv = new ImageView(img);
//                iv.setFitWidth(18);
//                iv.setFitHeight(18);
//                btn.setGraphic(iv);
//                btn.setStyle("-fx-background-color: red;");
//                btn.setOnAction(e -> {
//                    DetalleCategoria d = getTableView().getItems().get(getIndex());
//                    listaVehiculos.remove(d);
//                });
//            }
//            @Override
//            protected void updateItem(Void item, boolean empty) {
//                super.updateItem(item, empty);
//                setGraphic(empty ? null : btn);
//            }
//        });
//
//    }
    private void cargarVehiculos() {
        listaVehiculos.clear();
        listaExistentes.clear();
        listaNuevos.clear();

        List<DetalleCategoria> existentes = detalleCategoriaService.getByMarca(marcaSeleccionada.getMarcaId());
        listaExistentes.addAll(existentes);
        listaVehiculos.addAll(existentes);

        tablaVehiculos.setItems(listaVehiculos);

        colModelo.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getModelo().getNombreModelo()));
        colCategoria.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCategoria().getNombreCategoria()));

        // 🔹 Botón eliminar solo para nuevos
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
                    listaNuevos.remove(d);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    DetalleCategoria detalle = getTableView().getItems().get(getIndex());
                    boolean esExistente = listaExistentes.stream().anyMatch(
                            ex -> ex.getModelo().getNombreModelo().equalsIgnoreCase(detalle.getModelo().getNombreModelo())
                                    && ex.getCategoria().getCategoriaId().equals(detalle.getCategoria().getCategoriaId())
                    );
                    setGraphic(esExistente ? null : btn);
                }
            }
        });
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
            } else if (!texto.matches("^[A-Z0-9\\s\\p{Punct}]{1,20}$")) {
                ModeloValido.set(false);
                txtModelo.setStyle("-fx-border-color: red;");
            } else {
                ModeloValido.set(true);
                txtModelo.setStyle("");
            }
        });

        // Deshabilitar botón "Agregar Vehículo" hasta que se llenen los campos requeridos
        btnAgregarVehiculo.disableProperty().bind(
                choiceCategoria.valueProperty().isNull()
                        .or(txtModelo.textProperty().isEmpty())
                        .or(ModeloValido.not())
        );

        //Deshabilitar botón "Guardar" cuando NO haya cliente o la lista de vehículos esté vacía
        btnGuardar.disableProperty().bind(
                Bindings.isEmpty(listaVehiculos)
        );
    }

//    @FXML
//    private void agregarVehiculo() {
//        String modeloNombre = txtModelo.getText();
//        Categoria categoria = choiceCategoria.getValue();
//
//        if (modeloNombre.isEmpty() || categoria == null) {
//            mostrarError("Error", "Campos Vacíos", "Debes ingresar un modelo y seleccionar una categoría.");
//            return;
//        }
//
//        // 🔹 Validación de duplicados
//        boolean existe = listaVehiculos.stream().anyMatch(
//                v -> v.getModelo().getNombreModelo().equalsIgnoreCase(modeloNombre) &&
//                        v.getCategoria().getCategoriaId().equals(categoria.getCategoriaId())
//        );
//
//        if (existe) {
//            mostrarError("Duplicado", "Modelo y Categoría repetidos",
//                    "Ya existe este modelo con la misma categoría en la tabla.");
//            return;
//        }
//
//        Modelo modelo = new Modelo();
//        modelo.setNombreModelo(modeloNombre);
//
//        DetalleCategoria detalle = new DetalleCategoria();
//        detalle.setMarca(marcaSeleccionada);
//        detalle.setModelo(modelo);
//        detalle.setCategoria(categoria);
//
//        listaVehiculos.add(detalle);
//
//        txtModelo.clear();
//        choiceCategoria.setValue(null);
//    }
    @FXML
    private void agregarVehiculo() {
        String modeloNombre = txtModelo.getText();
        Categoria categoria = choiceCategoria.getValue();

        if (modeloNombre.isEmpty() || categoria == null) {
            mostrarError("Error", "Campos Vacíos", "Debes ingresar un modelo y seleccionar una categoría.");
            return;
        }

        // 🔹 Verificar duplicado entre existentes y nuevos
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
        modelo.setMarca_id(marcaSeleccionada);

        DetalleCategoria detalle = new DetalleCategoria();
        detalle.setMarca(marcaSeleccionada);
        detalle.setModelo(modelo);
        detalle.setCategoria(categoria);

        listaVehiculos.add(detalle);
        listaNuevos.add(detalle);

        txtModelo.clear();
        choiceCategoria.setValue(null);
    }

    @FXML
    private void GuardarCambios() {

        boolean confirmar = MensajesAlert.mostrarConfirmacion(
                "Confirmar Agregar",
                "¿Desea agregar este Modelo?",
                "Se agregara los modelo de la tabla a esta marca.",
                "Sí, agregar",
                "Cancelar"
        );

        if (confirmar) {
            try {
                if (listaNuevos.isEmpty()) {
                    cerrarVentana();
                    return;
                }

                for (DetalleCategoria detalle : listaNuevos) {
                    // 🔹 Guardar modelo
                    Modelo nuevoModelo = modeloService.guardarModelo(detalle.getModelo());

                    // 🔹 Crear relación detalle_categoria
                    DetalleCategoria nuevoDetalle = new DetalleCategoria();
                    nuevoDetalle.setMarca(marcaSeleccionada);
                    nuevoDetalle.setModelo(nuevoModelo);
                    nuevoDetalle.setCategoria(detalle.getCategoria());
                    detalleCategoriaService.guardarDetalleCategoria(nuevoDetalle);
                }


                MensajesAlert.mostrarInformacion("Éxito", "Modelo agregado", "El modelo se agrego correctamente.");

                cerrarVentana();


            } catch (Exception e) {
                MensajesAlert.mostrarError(
                        "Error al agregar",
                        "No se pudo agregar el modelo",
                        "Detalles: " + e.getMessage()
                );
                e.printStackTrace();
            }
        }


    }

    private void limpiarCamposVehiculo() {

        choiceCategoria.setValue(null);
        txtModelo.clear();
        listaVehiculos.clear();

    }

    @FXML
    private void cerrarVentana() {
        limpiarCamposVehiculo();
        Stage stage = (Stage) txtModelo.getScene().getWindow();
        stage.close();
    }

}
