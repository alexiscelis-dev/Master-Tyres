package com.mastertyres.fxControllers.ProximosServicios;


import com.mastertyres.common.MensajesAlert;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.promociones.model.Promocion;
import com.mastertyres.vehiculo.model.VehiculoDTO;
import com.mastertyres.vehiculo.service.VehiculoService;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    @FXML private Label lblMensajesEnviados;
    @FXML private Button btnEnviarAviso;
    @FXML private Button MarcaServicioRealizado;
    @FXML private TextField txtBuscar;

    private VehiculoService vehiculoService;

    private VehiculoDTO vehiculoSeleccion;

    public ProximosServiciosController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    private HostServices hostServices;


    private static final int SERVICIOS_POR_PAGINA = 20;

    private int tamañoPagina = 20;

    private String filtroActual = null;

    @FXML private Pagination PaginadorServicios;

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    private void initialize(){

        cargarServicios();

        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                cargarServicios();
            } else {
                cargarServiciosFiltrados(newValue);
            }
        });

    }//initialize

    private void configurarPaginador() {
        //Page<String> paginaInicial = marcaService.listarNombresMarcas(PageRequest.of(0, tamañoPagina));
        Page<VehiculoDTO> paginaInicial = vehiculoService.obtenerVehiculosConServicioVencidoPaginado(PageRequest.of(0, tamañoPagina));
        mostrarServicios(paginaInicial.getContent());

        PaginadorServicios.setPageCount(paginaInicial.getTotalPages());
        PaginadorServicios.setCurrentPageIndex(0);

        // Evento para cuando el usuario cambia de página
        PaginadorServicios.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            Page<VehiculoDTO> nuevaPagina = vehiculoService.obtenerVehiculosConServicioVencidoPaginado(PageRequest.of(newIndex.intValue(), tamañoPagina));
            mostrarServicios(nuevaPagina.getContent());
        });
    }

    private void configurarPaginadorFiltradas(String filtro) {
        Page<VehiculoDTO> paginaFiltrada = vehiculoService.BuscarVehiculosConServicioVencidoPaginado(filtro, PageRequest.of(0, tamañoPagina));
        mostrarServicios(paginaFiltrada.getContent());
        PaginadorServicios.setPageCount(paginaFiltrada.getTotalPages());
        PaginadorServicios.setCurrentPageIndex(0);
        PaginadorServicios.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            Page<VehiculoDTO> nuevaPagina = vehiculoService.BuscarVehiculosConServicioVencidoPaginado(filtro, PageRequest.of(newIndex.intValue(), tamañoPagina));
            mostrarServicios(nuevaPagina.getContent());
        });
    }
    
    private void cargarServiciosFiltrados(String filtro) {
        configurarPaginadorFiltradas(filtro);
    }

    @FXML
    private void EnviarAviso(ActionEvent actionEvent){

            String telefono = vehiculoSeleccion.getNumTelefono();
            String mensaje = "Master tyres. \n\n" + "¡Hola " + vehiculoSeleccion.getNombreCliente() + "! Recuerda hacer el servicio de tu vehiculo "+ vehiculoSeleccion.getNombreMarca() + " " + vehiculoSeleccion.getNombreModelo() + " " + vehiculoSeleccion.getAnio() + "🚗🔥\n\n" + "Tu ultimo servicio fue el: " + vehiculoSeleccion.getUltimoServicio();

            String url = "https://api.whatsapp.com/send?phone=" + telefono + "&text=" +
                    java.net.URLEncoder.encode(mensaje, java.nio.charset.StandardCharsets.UTF_8);

            if (hostServices != null) {
                hostServices.showDocument(url);
            }

            vehiculoService.actualizarContador(vehiculoSeleccion.getId(), vehiculoSeleccion.getContador_mensaje()+1);
            txtBuscar.setText("");
            limpiarDetalleVehiculo();
            cargarServicios();
    }

    private void cargarServicios() {
//        List<VehiculoDTO> vehiculos = vehiculoService.obtenerVehiculosConServicioVencido();
//        mostrarServicios(vehiculos);
        configurarPaginador();
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
        vehiculoService.actualizarContador(vehiculoSeleccion.getId(), 0);
        txtBuscar.setText("");
        limpiarDetalleVehiculo();
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
        lblFechaUltimoServicio.setText(formatearFecha(p.getUltimoServicio()));
        lblMensajesEnviados.setText(p.getContador_mensaje()+"");
    }

    private void limpiarDetalleVehiculo() {
        lblNombre.setText("");
        lblNumeroTelefono.setText("");
        lblMarca.setText("");
        lblModelo.setText("");
        lblAnio.setText("");
        lblFechaUltimoServicio.setText("");
        lblMensajesEnviados.setText("");
        vehiculoSeleccion = null;
        btnEnviarAviso.setDisable(true);
        MarcaServicioRealizado.setDisable(true);
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

    public void actualizar(ActionEvent actionEvent) {
        txtBuscar.setText("");
        limpiarDetalleVehiculo();
        cargarServicios();
    }

}
