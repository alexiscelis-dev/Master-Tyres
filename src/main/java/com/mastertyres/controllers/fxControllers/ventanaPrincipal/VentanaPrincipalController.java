package com.mastertyres.controllers.fxControllers.ventanaPrincipal;

import com.mastertyres.MasterTyresApplication;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.ApplicationContextProvider;
import com.mastertyres.components.fxComponents.LoadingComponentController;
import com.mastertyres.common.interfaces.ILoader;
import com.mastertyres.common.interfaces.ICleanable;
import com.mastertyres.controllers.fxControllers.ProximosServicios.ProximosServiciosController;
import com.mastertyres.common.interfaces.IVentanaPrincipal;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

import java.io.IOException;
import java.util.Stack;

import static com.mastertyres.common.utils.MensajesAlert.mostrarError;


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
    @FXML private ImageView irAtras;
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

    @FXML
    public void initialize() {
        historialVistas.push("/fxmlViews/master_tires/RegresarMenu.fxml");
        historialNombreVistas.push("MENU");

        HBoxLogOut.setOnMouseClicked(event -> logOut(event, "/fxmlViews/login/Login.fxml"));

        HBoxVehiculos.setOnMouseClicked(event -> {
            viewContent(event, "/fxmlViews/vehiculo/Vehiculo.fxml", "Vehiculos");
            cambiarPaginaEtiqueta.setText("VEHICULOS");
        });

        HBoxNotas.setOnMouseClicked(event -> {
            viewContent(event, "/fxmlViews/nota/Nota.fxml","Notas");
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
            viewContent(event, "/fxmlViews/configuracion/Configuracion.fxml", "Configuracion");
            cambiarPaginaEtiqueta.setText("CONFIGURACION");
        });

        LogoPrincipal.setOnMouseClicked(event -> {
            regresarInicio("/fxmlViews/master_tires/RegresarMenu.fxml");
            cambiarPaginaEtiqueta.setText("INICIO");
        });
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
            mostrarError("Error de carga ","","Ocurrio un error al cargar la vista. Vuelva a intentarlo mas tarde.");
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

        } catch (IOException e) {
            mostrarError("Error de carga ","","Ocurrio un error al cargar la vista. Vuelva a intentarlo mas tarde.");
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
            viewContentSinHistorial(null, "/fxmlViews/master_tires/RegresarMenu.fxml", "Inicio");
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

            // NUEVO: Forzar garbage collection (opcional, usar con moderación)
            System.gc();

            return controller;

        } catch (IOException e) {

            mostrarError("Error de carga ","","Ocurrio un error al cargar la vista. Vuelva a intentarlo mas tarde.");
            e.printStackTrace();
            return null;
        }
    }

    // NUEVO: Método para limpiar el controlador actual
    private void limpiarControladorActual() {
        if (controladorActual != null && controladorActual instanceof ICleanable) {
            ((ICleanable) controladorActual).cleanup();
        }
        controladorActual = null;
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

        } catch (Exception e) {
            mostrarError("Error de carga ","","Ocurrio un error al cargar la vista. Vuelva a intentarlo mas tarde.");
            e.printStackTrace();
        }
    }

    public LoadingComponentController getLoading(){
        return loadingOverlayController;
    }
}