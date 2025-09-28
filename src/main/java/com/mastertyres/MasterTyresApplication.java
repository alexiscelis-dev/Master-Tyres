package com.mastertyres;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class MasterTyresApplication extends Application {

	private ConfigurableApplicationContext context; //Contenedor de spring
	private static HostServices hostServices;

	@Override
	public void init() throws Exception {
		context = new SpringApplicationBuilder(MasterTyresApplication.class).run();
	}

	public static HostServices getAppHostServices() {
		return hostServices;
	}

	//Mostrar Ventana Principal
	@Override
	public void start(Stage ventanaPrincipal) throws Exception {
		hostServices = getHostServices();


		//Cargar ventana para ajustar tamaño

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/Login.fxml"));

        FXMLLoader  loaderNavigator = new FXMLLoader(getClass().getResource("/fxmlViews/Login.fxml"));


		Parent root = null;
        Parent rooNavigator = null;

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

	}

	@Override
	public void stop() throws Exception {
		context.stop();
	}

	public static void main(String[] args) {
		launch(args);
	}

}//clase