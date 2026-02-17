package com.mastertyres.fxControllers.Configuracion;

import com.mastertyres.common.exeptions.RespaldoException;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.interfaces.ILoader;
import com.mastertyres.common.interfaces.IVentanaPrincipal;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.fxComponents.LoadingComponentController;
import com.mastertyres.fxControllers.ventanaPrincipal.VentanaPrincipalController;
import com.mastertyres.respaldo.service.RespaldoService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.mastertyres.common.utils.MensajesAlert.mostrarError;
import static com.mastertyres.common.utils.MensajesAlert.mostrarInformacion;

@Component
public class ConfiguracionController implements IVentanaPrincipal, IFxController, ILoader {
    @FXML
    private Button btnRespaldo;

    @Autowired
    private RespaldoService respaldoService;
    @Autowired
    private TaskService taskService;


    private LoadingComponentController loadingComponentController;
    private VentanaPrincipalController ventanaPrincipalController;

    @FXML
    private void initialize() {
        configuraciones();
        listeners();

    }//initialize

    @Override
    public void configuraciones() {

    }

    @Override
    public void listeners() {
        btnRespaldo.setOnAction(event -> crearRespaldo());

    }

    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingComponentController = loading;
    }

    @Override
    public void setVentanaPrincipalController(VentanaPrincipalController controller) {
        this.ventanaPrincipalController = controller;
    }


    private void crearRespaldo() {
        taskService.runTask(
                loadingComponentController,
                () -> {
                    respaldoService.generarRespaldo();
                    return null;
                }, (resultado) -> {
                    mostrarInformacion("Respaldo creado","","El respaldo se creo exitosamente.");
                }, (ex) -> {
                    if (ex instanceof RespaldoException){
                        mostrarError("No se pudo crear respaldo",
                                "Ocurrio un problema al crear el respaldo",
                                ""+ex.getMessage());
                    } else if (ex instanceof InterruptedException || ex instanceof java.util.concurrent.CancellationException) {
                        mostrarError("Accion cancelada","","Accion cancelada por el usuario");
                    }else {
                        mostrarError("Error interno","","Ocurrio un error inesperado al crear el respaldo. Vuelva a intentarlo mas tarde.");
                    }
                },null

        );
    }//crearRespaldo


}//class
