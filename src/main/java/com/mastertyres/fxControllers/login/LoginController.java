package com.mastertyres.fxControllers.login;

import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.utils.MenuContextSetting;
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

import static com.mastertyres.common.utils.MensajesAlert.mostrarError;


@Component
public class LoginController implements IFxController {
    @FXML
    private AnchorPane rootPane;



    @FXML
    private void initialize(){

        configuraciones();
        listeners();

    }//initialize

    @Override
    public void configuraciones(){

        MenuContextSetting.disableMenu(rootPane);
    }//configuraciones

    @Override
    public void listeners() {

    }//listeners

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
            mostrarError("Error de carga","",
                    "Ocurrio un error al cargar la vista. Vuelva a intentarlo mas tarde.");
            e.getMessage();
        }

    }//cambiarVistaPrincipal





}
