package com.mastertyres.common.utils;

import javafx.scene.Scene;
import javafx.stage.Stage;

//Esta clase sirve para establecer configuraciones de la aplicacion como el tamaño de la pantalla
//Ayuda a no tener codigo repetido

public class Setting {


    public static void setPantallaSize(Stage stage, Scene scene, String titulo, boolean maximized, boolean resizable, int width, int height){

        stage.setScene(scene);
        stage.setTitle(titulo);
        stage.setMaximized(maximized);
        stage.setResizable(resizable);
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setMinWidth(width);
        stage.setMinHeight(height);
        stage.show();


    }//setPantallaSize

}//class
