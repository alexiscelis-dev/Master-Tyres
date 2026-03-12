package com.mastertyres.controllers.fxControllers.Promociones;

import com.mastertyres.ClientesPromocion.model.ClientesPromocion;
import com.mastertyres.ClientesPromocion.service.ClientePromocionService;
import com.mastertyres.MasterTyresApplication;
import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.common.exeptions.PromocionException;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.interfaces.ILoader;
import com.mastertyres.common.interfaces.IVentanaPrincipal;
import com.mastertyres.common.service.NotaUtils;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.ApplicationContextProvider;
import com.mastertyres.common.utils.MensajesAlert;
import com.mastertyres.common.utils.MenuContextSetting;
import com.mastertyres.components.fxComponents.loader.LoadingComponentController;
import com.mastertyres.controllers.fxControllers.ventanaPrincipal.VentanaPrincipalController;
import com.mastertyres.detalleCategoria.service.DetalleCategoriaService;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.promociones.model.Promocion;
import com.mastertyres.promociones.model.TipoPromocion;
import com.mastertyres.promociones.service.PromocionService;
import com.mastertyres.vehiculoPromocion.service.VehiculoPromocionService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.mastertyres.common.utils.MensajesAlert.mostrarError;
import static com.mastertyres.common.utils.MensajesAlert.mostrarInformacion;

@Component
public class PromocionesActivasController implements IVentanaPrincipal, IFxController, ILoader{

    @FXML
    private AnchorPane ventanaPromocionesActivas;
    @FXML private TilePane contenedorPromociones;
    @FXML private Label lblNombre;
    @FXML private Label lblDescripcion;
    //@FXML private Label lblTipoDescuento;
    @FXML private Label lblPrecioConDescuento;
    @FXML private Label lblValorDescuento;
    @FXML private Label lblPrecio;
    @FXML private Label lblFechaInicio;
    @FXML private Label lblFechaFin;
    @FXML private Label lblTituloVehiculos;
    @FXML private Label lblTituloClientes;
    @FXML private Button btnAgregarPromocion;
    @FXML private Button btnAgregarClientesPromocion;
    @FXML private Button btnEliminarPromocion;
    @FXML private Button btnEditarPromocion;
    @FXML private Button btnClientesPromocion;
    @FXML private TextField txtBuscar;
    @FXML private ListView<Promocion> listaPromociones;
    @FXML private ListView<String> ListaVehiculosPromocion;
    @FXML private ListView<String> ListaClientesPromocion;
    @FXML private Button btnRefrescar;
    @FXML private Button btnBuscar;

    private VentanaPrincipalController ventanaPrincipalController;

    @Autowired
    private VehiculoPromocionService vehiculoPromocionService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private DetalleCategoriaService detalleCategoriaService;

    @Autowired
    private ClientePromocionService clientePromocionService;
    @Autowired
    private NotaUtils notaUtils;

    private final PromocionService promocionService;

    private Promocion promocionSeleccionada;

    private LoadingComponentController loadingOverlayController;

    public PromocionesActivasController(PromocionService promocionService) {
        this.promocionService = promocionService;
    }

