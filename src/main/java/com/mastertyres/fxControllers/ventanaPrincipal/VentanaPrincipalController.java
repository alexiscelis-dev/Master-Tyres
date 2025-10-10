package com.mastertyres.fxControllers.ventanaPrincipal;


import com.mastertyres.MasterTyresApplication;
import com.mastertyres.common.ApplicationContextProvider;
import com.mastertyres.fxControllers.PromocionActiva.PromocionesActivasController;
import com.mastertyres.fxControllers.cliente.ClienteController;
import com.mastertyres.fxControllers.inventario.InventarioController;
import com.mastertyres.fxControllers.proximosServicios.ProximosServiciosController;
import com.mastertyres.fxControllers.vehiculo.VehiculoController;
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
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Stack;


@Component
public class VentanaPrincipalController {
    @FXML
    private AnchorPane sidebar;
    @FXML
    private HBox HBoxLogOut;
    @FXML
    private HBox HBoxNotas;
    @FXML
    private HBox HBoxVehiculos;
    @FXML
    private HBox HBoxClientes;
    @FXML
    private HBox HBoxPromociones;
    @FXML
    private HBox HBoxInventario;
    @FXML
    private HBox HBoxServicios;
    @FXML
    private Pane panelMenu;
    @FXML
    private ImageView LogoPrincipal;
    @FXML
    public Label cambiarPaginaEtiqueta;
    @FXML
    private ImageView irAtras;
    @FXML
    private ImageView imgPerfil;

    @Autowired
    private ApplicationContext springContex;

    private boolean sidebarVisible = true;
    private double posicionMenu;
    private TranslateTransition transition;
    private TranslateTransition transitionMenu;

    private final Stack<String> historialVistas = new Stack<>();
    private final Stack<String> historialNombreVistas = new Stack<>();
    private String vistaActual = null;
    private String NombreVistaActual = null;



    @FXML
    public void initialize() {


        // iconoMenu.setOnMouseClicked(event -> toggleSidebar());
        HBoxLogOut.setOnMouseClicked(event -> logOut(event, "/fxmlViews/Login.fxml"));

        HBoxVehiculos.setOnMouseClicked(event -> {
                    viewContent(event, "/fxmlViews/Vehiculo.fxml", "Vehiculos");
                    cambiarPaginaEtiqueta.setText("Vehiculos");

                }
        );

        HBoxNotas.setOnMouseClicked(event -> {
            viewContent(event,"/fxmlViews/Nota.fxml","Notas");
            cambiarPaginaEtiqueta.setText("Notas");
        });


        HBoxClientes.setOnMouseClicked(event -> {
                    viewContent(event, "/fxmlViews/Cliente.fxml", "Clientes");
                    cambiarPaginaEtiqueta.setText("Clientes");
                }
        );
        HBoxPromociones.setOnMouseClicked(event -> {
                    viewContent(event, "/fxmlViews/PromocionesActivas.fxml", "Promociones");
                    cambiarPaginaEtiqueta.setText("Promociones");
                }
        );
        HBoxInventario.setOnMouseClicked(event -> {
            viewContent(event, "/fxmlViews/Inventario.fxml", "Inventario de llantas");
            cambiarPaginaEtiqueta.setText("Inventario de llantas");
        });
        HBoxServicios.setOnMouseClicked(event -> {
            viewContent(event, "/fxmlViews/ProximosServicios.fxml", "Proximos Servicios");
            cambiarPaginaEtiqueta.setText("Proximos Servicios");
        });

        LogoPrincipal.setOnMouseClicked(event -> {
            regresarInicio("/fxmlViews/RegresarMenu.fxml");
            cambiarPaginaEtiqueta.setText("Inicio");
        });




    }


    private void logOut(MouseEvent event, String archivoFXML) {

        try {
            Parent root = FXMLLoader.load(VentanaPrincipalController.class.getResource(archivoFXML));
            Stage ventanaLogin = (Stage) ((Node) event.getSource()).getScene().getWindow();
            ventanaLogin.setScene(new Scene(root));


            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds(); //Se ajusta la pantalla al tamaño maximo

            ventanaLogin.setX(screenBounds.getMinX());
            ventanaLogin.setY(screenBounds.getMinY());
            ventanaLogin.setWidth(screenBounds.getWidth());
            ventanaLogin.setHeight(screenBounds.getHeight());


            ventanaLogin.show();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }//logOut


    @FXML
    private void irAtras(MouseEvent event) {
        if (!historialVistas.isEmpty()) {
            String vistaAnterior = historialVistas.pop();
            vistaActual = vistaAnterior;
            viewContent(event, vistaAnterior, historialNombreVistas.pop());
            cambiarPaginaEtiqueta.setText(historialNombreVistas.pop());
        }
    }


    private void viewContent(MouseEvent event, String archivoFXML, String nombreVentana) {

        try {

            if (vistaActual != null) {
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

//verifica a que instancia pertenece el objeto para mostrar un panel(evita el error NullPointerException) por mandar un Panel null
            if (controller instanceof PromocionesActivasController) {
                ((PromocionesActivasController) controller).setVentanaPrincipalController(this);
            }
            if (controller instanceof ClienteController) {
                ((ClienteController) controller).setVentanaPrincipalController(this);
            }
            if (controller instanceof VehiculoController) {
                ((VehiculoController) controller).setVentanaPrincipalController(this);
            }
            if (controller instanceof InventarioController) {
                ((InventarioController) controller).setVentanaPrincipalController(this);
            }
            if (controller instanceof ProximosServiciosController) {
                ((ProximosServiciosController) controller).setHostServices(MasterTyresApplication.getAppHostServices());
            }
            panelMenu.getChildren().clear();
            panelMenu.getChildren().add(contenido);

            AnchorPane.setTopAnchor(contenido, 0.0);
            AnchorPane.setBottomAnchor(contenido, 0.0);
            AnchorPane.setLeftAnchor(contenido, 0.0);
            AnchorPane.setRightAnchor(contenido, 0.0);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }//ventanasSidebar


    public Pane getPanelMenu() {
        return panelMenu;
    }

    private void regresarInicio(String archivoFXML) {
        try {


            FXMLLoader loader = new FXMLLoader(getClass().getResource(archivoFXML));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent contenido = loader.load();
            panelMenu.getChildren().clear();
            panelMenu.getChildren().add(contenido);

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch

    }//regresarInicio



}//clase
