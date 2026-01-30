package com.mastertyres.fxControllers.AdministrarMarcasModelosCategorias;


import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.categoria.service.CategoriaService;
import com.mastertyres.common.exeptions.MarcaException;
import com.mastertyres.common.exeptions.ModeloException;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.interfaces.ILoading;
import com.mastertyres.common.interfaces.IVentanaPrincipal;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.MensajesAlert;
import com.mastertyres.common.utils.MenuContextSetting;
import com.mastertyres.detalleCategoria.model.DetalleCategoria;
import com.mastertyres.detalleCategoria.service.DetalleCategoriaService;
import com.mastertyres.fxComponents.LoadingComponentController;
import com.mastertyres.fxControllers.ventanaPrincipal.VentanaPrincipalController;
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
import javafx.event.ActionEvent;
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

import static com.mastertyres.common.utils.MensajesAlert.mostrarError;
import static com.mastertyres.common.utils.MensajesAlert.mostrarInformacion;

@Component
public class AgregarMarcaController implements IVentanaPrincipal, IFxController, ILoading {


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
    private TableView<ModeloCategoriaTemp> tablaVehiculos;
    @FXML
    private TableColumn<ModeloCategoriaTemp, String> colCategoria;
    @FXML
    private TableColumn<ModeloCategoriaTemp, String> colModelo;
    @FXML
    private TableColumn<ModeloCategoriaTemp, Void> colEliminar;


    @FXML
    private ChoiceBox<Categoria> choiceCategoria;
    @FXML
    private TextField txtModelo;
    @FXML
    private TextField txtMarca;

    @FXML
    private Button btnAgregarVehiculo;
    @FXML
    private Button btnAgregar;

    private BooleanProperty ModeloValido = new SimpleBooleanProperty(true);
    private BooleanProperty MarcaValido = new SimpleBooleanProperty(true);

    private ObservableList<ModeloCategoriaTemp> listaVehiculos = FXCollections.observableArrayList();

    private VentanaPrincipalController ventanaPrincipalController;

    private LoadingComponentController loadngOverlayController;

    @Override
    public void setVentanaPrincipalController(VentanaPrincipalController controller) {
        this.ventanaPrincipalController = controller;
    }

    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadngOverlayController = loading;
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

        tablaVehiculos.setItems(listaVehiculos);

