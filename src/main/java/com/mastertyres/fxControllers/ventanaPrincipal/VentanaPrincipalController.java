package com.mastertyres.fxControllers.ventanaPrincipal;


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
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.stereotype.Component;


import java.io.IOException;


@Component
public class VentanaPrincipalController {
    @FXML
    private AnchorPane sidebar;
    @FXML
    private ImageView iconoMenu;
    @FXML
    private AnchorPane menuPane;
    @FXML
    private HBox HBoxLogOut;
    @FXML
    private HBox HBoxVehiculos;
    @FXML
    private  HBox HBoxClientes;

    private boolean sidebarVisible = true;
    private double posicionMenu;
    private TranslateTransition transition;
    private TranslateTransition transitionMenu;
    private Image iconShow;
    private Image iconHide;



    @FXML
    public void initialize() {
        posicionMenu = menuPane.getLayoutX(); //guarda la posicion original del icono menu

        //Cargar imagenes iconos sidebar
         iconShow = new Image(getClass().getResource("/icons/arrow-right-s-line.png").toExternalForm());
         iconHide = new Image(getClass().getResource("/icons/arrow-left-s-line.png").toExternalForm());



        iconoMenu.setOnMouseClicked(event -> toggleSidebar());
        HBoxLogOut.setOnMouseClicked(event -> logOut(event,"/fxml_views/Login.fxml"));

        HBoxVehiculos.setOnMouseClicked(event -> ventanasSidebar(event, "/fxml_views/Vehiculo.fxml","Vehiculos"));
        HBoxClientes.setOnMouseClicked(event -> ventanasSidebar(event,"/fxml_views/Cliente.fxml","Clientes"));


    }


    private void toggleSidebar() {

        transitionMenu = new TranslateTransition(Duration.millis(300), menuPane);

        posicionMenu = menuPane.getLayoutX();

        try {
            transition = new TranslateTransition(Duration.millis(300), sidebar);


            if (!sidebarVisible) {
                transition.setToX(0);
                sidebarVisible = !sidebarVisible;
                iconoMenu.setImage(iconHide);

                transitionMenu.setToX(2);

            } else {

                transition.setToX(-sidebar.getWidth());
                sidebarVisible = !sidebarVisible;

                menuPane.setLayoutX(posicionMenu);

                iconoMenu.setImage(iconShow);


                transitionMenu.setToX(-280);


            }
            transitionMenu.play();
            transition.play();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }//togglesidebar

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
            e.getMessage();
        }

    }//logOut

    private void ventanasSidebar(MouseEvent event, String archivoFXML, String nombreVentana){

        try {
            Parent root = FXMLLoader.load(VentanaPrincipalController.class.getResource(archivoFXML));
            Stage ventana = (Stage) ((Node) event.getSource()).getScene().getWindow();
            ventana.setScene(new Scene(root));

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            ventana.setX(screenBounds.getMinX());
            ventana.setY(screenBounds.getMinY());
            ventana.setWidth(screenBounds.getWidth());
            ventana.setHeight(screenBounds.getHeight());

            ventana.setTitle(nombreVentana);
            ventana.show();


        } catch (IOException e) {
            e.getMessage();
        }

    }//ventanasSidebar




}//clase
