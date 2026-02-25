package com.mastertyres.fxControllers.ProximosServicios;


import com.mastertyres.common.exeptions.VehiculoException;
import com.mastertyres.common.interfaces.ICleanable;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.interfaces.ILoader;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.MensajesAlert;
import com.mastertyres.common.utils.MenuContextSetting;
import com.mastertyres.fxComponents.LoadingComponentController;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.mastertyres.common.utils.MensajesAlert.mostrarError;
import static com.mastertyres.common.utils.MensajesAlert.mostrarInformacion;


@Component
public class ProximosServiciosController implements IFxController, ILoader {


    @FXML
    private AnchorPane ventanaProximosServicios;
    @FXML
    private TilePane contenedorServicios;
    @FXML
    private Label lblNombre;
    @FXML
    private Label lblNumeroTelefono;
    @FXML
    private Label lblMarca;
    @FXML
    private Label lblModelo;
    @FXML
    private Label lblAnio;
    @FXML
    private Label lblFechaUltimoServicio;
    @FXML
    private Label lblMensajesEnviados;
    @FXML
    private Button btnEnviarAviso;
    @FXML
    private Button MarcaServicioRealizado;
    @FXML
    private TextField txtBuscar;

    @Autowired
    private TaskService taskService;

    private VehiculoService vehiculoService;

    private VehiculoDTO vehiculoSeleccion;

    public ProximosServiciosController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    private HostServices hostServices;

    private LoadingComponentController loadingOverlayController;


    private static final int SERVICIOS_POR_PAGINA = 20;

    private int tamañoPagina = 20;

    private String filtroActual = null;

