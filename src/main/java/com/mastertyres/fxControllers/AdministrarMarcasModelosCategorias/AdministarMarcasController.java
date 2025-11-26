package com.mastertyres.fxControllers.AdministrarMarcasModelosCategorias;

import com.mastertyres.categoria.services.CategoriaService;
import com.mastertyres.common.ApplicationContextProvider;
import com.mastertyres.common.MensajesAlert;
import com.mastertyres.detalleCategoria.model.DetalleCategoria;
import com.mastertyres.detalleCategoria.service.DetalleCategoriaService;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.marca.services.MarcaService;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.modelo.services.ModeloService;
import com.mastertyres.vehiculo.service.VehiculoService;
import com.mastertyres.vehiculoPromocion.service.VehiculoPromocionService;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class AdministarMarcasController {

    @FXML
    private Label lblNombre;

    @FXML
    private TextField txtBuscar;

    @FXML private TableView<DetalleCategoria> TablaVehiculoMarca;
    @FXML private TableColumn<DetalleCategoria, String> colMarca;
    @FXML private TableColumn<DetalleCategoria, String> colModelo;
    @FXML private TableColumn<DetalleCategoria, String> colCategoria;


    @FXML private Button btnAgregarMarca;
    @FXML private Button BtnEliminarModelo;
    @FXML private Button BtnAgregarModelo;
    @FXML private Button btnEditarMarca;
    @FXML private Button EliminarMarca;
    @FXML private Button BtnEditarModelo;

    @FXML private Pagination PaginadorMarcas;
    @FXML private TilePane contenedorMarcas;

    @FXML private ListView<Marca> listaMarca;

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
                                      DetalleCategoriaService detalleCategoriaService, ModeloService modeloService, CategoriaService categoriaService,  VehiculoService vehiculoService, VehiculoPromocionService vehiculoPromocionService ) {
        this.marcaService = marcaService;
        this.detalleCategoriaService = detalleCategoriaService;
        this.modeloService = modeloService;
        this.categoriaService = categoriaService;
        this.vehiculoService = vehiculoService;
        this.vehiculoPromocionService = vehiculoPromocionService;
    }

    @FXML
    private void initialize(){

        colMarca.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMarca().getNombreMarca()));

        colModelo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getModelo().getNombreModelo()));

        colCategoria.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategoria().getNombreCategoria()));

        validacionesModeloBtn();

        // Cargar paginador inicial
        configurarPaginador();

        // Listener para el buscador
        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                filtroActual = null;
                configurarPaginador();
            } else {
                filtroActual = newValue;
                cargarMarcasFiltradas(filtroActual);
            }
        });
    }

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

//    private void cargarMarcasFiltradas(String filtro) {
//        Pageable pageable = PageRequest.of(0, tamañoPagina);
//
//        // 🔍 Buscar en marcas, modelos y categorías
//        Page<Marca> paginaMarcas = marcaService.buscarMarcasPorNombre(filtro, pageable);
//        Page<Modelo> paginaModelos = modeloService.buscarModelosPorNombre(filtro, pageable);
//        Page<Categoria> paginaCategorias = categoriaService.buscarCategoriasPorNombre(filtro, pageable);
//
//        // 🔄 Unificar resultados (solo marcas únicas)
//        Set<Marca> marcasUnicas = new HashSet<>(paginaMarcas.getContent());
//
//        // 🔗 Agregar marcas encontradas desde modelos
//        for (Modelo modelo : paginaModelos.getContent()) {
//            if (modelo.getMarca_id() != null) {
//                marcasUnicas.add(modelo.getMarca_id());
//            }
//        }
//
//        // 🔗 Agregar marcas encontradas a través de las categorías (por detalleCategoria)
//        for (Categoria categoria : paginaCategorias.getContent()) {
//            List<DetalleCategoria> detalles = detalleCategoriaService.findByCategoria(categoria);
//            for (DetalleCategoria detalle : detalles) {
//                if (detalle.getMarca() != null) {
//                    marcasUnicas.add(detalle.getMarca());
//                }
//            }
//        }
//
//        // 🧱 Mostrar resultados en la UI
//        List<Marca> listaFinal = new ArrayList<>(marcasUnicas);
//        mostrarMarcas(listaFinal);
//
//        // ⚙️ Configurar el paginador
//        int totalPaginas = Math.max(
//                Math.max(paginaMarcas.getTotalPages(), paginaModelos.getTotalPages()),
//                paginaCategorias.getTotalPages()
//        );
//
//        PaginadorMarcas.setPageCount(totalPaginas);
//        PaginadorMarcas.setCurrentPageIndex(0);
//
//        // 📄 Escuchar cambios de página
//        PaginadorMarcas.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
//            int pageIndex = newIndex.intValue();
//            Pageable nuevaPagina = PageRequest.of(pageIndex, tamañoPagina);
//
//            Page<Marca> nuevasMarcas = marcaService.buscarMarcasPorNombre(filtro, nuevaPagina);
//            Page<Modelo> nuevosModelos = modeloService.buscarModelosPorNombre(filtro, nuevaPagina);
//            Page<Categoria> nuevasCategorias = categoriaService.buscarCategoriasPorNombre(filtro, nuevaPagina);
//
//            Set<Marca> nuevasUnicas = new HashSet<>(nuevasMarcas.getContent());
//
//            for (Modelo mo : nuevosModelos.getContent()) {
//                if (mo.getMarca_id() != null) {
//                    nuevasUnicas.add(mo.getMarca_id());
//                }
//            }
//
//            for (Categoria ca : nuevasCategorias.getContent()) {
//                List<DetalleCategoria> detalles = detalleCategoriaService.findByCategoria(ca);
//                for (DetalleCategoria det : detalles) {
//                    if (det.getMarca() != null) {
//                        nuevasUnicas.add(det.getMarca());
//                    }
//                }
//            }
//
//            mostrarMarcasPorNombre(new ArrayList<>(nuevasUnicas));
//        });
//    }

