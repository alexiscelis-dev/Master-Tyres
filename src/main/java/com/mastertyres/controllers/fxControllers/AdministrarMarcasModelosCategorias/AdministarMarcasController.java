package com.mastertyres.controllers.fxControllers.AdministrarMarcasModelosCategorias;

import com.mastertyres.categoria.service.CategoriaService;
import com.mastertyres.common.exeptions.MarcaException;
import com.mastertyres.common.exeptions.ModeloException;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.interfaces.ILoader;
import com.mastertyres.common.interfaces.IRestaurableDatos;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.ApplicationContextProvider;
import com.mastertyres.common.utils.MensajesAlert;
import com.mastertyres.components.fxComponents.loader.LoadingComponentController;
import com.mastertyres.detalleCategoria.model.DetalleCategoria;
import com.mastertyres.detalleCategoria.service.DetalleCategoriaService;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.marca.service.MarcaService;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.modelo.service.ModeloService;
import com.mastertyres.vehiculo.service.VehiculoService;
import com.mastertyres.vehiculoPromocion.service.VehiculoPromocionService;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import static com.mastertyres.common.utils.MensajesAlert.*;
import static com.mastertyres.common.utils.MenuContextSetting.disableMenu;

//import static com.mastertyres.common.utils.MensajesAlert.mostrarError;

@Component
public class AdministarMarcasController implements IFxController, ILoader, IRestaurableDatos {

    @FXML
    private Label lblNombre;
    @FXML
    private TextField txtBuscar;
    @FXML
    private TableView<DetalleCategoria> TablaVehiculoMarca;
    @FXML
    private TableColumn<DetalleCategoria, String> colMarca;
    @FXML
    private TableColumn<DetalleCategoria, String> colModelo;
    @FXML
    private TableColumn<DetalleCategoria, String> colCategoria;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button btnAgregarMarca;
    @FXML
    private Button BtnEliminarModelo;
    @FXML
    private Button BtnAgregarModelo;
    @FXML
    private Button btnEditarMarca;
    @FXML
    private Button EliminarMarca;
    @FXML
    private Button BtnEditarModelo;

    @FXML
    private Pagination PaginadorMarcas;
    @FXML
    private TilePane contenedorMarcas;

    @FXML
    private ListView<Marca> listaMarca;

    @Autowired
    private TaskService taskService;

    private LoadingComponentController loadingOverlayController;
    private final MarcaService marcaService;
    private final ModeloService modeloService;
    private final CategoriaService categoriaService;
    private final DetalleCategoriaService detalleCategoriaService;
    private final VehiculoService vehiculoService;
    private final VehiculoPromocionService vehiculoPromocionService;

    private Marca marcaSeleccionada;

    private static final int MARCAS_POR_PAGINA = 20;

    private int tamañoPagina = 20;

    private String filtroActual = null;

