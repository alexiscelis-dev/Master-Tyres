package com.mastertyres.common.utils;

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
        stage.setResizable(resizable);

        if (maximized) {
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());

        } else {
            stage.setWidth(width);
            stage.setHeight(height);
            stage.setMinWidth(width);
            stage.setMinHeight(height);
        }

        stage.show();
    }

}//class
