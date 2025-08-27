package com.mastertyres.fxControllers.PromocionActiva;

import com.mastertyres.common.ApplicationContextProvider;
import com.mastertyres.fxControllers.ventanaPrincipal.VentanaPrincipalController;
import com.mastertyres.promociones.model.Promocion;
import com.mastertyres.promociones.service.PromocionService;
import com.mastertyres.vehiculoPromocion.service.VehiculoPromocionService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class PromocionesActivasController {

    @FXML
    private TilePane contenedorPromociones;

    @FXML private Label lblNombre;
    @FXML private Label lblDescripcion;
    @FXML private Label lblTipoDescuento;
    @FXML private Label lblValorDescuento;
    @FXML private Label lblPrecio;
    @FXML private Label lblFechaInicio;
    @FXML private Label lblFechaFin;
    @FXML private Button btnAgregarPromocion;

    @FXML
    private TextField txtBuscar;

    @FXML
    private ListView<Promocion> listaPromociones;

    @FXML
    private ListView<String> ListaVehiculosPromocion;

    private VentanaPrincipalController ventanaPrincipalController;

    @Autowired
    private VehiculoPromocionService vehiculoPromocionService;

    private final PromocionService promocionService;

    public PromocionesActivasController(PromocionService promocionService) {
        this.promocionService = promocionService;
    }

    @FXML
    private void initialize(){
        cargarPromociones();

        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                cargarPromociones();
            } else {
                cargarPromocionesFiltradas(newValue);
            }
        });

        btnAgregarPromocion.setOnAction( event -> agregarPromociones(event));

    }//initialize

    public void setVentanaPrincipalController(VentanaPrincipalController controller) {
        this.ventanaPrincipalController = controller;
    }

    @FXML
    private void agregarPromociones(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml_views/NuevaPromocion.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent root = loader.load();

            Pane panel = ventanaPrincipalController.getPanelMenu();
            panel.getChildren().setAll(root);
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }







    private void cargarPromocionesFiltradas(String filtro) {
        List<Promocion> promociones = promocionService.buscarPromociones(filtro);
        mostrarPromociones(promociones);
    }

    private void cargarPromociones() {
        System.out.println("esta entrando");
        List<Promocion> promociones = promocionService.obtenerPromocionesActivas();
        mostrarPromociones(promociones);
    }

    private void mostrarPromociones(List<Promocion> promociones) {
        System.out.println("Entro e mostrar promociones");
        contenedorPromociones.getChildren().clear();
        for (Promocion p : promociones) {
            System.out.println("Estaentrando al for");
            VBox card = crearCardPromocion(p);
            contenedorPromociones.getChildren().add(card);
        }
    }

    private VBox crearCardPromocion(Promocion p) {
        System.out.println("Entro en crear promociones");
        VBox card = new VBox(5);
        card.setStyle("-fx-background-color: #1A1A1A; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;");
        card.setPrefSize(500, 100);

        Label lblNombre = new Label(p.getNombre());
        lblNombre.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: white;");

        Label lblPrecio = new Label("Precio: $" + p.getPrecio());
        lblPrecio.setStyle("-fx-text-fill: white;");

        Label lblValorDesc;
        if (p.getTipoDescuento().equals("PORCENTAJE")){
            lblValorDesc = new Label("Descuento: " + p.getValorDescuento() + "%");
        }else{
            lblValorDesc = new Label("Descuento: -$" + p.getValorDescuento());
        }

        lblValorDesc.setStyle("-fx-text-fill: white;");



        card.getChildren().addAll(lblNombre, lblPrecio, lblValorDesc);

        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #8EB83D; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #1A1A1A; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;"));

        // Al hacer clic
        card.setOnMouseClicked(event -> {
            card.setStyle("-fx-background-color: #8EB83D; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;"); // se mantiene verde al hacer click
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

    private void mostrarDetallePromocion(Promocion p) {
        //System.out.println("Seleccionaste la promocion: " + p.getNombre());
        lblNombre.setText(p.getNombre());
        lblDescripcion.setText(p.getDescripcion());
        lblTipoDescuento.setText(p.getTipoDescuento());
        lblValorDescuento.setText(p.getValorDescuento()+"");
        lblPrecio.setText(p.getPrecio()+"");
        lblFechaInicio.setText(p.getFechaInicio());
        lblFechaFin.setText(p.getFechaFin());

        cargarVehiculosPromocion(p.getPromocionId());
    }
}
