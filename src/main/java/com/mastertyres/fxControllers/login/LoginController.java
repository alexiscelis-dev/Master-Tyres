package com.mastertyres.fxControllers.login;

import com.mastertyres.common.MenuContextSetting;
import com.mastertyres.fxControllers.ventanaPrincipal.VentanaPrincipalController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class LoginController {
    private AnchorPane rootPane;



    @FXML
    private void initialize(){
        configuraciones();
    }

    private void configuraciones(){

        MenuContextSetting.disableMenu(rootPane);
    }

    @FXML
    private void logIn(ActionEvent event){
        cambiarVistaPrincipal(event, "/fxmlViews/master_tires/Master Tires Principal.fxml");

    }

    private void cambiarVistaPrincipal(ActionEvent event,String archivoFXML){

        try {
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
            e.getMessage();
        }

    }//cambiarVistaPrincipal





}
