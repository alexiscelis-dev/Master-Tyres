package com.mastertyres.fxControllers.cliente;

import com.mastertyres.fxControllers.ventanaPrincipal.VentanaPrincipalController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Component;


import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.IOException;


@Component
public class ClienteController {


    @FXML
    private TableView<String> tablaClientes;

    @FXML
    private TableColumn<String, String> colTipoCliente;

    @FXML
    private TableColumn<String, String> colNombre;

    @FXML
    private TableColumn<String, String> colApellido;

    @FXML
    private TableColumn<String, String> colTelefono;

    @FXML
    private TableColumn<String, String> colEstado;

    @FXML
    private TableColumn<String, String> colCiudad;

    @FXML
    private TableColumn<String, String> colDomicilio;

    @FXML
    private TableColumn<String, String> colCurp;

    @FXML
    private TableColumn<String, String> colVehiculo;


    @FXML
    private void initialize() {
        tablaClientes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colTipoCliente.setMaxWidth(1f * Integer.MAX_VALUE * 10);
        colNombre.setMaxWidth(1f * Integer.MAX_VALUE * 12);
        colApellido.setMaxWidth(1f * Integer.MAX_VALUE * 12);
        colTelefono.setMaxWidth(1f * Integer.MAX_VALUE * 10);
        colEstado.setMaxWidth(1f * Integer.MAX_VALUE * 10);
        colCiudad.setMaxWidth(1f * Integer.MAX_VALUE * 10);
        colDomicilio.setMaxWidth(1f * Integer.MAX_VALUE * 14);
        colCurp.setMaxWidth(1f * Integer.MAX_VALUE * 14);
        colVehiculo.setMaxWidth(1f * Integer.MAX_VALUE * 8);






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
            e.getMessage();
        }

    }//logOut




}//clase
