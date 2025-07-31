package com.mastertyres.fxControllers.ventanaPrincipal;


import com.mastertyres.common.ApplicationContextProvider;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URL;


@Component
public class VentanaPrincipalController {
    @FXML
    private AnchorPane sidebar;
    @FXML
    private HBox HBoxLogOut;
    @FXML
    private HBox HBoxVehiculos;
    @FXML
    private  HBox HBoxClientes;
    @FXML
    private Pane panelMenu;
    @Autowired
    private ApplicationContext springContex;

    private boolean sidebarVisible = true;
    private double posicionMenu;
    private TranslateTransition transition;
    private TranslateTransition transitionMenu;
    private Image iconShow;
    private Image iconHide;




    @FXML
    public void initialize() {



       // iconoMenu.setOnMouseClicked(event -> toggleSidebar());
        HBoxLogOut.setOnMouseClicked(event -> logOut(event,"/fxml_views/Login.fxml"));

        HBoxVehiculos.setOnMouseClicked(event -> viewContent(event, "/fxml_views/Vehiculo.fxml","Vehiculos"));
        HBoxClientes.setOnMouseClicked(event -> viewContent(event,"/fxml_views/Cliente.fxml","Clientes"));



    }



    private void logOut(MouseEvent event, String archivoFXML){

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



    private void viewContent(MouseEvent event, String archivoFXML, String nombreVentana){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(archivoFXML));

            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent contenido = loader.load();
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


}//clase
