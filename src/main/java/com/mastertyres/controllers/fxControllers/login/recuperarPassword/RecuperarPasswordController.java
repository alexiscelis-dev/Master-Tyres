package com.mastertyres.controllers.fxControllers.login.recuperarPassword;

import com.mastertyres.auth.SupabaseAuthService;
import com.mastertyres.auth.SupabaseService;
import com.mastertyres.common.exeptions.UserException;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.interfaces.ILoader;
import com.mastertyres.common.service.EmailService;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.components.fxComponents.loader.LoadingComponentController;
import com.mastertyres.user.entity.User;
import com.mastertyres.user.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.mastertyres.common.utils.MensajesAlert.*;
import static com.mastertyres.common.utils.MenuContextSetting.disableMenu;
import static com.mastertyres.common.utils.PasswordGenerator.generarPassword;

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
    @Autowired
    private SupabaseAuthService supabaseAuthService;
    @Autowired
    private SupabaseService supabaseService;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

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
        disableMenu(rootPane);
    }

    @Override
    public void listeners() {

        btnEnviar.setOnAction(event -> {
            if (txtCorreoRecuperacion.getText().isEmpty()){
                mostrarWarning("Campos vacios",
                        "",
                        "Debe ingresar un correo electronico para recuperar su contraseña.");
                return;
            }

            enviarCorreo();
        });

    }

    private void enviarCorreo(){

        taskService.disable(rootPane);


        taskService.runTask(
                loadingOverlayController,
                ()->{

                User supabaseUser =  supabaseService.findUsuarioByCorreo(txtCorreoRecuperacion.getText().trim());


               if (supabaseUser != null && supabaseUser.getCorreo().equals(txtCorreoRecuperacion.getText().trim())){

                String password = generarPassword(10);


                //Actualiza contraseña en tabla de autenticacion

                supabaseAuthService.actualizarPassword(supabaseUser.getAuthId(),password);
                //Actualiza contraseña en Supabase
                supabaseService.actualizarPassword(supabaseUser.getAuthId(),supabaseUser,password);
                //Actualiza la contraseña localmente

                User localUser = userService.findByEmail(supabaseUser.getCorreo());

                localUser.setPassword(passwordEncoder.encode(password));
                localUser.setUpdatedAt(LocalDateTime.now().toString());
                localUser.setNextCheck(LocalDateTime.now().toString());

                userService.guardarUsuario(localUser);

                   final String destino = supabaseUser.getCorreo();
                   final String asunto = "Recuperación de contraseña";

                   final String mensaje = "Hola,\n\n"
                           + "Se ha generado una nueva contraseña para tu cuenta.\n"
                           + "Tu nueva contraseña es: " + password + "\n\n"
                           + "Por seguridad, te recomendamos cambiarla después de iniciar sesión.\n\n"
                           + "Saludos.";

                emailService.enviarCorreoRecuperarPassword(supabaseUser.getCorreo(),asunto,mensaje);


               }


                    return null;
                },(resultado)->{
                    taskService.enable(rootPane);


                    close(null);

                    mostrarInformacion(
                            "Recuperación de contraseña",
                            "",
                            "Si el correo ingresado está asociado a una cuenta, "
                                    + "recibirás una nueva contraseña en tu correo electrónico."
                    );




                },(ex) ->{
                    taskService.enable(rootPane);

                    if (ex instanceof InterruptedException || ex instanceof java.util.concurrent.CancellationException){
                        mostrarError("Accion cancelada","","Accion cancelada por el usuario");

                    } else if (ex instanceof UserException) {
                        mostrarError("Error interno", "", "Ocurrio un error al cambiar la contraseña. Verifica tu conexion a internet.");
                    } else if (ex instanceof MailException) {
                        mostrarError("Error al enviar correo","","No se pudo enviar el correo con la contraseña. Vuelve a intentarlo mas tarde.");
                    }
                },null
        );

    }//enviarCorreo

    @FXML
    private void close(ActionEvent event) {

        txtCorreoRecuperacion.clear();

        Stage stage = (Stage) btnEnviar.getScene().getWindow();
        stage.close();

    }

}//class
