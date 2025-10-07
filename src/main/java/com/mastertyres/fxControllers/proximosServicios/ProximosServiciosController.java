package com.mastertyres.fxControllers.proximosServicios;


import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.common.MensajesAlert;
import com.mastertyres.fxControllers.ClientesPromocionesController.ClientesPromocionesController;
import com.mastertyres.vehiculo.model.VehiculoDTO;
import com.mastertyres.vehiculo.service.VehiculoService;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ProximosServiciosController {


    @FXML
    private TilePane contenedorServicios;

    @FXML private Label lblNombre;
    @FXML private Label lblNumeroTelefono;
    @FXML private Label lblMarca;
    @FXML private Label lblModelo;
    @FXML private Label lblAnio;
    @FXML private Label lblFechaUltimoServicio;



    @FXML
    private Button btnEnviarAviso;
    @FXML
    private Button MarcaServicioRealizado;
    @FXML
    private TextField txtBuscar;

    private VehiculoService vehiculoService;

    private VehiculoDTO vehiculoSeleccion;

    public ProximosServiciosController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    private HostServices hostServices;

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    private void initialize(){

        cargarServicios();

    }//initialize

    @FXML
    private void EnviarAviso(ActionEvent actionEvent){

            String telefono = vehiculoSeleccion.getNumTelefono();
            String mensaje = "Master tyres. \n\n" + "¡Hola " + vehiculoSeleccion.getNombreCliente() + "! Recuerda que te toca hacer el servicio a tu vehiculo 🚗🔥\n\n" +"Tu ultimo servicio fue el:" + vehiculoSeleccion.getUltimoServicio();

            String url = "https://api.whatsapp.com/send?phone=" + telefono + "&text=" +
                    java.net.URLEncoder.encode(mensaje, java.nio.charset.StandardCharsets.UTF_8);

            if (hostServices != null) {
                hostServices.showDocument(url);
            }

    }

    private void cargarServicios() {
        List<VehiculoDTO> vehiculos = vehiculoService.obtenerVehiculosConServicioVencido();
        mostrarServicios(vehiculos);
    }

    private void mostrarServicios(List<VehiculoDTO> vehiculos) {
        contenedorServicios.getChildren().clear();
        for (VehiculoDTO p : vehiculos) {
            VBox card = crearCardVehiculo(p);
            contenedorServicios.getChildren().add(card);
        }
    }

    private VBox crearCardVehiculo(VehiculoDTO p) {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: #1A1A1A; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;");
        card.setPrefSize(500, 100);

        Label lblNombreCliente = new Label( p.getNombreCliente() + " " + p.getApellido() + " " + p.getSegundoApellido() );
        lblNombreCliente.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: white;");

        Label lblNombreMarca  = new Label( p.getNombreMarca());
        lblNombreMarca .setStyle("-fx-text-fill: white;");

        Label lblNombreModelo = new Label( p.getNombreModelo());
        lblNombreModelo.setStyle("-fx-text-fill: white;");

        Label lblAnnio = new Label("" + p.getAnio());
        lblAnnio.setStyle("-fx-text-fill: white;");

        HBox contentBox = new HBox(10, lblNombreMarca, lblNombreModelo, lblAnnio);

        VBox textBox = new VBox(5, lblNombreCliente, contentBox);

        card.getChildren().add(textBox);

        // ========= ESTILOS INTERACTIVOS =========
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #8EB83D; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #1A1A1A; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;"));

        // Al hacer clic
        card.setOnMouseClicked(event -> {
            card.setStyle("-fx-background-color: #8EB83D; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;");
            mostrarDetalleVehiculo(p);
        });

        return card;
    }


    @FXML
    private void ActualizarServicio(ActionEvent actionEvent){

        boolean confirmar = MensajesAlert.mostrarConfirmacion(
                "Confirmar actualizacion.",
                "¿Desea marcar como servicio realizado?",
                "Se modificara el ultimo servicio del vehiculo seleccionado a la fecha de hoy.",
                "Sí, actualizar",
                "Cancelar"
        );

        if (!confirmar) {
            return; // Usuario canceló
        }

        vehiculoService.actualizarUltimoServicio(vehiculoSeleccion.getId());
        cargarServicios();
    }

    private void mostrarDetalleVehiculo(VehiculoDTO p) {

        vehiculoSeleccion = p;

        btnEnviarAviso.setDisable(false);
        MarcaServicioRealizado.setDisable(false);

        lblNombre.setText(p.getNombreCliente() + " " + p.getApellido() + " " + p.getSegundoApellido());
        lblNumeroTelefono.setText(p.getNumTelefono());
        lblMarca.setText(p.getNombreMarca());
        lblModelo.setText(p.getNombreModelo());
        lblAnio.setText(p.getAnio()+"");
        lblFechaUltimoServicio.setText(p.getUltimoServicio());



    }

    private void limpiarDetalleVehiculo() {
        lblNombre.setText("");
        lblNumeroTelefono.setText("");
        lblMarca.setText("");
        lblModelo.setText("");
        lblAnio.setText("");
        lblFechaUltimoServicio.setText("");
        vehiculoSeleccion = null;
        btnEnviarAviso.setDisable(true);
        MarcaServicioRealizado.setDisable(true);
    }

    public void actualizar(ActionEvent actionEvent) {
        txtBuscar.setText("");
        limpiarDetalleVehiculo();
        cargarServicios();
    }

}
