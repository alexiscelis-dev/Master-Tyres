package com.mastertyres.controllers.fxControllers.Promociones;

import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.interfaces.ILoader;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.MensajesAlert;
import com.mastertyres.components.fxComponents.loader.LoadingComponentController;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.marca.service.MarcaService;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.modelo.service.ModeloService;
import com.mastertyres.promociones.model.Promocion;
import com.mastertyres.promociones.model.TipoDescuento;
import com.mastertyres.promociones.service.PromocionService;
import com.mastertyres.vehiculoPromocion.model.VehiculoPromocion;
import com.mastertyres.vehiculoPromocion.service.VehiculoPromocionService;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.detalleCategoria.service.DetalleCategoriaService;
import javafx.util.StringConverter;
import java.util.Objects;
import java.util.stream.Collectors;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javafx.collections.FXCollections.observableList;

@Component
public class EditarPromocionController implements IFxController, ILoader {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private TextField txtPrecio;
    @FXML private TextField precioConDescuento;
//    @FXML
//    private ChoiceBox<String> txtTipoDescuento;
    @FXML
    private Slider porcentajeDescuento;
    @FXML
    private Label porcentaje; //  el Label donde mostramos el valor
    @FXML
    private DatePicker dateInicio;
    @FXML
    private DatePicker dateFin;
    @FXML
    private TextField txtRutaImagen;
    @FXML
    private Button btnSeleccionarImagen;
    @FXML
    private Button btnActualizar;
    private Promocion promocionSeleccionada;

    @FXML
    private ChoiceBox<Marca> choiceMarca;
    @FXML
    private ChoiceBox<Modelo> choiceModelo;
    @FXML
    private ChoiceBox<Integer> choiceAnio;
    @FXML
    private Button btnAgregarVehiculo;
    @FXML
    private TableView<VehiculoPromocion> tableVehiculosParticipantes;
    @FXML
    private TableColumn<VehiculoPromocion, String> colMarca;
    @FXML
    private TableColumn<VehiculoPromocion, String> colModelo;
    @FXML
    private TableColumn<VehiculoPromocion, Integer> colAnio;
    @FXML
    private TableColumn<VehiculoPromocion, Void> colEliminar;

    private ObservableList<VehiculoPromocion> vehiculosPromocionList = FXCollections.observableArrayList();

    private LoadingComponentController loadingOverlayController;


    @Autowired
    private PromocionService promocionService;
    @Autowired
    private VehiculoPromocionService vehiculoPromocionService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private DetalleCategoriaService detalleCategoriaService;
    @Autowired
    private MarcaService marcaService;
    @Autowired
    private ModeloService modeloService;

    //Validaciones:
    private BooleanProperty precioValido = new SimpleBooleanProperty(true);
    private BooleanProperty nombreValido = new SimpleBooleanProperty(true);
    private BooleanProperty descripcionValido = new SimpleBooleanProperty(true);


