package com.mastertyres.components.fxComponents.tittleBar;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class TitleBarController {

    @FXML private ImageView iconoMaximizar;
    @FXML private AnchorPane tittleBar; // Cambiar StackPane por AnchorPane
    @FXML private StackPane btnMinimizar;
    @FXML private StackPane btnMaximizar;
    @FXML private StackPane btnCerrar;

    private double dragOffsetX;
    private double dragOffsetY;

    private boolean maximizado = false;

    private Image imgMaximizar;
    private Image imgRestaurar;



    @FXML
    private void initialize() {

        imgMaximizar = new Image(getClass().getResourceAsStream("/icons/ventana_principal/maximize.png"));
        imgRestaurar = new Image(getClass().getResourceAsStream("/icons/ventana_principal/restaurar.png"));

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
        // Forzar tamaños mínimos para que no colapsen en cambios de resolución
        boton.setMinSize(46, 35);
        boton.setPrefSize(46, 35);

        // Alineación explícita para que el ImageView no se mueva
        boton.setAlignment(javafx.geometry.Pos.CENTER);

        String baseStyle = "-fx-background-radius: 0; -fx-cursor: hand; "; // Radius 0 suele verse mejor en tittlebars
        String colorNormal = "transparent";
        String colorHover  = esCerrar ? "#e81123" : "#2a3d12"; // Rojo estándar de cierre
        String colorPressed = esCerrar ? "#f1707a" : "#1a2a0a";

        boton.setStyle("-fx-background-color: " + colorNormal + "; " + baseStyle);

        boton.setOnMouseEntered(e -> boton.setStyle("-fx-background-color: " + colorHover + "; " + baseStyle));
        boton.setOnMouseExited(e -> boton.setStyle("-fx-background-color: " + colorNormal + "; " + baseStyle));
        boton.setOnMousePressed(e -> boton.setStyle("-fx-background-color: " + colorPressed + "; " + baseStyle));
        boton.setOnMouseReleased(e -> boton.setStyle("-fx-background-color: " + colorHover + "; " + baseStyle));
    }

    @FXML
    private void minimizar(MouseEvent event) {
        obtenerStage(event).setIconified(true);
    }

    @FXML
    private void maximizarRestaurar(MouseEvent event) {
        //Stage stage = (Stage) btnMaximizar.getScene().getWindow();

        if (maximizado) {
            //stage.setMaximized(false);
            iconoMaximizar.setImage(imgMaximizar); // Cambia a ícono de maximizar
            maximizado = false;
        } else {
           // stage.setMaximized(true);
            iconoMaximizar.setImage(imgRestaurar); // Cambia a ícono de restaurar
            maximizado = true;
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