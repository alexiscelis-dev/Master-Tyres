package com.mastertyres.fxControllers.login;

import com.mastertyres.fxControllers.ventanaPrincipal.VentanaPrincipalController;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;



@Component
public class LoginController {

    @FXML
    private void logIn(ActionEvent event){
        cambiarVistaPrincipal(event, "/fxml_views/Master_Tyres_Principal.fxml");
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

   /* private void cambiarAVistaPrincipal(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml_views/Master_Tyres_Principal.fxml"));
            Parent root = loader.load();

            Stage ventanaPrincipal = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);

            ventanaPrincipal.setScene(scene);
            ventanaPrincipal.setMaximized(false);
            ventanaPrincipal.setMaximized(true);
            ventanaPrincipal.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


}