//    private void cargarMarcasFiltradas(String filtro) {
//        // Buscar detalleCategoria que coincidan con el texto
//        List<DetalleCategoria> detalles = detalleCategoriaService.buscarPorTexto(filtro);
//
//        // Extraer marcas únicas de los resultados
//        List<Marca> marcasFiltradas = detalles.stream()
//                .map(DetalleCategoria::getMarca)
//                .distinct()
//                .toList();
//
//
//        mostrarMarcas(marcasFiltradas);
//    }
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

    private void validacionesModeloBtn(){
        BtnEditarModelo.disableProperty().bind(
                javafx.beans.binding.Bindings.isEmpty(TablaVehiculoMarca.getSelectionModel().getSelectedItems())
        );

//        BtnAgregarModelo.disableProperty().bind(
//                javafx.beans.binding.Bindings.isEmpty(TablaVehiculoMarca.getSelectionModel().getSelectedItems())
//        );

        BtnEliminarModelo.disableProperty().bind(
                javafx.beans.binding.Bindings.isEmpty(TablaVehiculoMarca.getSelectionModel().getSelectedItems())
        );
    }


    @FXML
    private void agregarMarca (){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/marca/AgregarMarca.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent root = loader.load();

            AgregarMarcaController controller = loader.getController();


            Stage stage = new Stage();
            stage.setTitle("Agregar Marca");
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();
            //cargarMarcas();
            configurarPaginador();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

//    private void cargarMarcas() {
//        List<Marca> marcas = marcaService.listarMarcas();
//        //mostrarMarcas(marcas);
//    }
//
//    private void mostrarMarcas(List<Marca> marcas) {
//        contenedorMarcas.getChildren().clear();
//        for (Marca p : marcas) {
//            //VBox card = crearCardMarca(p);
//            contenedorMarcas.getChildren().add(card);
//        }
//    }
//
//    private VBox crearCardMarca(Marca p) {
//        VBox card = new VBox();
//        card.setStyle("-fx-background-color: #1A1A1A; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;");
//        card.setPrefSize(500, 100);
//
//        // ========= TEXTO =========
//        Label lblNombre = new Label(p.getNombreMarca());
//        lblNombre.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: white;");
//
//
//        VBox textBox = new VBox(5, lblNombre);
//
//        // ========= CONTENEDOR HORIZONTAL =========
//        HBox contentBox = new HBox(10, textBox);
//
//        card.getChildren().add(contentBox);
//
//        // ========= ESTILOS INTERACTIVOS =========
//        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #8EB83D; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;"));
//        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #1A1A1A; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;"));
//
//        // Al hacer clic
//        card.setOnMouseClicked(event -> {
//            card.setStyle("-fx-background-color: #8EB83D; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;");
//            mostrarModelos(p);
//        });
//
//        return card;
//    }
    private void mostrarModelos(Marca m){
        marcaSeleccionada = m;
        btnEditarMarca.setDisable(false);
        EliminarMarca.setDisable(false);
        BtnAgregarModelo.setDisable(false);

        lblNombre.setText(m.getNombreMarca());
        llenarTabla(m);
    }

    private void llenarTabla(Marca m){
        int id = m.getMarcaId();
        TablaVehiculoMarca.getItems().clear();
        TablaVehiculoMarca.getItems().addAll(detalleCategoriaService.listarPorMarca(m));
    }

    public void EditarMarca(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/marca/EditarMarca.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent root = loader.load();

            EditarMarcaController controller = loader.getController();
            controller.setMarcaModelos(marcaSeleccionada);

            Stage stage = new Stage();
            stage.setTitle("Editar Marca");
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();
            //cargarMarcas();
            configurarPaginador();
        } catch (IOException ex) {
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

            Stage stage = new Stage();
            stage.setTitle("Editar Modelo");
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();
            //cargarMarcas();

            configurarPaginador();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void AgregarModelo(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/modelo/AgregarModelo.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent root = loader.load();

            AgregarModeloController controller = loader.getController();
            controller.setMarcaModelos(marcaSeleccionada);

            Stage stage = new Stage();
            stage.setTitle("Agregar Modelo");
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();
            //cargarMarcas();
            configurarPaginador();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void EliminarModelo(ActionEvent actionEvent) {

        Modelo modeloSeleccionada = TablaVehiculoMarca.getSelectionModel().getSelectedItem().getModelo();

        if (modeloSeleccionada == null) {
            MensajesAlert.mostrarWarning("Advertencia", "Sin seleccion","Debe seleccionar un modelo para eliminar.");
            return;
        }

        // Evitar eliminar el modelo genérico
        if (modeloSeleccionada.getModeloId() == 1) {
            MensajesAlert.mostrarWarning("Advertencia","Operación no permitida", "No se puede eliminar el modelo genérico (SIN MODELO).");
            return;
        }

        boolean confirmar = MensajesAlert.mostrarConfirmacion(
                "Confirmar Eliminacion",
                "¿Desea eliminar este Modelo?",
                "Se eliminara el modelo seleccionada.",
                "Sí, eliminar",
                "Cancelar"
        );

        if (confirmar) {
            try {
                vehiculoService.reasignarModeloPorId(modeloSeleccionada.getModeloId());
                detalleCategoriaService.reasignarOEliminarPorModelo(modeloSeleccionada.getModeloId());
                vehiculoPromocionService.reasignarModeloPorId(modeloSeleccionada.getModeloId());

                // Eliminar el modelo
                modeloService.eliminarModeloPorId(modeloSeleccionada.getModeloId());


                MensajesAlert.mostrarInformacion("Éxito", "Modelo eliminado", "El modelo se elimino correctamente.");

                txtBuscar.setText("");
                marcaSeleccionada = null;
                btnEditarMarca.setDisable(true);
                EliminarMarca.setDisable(true);
                lblNombre.setText("");
                TablaVehiculoMarca.getItems().clear();
                configurarPaginador();

            } catch (Exception e) {
                MensajesAlert.mostrarError(
                        "Error al eliminar",
                        "No se pudo eliminar el modelo",
                        "Detalles: " + e.getMessage()
                );
                e.printStackTrace();
            }
        }
    }

    public void EliminarMarca(ActionEvent actionEvent){


        if (marcaSeleccionada == null) {
            MensajesAlert.mostrarWarning("Advertencia", "Sin seleccion","Debe seleccionar una marca para eliminar.");
            return;
        }

        // Evitar eliminar el modelo genérico
        if (marcaSeleccionada.getMarcaId() == 1) {
            MensajesAlert.mostrarWarning("Advertencia","Operación no permitida", "No se puede eliminar la marca genérica (SIN MARCA).");
            return;
        }

        boolean confirmar = MensajesAlert.mostrarConfirmacion(
                "Confirmar Eliminacion",
                "¿Desea eliminar esta Marca?",
                "Se eliminara el marca seleccionada.",
                "Sí, eliminar",
                "Cancelar"
        );

        if (confirmar) {
            try {
                vehiculoService.reasignarModeloYCategoriaPorMarca(marcaSeleccionada.getMarcaId());
                vehiculoPromocionService.reasignarModeloYCategoriaPorMarca(marcaSeleccionada.getMarcaId());
                vehiculoService.reasignarMarcaPorId(marcaSeleccionada.getMarcaId());
                vehiculoPromocionService.reasignarMarcaPorId(marcaSeleccionada.getMarcaId());
                modeloService.reasignarMarcaPorId(marcaSeleccionada.getMarcaId());
                detalleCategoriaService.eliminarPorMarca(marcaSeleccionada.getMarcaId());

                // Eliminar marca
                marcaService.eliminarMarcaPorId(marcaSeleccionada.getMarcaId());

                MensajesAlert.mostrarInformacion("Éxito", "Marca eliminada", "La marca se elimino correctamente.");

                txtBuscar.setText("");
                marcaSeleccionada = null;
                btnEditarMarca.setDisable(true);
                EliminarMarca.setDisable(true);
                lblNombre.setText("");
                TablaVehiculoMarca.getItems().clear();
                configurarPaginador();

            } catch (Exception e) {
                MensajesAlert.mostrarError(
                        "Error al eliminar",
                        "No se pudo eliminar la marca",
                        "Detalles: " + e.getMessage()
                );
                e.printStackTrace();
            }
        }

    }

    public void Actualizar(ActionEvent actionEvent) {
        txtBuscar.setText("");
        marcaSeleccionada = null;
        btnEditarMarca.setDisable(true);
        EliminarMarca.setDisable(true);
        lblNombre.setText("");
        TablaVehiculoMarca.getItems().clear();
        //cargarMarcas();
        configurarPaginador();
    }
}
