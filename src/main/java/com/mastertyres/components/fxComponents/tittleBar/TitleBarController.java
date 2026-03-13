package com.mastertyres.components.fxComponents.tittleBar;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class TitleBarController {

    @FXML
    private StackPane tittleBar;
    @FXML private StackPane btnMinimizar;
    @FXML private StackPane btnMaximizar;
    @FXML private StackPane btnCerrar;

    private double dragOffsetX;
    private double dragOffsetY;

    @FXML
    private void initialize() {

        tittleBar.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                //System.out.println("TitleBar conectado a la escena");
                tittleBar.setOnMousePressed(this::capturarPuntoArrastre);
                tittleBar.setOnMouseDragged(this::arrastrarVentana);
            }
        });

    }

    @FXML
    private void minimizar(MouseEvent event) {
        obtenerStage(event).setIconified(true);
    }

    @FXML
    private void maximizarRestaurar(MouseEvent event) {
        Stage stage = obtenerStage(event);
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

        // Detectar si está "maximizado" comparando tamaño actual con el de la pantalla
        boolean estaMaximizado = stage.getWidth() == bounds.getWidth()
                && stage.getHeight() == bounds.getHeight();

        if (estaMaximizado) {
            // Restaurar a tamaño normal centrado
            double w = 1344, h = 600;
            stage.setX(bounds.getMinX() + (bounds.getWidth() - w) / 2);
            stage.setY(bounds.getMinY() + (bounds.getHeight() - h) / 2);
            stage.setWidth(w);
            stage.setHeight(h);
        } else {
            // Maximizar respetando barra de tareas
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
        }
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