    // Inicializa el controlador
    public void initialize() {

        configuraciones();
        listeners();
        vehiculosParticipantesInitialize();

    }//initialize

    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingOverlayController = loading;
    }

    @Override
    public void configuraciones() {
        //elimina scroll horizontal de tabla vehculos paricipantes
        tableVehiculosParticipantes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //txtTipoDescuento.getItems().addAll("Porcentaje", "Otro");

        // Inicializar columnas
        colMarca.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMarca().getNombreMarca()));
        //colModelo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getModelo().getNombreModelo()));
        colModelo.setCellValueFactory(data -> new SimpleStringProperty(
                obtenerNombreModeloConCategoria(data.getValue().getMarca(), data.getValue().getModelo())
        ));
        colAnio.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getAnnio()).asObject());

        // Columna eliminar
        colEliminar.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button();

            {
                // Quitar texto y agregar imagen
                Image img = new Image(getClass().getResourceAsStream("/icons/delete.png"));
                ImageView iv = new ImageView(img);
                iv.setFitWidth(18);   // tamaño icono
                iv.setFitHeight(18);
                btn.setGraphic(iv);

                btn.setOnAction(event -> {
                    VehiculoPromocion vp = getTableView().getItems().get(getIndex());
                    vehiculosPromocionList.remove(vp);
                    // Aquí también puedes llamar al service/repository para eliminar en BD
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

        tableVehiculosParticipantes.setItems(vehiculosPromocionList);


        // Configuración del slider
        porcentajeDescuento.setMin(0);
        porcentajeDescuento.setMax(100);
        porcentajeDescuento.setBlockIncrement(1);


    }//configuraciones

    @Override
    public void listeners() {

        choiceMarca.setOnMousePressed(event -> {
            if (!choiceMarca.isShowing()) {
                choiceMarca.show();
                choiceMarca.hide();
            }
        });

        choiceModelo.setOnMousePressed(event -> {
            if (!choiceModelo.isShowing()) {
                choiceModelo.show();
                choiceModelo.hide();
            }
        });

        choiceAnio.setOnMousePressed(event -> {
            if (!choiceAnio.isShowing()) {
                choiceAnio.show();
                choiceAnio.hide();
            }
        });

        configurarValidaciones();

        // Mostrar el valor del slider en el Label en tiempo real
        porcentajeDescuento.valueProperty().addListener((obs, oldVal, newVal) -> {
            porcentaje.setText(String.format("%d%%", newVal.intValue()));
        });

        // Acción de agregar vehículo
        btnAgregarVehiculo.setOnAction(e -> agregarVehiculo());

        // Acción para abrir el FileChooser
        btnSeleccionarImagen.setOnAction(e -> seleccionarImagen());

        //Actualizar valores
        btnActualizar.setOnAction(event -> actualizarPromocion());

        porcentajeDescuento.valueProperty().addListener((observable, valAnterior, valNuevo) -> {
            //obtenerPorcentaje(valNuevo.doubleValue());
            calcularPrecioConDescuento();
        });


        txtPrecio.textProperty().addListener((observable, valAnterior, nuevoValor) -> {
            calcularPrecioConDescuento();
        });

    }//listeners

    private void calcularPrecioConDescuento() {
        String precioTexto = txtPrecio.getText();

        if (precioTexto == null || precioTexto.isEmpty()) {
            precioConDescuento.setText("");
            return;
        }

        try {
            double precio = Double.parseDouble(precioTexto);
            double porcentaje = porcentajeDescuento.getValue();
            double descuento = precio * (porcentaje / 100.0);
            double precioFinal = precio - descuento;

            precioConDescuento.setText(String.format("%.2f", precioFinal));
        } catch (NumberFormatException e) {
            precioConDescuento.setText("");
        }
    }//calcularPrecioConDescuento

    private void configurarValidaciones() {

        //VALIDACIONES DE CHOICESBOXS
        choiceModelo.disableProperty().bind(choiceMarca.valueProperty().isNull());
        choiceAnio.disableProperty().bind(
                choiceMarca.valueProperty().isNull().or(choiceModelo.valueProperty().isNull())
        );


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

        // DESCRIPCION
        txtDescripcion.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.isBlank()) {
                descripcionValido.set(false);
                txtDescripcion.setStyle("-fx-border-color: red;");
            } else {
                descripcionValido.set(true);
                txtDescripcion.setStyle("");
            }
        });
        txtDescripcion.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // pierde foco
                txtDescripcion.setText(formatoOracion(txtDescripcion.getText()));
            }
        });


        // Precio (solo números decimales)
        txtPrecio.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.isEmpty()) {
                precioValido.set(true);
                txtPrecio.setStyle(""); // estilo normal
            } else if (!newText.matches("\\d*(\\.\\d{0,2})?")) {
                // 🔹 Permite enteros o con hasta 2 decimales
                precioValido.set(false);
                txtPrecio.setStyle("-fx-border-color: red;");
            } else {
                precioValido.set(true);
                txtPrecio.setStyle("");
            }
        });

        // TextFormatter para bloquear entradas inválidas
        txtPrecio.setTextFormatter(new TextFormatter<>(c -> {
            if (c.getControlNewText().matches("\\d*(\\.\\d{0,2})?")) {
                return c;
            }
            return null;
        }));

        // Deshabilitar botón "Agregar Vehículo" hasta que se llenen los campos requeridos
        btnAgregarVehiculo.disableProperty().bind(
                choiceMarca.valueProperty().isNull()
                        .or(choiceModelo.valueProperty().isNull())
                        .or(choiceAnio.valueProperty().isNull())
        );

        //Deshabilitar botón "Guardar" cuando NO haya cliente o la lista de vehículos esté vacía
        btnActualizar.disableProperty().bind(
                Bindings.isEmpty(vehiculosPromocionList)
                        //.or(txtTipoDescuento.valueProperty().isNull())
                        .or(txtNombre.textProperty().isEmpty())
                        .or(txtDescripcion.textProperty().isEmpty())
                        .or(precioValido.not())
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

    private void cargarVehiculos() {
        if (promocionSeleccionada != null && promocionSeleccionada.getPromocionId() != null) {
            vehiculosPromocionList.clear();

            var vehiculos = vehiculoPromocionService.obtenerVehiculosPorPromocion(promocionSeleccionada.getPromocionId());

            vehiculosPromocionList.addAll(vehiculos);
        }
    }

    private void agregarVehiculo() {
        Marca marca = choiceMarca.getValue();
        Modelo modelo = choiceModelo.getValue();
        Integer anio = choiceAnio.getValue();

        if (marca != null && modelo != null && anio != null) {
            VehiculoPromocion vehiculo = new VehiculoPromocion();
            vehiculo.setMarca(marca);
            vehiculo.setModelo(modelo);
            vehiculo.setAnnio(anio);

            // Validar duplicados según equals/hashCode
            if (!vehiculosPromocionList.contains(vehiculo)) {
                vehiculosPromocionList.add(vehiculo);
            }
        }
    }

    private void vehiculosParticipantesInitialize() {

        choiceMarca.setItems(observableList(marcaService.listarMarcas()));
        List<Modelo> modelos = modeloService.listarModelos();
        choiceModelo.setItems(observableList(modelos));
        choiceModelo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Modelo modelo) {
                return modelo != null ? obtenerNombreModeloConCategoria(choiceMarca.getValue(), modelo) : "";
            }

            @Override
            public Modelo fromString(String string) {
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

        List<Integer> anios = new ArrayList<>();

        int anioActual = LocalDate.now().getYear();

        for (int i = anioActual; i >= 1950; i--) {
            anios.add(i);
        }
        choiceAnio.getItems().setAll(anios);


    }//vehiculosParticipantesInitialize

    private String obtenerNombreModeloConCategoria(Marca marca, Modelo modelo) {
        if (modelo == null) {
            return "";
        }

        String nombreModelo = modelo.getNombreModelo() != null ? modelo.getNombreModelo() : "";
        Marca marcaSeleccionada = marca != null ? marca : modelo.getMarca_id();
        if (marcaSeleccionada == null || marcaSeleccionada.getMarcaId() == null) {
            return nombreModelo;
        }

        List<Categoria> categorias = detalleCategoriaService
                .listarCategoriasPorMarcaYModelo(marcaSeleccionada.getMarcaId(), modelo.getModeloId());
        String categoriasTexto = categorias.stream()
                .map(Categoria::getNombreCategoria)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.joining("/"));

        if (categoriasTexto.isBlank()) {
            return nombreModelo;
        }

        return nombreModelo + " (" + categoriasTexto + ")";
    }

    public void setPromocion(Promocion promocion) {
        this.promocionSeleccionada = promocion;

        if (promocion != null) {
            txtNombre.setText(promocion.getNombre());
            txtDescripcion.setText(promocion.getDescripcion());
            txtPrecio.setText(String.valueOf(promocion.getPrecio()));
            //txtTipoDescuento.setValue(promocion.getTipoDescuento());

            // Configurar el valor del slider y actualizar el label
            porcentajeDescuento.setValue(promocion.getPorcentaje());
            porcentaje.setText(promocion.getPorcentaje() + "%");
            calcularPrecioConDescuento();

            dateInicio.setValue(LocalDate.parse(promocion.getFechaInicio()));
            dateFin.setValue(LocalDate.parse(promocion.getFechaFin()));
            txtRutaImagen.setText(promocion.getImg());
        }
        cargarVehiculos();
    }

    // Selección de imagen con FileChooser
    @FXML
    private void seleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            txtRutaImagen.setText(file.getAbsolutePath());
        }
    }

    // Acción de cancelar -> cerrar ventana
    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    // Acción para guardar cambios
    @FXML
    private void actualizarPromocion() {

        if (promocionSeleccionada == null) {
            MensajesAlert.mostrarError(
                    "Error",
                    "No hay promoción seleccionada",
                    "Debe seleccionar una promoción antes de modificarla."
            );
            return;
        }

        if (vehiculosPromocionList == null || vehiculosPromocionList.isEmpty()) {
            MensajesAlert.mostrarWarning(
                    "Validación",
                    "Vehículos requeridos",
                    "Debe asignar al menos un vehículo."
            );
            return;
        }

        boolean confirmar = MensajesAlert.mostrarConfirmacion(
                "Confirmar actualización",
                "¿Desea guardar los cambios?",
                "",
                "Sí, guardar",
                "Cancelar"
        );

        if (!confirmar) return;

        taskService.runTask(
                loadingOverlayController,
                () -> {

                    // 🔹 Actualizar datos generales
                    promocionSeleccionada.setNombre(txtNombre.getText().trim());
                    promocionSeleccionada.setDescripcion(txtDescripcion.getText().trim());
                    promocionSeleccionada.setPrecio(Float.parseFloat(txtPrecio.getText().trim()));
                    promocionSeleccionada.setTipoDescuento(TipoDescuento.PORCENTAJE.toString());
                    promocionSeleccionada.setPorcentaje((int) porcentajeDescuento.getValue());
                    promocionSeleccionada.setFechaInicio(dateInicio.getValue().toString());
                    promocionSeleccionada.setFechaFin(dateFin.getValue().toString());
                    promocionSeleccionada.setImg(txtRutaImagen.getText());
                    promocionSeleccionada.setUpdated_at(LocalDateTime.now());

                    // 🔥 UNA SOLA LLAMADA
                    promocionService.actualizarPromocionConVehiculos(
                            promocionSeleccionada,
                            vehiculosPromocionList
                    );

                    return null;

                },
                (resultado) -> {

                    MensajesAlert.mostrarInformacion(
                            "Promoción actualizada",
                            "",
                            "La promoción se actualizó correctamente."
                    );

                    cerrarVentana();

                },
                (ex) -> {

                    MensajesAlert.mostrarError(
                            "Error al actualizar",
                            "",
                            ex.getMessage() != null
                                    ? ex.getMessage()
                                    : "Error inesperado al actualizar."
                    );

                },
                null
        );
    }
