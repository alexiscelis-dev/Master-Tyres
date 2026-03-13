package com.mastertyres.controllers.fxControllers.login.recuperarPassword;

import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.interfaces.ILoader;
import com.mastertyres.common.service.EmailService;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.MenuContextSetting;
import com.mastertyres.components.fxComponents.loader.LoadingComponentController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.mastertyres.common.utils.MensajesAlert.mostrarError;

@Component
public class RecuperarPasswordController implements ILoader, IFxController {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField txtCorreoRecuperacion;
    @FXML
    private Button btnEnviar;

    @Autowired
    private EmailService emailService;
    @Autowired
    private TaskService taskService;

    @FXML
    private void initialize(){
        configuraciones();
        listeners();
    }

    private LoadingComponentController loadingOverlayController;

    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingOverlayController = loading;
    }

    @Override
    public void configuraciones() {
        MenuContextSetting.disableMenu(rootPane);
    }

    @Override
    public void listeners() {
        btnEnviar.setOnAction(event -> {
            enviarCorreo();
        });

    }

    private void enviarCorreo(){

        taskService.runTask(
                loadingOverlayController,
                ()->{

                    return null;
                },(resultado)->{

                },(ex) ->{
                    if (ex instanceof InterruptedException || ex instanceof java.util.concurrent.CancellationException){
                        mostrarError("Accion cancelada","","Accion cancelada por el usuario");

                    }

                },null
        );
    }//enviarCorreo

}//class