    @FXML
    private Pagination PaginadorServicios;

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    private void initialize() {

        configuraciones();
        listeners();
        cargarServicios();


    }//initialize

    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingOverlayController = loading;
    }

    @Override
    public void configuraciones() {
        MenuContextSetting.disableMenu(ventanaProximosServicios);
    }//configuraciones

    @Override
    public void listeners() {

        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                cargarServicios();
            } else {
                cargarServiciosFiltrados(newValue);
            }
        });

    }//listeners

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
    private void EnviarAviso(ActionEvent actionEvent) {

        if (vehiculoSeleccion == null) return;

        taskService.runTask(
                loadingOverlayController,
                () -> {
                    vehiculoService.actualizarContador(vehiculoSeleccion.getId(), vehiculoSeleccion.getContador_mensaje() + 1);

                    String telefono = vehiculoSeleccion.getNumTelefono();
                    String mensaje = "Master tires. \n\n" + "¡Hola "
                            + vehiculoSeleccion.getNombreCliente() +
                            "! Recuerda hacer el servicio de tu vehiculo "
                            + vehiculoSeleccion.getNombreMarca() + " " + vehiculoSeleccion.getNombreModelo() + " " + vehiculoSeleccion.getAnio()
                            + "🚗🔥\n\n" + "Tu ultimo servicio fue el: " + vehiculoSeleccion.getUltimoServicio();

                    String url = "https://api.whatsapp.com/send?phone=" + telefono + "&text=" +
                            java.net.URLEncoder.encode(mensaje, java.nio.charset.StandardCharsets.UTF_8);


                    return url;
                }, (url) -> {

                    if (hostServices != null) {
                        hostServices.showDocument(url);

                    }

                    txtBuscar.setText("");
                    limpiarDetalleVehiculo();
                    cargarServicios();

                    mostrarInformacion("Aviso enviado",
                            "Aviso enviado con exito",
                            "Presione enviar en cada una de las ventanas de su navegador para completar la operacion.");

                }, (ex) -> {

                    if (ex instanceof VehiculoException){
                        mostrarError("Error al enviar aviso", "No se pudo enviar el aviso ", "" + ex.getMessage());

                    }else {
                        mostrarError("Error interno",
                                "",
                                "Ocurrio un error inesperaro al mandar aviso. Vuelve a intentarlo mas tarde.");
                    }

                }, null
        );


    }//EnviarAviso

    private void cargarServicios() {
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

        Label lblNombreCliente = new Label(
                emptyIfNull(p.getNombreCliente()) + " " +
                        emptyIfNull(p.getApellido()) + " " +
                        emptyIfNull(p.getSegundoApellido())
        );
        lblNombreCliente.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: white;");

        Label lblNombreMarca = new Label(
                naIfNullOrEmpty(p.getNombreMarca())
        );
        lblNombreMarca.setStyle("-fx-text-fill: white;");

        Label lblNombreModelo = new Label(
                naIfNullOrEmpty(p.getNombreModelo())
        );
        lblNombreModelo.setStyle("-fx-text-fill: white;");

        Label lblAnnio = new Label(
                p.getAnio() == null ? "N/A" : String.valueOf(p.getAnio())
        );
        lblAnnio.setStyle("-fx-text-fill: white;");

        HBox contentBox = new HBox(10, lblNombreMarca, lblNombreModelo, lblAnnio);
        VBox textBox = new VBox(5, lblNombreCliente, contentBox);

        card.getChildren().add(textBox);

        card.setOnMouseEntered(e ->
                card.setStyle("-fx-background-color: #8EB83D; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;")
        );

        card.setOnMouseExited(e ->
                card.setStyle("-fx-background-color: #1A1A1A; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;")
        );

        card.setOnMouseClicked(event -> {
            card.setStyle("-fx-background-color: #8EB83D; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;");
            mostrarDetalleVehiculo(p);
        });

        return card;
    }

    private String emptyIfNull(String value) {
        return value == null ? "" : value;
    }

    private String naIfNullOrEmpty(String value) {
        return (value == null || value.trim().isEmpty()) ? "N/A" : value;
    }

    @FXML
    private void ActualizarServicio(ActionEvent actionEvent) {

        boolean confirmar = MensajesAlert.mostrarConfirmacion(
                "Confirmar actualizacion.",
                "¿Desea marcar como servicio realizado?",
                "Se modificara el ultimo servicio del vehiculo seleccionado a la fecha de hoy.",
                "Sí, actualizar",
                "Cancelar"
        );

        if (!confirmar)
            return; // Usuario canceló

        taskService.runTask(
                loadingOverlayController,
                () -> {
                    vehiculoService.actualizarUltimoServicio(vehiculoSeleccion.getId());
                    vehiculoService.actualizarContador(vehiculoSeleccion.getId(), 0);
                    return null;
                }, (resultado) -> {
                    txtBuscar.setText("");
                    limpiarDetalleVehiculo();
                    cargarServicios();
                }, (ex) -> {
                    if (ex instanceof VehiculoException) {
                        mostrarError("Ocurrio un error", "No se pudo marcar como 'servicio realizado' ", "" + ex.getMessage());
                    } else {
                        mostrarError("Error interno",
                                "No se pudo marcar como 'servicio realizado' ",
                                "Ocurrio un error inesperado. Vuelve a intentarlo mas tarde.");
                    }

                }, null
        );


    }//actualizarServicio

    private void mostrarDetalleVehiculo(VehiculoDTO p) {

        vehiculoSeleccion = p;

        btnEnviarAviso.setDisable(false);
        MarcaServicioRealizado.setDisable(false);

        lblNombre.setText(emptyIfNull(p.getNombreCliente()) + " " + emptyIfNull(p.getApellido()) + " " + emptyIfNull(p.getSegundoApellido()));
        lblNumeroTelefono.setText(p.getNumTelefono());
        lblMarca.setText(p.getNombreMarca());
        lblModelo.setText(p.getNombreModelo());
        lblAnio.setText(p.getAnio() + "");
        lblFechaUltimoServicio.setText(formatearFecha(p.getUltimoServicio()));
        lblMensajesEnviados.setText(p.getContador_mensaje() + "");
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
        if (fecha == null || fecha.isBlank()) return "N/D";

        try {
            LocalDate f = LocalDate.parse(fecha);  // yyyy-MM-dd
            return f.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (Exception e) {
            return "N/D";
        }
    }

    public void actualizar(ActionEvent actionEvent) {
        txtBuscar.setText("");
        limpiarDetalleVehiculo();
        cargarServicios();
    }


    public void accionBuscarCliente(ActionEvent actionEvent) {

        String filtro = txtBuscar.getText();


        Page<VehiculoDTO> paginaFiltrada = vehiculoService.BuscarVehiculosConServicioVencidoPaginado(filtro, PageRequest.of(0, tamañoPagina));
        mostrarServicios(paginaFiltrada.getContent());
        PaginadorServicios.setPageCount(paginaFiltrada.getTotalPages());
        PaginadorServicios.setCurrentPageIndex(0);
        PaginadorServicios.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            Page<VehiculoDTO> nuevaPagina = vehiculoService.BuscarVehiculosConServicioVencidoPaginado(filtro, PageRequest.of(newIndex.intValue(), tamañoPagina));
            mostrarServicios(nuevaPagina.getContent());
        });

    }
}//class
