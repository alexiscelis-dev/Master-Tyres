package com.mastertyres.fxControllers.ventanaPrincipal;


import com.mastertyres.MasterTyresApplication;
import com.mastertyres.common.ApplicationContextProvider;
import com.mastertyres.fxControllers.AgregarInventario.AgregarInventarioController;
import com.mastertyres.fxControllers.AdministrarMarcasModelosCategorias.AgregarMarcaController;
import com.mastertyres.fxControllers.PromocionActiva.PromocionesActivasController;
import com.mastertyres.fxControllers.agregarCliente.AgregarClienteController;
import com.mastertyres.fxControllers.agregarVehiculo.AgregarVehiculoController;
import com.mastertyres.fxControllers.cliente.ClienteController;
import com.mastertyres.fxControllers.inventario.InventarioController;
import com.mastertyres.fxControllers.ProximosServicios.ProximosServiciosController;
import com.mastertyres.fxControllers.nuevaPromocion.NuevaPromocionController;
import com.mastertyres.fxControllers.nota.NotaController;
import com.mastertyres.fxControllers.nota.NotaController;
import com.mastertyres.fxControllers.nuevaPromocion.NuevaPromocionController;
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
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Stack;

import static com.mastertyres.common.MensajesAlert.mostrarError;


@Component
public class VentanaPrincipalController {
    @FXML private AnchorPane sidebar;
    @FXML private HBox HBoxLogOut;
    @FXML private HBox HBoxNotas;
    @FXML private HBox HBoxVehiculos;
    @FXML private HBox HBoxClientes;
    @FXML private HBox HBoxPromociones;
    @FXML private HBox HBoxInventario;
    @FXML private HBox HBoxServicios;
    @FXML private Pane panelMenu;
    @FXML private ImageView LogoPrincipal;
    @FXML public Label cambiarPaginaEtiqueta;
    @FXML private ImageView irAtras;
    @FXML private ImageView imgPerfil;

    @Autowired
    private ApplicationContext springContex;

    private boolean sidebarVisible = true;
    private double posicionMenu;
    private TranslateTransition transition;
    private TranslateTransition transitionMenu;

    public final Stack<String> historialVistas = new Stack<>();
    public final Stack<String> historialNombreVistas = new Stack<>();
    public String vistaActual = null;
    public String NombreVistaActual = null;



    @FXML
    public void initialize() {
        //viewContent(null, "RegresarMenu.fxml", "Menu");
        historialVistas.push("/fxmlViews/master_tires/RegresarMenu.fxml");
        historialNombreVistas.push("Menu");

        // iconoMenu.setOnMouseClicked(event -> toggleSidebar());
        HBoxLogOut.setOnMouseClicked(event -> logOut(event, "/fxmlViews/login/Login.fxml"));

        HBoxVehiculos.setOnMouseClicked(event -> {
                    viewContent(event, "/fxmlViews/vehiculo/Vehiculo.fxml", "Vehiculos");
                    cambiarPaginaEtiqueta.setText("Vehiculos");

                }
        );

        HBoxNotas.setOnMouseClicked(event -> {
            viewContent(event, "/fxmlViews/nota/Nota.fxml","Notas");
            cambiarPaginaEtiqueta.setText("Notas");
        });


        HBoxClientes.setOnMouseClicked(event -> {
                    viewContent(event, "/fxmlViews/cliente/Cliente.fxml", "Clientes");
                    cambiarPaginaEtiqueta.setText("Clientes");
                }
        );
        HBoxPromociones.setOnMouseClicked(event -> {
                    viewContent(event, "/fxmlViews/promocion/PromocionesActivas.fxml", "Promociones");
                    cambiarPaginaEtiqueta.setText("Promociones");
                }
        );
        HBoxInventario.setOnMouseClicked(event -> {
            viewContent(event, "/fxmlViews/inventario/Inventario.fxml", "Inventario de llantas");
            cambiarPaginaEtiqueta.setText("Inventario de llantas");
        });
        HBoxServicios.setOnMouseClicked(event -> {
            viewContent(event, "/fxmlViews/vehiculo/ProximosServicios.fxml", "Proximos Servicios");
            cambiarPaginaEtiqueta.setText("Proximos Servicios");
        });

        LogoPrincipal.setOnMouseClicked(event -> {
            regresarInicio("/fxmlViews/master_tires/RegresarMenu.fxml");
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


    public void viewContentSinHistorial(MouseEvent event, String archivoFXML, String nombreVentana) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(archivoFXML));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent contenido = loader.load();

            Object controller = loader.getController();
            // asignaciones...
            panelMenu.getChildren().setAll(contenido);
            AnchorPane.setTopAnchor(contenido, 0.0);
            AnchorPane.setBottomAnchor(contenido, 0.0);
            AnchorPane.setLeftAnchor(contenido, 0.0);
            AnchorPane.setRightAnchor(contenido, 0.0);

        } catch (IOException e) {
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
            // Si no hay historial, vuelve al inicio (o a la vista que prefieras)
            viewContentSinHistorial(null, "/fxmlViews/RegresarMenu.fxml", "Inicio");
            cambiarPaginaEtiqueta.setText("Inicio");
        }
    }

    @FXML
    public void irAtras(MouseEvent event) {
        irAtras();
    }

    public Object viewContent(MouseEvent event, String archivoFXML, String nombreVentana) {

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
            if (controller instanceof AgregarVehiculoController) {
                ((AgregarVehiculoController) controller).setVentanaPrincipalController(this);
            }
            if (controller instanceof AgregarClienteController) {
                ((AgregarClienteController) controller).setVentanaPrincipalController(this);
            }
            if (controller instanceof NuevaPromocionController) {
                ((NuevaPromocionController) controller).setVentanaPrincipalController(this);
            }
            if (controller instanceof AgregarInventarioController) {
                ((AgregarInventarioController) controller).setVentanaPrincipalController(this);
            }
            if (controller instanceof AgregarMarcaController) {
                ((AgregarMarcaController) controller).setVentanaPrincipalController(this);
            }
            if (controller instanceof NotaController){
                ((NotaController) controller).setVentanaPrincipalController(this);
            }
            panelMenu.getChildren().clear();
            panelMenu.getChildren().add(contenido);

            AnchorPane.setTopAnchor(contenido, 0.0);
            AnchorPane.setBottomAnchor(contenido, 0.0);
            AnchorPane.setLeftAnchor(contenido, 0.0);
            AnchorPane.setRightAnchor(contenido, 0.0);

            return loader.getController();


        } catch (IOException e) {

            mostrarError("Ocurrio un error","",""+ e.getMessage());
            e.printStackTrace();
            return null;
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