    @Override
    public void setVentanaPrincipalController(VentanaPrincipalController controller) {
        this.ventanaPrincipalController = controller;
    }

    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingOverlayController = loading;
    }

    @FXML
    private void initialize() {

        configuraciones();
        listeners();
        cargarPromociones();

    }//initialize

    @Override
    public void configuraciones() {

        MenuContextSetting.disableMenu(ventanaPromocionesActivas);
        notaUtils.descripcionComponent(btnBuscar,"Buscar");
        notaUtils.descripcionComponent(btnRefrescar,"Refrescar");

    }

    @Override
    public void listeners() {

        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                cargarPromociones();
            } else {
                cargarPromocionesFiltradas(newValue);
            }
        });

        btnAgregarPromocion.setOnAction(event -> agregarPromociones(event));

        btnAgregarClientesPromocion.setOnAction(event -> agregarClientesPromociones(event));

        btnEditarPromocion.setOnAction(event -> abrirVentanaEditarPromocion());

        btnEliminarPromocion.setOnAction(event -> eliminarPromocion());

    }

    @FXML
    private void agregarPromociones(ActionEvent event) {

        ventanaPrincipalController.viewContent(
                null, // no se requiere el MouseEvent
                "/fxmlViews/promocion/NuevaPromocionVehiculos.fxml",
                "Agregar promocion vehiculos"
        );
        ventanaPrincipalController.cambiarPaginaEtiqueta.setText("AGREGAR PROMOCION VEHICULOS");

    }@FXML

    private void agregarClientesPromociones(ActionEvent event) {


        ventanaPrincipalController.viewContent(
                null, // no se requiere el MouseEvent
                "/fxmlViews/promocion/NuevaPromocionCliente.fxml",
                "Agregar promocion cliente"
        );
        ventanaPrincipalController.cambiarPaginaEtiqueta.setText("AGREGAR PROMOCION CLIENTES");

    }

    private void cargarPromocionesFiltradas(String filtro) {
        List<Promocion> promociones = promocionService.buscarPromociones(filtro);
        mostrarPromociones(promociones);
    }

    private void cargarPromociones() {
        List<Promocion> promociones = promocionService.obtenerPromocionesActivas();
        mostrarPromociones(promociones);
    }

    @FXML
    private void AbrirClientesAplicables() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/cliente/ClientesAplicablesPromocion.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent root = loader.load();

            ClientesAplicablesController controller = loader.getController();

            //  Aquí pasas el HostServices desde tu Application
            controller.setHostServices(MasterTyresApplication.getAppHostServices());
            controller.LlenarTabla(promocionSeleccionada);
            controller.setInitializeLoading(loadingOverlayController);

            Stage stage = new Stage(StageStyle.UTILITY);
            stage.setTitle("Clientes aplicables a la promoción");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();
            cargarPromociones();
        } catch (IOException ex) {
          mostrarError("Error de carga","","Ocurrio un error al cargar la vista. Vuelva a intentarlo mas tarde.");
        }
    }

    private void mostrarPromociones(List<Promocion> promociones) {
        contenedorPromociones.getChildren().clear();

        for (Promocion p : promociones) {
            VBox card = crearCardPromocion(p);
            contenedorPromociones.getChildren().add(card);
        }
    }

    private VBox crearCardPromocion(Promocion p) {

        VBox card = new VBox();
        card.setStyle("-fx-background-color: #1A1A1A; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;");
        card.setPrefSize(500, 100);

        // ========= IMAGEN =========
        ImageView imageView;
        try {
            if (p.getImg() != null && !p.getImg().isBlank()) {
                // Intentar crear la imagen desde la ruta
                Image img = new Image("file:" + p.getImg(), 70, 70, true, true);

                // Validar que realmente se haya cargado
                if (img.isError()) {
                    throw new Exception("No se pudo cargar la imagen");
                }

                imageView = new ImageView(img);
            } else {
                throw new Exception("Ruta de imagen nula o vacía");
            }
        } catch (Exception e) {
            // Imagen por defecto si falla
            imageView = new ImageView(
                    new Image(getClass().getResourceAsStream("/icons/imagenPorDefecto.jpg"), 70, 70, true, true)
            );
        }

        imageView.setFitWidth(70);
        imageView.setFitHeight(70);
        imageView.setPreserveRatio(true);


        // ========= TEXTO =========
        Label lblNombre = new Label(p.getNombre());
        lblNombre.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: white;");

        Label lblPrecio = new Label("Precio: $" + p.getPrecio());
        lblPrecio.setStyle("-fx-text-fill: white;");

        Label lblValorDesc;
        if ("PORCENTAJE".equals(p.getTipoDescuento())) {
            lblValorDesc = new Label("Descuento: " + p.getPorcentaje() + "%");
        } else {
            lblValorDesc = new Label("Descuento:  $" + p.getPorcentaje());
        }
        lblValorDesc.setStyle("-fx-text-fill: white;");

        VBox textBox = new VBox(5, lblNombre, lblPrecio, lblValorDesc);

        // ========= CONTENEDOR HORIZONTAL =========
        HBox contentBox = new HBox(10, imageView, textBox);

        card.getChildren().add(contentBox);

        // ========= ESTILOS INTERACTIVOS =========
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #8EB83D; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #1A1A1A; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;"));

        // Al hacer clic
        card.setOnMouseClicked(event -> {
            card.setStyle("-fx-background-color: #8EB83D; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;");
            mostrarDetallePromocion(p);
        });

        return card;
    }

    private void cargarVehiculosPromocion(Integer promocionId) {
        ListaVehiculosPromocion.getItems().clear();

        var vehiculos = vehiculoPromocionService.obtenerVehiculosPorPromocion(promocionId);

        for (var vp : vehiculos) {
            String descripcion = vp.getMarca().getNombreMarca() + " " +
                    //vp.getModelo().getNombreModelo() + " " +
                    obtenerNombreModeloConCategoria(vp.getMarca(), vp.getModelo()) + " " +
                    vp.getAnnio();
            ListaVehiculosPromocion.getItems().add(descripcion);
        }
    }

    private String obtenerNombreModeloConCategoria(Marca marca, Modelo modelo) {
        if (modelo == null) {
            return "";
        }

        String nombreModelo = modelo.getNombreModelo() != null ? modelo.getNombreModelo() : "";
        if (marca == null || marca.getMarcaId() == null) {
            return nombreModelo;
        }

        List<Categoria> categorias = detalleCategoriaService
                .listarCategoriasPorMarcaYModelo(marca.getMarcaId(), modelo.getModeloId());
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

    private void cargarClientesPromocion(Integer promocionId) {

        ListaClientesPromocion.getItems().clear();

        var relaciones = clientePromocionService
                .listarClientesPorPromocion(promocionId);

        for (ClientesPromocion cp : relaciones) {
            Cliente c = cp.getCliente();

            String descripcion = String.join(" ",
                    c.getNombre() != null ? c.getNombre() : "",
                    c.getApellido() != null ? c.getApellido() : "",
                    c.getSegundoApellido() != null ? c.getSegundoApellido() : ""
            ).trim();

            ListaClientesPromocion.getItems().add(
                    descripcion.isBlank() ? "N/A" : descripcion
            );
        }
    }

    @FXML
    private void abrirVentanaEditarPromocion() {

        if (promocionSeleccionada.getTipoPromocion().equals(TipoPromocion.VEHICULO.toString())){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/promocion/EditarPromocion.fxml"));
                loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
                Parent root = loader.load();

                EditarPromocionController controller = loader.getController();
                controller.setPromocion(promocionSeleccionada);
                controller.setInitializeLoading(loadingOverlayController);

                Stage stage = new Stage(StageStyle.UTILITY);
                stage.setTitle("Editar Promoción");
                stage.setScene(new Scene(root));
                stage.setMaximized(false);
                stage.setResizable(false);

                stage.initModality(Modality.APPLICATION_MODAL);

                stage.showAndWait();
                cargarPromociones();
            } catch (IOException ex) {
                mostrarError("Error de carga","","Ocurrio un error al cargar la vista. Vuelva a intentarlo mas tarde.");
            }
        } else if (promocionSeleccionada.getTipoPromocion().equals(TipoPromocion.CLIENTE.toString())){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/promocion/EditarPromocionCliente.fxml"));
                loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
                Parent root = loader.load();

                EditarPromocionControllerCliente controller = loader.getController();
                controller.setPromocion(promocionSeleccionada);

                Stage stage = new Stage(StageStyle.UTILITY);
                stage.setTitle("Editar Promoción");
                stage.setScene(new Scene(root));
                stage.setMaximized(false);
                stage.setResizable(false);

                stage.initModality(Modality.APPLICATION_MODAL);

                stage.showAndWait();
                cargarPromociones();
            } catch (IOException ex) {
                mostrarError("Error de carga","","Ocurrio un error al cargar la vista. Vuelva a intentarlo mas tarde.");
            }
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/promocion/EditarPromocion.fxml"));
                loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
                Parent root = loader.load();

                EditarPromocionController controller = loader.getController();
                controller.setPromocion(promocionSeleccionada);
                controller.setInitializeLoading(loadingOverlayController);

                Stage stage = new Stage(StageStyle.UTILITY);
                stage.setTitle("Editar Promoción");
                stage.setScene(new Scene(root));

                stage.initModality(Modality.APPLICATION_MODAL);

                stage.showAndWait();
                cargarPromociones();
            } catch (IOException ex) {
                mostrarError("Error de carga","","Ocurrio un error al cargar la vista. Vuelva a intentarlo mas tarde.");
            }
        }


    }

    private void mostrarDetallePromocion(Promocion p) {

        promocionSeleccionada = p;

        btnEliminarPromocion.setDisable(false);
        btnEditarPromocion.setDisable(false);
        btnClientesPromocion.setDisable(false);


        lblNombre.setText(p.getNombre());
        lblDescripcion.setText(p.getDescripcion());
        //lblTipoDescuento.setText(p.getTipoDescuento());
        lblValorDescuento.setText(p.getPorcentaje() + "%");
        lblPrecio.setText(p.getPrecio() + "");
        lblPrecioConDescuento.setText("$"+ ( p.getPrecio() - ( (p.getPorcentaje() * p.getPrecio()) / 100 ) ) );
        lblFechaInicio.setText(formatearFecha(p.getFechaInicio()));
        lblFechaFin.setText(formatearFecha(p.getFechaFin()));

        if (p.getTipoPromocion().equals(TipoPromocion.VEHICULO.toString())){
            lblTituloClientes.setManaged(false);
            lblTituloClientes.setVisible(false);
            lblTituloVehiculos.setVisible(true);
            lblTituloVehiculos.setManaged(true);
            ListaClientesPromocion.setVisible(false);
            ListaClientesPromocion.setManaged(false);
            ListaVehiculosPromocion.setVisible(true);
            ListaVehiculosPromocion.setManaged(true);
            cargarVehiculosPromocion(p.getPromocionId());
        }else if (p.getTipoPromocion().equals(TipoPromocion.CLIENTE.toString())){
            lblTituloVehiculos.setVisible(false);
            lblTituloVehiculos.setManaged(false);
            lblTituloClientes.setVisible(true);
            lblTituloClientes.setManaged(true);
            ListaVehiculosPromocion.setVisible(false);
            ListaVehiculosPromocion.setManaged(false);
            ListaClientesPromocion.setVisible(true);
            ListaClientesPromocion.setManaged(true);
            cargarClientesPromocion(p.getPromocionId());
        }else {
            lblTituloClientes.setManaged(false);
            lblTituloClientes.setVisible(false);
            lblTituloVehiculos.setVisible(true);
            lblTituloVehiculos.setManaged(true);
            ListaClientesPromocion.setVisible(false);
            ListaClientesPromocion.setManaged(false);
            ListaVehiculosPromocion.setVisible(true);
            ListaVehiculosPromocion.setManaged(true);
            cargarVehiculosPromocion(p.getPromocionId());
        }
    }

    private String formatearFecha(String fecha) {
        if (fecha == null || fecha.isBlank()) return "N/A";

        try {
            LocalDate f = LocalDate.parse(fecha);  // yyyy-MM-dd
            return f.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (Exception e) {
            return "N/D";
        }
    }

    private void eliminarPromocion() {
        if (promocionSeleccionada != null) {

            //  Confirmación antes de actualizar
            boolean confirmar = MensajesAlert.mostrarConfirmacion(
                    "Confirmar eliminación.",
                    "¿Desea eliminar la promoción?",
                    "Esta accion no podrá deshacerse.",
                    "Sí, eliminar",
                    "Cancelar"
            );


            if (confirmar) {

                taskService.runTask(
                        loadingOverlayController,
                        () ->{
                            promocionService.desactivarPromocion(promocionSeleccionada.getPromocionId());
                            return  null;

                        }, (resultado) ->{
                            cargarPromociones(); // Recargamos la lista para reflejar cambios
                            limpiarDetallePromocion(); // Opcional: limpiar labels después
                            mostrarInformacion("Eliminado", "Promocion Eliminada", "Promocion Eliminada con exito.");
                        }, (ex) ->{

                            if( ex instanceof PromocionException){
                                mostrarError("Error al Eliminar","Ocurrio un error al eliminar la promocion",""+ex.getMessage());
                            }else {
                                mostrarError("Error inesperado","","Ocurrio un error inesperado al eliminar la promocon. Vuelve a intentarlo mas tarde.");
                            }

                        },null
                );


            }//if confirmar
        }

    }

    private void limpiarDetallePromocion() {
        lblNombre.setText("");
        lblDescripcion.setText("");
        lblPrecioConDescuento.setText("");
        lblValorDescuento.setText("");
        lblPrecio.setText("");
        lblFechaInicio.setText("");
        lblFechaFin.setText("");
        ListaVehiculosPromocion.getItems().clear();
        promocionSeleccionada = null;
        btnEliminarPromocion.setDisable(true);
        btnEditarPromocion.setDisable(true);
        btnClientesPromocion.setDisable(true);
    }

    public void actualizar(ActionEvent actionEvent) {
        txtBuscar.setText("");
        limpiarDetallePromocion();
        cargarPromociones();
    }

    public void actualizarOut (){
        actualizar(null);
    }

    public void accionBuscarCliente(ActionEvent actionEvent) {

        String filtro = txtBuscar.getText();
        List<Promocion> promociones = promocionService.buscarPromociones(filtro);
        mostrarPromociones(promociones);

    }
}//class
