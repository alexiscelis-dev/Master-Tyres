package com.mastertyres.components.fxComponents.tittleBarModalComponent;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TittleBarModalController {

    @FXML
    private StackPane tittleBar;
    @FXML private StackPane btnCerrar;

    private double dragOffsetX;
    private double dragOffsetY;

    @FXML
    private void initialize() {

        tittleBar.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {

                tittleBar.setOnMousePressed(this::capturarPuntoArrastre);
                tittleBar.setOnMouseDragged(this::arrastrarVentana);
            }
        });

    }


    @FXML
    private void cerrar(MouseEvent event) {
        obtenerStage(event).close();
    }

    private void capturarPuntoArrastre(MouseEvent event) {
        dragOffsetX = event.getSceneX();
        dragOffsetY = event.getSceneY();
    }

    private void arrastrarVentana(MouseEvent event) {
        Stage stage = obtenerStage(event);
        stage.setX(event.getScreenX() - dragOffsetX);
        stage.setY(event.getScreenY() - dragOffsetY);
    }

    private Stage obtenerStage(MouseEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

}//class
