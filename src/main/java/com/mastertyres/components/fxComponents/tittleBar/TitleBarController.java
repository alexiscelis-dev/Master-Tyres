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

        configurarBoton(btnMinimizar, false);
        configurarBoton(btnMaximizar, false);
        configurarBoton(btnCerrar, true);

        tittleBar.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                tittleBar.setOnMousePressed(this::capturarPuntoArrastre);
                tittleBar.setOnMouseDragged(this::arrastrarVentana);
            }
        });
    }

    private void configurarBoton(StackPane boton, boolean esCerrar) {

        boton.setPrefWidth(46);
        boton.setPrefHeight(35);
        boton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-background-radius: 4;");

        String colorHover  = esCerrar ? "#c0392b" : "#2a3d12";
        String colorPressed = esCerrar ? "#922b21" : "#1a2a0a";

        boton.setOnMouseEntered(e ->
                boton.setStyle("-fx-background-color: " + colorHover + "; -fx-cursor: hand; -fx-background-radius: 4;")
        );
        boton.setOnMouseExited(e ->
                boton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-background-radius: 4;")
        );
        boton.setOnMousePressed(e ->
                boton.setStyle("-fx-background-color: " + colorPressed + "; -fx-cursor: hand; -fx-background-radius: 4;")
        );
        boton.setOnMouseReleased(e ->
                boton.setStyle("-fx-background-color: " + colorHover + "; -fx-cursor: hand; -fx-background-radius: 4;")
        );
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