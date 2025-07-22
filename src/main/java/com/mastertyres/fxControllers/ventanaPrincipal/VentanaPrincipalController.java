package com.mastertyres.fxControllers.ventanaPrincipal;


import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.springframework.stereotype.Component;

import javax.swing.*;

import java.net.URL;
import java.util.ResourceBundle;


@Component
public class VentanaPrincipalController {
    @FXML
    private AnchorPane sidebar;
    @FXML
    private ImageView iconoMenu;
    @FXML
    AnchorPane menuPane;

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


        sidebar.setTranslateX(0);
        iconoMenu.setOnMouseClicked(event -> toggleSidebar());

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

}//clase
