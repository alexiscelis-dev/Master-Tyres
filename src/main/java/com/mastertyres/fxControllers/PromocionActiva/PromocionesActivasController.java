package com.mastertyres.fxControllers.PromocionActiva;

import com.mastertyres.MasterTyresApplication;
import com.mastertyres.common.utils.ApplicationContextProvider;
import com.mastertyres.common.utils.MensajesAlert;
import com.mastertyres.fxControllers.ClientesPromocionesController.ClientesPromocionesController;
import com.mastertyres.fxControllers.EditarControllers.EditarPromocionController;
import com.mastertyres.fxControllers.ventanaPrincipal.VentanaPrincipalController;
import com.mastertyres.fxControllers.ventanaPrincipal.interfaces.IVentanaPrincipal;
import com.mastertyres.promociones.model.Promocion;
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
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.mastertyres.common.utils.MensajesAlert.mostrarInformacion;

@Component
public class PromocionesActivasController implements IVentanaPrincipal {

    @FXML private TilePane contenedorPromociones;
    @FXML private Label lblNombre;
    @FXML private Label lblDescripcion;
    @FXML private Label lblTipoDescuento;
    @FXML private Label lblValorDescuento;
    @FXML private Label lblPrecio;
    @FXML private Label lblFechaInicio;
    @FXML private Label lblFechaFin;
    @FXML private Button btnAgregarPromocion;
    @FXML private Button btnEliminarPromocion;
    @FXML private Button btnEditarPromocion;
    @FXML private Button btnClientesPromocion;
    @FXML private TextField txtBuscar;
    @FXML private ListView<Promocion> listaPromociones;
    @FXML private ListView<String> ListaVehiculosPromocion;

    private VentanaPrincipalController ventanaPrincipalController;

    @Autowired
    private VehiculoPromocionService vehiculoPromocionService;

    private final PromocionService promocionService;

    private Promocion promocionSeleccionada;

    public PromocionesActivasController(PromocionService promocionService) {
        this.promocionService = promocionService;
    }

    @Override
    public void setVentanaPrincipalController(VentanaPrincipalController controller) {
        this.ventanaPrincipalController = controller;
    }

    @FXML
    private void initialize() {

        cargarPromociones();

        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                cargarPromociones();
            } else {
                cargarPromocionesFiltradas(newValue);
            }
        });

        btnAgregarPromocion.setOnAction(event -> agregarPromociones(event));

        btnEditarPromocion.setOnAction(event -> abrirVentanaEditarPromocion());

        btnEliminarPromocion.setOnAction(event -> eliminarPromocion());


    }//initialize



    @FXML
    private void agregarPromociones(ActionEvent event) {


        ventanaPrincipalController.viewContent(
                null, // no se requiere el MouseEvent
                "/fxmlViews/promocion/NuevaPromocion.fxml",
                "Agregar promocion"
        );
        ventanaPrincipalController.cambiarPaginaEtiqueta.setText("AGREGAR PROMOCION");

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

            ClientesPromocionesController controller = loader.getController();

            //  Aquí pasas el HostServices desde tu Application
            controller.setHostServices(MasterTyresApplication.getAppHostServices());

            controller.LlenarTabla(promocionSeleccionada);

            Stage stage = new Stage();
            stage.setTitle("Clientes aplicables a la promoción");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();
            cargarPromociones();
        } catch (IOException ex) {
            ex.printStackTrace();
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
                    vp.getModelo().getNombreModelo() + " " +
                    vp.getAnnio();
            ListaVehiculosPromocion.getItems().add(descripcion);
        }
    }

    @FXML
    private void abrirVentanaEditarPromocion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/promocion/EditarPromocion.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent root = loader.load();

            EditarPromocionController controller = loader.getController();
            controller.setPromocion(promocionSeleccionada);

            Stage stage = new Stage();
            stage.setTitle("Editar Promoción");
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();
            cargarPromociones();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void mostrarDetallePromocion(Promocion p) {

        promocionSeleccionada = p;

        btnEliminarPromocion.setDisable(false);
        btnEditarPromocion.setDisable(false);
        btnClientesPromocion.setDisable(false);


        lblNombre.setText(p.getNombre());
        lblDescripcion.setText(p.getDescripcion());
        lblTipoDescuento.setText(p.getTipoDescuento());
        lblValorDescuento.setText(p.getPorcentaje() + "%");
        lblPrecio.setText(p.getPrecio() + "");
        lblFechaInicio.setText(formatearFecha(p.getFechaInicio()));
        lblFechaFin.setText(formatearFecha(p.getFechaFin()));

        cargarVehiculosPromocion(p.getPromocionId());
    }

    private String formatearFecha(String fecha) {
        if (fecha == null || fecha.isBlank()) return "N/A";

        try {
            LocalDate f = LocalDate.parse(fecha);  // yyyy-MM-dd
            return f.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (Exception e) {
            return "N/A";
        }
    }

    private void eliminarPromocion() {
        if (promocionSeleccionada != null) {

            //  Confirmación antes de actualizar
            boolean confirmar = MensajesAlert.mostrarConfirmacion(
                    "Confirmar eliminacion.",
                    "¿Desea eliminar la promoción?",
                    "Se eliminara la promoción seleccionada.",
                    "Sí, eliminar",
                    "Cancelar"
            );


            if (confirmar) {

                try {
                    promocionService.desactivarPromocion(promocionSeleccionada.getPromocionId());
                    mostrarInformacion("Eliminado", "Promocion Eliminada", "Promocion Eliminada con exito.");
                } catch (Exception e) {
                    MensajesAlert.mostrarError(
                            "Error al Eliminar",
                            "No se pudo eliminar la promoción",
                            "Detalles: " + e.getMessage()
                    );
                    e.printStackTrace();
                }

                cargarPromociones(); // Recargamos la lista para reflejar cambios
                limpiarDetallePromocion(); // Opcional: limpiar labels después
            }
        }

    }

    private void limpiarDetallePromocion() {
        lblNombre.setText("");
        lblDescripcion.setText("");
        lblTipoDescuento.setText("");
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


}
