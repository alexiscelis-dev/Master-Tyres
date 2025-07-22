package com.mastertyres;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MasterTyresApplication extends Application {

    private ConfigurableApplicationContext context; //Contenedor de spring

    @Override
    public void init() throws Exception {
  //      context = new SpringApplicationBuilder(MasterTyresApplication.class).run();
    }

    //Mostrar Ventana Principal
    @Override
    public void start(Stage ventanaPrincipal) throws Exception {
/*
        //Cargar ventana para ajustar tamaño

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml_views/Master_Tyres_Principal.fxml"));

        //Configurar la inyeccion en la clase VentanaPrincipalController
        loader.setControllerFactory(context::getBean);

        Parent root = null;
        try {
            root = loader.load();

        } catch (Exception e) {
            e.printStackTrace();
        }


        Scene scene = new Scene(root);

        ventanaPrincipal.setScene(scene);

        ventanaPrincipal.setTitle("Master Tyres");
        ventanaPrincipal.setMaximized(true);
        ventanaPrincipal.show();
*/
    }

    @Override
    public void stop() throws Exception {
  //      context.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
