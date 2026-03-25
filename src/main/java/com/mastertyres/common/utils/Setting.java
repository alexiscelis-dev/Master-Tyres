package com.mastertyres.common.utils;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

//Esta clase sirve para establecer configuraciones de la aplicacion como el tamaño de la pantalla
//Ayuda a no tener codigo repetido

public class Setting {


    public static void setPantallaSize(Stage stage, Scene scene, String titulo,
                                       boolean maximized, boolean resizable, int width, int height) {
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.setTitle(titulo);
        stage.setResizable(false); // siempre false

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());

        // Evitar que se pueda mover o redimensionar
        stage.setMinWidth(bounds.getWidth());
        stage.setMinHeight(bounds.getHeight());
        stage.setMaxWidth(bounds.getWidth());
        stage.setMaxHeight(bounds.getHeight());

        stage.show();

        // === Avisar al splash que la ventana ya está lista ===
        // === Avisar al splash cuando la ventana ya está pintada ===
        Platform.runLater(() -> {
            try {
                new java.io.File("C:\\MasterTires\\app_ready.flag").createNewFile();
            } catch (Exception ignored) {}
        });
    }

}//class