        colModelo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getModelo()));
        colCategoria.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getCategoria() != null ? data.getValue().getCategoria().getNombreCategoria() : ""
        ));
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
                    ModeloCategoriaTemp item = getTableView().getItems().get(getIndex());
                    listaVehiculos.remove(item);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

    }//configuraciones

    @Override
    public void listeners() {

        configurarValidaciones();

    }

    @FXML
    private void agregarVehiculo(ActionEvent event) {
        String modelo = txtModelo.getText().trim().toUpperCase();
        Categoria categoria = choiceCategoria.getValue();

        if (modelo.isEmpty() || categoria == null) {
            mostrarError("Error", "Campos Vacíos", "Debes ingresar un modelo y seleccionar una categoría.");
            return;
        }

        //  Validación de duplicados
        boolean existe = listaVehiculos.stream().anyMatch(
                v -> v.getModelo().equalsIgnoreCase(modelo) &&
                        v.getCategoria().getCategoriaId().equals(categoria.getCategoriaId())
        );

        if (existe) {
            mostrarError("Duplicado", "Modelo y Categoría repetidos",
                    "Ya existe este modelo con la misma categoría en la tabla.");
            return;
        }

        // Si no existe, lo agregamos
        listaVehiculos.add(new ModeloCategoriaTemp(modelo, categoria));

        choiceCategoria.setValue(null);
        txtModelo.clear();
    }


    private void limpiarCamposVehiculo() {

        choiceCategoria.setValue(null);
        txtModelo.clear();
        txtMarca.clear();
        listaVehiculos.clear();

    }

    public void cerrarVentana(ActionEvent actionEvent) {
        limpiarCamposVehiculo();

        Stage stage = (Stage) tablaVehiculos.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void GuardarMarca() {

        boolean confirmar = MensajesAlert.mostrarConfirmacion(
                "Confirmar Agregar",
                "¿Desea agregar este Modelo?",
                "Se agregara los modelo de la tabla a esta marca.",
                "Sí, agregar",
                "Cancelar"
        );

        if (confirmar) {


            taskService.runTask(
                    loadngOverlayController,
                    () -> {

                        Marca m = new Marca();
                        m.setNombreMarca(txtMarca.getText().trim());
                        m = marcaService.guardarMarca(m);

                        // Guardar Modelos y DetalleCategoria
                        for (ModeloCategoriaTemp temp : listaVehiculos) {
                            // Guardar Modelo
                            Modelo modelo = new Modelo();
                            modelo.setNombreModelo(temp.getModelo());
                            modelo.setMarca_id(m);
                            modelo = modeloService.guardarModelo(modelo);

                            // Guardar DetalleCategoria
                            DetalleCategoria detalle = new DetalleCategoria();
                            detalle.setMarca(m);
                            detalle.setModelo(modelo);
                            detalle.setCategoria(temp.getCategoria());

                            detalleCategoriaService.guardarDetalleCategoria(detalle);
                        }
                        return null;

                    },
                    (resultado) -> {

                        limpiarCamposVehiculo();

                        mostrarInformacion("Guardado exitoso", "", "Marca y modelo(s) guardados correctamente.");
                        cerrarVentana(null);

                    }, (ex) -> {
                        Throwable causaActual = ex;
                        Throwable excepcionEncontrada = null;

                        while (causaActual != null) {
                            if (causaActual instanceof MarcaException || causaActual instanceof ModeloException) {
                                excepcionEncontrada = causaActual;
                                break; // ¡La encontramos!
                            }
                            causaActual = causaActual.getCause(); // Bajar un nivel
                        }


                        if (excepcionEncontrada instanceof MarcaException) {

                            mostrarError("No se pudo guardar", "Ocurrio un problema al intentar guardar la marca", "" + excepcionEncontrada.getMessage());

                        } else if (excepcionEncontrada instanceof ModeloException) {
                            mostrarError("No se pudo guardar", "Ocurrio un problema al intentar guardar alguno(s) de los modelos", "" + excepcionEncontrada.getMessage());

                        } else {
                            cerrarVentana(null);
                            mostrarError("Error interno",
                                    "Error inesperado",
                                    "Ocurrio un error al intentar guardar la marca y modelo(s) proporsonados");
                        }

                    }, null
            );
            //        try {
            // Guardar Marca



/*
            } catch (Exception e) {
                mostrarError("Error ", "Error al guardar: ", e.getMessage());
                e.printStackTrace();
            }

 */
        }

    }//GuardarMarca

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

        txtMarca.textProperty().addListener((obs, oldText, newText) -> {
            String texto = newText.toUpperCase();
            txtMarca.setText(texto);
            if (texto.isEmpty()) {
                MarcaValido.set(false);
                txtMarca.setStyle("-fx-border-color: red;");
            } else if (!texto.matches("^[A-Z0-9\\s\\p{Punct}]{1,20}$")) {
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
                        .or(ModeloValido.not())
        );

        //Deshabilitar botón "Guardar" cuando NO haya cliente o la lista de vehículos esté vacía
        btnAgregar.disableProperty().bind(
                Bindings.isEmpty(listaVehiculos)
                        .or(txtMarca.textProperty().isEmpty())
                        .or(MarcaValido.not())
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


    public static class ModeloCategoriaTemp {
        private final String modelo;
        private final Categoria categoria;

        public ModeloCategoriaTemp(String modelo, Categoria categoria) {
            this.modelo = modelo;
            this.categoria = categoria;
        }

        public String getModelo() {
            return modelo;
        }

        public Categoria getCategoria() {
            return categoria;
        }
    }


}//class
