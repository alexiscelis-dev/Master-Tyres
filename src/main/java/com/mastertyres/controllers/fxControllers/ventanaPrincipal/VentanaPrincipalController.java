package com.mastertyres.controllers.fxControllers.ventanaPrincipal;

import com.mastertyres.MasterTyresApplication;
import com.mastertyres.common.exeptions.RespaldoException;
import com.mastertyres.common.interfaces.*;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.ApplicationContextProvider;
import com.mastertyres.common.utils.MensajesAlert;
import com.mastertyres.components.fxComponents.loader.LoadingComponentController;
import com.mastertyres.controllers.fxControllers.ProximosServicios.ProximosServiciosController;
import com.mastertyres.controllers.fxControllers.configuracion.ConfiguracionController;
import com.mastertyres.respaldo.service.IRespaldoService;
import com.mastertyres.user.model.User;
import com.mastertyres.user.service.UserService;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Stack;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.mastertyres.common.utils.MensajesAlert.*;

@Component
public class VentanaPrincipalController implements ILoader, IFxController {


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
    @FXML private StackPane profileContainer;
    @FXML private Circle profileCircle;
    //@FXML private ImageView imgPerfil;
    @FXML public LoadingComponentController loadingOverlayController;
    @FXML private Label lblUsuario;

    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingOverlayController = loading;
    }

    @Autowired
    private ApplicationContext springContex;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    private boolean sidebarVisible = true;
    private double posicionMenu;
    private TranslateTransition transition;
    private TranslateTransition transitionMenu;
    private User userSession;

    // CAMBIO: Usar solo Strings para el historial, no guardar controladores
    public final Stack<String> historialVistas = new Stack<>();
    public final Stack<String> historialNombreVistas = new Stack<>();
    public String vistaActual = null;
    public String NombreVistaActual = null;

    // NUEVO: Referencia al controlador actual para limpiarlo
    private Object controladorActual = null;

    @Autowired
    private IRespaldoService respaldoService; // Agrega esta inyección

    @FXML private ScrollPane scrollPanelMenu;

    @FXML
    public void initialize() {

        configuraciones();
        listeners();
        verificarUltimoRespaldo();

    }//initialize

    private void aplicarEscalaAlContenido(Parent contenido) {
        final double ANCHO_REFERENCIA = 1004.0;
        final double ALTO_REFERENCIA  = 600.0;

        Scale scale = new Scale(1, 1, 0, 0);
        contenido.getTransforms().add(scale);

        // Wrap en Group para que el ScrollPane vea los bounds reales post-escala
        Group group = new Group(contenido);
        panelMenu.getChildren().clear();
        panelMenu.getChildren().add(group);

        Runnable recalcular = () -> {
            double anchoPanel = scrollPanelMenu.getViewportBounds().getWidth();
            double altoPanel  = scrollPanelMenu.getViewportBounds().getHeight();
            if (anchoPanel <= 0 || altoPanel <= 0) return;

            double scaleX = anchoPanel / ANCHO_REFERENCIA;
            double scaleY = altoPanel  / ALTO_REFERENCIA;
            double factor = Math.min(scaleX, scaleY);
            factor = Math.min(factor, 1.25);
            factor = Math.max(factor, 0.6);

            scale.setX(factor);
            scale.setY(factor);
        };

        scrollPanelMenu.viewportBoundsProperty().addListener((obs, o, n) -> recalcular.run());
        recalcular.run();
    }

    private void recalcularEscala(Scale scale, Parent contenido, double anchoPanel, double altoPanel,
                                  double anchoRef, double altoRef) {
        if (anchoPanel <= 0 || altoPanel <= 0) return;

        double scaleX = anchoPanel / anchoRef;
        double scaleY = altoPanel / altoRef;

        // Usar el menor para que quepa sin recortar, máximo 1.0 para no agrandar demasiado
        double factor = Math.min(scaleX, scaleY);
        factor = Math.min(factor, 1.25); // tope máximo
        factor = Math.max(factor, 0.6);  // tope mínimo

        scale.setX(factor);
        scale.setY(factor);

        // Ajustar el tamaño lógico del Pane para que el ScrollPane sepa cuándo poner scrollbars
        panelMenu.setPrefWidth(anchoRef * factor);
        panelMenu.setPrefHeight(altoRef * factor);
    }

    private void updateProfileCircle(String rutaImagen) {
        Image imagen;

        if (rutaImagen != null && !rutaImagen.isBlank()) {
            File file = new File(rutaImagen);
            imagen = file.exists()
                    ? new Image(file.toURI().toString(), 54, 54, false, true)
                    : new Image(getClass().getResource("/icons/user.png").toExternalForm(), 54, 54, false, true);
        } else {
            imagen = new Image(getClass().getResource("/icons/user.png").toExternalForm(), 54, 54, false, true);
        }

        profileCircle.setFill(new ImagePattern(imagen));
    }

    private void verificarUltimoRespaldo() {
        taskService.runTask(
                loadingOverlayController,
                () -> respaldoService.ObtenerUltimoRespaldo(),
                (respaldo) -> {
                    if (respaldo == null) {
                        boolean confirmar = mostrarConfirmacion(
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

                        boolean confirmar = mostrarConfirmacion(
                                "Respaldo pendiente",
                                "Tu último respaldo fue el " + fechaFormateada + ".",
                                "Han pasado " + diasTranscurridos + " días desde tu último respaldo. ¿Deseas realizar uno ahora?",
                                "Sí, crear respaldo",
                                "Ahora no"
                        );
                        if (confirmar) ejecutarRespaldo();
                    }
                },
                (ex) -> mostrarExcepcionThrowable(
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
                (resultado) -> mostrarInformacion(
                        "Respaldo creado",
                        "",
                        "El respaldo se creó exitosamente."
                ),
                (ex) -> {
                    ex.printStackTrace();
                    if (ex instanceof RespaldoException) {
                        mostrarError(
                                "No se pudo crear el respaldo",
                                "Ocurrió un problema al crear el respaldo",
                                ex.getMessage()
                        );
                    } else if (ex instanceof InterruptedException ||
                            ex instanceof java.util.concurrent.CancellationException) {
                        mostrarError("Acción cancelada", "", "Acción cancelada por el usuario.");
                    } else {
                        mostrarError(
                                "Error interno",
                                "",
                                "Ocurrió un error inesperado al crear el respaldo. Vuelva a intentarlo más tarde."
                        );
                    }
                },
                null
        );
    }

    public void refreshUserInfo(User user) {
        this.userSession = user;
        lblUsuario.setText(user.getUsername());
        updateProfileCircle(user.getFotoPerfil());
    }

    private void logOut(MouseEvent event, String archivoFXML) {

        try {

            limpiarControladorActual();

            FXMLLoader loader = new FXMLLoader(VentanaPrincipalController.class.getResource(archivoFXML));

            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);

            Parent root = loader.load();

            Stage ventanaLogin = (Stage) ((Node) event.getSource()).getScene().getWindow();
            ventanaLogin.setScene(new Scene(root));

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            ventanaLogin.setX(screenBounds.getMinX());
            ventanaLogin.setY(screenBounds.getMinY());
            ventanaLogin.setWidth(screenBounds.getWidth());
            ventanaLogin.setHeight(screenBounds.getHeight());

            ventanaLogin.show();

        } catch (IOException e) {
            mostrarExcepcion(
                    "Error de carga",
                    "No se pudo inicializar la interfaz",
                    "Ocurrió un error al intentar cargar la vista. Por favor, inténtelo de nuevo más tarde.",
                    e
            );

        }
    }//logOut

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

            panelMenu.getChildren().clear();

            aplicarEscalaAlContenido(contenido); // él hace el add internamente


            AnchorPane.setTopAnchor(contenido, 0.0);
            AnchorPane.setBottomAnchor(contenido, 0.0);
            AnchorPane.setLeftAnchor(contenido, 0.0);
            AnchorPane.setRightAnchor(contenido, 0.0);

            restaurarEstadoInicial();

        } catch (IOException e) {
            mostrarExcepcion(
                    "Error de carga",
                    "No se pudo inicializar la interfaz",
                    "Ocurrió un error al intentar cargar la vista. Por favor, inténtelo de nuevo más tarde.",
                    e
            );

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

            // ← Inicializar datos si la vista lo requiere
            if (controladorActual instanceof ConfiguracionController config) {
                config.setUser(userSession);
            }

        } else {
            viewContentSinHistorial(null, "/fxmlViews/masterTires/RegresarMenu.fxml", "INICIO");
            cambiarPaginaEtiqueta.setText("INICIO");
        }
    }

    @FXML
    public void irAtras(MouseEvent event) {
        irAtras();
    }



    /*

    public Object viewContent(MouseEvent event, String archivoFXML, String nombreVentana) {
        try {
            // NUEVO: Limpiar controlador anterior antes de cargar nuevo
            limpiarControladorActual();

            if (vistaActual != null) {
                historialVistas.push(vistaActual);
            }
            if (NombreVistaActual != null) {
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

            panelMenu.getChildren().clear();

            aplicarEscalaAlContenido(contenido); // él hace el add internamente

            AnchorPane.setTopAnchor(contenido, 0.0);
            AnchorPane.setBottomAnchor(contenido, 0.0);
            AnchorPane.setLeftAnchor(contenido, 0.0);
            AnchorPane.setRightAnchor(contenido, 0.0);

            restaurarEstadoInicial();

            System.gc();

            return controller;

        } catch (IOException e) {

            mostrarExcepcion(
                    "Error de carga",
                    "No se pudo inicializar la interfaz",
                    "Ocurrió un error al intentar cargar la vista. Por favor, inténtelo de nuevo más tarde.",
                    e
            );

            return null;
        }
    }

     */



    public Object viewContent(MouseEvent event, String archivoFXML, String nombreVentana) {
        // Creamos un future para obtener el controller cuando termine la tarea
        CompletableFuture<Object> future = new CompletableFuture<>();


        taskService.runTask(
                loadingOverlayController,
                () -> {

                    // Carga en segundo plano
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(archivoFXML));
                    loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
                    Parent contenido = loader.load();
                    Object controller = loader.getController();

                    // Retornamos ambos dentro del future
                    future.complete(controller);

                    // Retornamos algo para TaskService
                    return new Object[]{contenido, controller};
                },
                (resultado) -> {
                    // Esto se ejecuta en el hilo de la UI
                    Object[] data = (Object[]) resultado;
                    Parent contenido = (Parent) data[0];
                    Object controller = data[1];

                    // Limpiamos controlador previo
                    limpiarControladorActual();

                    // Guardamos historial
                    if (vistaActual != null) historialVistas.push(vistaActual);
                    if (NombreVistaActual != null) historialNombreVistas.push(NombreVistaActual);

                    // Actualizamos la vista actual
                    vistaActual = archivoFXML;
                    NombreVistaActual = nombreVentana;
                    controladorActual = controller;

                    configurarControlador(controller);

                    // Limpiamos y agregamos nuevo contenido
                    panelMenu.getChildren().clear();

                    aplicarEscalaAlContenido(contenido);


                    AnchorPane.setTopAnchor(contenido, 0.0);
                    AnchorPane.setBottomAnchor(contenido, 0.0);
                    AnchorPane.setLeftAnchor(contenido, 0.0);
                    AnchorPane.setRightAnchor(contenido, 0.0);

                    restaurarEstadoInicial();
                    System.gc();

                },
                (ex) -> {
                    // Manejo de errores
                    mostrarExcepcionThrowable(
                            "Error de carga",
                            "No se pudo inicializar la interfaz",
                            "Ocurrió un error al intentar cargar la vista. Por favor, inténtelo de nuevo más tarde.",
                            (ex instanceof Exception ? (Exception) ex : new Exception(ex))
                    );

                    // Completamos el future con null en caso de error
                    future.complete(null);
                },
                null
        );

        try {
            // Bloqueamos hasta que el controller esté listo
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            mostrarExcepcionThrowable(
                    "Error de carga",
                    "No se pudo inicializar la interfaz",
                    "Ocurrió un error al intentar cargar la vista. Por favor, inténtelo de nuevo más tarde.",
                    e
            );
            return null;
        }
    }



    /*
    public Object viewContent(MouseEvent event, String archivoFXML, String nombreVentana) {
        try {
            // NUEVO: Limpiar controlador anterior antes de cargar nuevo
            limpiarControladorActual();

            if (vistaActual != null) {
                historialVistas.push(vistaActual);
            }
            if (NombreVistaActual != null) {
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

            panelMenu.getChildren().clear();

            aplicarEscalaAlContenido(contenido); // él hace el add internamente

            AnchorPane.setTopAnchor(contenido, 0.0);
            AnchorPane.setBottomAnchor(contenido, 0.0);
            AnchorPane.setLeftAnchor(contenido, 0.0);
            AnchorPane.setRightAnchor(contenido, 0.0);

            restaurarEstadoInicial();

            System.gc();

            return controller;

        } catch (IOException e) {

            mostrarExcepcion(
                    "Error de carga",
                    "No se pudo inicializar la interfaz",
                    "Ocurrió un error al intentar cargar la vista. Por favor, inténtelo de nuevo más tarde.",
                    e
            );

            return null;
        }
    }

     */

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

        if (controller instanceof IVentanaPrincipal) {
            ((IVentanaPrincipal) controller).setVentanaPrincipalController(this);
        }

        if (controller instanceof ProximosServiciosController) {
            ((ProximosServiciosController) controller).setHostServices(MasterTyresApplication.getAppHostServices());
        }

        if (controller instanceof ConfiguracionController){
            ((ConfiguracionController) controller).setUser(this.userSession);
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

            aplicarEscalaAlContenido(contenido);

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

    public LoadingComponentController getLoading() {
        return loadingOverlayController;
    }

    @Override
    public void configuraciones() {

    }

    @Override
    public void listeners() {

        historialVistas.push("/fxmlViews/masterTires/RegresarMenu.fxml");
        historialNombreVistas.push("INICIO");

        HBoxVehiculos.setOnMouseClicked(event -> {
            viewContent(event, "/fxmlViews/vehiculo/Vehiculo.fxml", "Vehiculos");
            cambiarPaginaEtiqueta.setText("VEHICULOS");
        });

        HBoxNotas.setOnMouseClicked(event -> {
            viewContent(event, "/fxmlViews/nota/Nota.fxml", "Notas");
            cambiarPaginaEtiqueta.setText("NOTAS");
        });

        HBoxClientes.setOnMouseClicked(event -> {
            viewContent(event, "/fxmlViews/cliente/Cliente.fxml", "Clientes");
            cambiarPaginaEtiqueta.setText("CLIENTES");
        });

        HBoxPromociones.setOnMouseClicked(event -> {
            viewContent(event, "/fxmlViews/promocion/PromocionesActivas.fxml", "Promociones");
            cambiarPaginaEtiqueta.setText("PROMOCIONES");
        });

        HBoxInventario.setOnMouseClicked(event -> {
            viewContent(event, "/fxmlViews/inventario/Inventario.fxml", "Inventario de llantas");
            cambiarPaginaEtiqueta.setText("INVENTARIO DE LLANTAS");
        });

        HBoxServicios.setOnMouseClicked(event -> {
            viewContent(event, "/fxmlViews/vehiculo/ProximosServicios.fxml", "Proximos Servicios");
            cambiarPaginaEtiqueta.setText("PROXIMOS SERVICIOS");
        });

        HBoxConfiguracion.setOnMouseClicked(event -> {
            ConfiguracionController controller;
            controller = (ConfiguracionController) viewContent(event, "/fxmlViews/configuracion/Configuracion.fxml", "Configuracion");
        //    controller.setUser(userSession);
            cambiarPaginaEtiqueta.setText("CONFIGURACION");

        });

        HBoxLogOut.setOnMouseClicked(event -> {

            boolean logOut = mostrarConfirmacion(
                    "Cerrar sesión",
                    "Se cerrará la sesión actual.",
                    "¿Desea continuar?",
                    "Cerrar sesión",
                    "Cancelar"
            );

            if (!logOut)
                return;


            userSession = null;
            historialVistas.clear();
            historialNombreVistas.clear();
            lblUsuario.setText("");
            //imgPerfil.setImage(new Image(getClass().getResource("/icons/user.png").toExternalForm()));
            updateProfileCircle(null);

             logOut(event, "/fxmlViews/login/login/Login.fxml");

        });

        LogoPrincipal.setOnMouseClicked(event -> {
            regresarInicio("/fxmlViews/masterTires/RegresarMenu.fxml");
            cambiarPaginaEtiqueta.setText("INICIO");
        });



    }

    public void setUser(User user) {
        this.userSession = user;
        lblUsuario.setText(userSession.getUsername());
        updateProfileCircle(userSession.getFotoPerfil());
    }


    public Label getCambiarPaginaEtiqueta() {
        return this.cambiarPaginaEtiqueta;
    }

    public void setCambiarPaginaEtiqueta(final Label cambiarPaginaEtiqueta) {
        this.cambiarPaginaEtiqueta = cambiarPaginaEtiqueta;
    }

}//class