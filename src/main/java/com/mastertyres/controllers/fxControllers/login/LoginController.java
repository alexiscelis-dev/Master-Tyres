package com.mastertyres.controllers.fxControllers.login;

import com.mastertyres.auth.LocalAuthService;
import com.mastertyres.auth.SupabaseAuthService;
import com.mastertyres.auth.SupabaseService;
import com.mastertyres.common.exeptions.UserException;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.ApplicationContextProvider;
import com.mastertyres.common.utils.MenuContextSetting;
import com.mastertyres.components.fxComponents.loader.LoadingComponentController;
import com.mastertyres.controllers.fxControllers.ventanaPrincipal.VentanaPrincipalController;
import com.mastertyres.user.model.User;
import com.mastertyres.user.service.UserService;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.mastertyres.common.utils.MensajesAlert.mostrarError;
import static com.mastertyres.common.utils.MensajesAlert.mostrarWarning;

@Component
public class LoginController implements IFxController {


    @FXML
    private AnchorPane rootPane;
    //Se puede tener el Nodo del componente mas el componente
    @FXML
    private StackPane loadingOverlay;
    @FXML
    private LoadingComponentController loadingOverlayController;
    @FXML
    private TextField txtCorreo;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private TextField txtPasswordVisible;
    @FXML
    private Button btnTogglePassword;
    @FXML
    private ImageView imageEye;
    @FXML
    private Button loginButton;


    @Autowired
    private TaskService taskService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private SupabaseAuthService supabaseAuthService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private SupabaseService supabaseService;
    @Autowired
    private LocalAuthService localAuthService;

    final String RUTA_VENTANA_PRINCIPAL = "/fxmlViews/masterTires/Master Tires Principal.fxml";


    @FXML
    private void initialize() {

        configuraciones();
        listeners();

    }//initialize

    @Override
    public void configuraciones() {
        txtPasswordVisible.textProperty().bindBidirectional(pfPassword.textProperty());
        txtPasswordVisible.setVisible(false);

        MenuContextSetting.disableMenu(rootPane);
    }//configuraciones

    @Override
    public void listeners() {

    }//listeners

    @FXML
    private void logIn(ActionEvent event) {

        if (txtCorreo.getText().isEmpty()) {
            mostrarWarning("Campos vacios", "", "Ingrese el nombre de usuario o correo");
            return;
        }

        if (pfPassword.getText().isEmpty() && txtPasswordVisible.getText().isEmpty()) {
            mostrarWarning("Campos vacios", "", "Ingrese la contraseña");
            return;
        }

        //Asegura que tanto el campo invisible de contraseña como el visible tengan la misma informacion
        if (pfPassword.getText().isEmpty() && !txtPasswordVisible.getText().isEmpty()){
            pfPassword.setText(txtPasswordVisible.getText());
        } else if (!pfPassword.getText().isEmpty() && txtPasswordVisible.getText().isEmpty()) {
            txtPasswordVisible.setText(pfPassword.getText());
        }


        taskService.runTask(
                loadingOverlayController,
                () -> {

                    User user = userService.findByEmail(txtCorreo.getText().trim());

                    if (user != null) { //Se encontro en la base de datos local

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                        String fechaStr = user.getNextCheck();

                        LocalDateTime fechaHora = LocalDateTime.parse(fechaStr,formatter);
                        LocalDate fecha = fechaHora.toLocalDate();



                        if (fecha.isEqual(LocalDate.now())){
                            //Existe el usuario pero es dia de sincronizar cambios
                            supabaseService.supabaseLogin(txtCorreo.getText().trim(), pfPassword.getText().trim());

                        }else { //Se autentifica localmente
                            boolean autenticado = localAuthService.authenticate(user, pfPassword.getText().trim());

                            if (!autenticado) {
                                throw new UserException("Usuario o contraseña incorrectos");
                            }
                        }

                    } else { // Se autentifica con supabase

                        supabaseService.supabaseLogin(txtCorreo.getText().trim(), pfPassword.getText().trim());

                    }


                    return userService.findByEmail(txtCorreo.getText().trim());

                }, (userAutenticado) -> {

                    cambiarVistaPrincipal(event, RUTA_VENTANA_PRINCIPAL, userAutenticado);

                }, (ex) -> {

                    if (ex instanceof UserException) {
                        mostrarWarning("Error de autenticación", "", ex.getMessage());

                    } else {
                        ex.printStackTrace();
                        mostrarError("Error interno",
                                "",
                                "Ocurrió un error inesperado al iniciar sesión. Vuelva a intentarlo más tarde.");
                    }

                }, null
        );


    }

    private void cambiarVistaPrincipal(ActionEvent event, String archivoFXML,User user){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(archivoFXML));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent root = loader.load();

            VentanaPrincipalController controller = loader.getController();
            controller.setUser(user);

            Stage ventanaLogin = (Stage) ((Node) event.getSource()).getScene().getWindow();
            ventanaLogin.setScene(new Scene(root));

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            ventanaLogin.setX(screenBounds.getMinX());
            ventanaLogin.setY(screenBounds.getMinY());
            ventanaLogin.setWidth(screenBounds.getWidth());
            ventanaLogin.setHeight(screenBounds.getHeight());

            ventanaLogin.show();

        }catch (Exception e){
            e.printStackTrace();
            e.getMessage();
            mostrarError("Error de carga",
                    "",
                    "Ocurrio un error al cargar la vista. Vuelva a intentarlo mas tarde.");
        }

    }//cambiarVistaPrincipal

    @FXML
    private void togglePassword(Event event) {
        if (pfPassword.isVisible()) {
            // Mostrar contraseña
            pfPassword.setVisible(false);
            txtPasswordVisible.setVisible(true);
            btnTogglePassword.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/visibility.png"))));
        } else {
            // Ocultar contraseña
            pfPassword.setVisible(true);
            txtPasswordVisible.setVisible(false);
            btnTogglePassword.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/visibility_off.png"))));
        }

    }//togglePassword



}//class
