package com.mastertyres;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import static com.mastertyres.common.utils.MensajesAlert.mostrarError;
import static com.mastertyres.common.utils.Setting.setPantallaSize;


@SpringBootApplication
public class MasterTyresApplication extends Application {

	private ConfigurableApplicationContext context; //Contenedor de spring
	private static HostServices hostServices;

	@Override
	public void init(){
		try {
			context = new SpringApplicationBuilder(MasterTyresApplication.class).run();
		} catch (Exception e) {
			Platform.runLater(() ->{
				e.printStackTrace();


				mostrarError("Error de Inicio",
						"No se pudo conectar con la base de datos.",
						"Por favor verifique que el servidor de base de datos esté encendido y configurado correctamente.\n\n" +
								"Si el problema persiste, contacte a soporte técnico. ");
				Platform.exit();
				System.exit(1);

			});
		}

	}

	public static HostServices getAppHostServices() {
		return hostServices;
	}

	//Mostrar Ventana Principal
	@Override
	public void start(Stage ventanaPrincipal) throws Exception {
		hostServices = getHostServices();


		//Cargar ventana para ajustar tamaño

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/login/Login.fxml"));

        FXMLLoader  loaderNavigator = new FXMLLoader(getClass().getResource("/fxmlViews/login/Login.fxml"));


		Parent root = null;
        Parent rooNavigator = null;

		try {
			root = loader.load();

		} catch (Exception e) {
			e.printStackTrace();
		}


		Scene scene = new Scene(root);

        setPantallaSize(ventanaPrincipal,scene,"Master Tyres",true,true,1000,800);


	}

	@Override
	public void stop() throws Exception {
		if (context != null){
			context.stop(); // CIERRA HIKARI para cerrar conexiones en BD
		}

		Platform.exit();
		System.exit(0);

	}

	public static void main(String[] args) {
		launch(args);
	}

}//clase