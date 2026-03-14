package com.mastertyres.controllers.fxControllers.AdministrarMarcasModelosCategorias;

import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.categoria.service.CategoriaService;
import com.mastertyres.common.exeptions.ModeloException;
import com.mastertyres.common.interfaces.ICleanable;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.interfaces.ILoader;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.MenuContextSetting;
import com.mastertyres.components.fxComponents.loader.LoadingComponentController;
import com.mastertyres.detalleCategoria.model.DetalleCategoria;
import com.mastertyres.detalleCategoria.service.DetalleCategoriaService;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.marca.service.MarcaService;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.modelo.service.ModeloService;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.mastertyres.common.utils.MensajesAlert.*;


@Component
public class AgregarModeloController implements IFxController, ILoader, ICleanable {

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
    private VehiculoRepository vehiculoRepository;
    @Autowired
    private TaskService taskService;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<DetalleCategoria> tablaVehiculos;
    @FXML
    private TableColumn<DetalleCategoria, String> colModelo;
    @FXML
    private TableColumn<DetalleCategoria, String> colCategoria;
    @FXML
    private TableColumn<DetalleCategoria, Void> colEliminar;

    @FXML
    private ChoiceBox<Categoria> choiceCategoria;
    @FXML
    private TextField txtModelo;

    @FXML
    private Button btnAgregarVehiculo;
    @FXML
    private Button btnGuardar;

    private ObservableList<DetalleCategoria> listaVehiculos = FXCollections.observableArrayList();
    private List<DetalleCategoria> listaExistentes = FXCollections.observableArrayList();
    private ObservableList<DetalleCategoria> listaNuevos = FXCollections.observableArrayList();

    private Marca marcaSeleccionada;
    //private DetalleCategoria detalleCategoriaSeleccionada;
    private BooleanProperty ModeloValido = new SimpleBooleanProperty(true);

    private LoadingComponentController loadingOverlayController;

    @FXML
    private void initialize() {

        cargarOpciones();
        configuraciones();
        listeners();

    }//initialize

    @Override
    public void configuraciones() {
        MenuContextSetting.disableMenu(rootPane);
    }

    @Override
    public void listeners() {
        configurarValidaciones();
    }


    public void setMarcaModelos(Marca m) {
        //this.detalleCategoriaSeleccionada = m;
        this.marcaSeleccionada = m;
        cargarVehiculos();
    }

    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingOverlayController = loading;
    }


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

        //  Botón eliminar solo para nuevos
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


    @FXML
    private void agregarVehiculo() {
        String modeloNombre = txtModelo.getText();
        Categoria categoria = choiceCategoria.getValue();

        if (modeloNombre.isEmpty() || categoria == null) {
            mostrarWarning(
                    "Datos incompletos",
                    "Campos obligatorios vacíos",
                    "Debe ingresar un modelo y seleccionar una categoría antes de continuar."
            );
            return;
        }

        //  Verificar duplicado entre existentes y nuevos
        boolean existe = listaVehiculos.stream().anyMatch(
                v -> v.getModelo().getNombreModelo().equalsIgnoreCase(modeloNombre) &&
                        v.getCategoria().getCategoriaId().equals(categoria.getCategoriaId())
        );

        if (existe) {
            mostrarWarning(
                    "Registro duplicado",
                    "Modelo y categoría ya registrados",
                    "Ya existe este modelo asociado a la misma categoría en la tabla."
            );
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

        boolean confirmar = mostrarConfirmacion(
                "Confirmar registro",
                "Agregar modelo",
                "¿Está seguro de que desea agregar este modelo? Se agregarán los modelos de la tabla a la marca seleccionada.",
                "Sí, agregar",
                "Cancelar"
        );

        if (confirmar) {

            if (listaNuevos.isEmpty()) {
                cerrarVentana();
                return;
            }

            taskService.runTask(
                    loadingOverlayController,
                    () -> {

                        for (DetalleCategoria detalle : listaNuevos) {
                            //  Guardar modelo
                            Modelo nuevoModelo = modeloService.guardarModelo(detalle.getModelo());

                            //  Crear relación detalle_categoria
                            DetalleCategoria nuevoDetalle = new DetalleCategoria();
                            nuevoDetalle.setMarca(marcaSeleccionada);
                            nuevoDetalle.setModelo(nuevoModelo);
                            nuevoDetalle.setCategoria(detalle.getCategoria());
                            detalleCategoriaService.guardarDetalleCategoria(nuevoDetalle);
                        }



                        return null;
                    }, (resultado) -> {

                       mostrarInformacion(
                                "Operación completada",
                                "Modelo agregado",
                                "El modelo ha sido registrado en el sistema correctamente."
                        );
                        cerrarVentana();


                    }, (ex) -> {

                        if(ex instanceof ModeloException){
                           mostrarError(
                                    "Error al guardar",
                                    "Ocurrió un problema al intentar guardar alguno/s de los modelos proporcionados:",
                                    "" + ex.getMessage());
                        } else {
                            mostrarExcepcionThrowable(
                                    "Error interno",
                                    "Error inesperado en el sistema",
                                    "Ocurrió un error inesperado al intentar guardar la marca y los modelos proporcionados.",
                                    ex
                            );
                            cerrarVentana();
                        }

                    }, null
            );
        }


    }//guardarCambios

    private void limpiarCamposVehiculo() {

        choiceCategoria.setValue(null);
        txtModelo.clear();
        listaVehiculos.clear();

    }

    @FXML
    private void cerrarVentana() {
        limpiarCamposVehiculo();
        cleanup();
        Stage stage = (Stage) txtModelo.getScene().getWindow();
        stage.close();
    }

    @Override
    public void cleanup() {
        // 1. Limpiar colecciones (CRÍTICO - son 3 listas)
        if (listaVehiculos != null) {
            listaVehiculos.clear();
        }
        if (listaExistentes != null) {
            listaExistentes.clear();
        }
        if (listaNuevos != null) {
            listaNuevos.clear();
        }

        // 2. Limpiar tabla
        if (tablaVehiculos != null) {
            tablaVehiculos.getItems().clear();
            tablaVehiculos.setItems(null);
        }

        // 4. Nullificar objetos seleccionados
        marcaSeleccionada = null;

        // 6. Limpiar campo de texto
        if (txtModelo != null) {
            txtModelo.clear();
        }

    }

}//class