    public AdministarMarcasController(MarcaService marcaService,
                                      DetalleCategoriaService detalleCategoriaService, ModeloService modeloService, CategoriaService categoriaService, VehiculoService vehiculoService, VehiculoPromocionService vehiculoPromocionService) {
        this.marcaService = marcaService;
        this.detalleCategoriaService = detalleCategoriaService;
        this.modeloService = modeloService;
        this.categoriaService = categoriaService;
        this.vehiculoService = vehiculoService;
        this.vehiculoPromocionService = vehiculoPromocionService;
    }

    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingOverlayController = loading;

    }

    @FXML
    private void initialize() {

        // Cargar paginador inicial
        configurarPaginador();
        configuraciones();
        listeners();

    }//initialize


    @Override
    public void configuraciones() {

        validacionesModeloBtn();

      disableMenu(rootPane);

        colMarca.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMarca().getNombreMarca()));

        colModelo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getModelo().getNombreModelo()));

        colCategoria.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategoria().getNombreCategoria()));

    }//configuraciones

    @Override
    public void listeners() {

        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                filtroActual = null;
                configurarPaginador();
            } else {
                filtroActual = newValue;
                cargarMarcasFiltradas(filtroActual);
            }
        });

    }//listeners

    private void configurarPaginador() {
        //Page<String> paginaInicial = marcaService.listarNombresMarcas(PageRequest.of(0, tamañoPagina));
        Page<Marca> paginaInicial = marcaService.listarMarcasPaginado(PageRequest.of(0, tamañoPagina));
        mostrarMarcasPorNombre(paginaInicial.getContent());

        PaginadorMarcas.setPageCount(paginaInicial.getTotalPages());
        PaginadorMarcas.setCurrentPageIndex(0);

        // Evento para cuando el usuario cambia de página
        PaginadorMarcas.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {

            Page<Marca> nuevaPagina = marcaService.listarMarcasPaginado(PageRequest.of(newIndex.intValue(), tamañoPagina));
            mostrarMarcasPorNombre(nuevaPagina.getContent());

        });
    }

    private void mostrarMarcasPorNombre(List<Marca> nombresMarcas) {
        contenedorMarcas.getChildren().clear();
        for (Marca nombre : nombresMarcas) {
            VBox card = crearCardMarcaDesdeNombre(nombre.getNombreMarca(), nombre);
            contenedorMarcas.getChildren().add(card);
        }
    }

    private VBox crearCardMarcaDesdeNombre(String nombre, Marca m) {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: #1A1A1A; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;");
        card.setPrefSize(500, 100);

        Label lblNombre = new Label(nombre);
        lblNombre.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: white;");

        VBox textBox = new VBox(5, lblNombre);
        HBox contentBox = new HBox(10, textBox);
        card.getChildren().add(contentBox);

        // Efectos visuales
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #8EB83D; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #1A1A1A; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;"));

        card.setOnMouseClicked(event -> {
            card.setStyle("-fx-background-color: #8EB83D; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;");
            mostrarModelos(m);
        });

        return card;
    }

    private void cargarMarcasFiltradas(String filtro) {
        Page<Marca> paginaFiltrada = marcaService.buscarMarcasPorNombre(filtro, PageRequest.of(0, tamañoPagina));
        mostrarMarcasPorNombre(paginaFiltrada.getContent());
        PaginadorMarcas.setPageCount(paginaFiltrada.getTotalPages());
        PaginadorMarcas.setCurrentPageIndex(0);
        PaginadorMarcas.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            Page<Marca> nuevaPagina = marcaService.buscarMarcasPorNombre(filtro, PageRequest.of(newIndex.intValue(), tamañoPagina));
            mostrarMarcasPorNombre(nuevaPagina.getContent());
        });
    }

    private void validacionesModeloBtn() {
        BtnEditarModelo.disableProperty().bind(
                javafx.beans.binding.Bindings.isEmpty(TablaVehiculoMarca.getSelectionModel().getSelectedItems())
        );


        BtnEliminarModelo.disableProperty().bind(
                javafx.beans.binding.Bindings.isEmpty(TablaVehiculoMarca.getSelectionModel().getSelectedItems())
        );
    }

    @FXML
    private void agregarMarca() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/marca/AgregarMarca.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent root = loader.load();

            AgregarMarcaController controller = loader.getController();
            controller.setInitializeLoading(loadingOverlayController);


            Stage stage = new Stage(StageStyle.UTILITY);
            stage.setTitle("Agregar Marca");
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setMaximized(false);

            stage.showAndWait();

            configurarPaginador();
        } catch (IOException ex) {
            mostrarExcepcion(
                    "Error del sistema",
                    "Fallo en la visualización",
                    "No fue posible cargar los componentes de la vista seleccionada.",
                    ex
            );

            ex.printStackTrace();
        }
    }

    private void mostrarModelos(Marca m) {
        marcaSeleccionada = m;
        btnEditarMarca.setDisable(false);
        EliminarMarca.setDisable(false);
        BtnAgregarModelo.setDisable(false);

        lblNombre.setText(m.getNombreMarca());
        llenarTabla(m);
    }

    private void llenarTabla(Marca m) {
        int id = m.getMarcaId();
        TablaVehiculoMarca.getItems().clear();
        TablaVehiculoMarca.getItems().addAll(detalleCategoriaService.listarPorMarca(m));
        TablaVehiculoMarca.getItems().sort(Comparator.comparing((DetalleCategoria dc) ->
                dc.getModelo().getNombreModelo()).thenComparing(dc -> dc.getCategoria().getNombreCategoria()));
    }

    public void EditarMarca(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/marca/EditarMarca.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent root = loader.load();

            EditarMarcaController controller = loader.getController();
            controller.setMarcaModelos(marcaSeleccionada);
            controller.setInitializeLoading(loadingOverlayController);


            Stage stage = new Stage(StageStyle.UTILITY);
            stage.setTitle("Editar Marca");
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setMaximized(false);

            stage.showAndWait();

            configurarPaginador();
            Actualizar(null);

        } catch (IOException ex) {
            mostrarExcepcion(
                    "Error del sistema",
                    "Fallo en la visualización",
                    "No fue posible cargar los componentes de la vista seleccionada.",
                    ex
            );

            ex.printStackTrace();
        }
    }

    public void EditarModelo(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/modelo/EditarModelo.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent root = loader.load();

            EditarModeloController controller = loader.getController();
            controller.setModelos(TablaVehiculoMarca.getSelectionModel().getSelectedItem());
            controller.setInitializeLoading(loadingOverlayController);

            Stage stage = new Stage(StageStyle.UTILITY);
            stage.setTitle("Editar Modelo");
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setMaximized(false);

            stage.showAndWait();


            configurarPaginador();
            Actualizar(null);
        } catch (IOException ex) {
            mostrarExcepcion(
                    "Error del sistema",
                    "Fallo en la visualización",
                    "No fue posible cargar los componentes de la vista seleccionada.",
                    ex
            );
        }
    }

    public void AgregarModelo(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/modelo/AgregarModelo.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent root = loader.load();

            AgregarModeloController controller = loader.getController();
            controller.setMarcaModelos(marcaSeleccionada);
            controller.setInitializeLoading(loadingOverlayController);

            Stage stage = new Stage(StageStyle.UTILITY);
            stage.setTitle("Agregar Modelo");
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setMaximized(false);

            stage.showAndWait();

            configurarPaginador();
            Actualizar(null);
        } catch (IOException ex) {
            ex.printStackTrace();
            mostrarExcepcion(
                    "Error del sistema",
                    "Fallo en la visualización",
                    "No fue posible cargar los componentes de la vista seleccionada.",
                    ex
            );
        }
    }

    public void EliminarModelo(ActionEvent actionEvent) {

        Modelo modeloSeleccionada = TablaVehiculoMarca.getSelectionModel().getSelectedItem().getModelo();

        if (modeloSeleccionada == null) {
           mostrarWarning(
                    "Advertencia",
                    "Sin selección",
                    "Debe seleccionar un modelo de la lista antes de intentar eliminarlo."
            );
            return;
        }

        // Evitar eliminar el modelo genérico
        if (modeloSeleccionada.getModeloId() == 1) {
            mostrarWarning(
                    "Advertencia",
                    "Operación no permitida",
                    "No es posible eliminar el modelo genérico del sistema."
            );
            return;
        }

        boolean confirmar = MensajesAlert.mostrarConfirmacion(
                "Confirmar eliminación",
                "Eliminar modelo",
                "¿Está seguro de que desea eliminar el modelo seleccionado? Esta acción no se puede deshacer.",
                "Eliminar",
                "Cancelar"
        );

        if (confirmar) {


            taskService.runTask(
                    loadingOverlayController,
                    () -> {
                        vehiculoService.reasignarModeloPorId(modeloSeleccionada.getModeloId());
                        detalleCategoriaService.reasignarOEliminarPorModelo(modeloSeleccionada.getModeloId());
                        vehiculoPromocionService.reasignarModeloPorId(modeloSeleccionada.getModeloId());

                        // Eliminar el modelo
                        modeloService.eliminarModeloPorId(modeloSeleccionada.getModeloId());
                        return null;
                    }, (resultado) -> {

                        mostrarInformacion(
                                "Operación completada",
                                "Modelo eliminado",
                                "El modelo ha sido eliminado del sistema correctamente."
                        );

                        txtBuscar.setText("");
                        marcaSeleccionada = null;
                        btnEditarMarca.setDisable(true);
                        EliminarMarca.setDisable(true);
                        lblNombre.setText("");
                        TablaVehiculoMarca.getItems().clear();
                        configurarPaginador();
                        Actualizar(null);

                    }, (ex) -> {

                        if (ex.getCause() instanceof ModeloException) {
                            mostrarError(
                                    "Error al eliminar modelo",
                                    "Se produjo un problema con el modelo",
                                    "" + ex.getMessage() );

                        } else if (ex.getCause() instanceof Exception) {
                           mostrarExcepcionThrowable(
                                    "Error inesperado",
                                    "No se pudo completar la operación",
                                    "Ocurrió un error inesperado al intentar eliminar el modelo seleccionado.",
                                    ex
                            );
                        }

                    }, null
            );


        }
    }

    public void EliminarMarca(ActionEvent actionEvent) {


        if (marcaSeleccionada == null) {
            mostrarWarning(
                    "Advertencia",
                    "Sin selección",
                    "Debe seleccionar una marca de la lista antes de intentar eliminarla."
            );
            return;
        }

        // Evitar eliminar el modelo genérico
        if (marcaSeleccionada.getMarcaId() == 1) {
            mostrarWarning(
                    "Advertencia",
                    "Operación no permitida",
                    "No es posible eliminar la marca genérica del sistema."
            );
            return;
        }

        boolean confirmar = MensajesAlert.mostrarConfirmacion(
                "Confirmar eliminación",
                "Eliminar marca",
                "¿Está seguro de que desea eliminar la marca seleccionada? Esta acción no se puede deshacer.",
                "Eliminar",
                "Cancelar"
        );

        if (confirmar) {

            taskService.runTask(
                    loadingOverlayController,
                    () -> {

                        vehiculoService.reasignarModeloYCategoriaPorMarca(marcaSeleccionada.getMarcaId());
                        vehiculoPromocionService.reasignarModeloYCategoriaPorMarca(marcaSeleccionada.getMarcaId());
                        vehiculoService.reasignarMarcaPorId(marcaSeleccionada.getMarcaId());
                        vehiculoPromocionService.reasignarMarcaPorId(marcaSeleccionada.getMarcaId());
                        modeloService.reasignarMarcaPorId(marcaSeleccionada.getMarcaId());
                        detalleCategoriaService.eliminarPorMarca(marcaSeleccionada.getMarcaId());

                        // Eliminar marca
                        marcaService.eliminarMarcaPorId(marcaSeleccionada.getMarcaId());
                        return null;

                    }, (resultado) -> {
                        mostrarInformacion(
                                "Operación completada",
                                "Marca eliminada",
                                "La marca ha sido eliminada del sistema correctamente."
                        );

                        txtBuscar.setText("");
                        marcaSeleccionada = null;
                        btnEditarMarca.setDisable(true);
                        EliminarMarca.setDisable(true);
                        lblNombre.setText("");
                        TablaVehiculoMarca.getItems().clear();
                        configurarPaginador();

                        Actualizar(null);

                    }, (ex) -> {

                        if (ex.getCause() instanceof MarcaException) {
                            MensajesAlert.mostrarExcepcionThrowable(
                                    "Error al eliminar marca",
                                    "Se produjo un problema con la marca",
                                    ex.getMessage(),
                                    ex
                            );
                        } else if (ex.getCause() instanceof Exception) {
                            MensajesAlert.mostrarExcepcionThrowable(
                                    "Error inesperado",
                                    "No se pudo completar la operación",
                                    "Ocurrió un error inesperado al intentar eliminar la marca seleccionada.",
                                    ex
                            );
                        }

                    }, null
            );
        }
    }

    public void Actualizar(ActionEvent actionEvent) {
        txtBuscar.setText("");
        marcaSeleccionada = null;
        btnEditarMarca.setDisable(true);
        EliminarMarca.setDisable(true);
        lblNombre.setText("");
        TablaVehiculoMarca.getItems().clear();
        configurarPaginador();
    }

    @Override
    public void restaurarEstadoInicial() {
        // 1. Limpiar campo de búsqueda
        if (txtBuscar != null) {
            txtBuscar.clear();
        }

        // 2. Resetear variables de estado
        marcaSeleccionada = null;
        filtroActual = null;

        // 3. Limpiar label de nombre
        if (lblNombre != null) {
            lblNombre.setText("");
        }

        // 4. Limpiar tabla
        if (TablaVehiculoMarca != null) {
            TablaVehiculoMarca.getItems().clear();
        }

        // 5. Deshabilitar botones que requieren selección
        if (btnEditarMarca != null) {
            btnEditarMarca.setDisable(true);
        }
        if (EliminarMarca != null) {
            EliminarMarca.setDisable(true);
        }
        if (BtnAgregarModelo != null) {
            BtnAgregarModelo.setDisable(true);
        }

        // 6. Recargar datos sin filtros (paginador inicial)
        configurarPaginador();
    }

}//class
