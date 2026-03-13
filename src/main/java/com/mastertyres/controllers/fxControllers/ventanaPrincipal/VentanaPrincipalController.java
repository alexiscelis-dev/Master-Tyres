package com.mastertyres.controllers.fxControllers.ventanaPrincipal;

import com.mastertyres.MasterTyresApplication;
import com.mastertyres.common.exeptions.RespaldoException;
import com.mastertyres.common.interfaces.IRestaurableDatos;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.ApplicationContextProvider;
import com.mastertyres.common.utils.MensajesAlert;
import com.mastertyres.components.fxComponents.LoadingComponentController;
import com.mastertyres.common.interfaces.ILoader;
import com.mastertyres.common.interfaces.ICleanable;
import com.mastertyres.controllers.fxControllers.ProximosServicios.ProximosServiciosController;
import com.mastertyres.common.interfaces.IVentanaPrincipal;
import com.mastertyres.respaldo.service.IRespaldoService;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import java.io.IOException;
import java.util.Stack;

@Component
public class VentanaPrincipalController implements ILoader {

    @FXML private AnchorPane sidebar;
    @FXML private HBox HBoxLogOut;
    @FXML private HBox HBoxNotas;
    @FXML private HBox HBoxVehiculos;
    @FXML private HBox HBoxClientes;
    @FXML private HBox HBoxPromociones;
    @FXML private HBox HBoxInventario;
    @FXML private HBox HBoxServicios;
    @FXML private HBox HBoxConfiguracion;
    @FXML private Pane panelMenu;
    @FXML private ImageView LogoPrincipal;
    @FXML public Label cambiarPaginaEtiqueta;
    //@FXML private ImageView irAtras;
    @FXML private Button IrAtras;
    @FXML private ImageView imgPerfil;
    @FXML public LoadingComponentController loadingOverlayController;

    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingOverlayController = loading;
    }

    @Autowired
    private ApplicationContext springContex;
    @Autowired
    private TaskService taskService;

    private boolean sidebarVisible = true;
    private double posicionMenu;
    private TranslateTransition transition;
    private TranslateTransition transitionMenu;

    // CAMBIO: Usar solo Strings para el historial, no guardar controladores
    public final Stack<String> historialVistas = new Stack<>();
    public final Stack<String> historialNombreVistas = new Stack<>();
    public String vistaActual = null;
    public String NombreVistaActual = null;

    // NUEVO: Referencia al controlador actual para limpiarlo
    private Object controladorActual = null;

    @Autowired
    private IRespaldoService respaldoService; // Agrega esta inyección

    @FXML
    public void initialize() {
        historialVistas.push("/fxmlViews/masterTires/RegresarMenu.fxml");
        historialNombreVistas.push("INICIO");

        HBoxLogOut.setOnMouseClicked(event -> logOut(event, "/fxmlViews/login/Login.fxml"));

        HBoxVehiculos.setOnMouseClicked(event -> {
            viewContent(event, "/fxmlViews/vehiculo/Vehiculo.fxml", "VEHICULOS");
            cambiarPaginaEtiqueta.setText("VEHICULOS");
        });

        HBoxNotas.setOnMouseClicked(event -> {
            viewContent(event, "/fxmlViews/nota/Nota.fxml","NOTAS");
            cambiarPaginaEtiqueta.setText("NOTAS");
        });

        HBoxClientes.setOnMouseClicked(event -> {
            viewContent(event, "/fxmlViews/cliente/Cliente.fxml", "CLIENTES");
            cambiarPaginaEtiqueta.setText("CLIENTES");
        });

        HBoxPromociones.setOnMouseClicked(event -> {
            viewContent(event, "/fxmlViews/promocion/PromocionesActivas.fxml", "PROMOCIONES");
            cambiarPaginaEtiqueta.setText("PROMOCIONES");
        });

        HBoxInventario.setOnMouseClicked(event -> {
            viewContent(event, "/fxmlViews/inventario/Inventario.fxml", "INVENTARIO DE LLANTAS");
            cambiarPaginaEtiqueta.setText("INVENTARIO DE LLANTAS");
        });

        HBoxServicios.setOnMouseClicked(event -> {
            viewContent(event, "/fxmlViews/vehiculo/ProximosServicios.fxml", "PROXIMOS SERVICIOS");
            cambiarPaginaEtiqueta.setText("PROXIMOS SERVICIOS");
        });

        HBoxConfiguracion.setOnMouseClicked(event -> {
            viewContent(event, "/fxmlViews/configuracion/Configuracion.fxml", "CONFIGURACION");
            cambiarPaginaEtiqueta.setText("CONFIGURACION");
        });

        LogoPrincipal.setOnMouseClicked(event -> {
            regresarInicio("/fxmlViews/masterTires/RegresarMenu.fxml");
            cambiarPaginaEtiqueta.setText("INICIO");
        });

        verificarUltimoRespaldo();
    }

    private void verificarUltimoRespaldo() {
        taskService.runTask(
                loadingOverlayController,
                () -> respaldoService.ObtenerUltimoRespaldo(),
                (respaldo) -> {
                    if (respaldo == null) {
                        boolean confirmar = MensajesAlert.mostrarConfirmacion(
                                "Sin respaldos registrados",
                                "No se encontró ningún respaldo previo.",
                                "Se recomienda crear un respaldo de seguridad. ¿Desea realizarlo ahora?",
                                "Sí, crear respaldo",
                                "Ahora no"
                        );
                        if (confirmar) ejecutarRespaldo();
                        return;
                    }

                    DateTimeFormatter formatoMySQL = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime fechaRespaldo = LocalDateTime.parse(respaldo.getCreatedAt(), formatoMySQL);

                    long diasTranscurridos = ChronoUnit.DAYS.between(fechaRespaldo, LocalDateTime.now());

                    if (diasTranscurridos >= 7) {
                        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        String fechaFormateada = fechaRespaldo.format(formato);

                        boolean confirmar = MensajesAlert.mostrarConfirmacion(
                                "Respaldo pendiente",
                                "Tu último respaldo fue el " + fechaFormateada + ".",
                                "Han pasado " + diasTranscurridos + " días desde tu último respaldo. ¿Deseas realizar uno ahora?",
                                "Sí, crear respaldo",
                                "Ahora no"
                        );
                        if (confirmar) ejecutarRespaldo();
                    }
                },
                (ex) -> MensajesAlert.mostrarExcepcionThrowable(
                        "Error al verificar respaldo",
                        "No se pudo verificar el estado del último respaldo",
                        "Ocurrió un error al consultar el historial de respaldos.",
                        ex
                ),
                null
        );
    }

    private void ejecutarRespaldo() {
        taskService.runTask(
                loadingOverlayController,
                () -> {
                    respaldoService.generarRespaldo();
                    return null;
                },
                (resultado) -> MensajesAlert.mostrarInformacion(
                        "Respaldo creado",
                        "",
                        "El respaldo se creó exitosamente."
                ),
                (ex) -> {
                    if (ex instanceof RespaldoException) {
                        MensajesAlert.mostrarError(
                                "No se pudo crear el respaldo",
                                "Ocurrió un problema al crear el respaldo",
                                ex.getMessage()
                        );
                    } else if (ex instanceof InterruptedException ||
                            ex instanceof java.util.concurrent.CancellationException) {
                        MensajesAlert.mostrarError("Acción cancelada", "", "Acción cancelada por el usuario.");
                    } else {
                        MensajesAlert.mostrarError(
                                "Error interno",
                                "",
                                "Ocurrió un error inesperado al crear el respaldo. Vuelva a intentarlo más tarde."
                        );
                    }
                },
                null
        );
    }

    private void logOut(MouseEvent event, String archivoFXML) {
        try {
            // NUEVO: Limpiar antes de salir
            limpiarControladorActual();

            Parent root = FXMLLoader.load(VentanaPrincipalController.class.getResource(archivoFXML));
            Stage ventanaLogin = (Stage) ((Node) event.getSource()).getScene().getWindow();
            ventanaLogin.setScene(new Scene(root));

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            ventanaLogin.setX(screenBounds.getMinX());
            ventanaLogin.setY(screenBounds.getMinY());
            ventanaLogin.setWidth(screenBounds.getWidth());
            ventanaLogin.setHeight(screenBounds.getHeight());

            ventanaLogin.show();
        } catch (IOException e) {
            MensajesAlert.mostrarExcepcion(
                    "Error de carga",
                    "No se pudo inicializar la interfaz",
                    "Ocurrió un error al intentar cargar la vista. Por favor, inténtelo de nuevo más tarde.",
                    e
            );
            e.printStackTrace();
        }
    }

    public void viewContentSinHistorial(MouseEvent event, String archivoFXML, String nombreVentana) {
        try {
            // NUEVO: Limpiar controlador anterior
            limpiarControladorActual();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(archivoFXML));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent contenido = loader.load();

            Object controller = loader.getController();
            controladorActual = controller;

            configurarControlador(controller);

            // NUEVO: Limpiar el panel antes de agregar nuevo contenido
            panelMenu.getChildren().clear();
            panelMenu.getChildren().add(contenido);

            AnchorPane.setTopAnchor(contenido, 0.0);
            AnchorPane.setBottomAnchor(contenido, 0.0);
            AnchorPane.setLeftAnchor(contenido, 0.0);
            AnchorPane.setRightAnchor(contenido, 0.0);

            restaurarEstadoInicial();

        } catch (IOException e) {
            MensajesAlert.mostrarExcepcion(
                    "Error de carga",
                    "No se pudo inicializar la interfaz",
                    "Ocurrió un error al intentar cargar la vista. Por favor, inténtelo de nuevo más tarde.",
                    e
            );
            e.printStackTrace();
        }
    }

    public void irAtras() {
        if (!historialVistas.isEmpty() && !historialNombreVistas.isEmpty()) {
            String vistaAnterior = historialVistas.pop();
            String nombreAnterior = historialNombreVistas.pop();

            vistaActual = vistaAnterior;
            NombreVistaActual = nombreAnterior;

            viewContentSinHistorial(null, vistaAnterior, nombreAnterior);
            cambiarPaginaEtiqueta.setText(nombreAnterior);
        } else {
            viewContentSinHistorial(null, "/fxmlViews/masterTires/RegresarMenu.fxml", "INICIO");
            cambiarPaginaEtiqueta.setText("INICIO");
        }
    }

    @FXML
    public void irAtras(MouseEvent event) {
        irAtras();
    }

    public Object viewContent(MouseEvent event, String archivoFXML, String nombreVentana) {
        try {
            // NUEVO: Limpiar controlador anterior antes de cargar nuevo
            limpiarControladorActual();

            if (vistaActual != null){
                historialVistas.push(vistaActual);
            }
            if (NombreVistaActual != null){
                historialNombreVistas.push(NombreVistaActual);
            }

            vistaActual = archivoFXML;
            NombreVistaActual = nombreVentana;

            FXMLLoader loader = new FXMLLoader(getClass().getResource(archivoFXML));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent contenido = loader.load();
            Object controller = loader.getController();

            // NUEVO: Guardar referencia al controlador actual
            controladorActual = controller;

            configurarControlador(controller);

            // NUEVO: Limpiar completamente antes de agregar
            panelMenu.getChildren().clear();
            panelMenu.getChildren().add(contenido);

            AnchorPane.setTopAnchor(contenido, 0.0);
            AnchorPane.setBottomAnchor(contenido, 0.0);
            AnchorPane.setLeftAnchor(contenido, 0.0);
            AnchorPane.setRightAnchor(contenido, 0.0);

            restaurarEstadoInicial();

            System.gc();

            return controller;

        } catch (IOException e) {

            MensajesAlert.mostrarExcepcion(
                    "Error de carga",
                    "No se pudo inicializar la interfaz",
                    "Ocurrió un error al intentar cargar la vista. Por favor, inténtelo de nuevo más tarde.",
                    e
            );
            e.printStackTrace();
            return null;
        }
    }

    private void limpiarControladorActual() {
        if (controladorActual != null && controladorActual instanceof ICleanable) {
            ((ICleanable) controladorActual).cleanup();
        }
        controladorActual = null;
    }

    private void restaurarEstadoInicial() {
        if (controladorActual instanceof IRestaurableDatos) {
            ((IRestaurableDatos) controladorActual).restaurarEstadoInicial();
        }
    }

    public void configurarControlador(Object controller) {
        if (controller instanceof ILoader) {
            ((ILoader) controller).setInitializeLoading(this.loadingOverlayController);
        }

        if (controller instanceof IVentanaPrincipal){
            ((IVentanaPrincipal) controller).setVentanaPrincipalController(this);
        }

        if (controller instanceof ProximosServiciosController) {
            ((ProximosServiciosController) controller).setHostServices(MasterTyresApplication.getAppHostServices());
        }
    }

    public Pane getPanelMenu() {
        return panelMenu;
    }

    private void regresarInicio(String archivoFXML) {
        try {
            // NUEVO: Limpiar antes de regresar al inicio
            limpiarControladorActual();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(archivoFXML));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent contenido = loader.load();

            controladorActual = loader.getController();
            configurarControlador(controladorActual);

            panelMenu.getChildren().clear();
            panelMenu.getChildren().add(contenido);

            restaurarEstadoInicial();

        } catch (Exception e) {
            MensajesAlert.mostrarExcepcion(
                    "Error de carga",
                    "No se pudo inicializar la interfaz",
                    "Ocurrió un error al intentar cargar la vista. Por favor, inténtelo de nuevo más tarde.",
                    e
            );
            e.printStackTrace();
        }
    }

    public LoadingComponentController getLoading(){
        return loadingOverlayController;
    }
}