//    @FXML
//    private void actualizarPromocion() {
//        if (promocionSeleccionada == null) {
//            MensajesAlert.mostrarError(
//                    "Error",
//                    "No hay promoción seleccionada",
//                    "Debe seleccionar una promoción antes de poder modificarla."
//            );
//            return;
//        }
//
//        //  Validaciones básicas
//        if (txtNombre.getText() == null || txtNombre.getText().trim().isEmpty()) {
//            MensajesAlert.mostrarWarning("Validación", "Campo requerido", "El nombre de la promoción no puede estar vacío.");
//            return;
//        }
//
//        if (txtDescripcion.getText() == null || txtDescripcion.getText().trim().isEmpty()) {
//            MensajesAlert.mostrarWarning("Validación", "Campo requerido", "La descipcion de la promoción no puede estar vacío.");
//            return;
//        }
//
//        try {
//            Float.parseFloat(txtPrecio.getText());
//        } catch (NumberFormatException e) {
//            MensajesAlert.mostrarError("Error de formato", "Precio inválido", "Ingrese un valor numérico válido para el precio.");
//            return;
//        }
//
//        if (dateInicio.getValue() == null || dateFin.getValue() == null) {
//            MensajesAlert.mostrarWarning("Validación", "Fechas requeridas", "Debe ingresar la fecha de inicio y de fin.");
//            return;
//        }
//
//        if (dateInicio.getValue().isAfter(dateFin.getValue())) {
//            MensajesAlert.mostrarError("Error en fechas", "Fechas inválidas", "La fecha de inicio no puede ser mayor a la fecha de fin.");
//            return;
//        }
//
//        if (txtTipoDescuento.getValue() == null) {
//            MensajesAlert.mostrarWarning("Validación", "Campo requerido", "Debe seleccionar un tipo de descuento.");
//            return;
//        }
//
//        if (vehiculosPromocionList == null || vehiculosPromocionList.isEmpty()) {
//            MensajesAlert.mostrarWarning("Validación", "Vehículos requeridos", "Debe asignar al menos un vehículo a la promoción.");
//            return;
//        }
//
//        //  Confirmación antes de actualizar
//        boolean confirmar = MensajesAlert.mostrarConfirmacion(
//                "Confirmar actualización",
//                "¿Desea guardar los cambios en la promoción?",
//                "Se actualizarán los datos de la promoción seleccionada.",
//                "Sí, guardar",
//                "Cancelar"
//        );
//
//        if (!confirmar) {
//            return; // Usuario canceló
//        }
//
//        taskService.runTask(
//                loadingOverlayController,
//                () -> {
//
//                    //  Actualizar datos generales
//                    promocionSeleccionada.setNombre(txtNombre.getText().trim());
//                    promocionSeleccionada.setDescripcion(txtDescripcion.getText().trim());
//                    promocionSeleccionada.setPrecio(Float.parseFloat(txtPrecio.getText().trim()));
//                    promocionSeleccionada.setTipoDescuento(txtTipoDescuento.getValue());
//                    promocionSeleccionada.setPorcentaje((int) porcentajeDescuento.getValue());
//                    promocionSeleccionada.setFechaInicio(dateInicio.getValue().toString());
//                    promocionSeleccionada.setFechaFin(dateFin.getValue().toString());
//                    promocionSeleccionada.setImg(txtRutaImagen.getText());
//                    promocionSeleccionada.setUpdated_at(LocalDateTime.now());
//
//                    //  Guardar cambios en la promoción
//                    promocionService.guardarPromocion(promocionSeleccionada);
//
//
//                    //  1. Eliminar todos los vehículos de esa promo en BD
//                    vehiculoPromocionService.eliminarPorPromocionId(promocionSeleccionada.getPromocionId());
//
//                    //  2. Guardar de nuevo los que están en la tabla
//                    for (VehiculoPromocion vp : vehiculosPromocionList) {
//                        vp.setVehiculoPromocionID(null); //  Forzar INSERT
//                        vp.setPromocion(promocionSeleccionada);
//                        vehiculoPromocionService.guardarVehiculosAplicables(vp);
//                    }
//
//                    return null;
//                }, (resultado) -> {
//
//
//                    MensajesAlert.mostrarInformacion("Promoción actualizada", "", "La promoción se actualizó correctamente.");
//                    cerrarVentana();
//
//                }, (ex) -> {
//
//                    if (ex instanceof PromocionException) {
//                        MensajesAlert.mostrarError(
//                                "Error al actualizar",
//                                "Ocurrio un problema al guardar los cambios",
//                                "" + ex.getMessage()
//                        );
//                    } else {
//                        MensajesAlert.mostrarError(
//                                "Error interno",
//                                "",
//                                "Ocurrio un error inesperado al actualizar los cambios. Vuelve a intentarlo mas tarde");
//                    }
//
//                }, null
//
//        );
//
//
//    }//actualizarPromocion

}//class
