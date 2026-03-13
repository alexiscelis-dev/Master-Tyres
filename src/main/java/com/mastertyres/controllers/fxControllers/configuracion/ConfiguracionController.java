package com.mastertyres.controllers.fxControllers.configuracion;

import com.mastertyres.auth.SupabaseAuthService;
import com.mastertyres.auth.SupabaseService;
import com.mastertyres.common.exeptions.RespaldoException;
import com.mastertyres.common.exeptions.UserException;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.interfaces.ILoader;
import com.mastertyres.common.interfaces.IVentanaPrincipal;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.MenuContextSetting;
import com.mastertyres.common.utils.SessionManager;
import com.mastertyres.components.fxComponents.loader.LoadingComponentController;
import com.mastertyres.controllers.fxControllers.ventanaPrincipal.VentanaPrincipalController;
import com.mastertyres.respaldo.model.Respaldo;
import com.mastertyres.respaldo.service.RespaldoService;
import com.mastertyres.security.SecurityPassword;
import com.mastertyres.user.model.User;
import com.mastertyres.user.service.UserService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.mastertyres.common.utils.MensajesAlert.*;

@Component
public class ConfiguracionController implements IVentanaPrincipal, Initializable, IFxController, ILoader {

    @Autowired
    private RespaldoService respaldoService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityPassword securityPassword;
    @Autowired
    private SupabaseAuthService supabaseAuthService;
    @Autowired
    private SupabaseService supabaseService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @FXML
    private AnchorPane rootPane;
    @FXML
    private StackPane detailContainer;
    @FXML
    private VBox panelBienvenida;

    // Perfil
    @FXML
    private VBox panelVerPerfil;
    @FXML
    private VBox panelCambiarContrasena;
    @FXML
    private VBox panelEditarCorreo;
    @FXML
    private VBox panelCambiarNombreUsuario;
    @FXML
    private VBox panelCambiarFoto;
    @FXML
    private Label lblNombreCompleto;
    @FXML
    private Label lblCorreo;
    @FXML
    private Label lblRol;
    @FXML
    private Label lblUsernameInfo;
    @FXML
    private Label lblCorreoInfo;
    @FXML
    private Label lblFechaRegistro;
    @FXML
    private ImageView avatarImageView;

    //Cambiar contraseña
    @FXML
    private PasswordField pfContrasenaActual;
    @FXML
    private PasswordField pfNuevaContrasena;
    @FXML
    private PasswordField pfConfirmarContrasena;
    @FXML
    private Button btnCambiarContrasena;
    @FXML
    private Label lblContrasenaError;
    @FXML
    private Label lblContrasenaError2;
    @FXML
    private TextField txtContrasenaActualVisible;
    @FXML
    private TextField txtNuevaContrasenaVisible;
    @FXML
    private TextField txtConfirmarContrasenaVisible;
    @FXML
    private ImageView imageEye;
    @FXML
    private ImageView imageEye2;
    @FXML
    private ImageView imageEye3;
    @FXML
    private Button btnTogglePassword;
    @FXML
    private Button btnTogglePassword2;
    @FXML
    private Button btnTogglePassword3;

    //Cambiar nombre usuario
    @FXML
    private TextField txtNuevoNombreUsuario;
    @FXML
    private Button btnCambiarNombreUsuario;
    @FXML
    private Label lblNombreUsuarioActual;

    //Cambiar foto dde perfil
    @FXML
    private Button btnSeleccionarFoto;
    @FXML
    private Button btnGuardarFoto;
    @FXML
    private TextField txtRutaImg;
    @FXML
    private ImageView avatarPreview;


    // Almacenamiento
    @FXML
    private VBox panelCrearRespaldo;

    @FXML
    private VBox panelFechaRespaldo;
    @FXML
    private Button btnRespaldo;
    @FXML
    private Label lblTamanoRespaldo;
    @FXML
    private Label lblFechaUltimoRespaldo;

    // Ayuda
    @FXML
    private VBox panelContacto;


    // FXML — TreeView
    @FXML
    private TreeView<NavItem> menuTree;

    // Mapa: id de panel → nodo FXML
    private final Map<String, Node> panelMap = new HashMap<>();
    private User userSession;

    // INICIALIZACIÓN
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registrarPaneles();
        construirMenu();
        configurarSeleccion();
        configuraciones();
        listeners();
    }

    /**
     * Registra todos los paneles en el mapa para acceso rápido por clave.
     */
    private void registrarPaneles() {
        panelMap.put("panelBienvenida", panelBienvenida);
        panelMap.put("panelVerPerfil", panelVerPerfil);
        panelMap.put("panelCambiarContrasena", panelCambiarContrasena);
        panelMap.put("panelCambiarNombreUsuario", panelCambiarNombreUsuario);
        panelMap.put("panelCambiarFoto", panelCambiarFoto);
        panelMap.put("panelCrearRespaldo", panelCrearRespaldo);
        panelMap.put("panelFechaRespaldo", panelFechaRespaldo);
        panelMap.put("panelContacto", panelContacto);
    }

    /**
     * Construye el árbol de navegación programáticamente.
     */
    private void construirMenu() {
        TreeItem<NavItem> root = new TreeItem<>();
        root.setExpanded(true);

        // ── PERFIL ──
        TreeItem<NavItem> secPerfil = seccion("👤", "Perfil");
        secPerfil.getChildren().addAll(
                hoja("🔍", "Ver perfil", "panelVerPerfil"),
                hoja("🔒", "Cambiar contraseña", "panelCambiarContrasena"),
                hoja("✏", "Cambiar nombre de usuario", "panelCambiarNombreUsuario"),
                hoja("🖼", "Cambiar foto de perfil", "panelCambiarFoto")
        );

        // ── ALMACENAMIENTO ──
        TreeItem<NavItem> secAlmacenamiento = seccion("💾", "Almacenamiento");
        secAlmacenamiento.getChildren().addAll(
                hoja("📦", "Crear respaldo", "panelCrearRespaldo"),
                hoja("📅", "Fecha de último respaldo", "panelFechaRespaldo")
        );

        // ── AYUDA ──
        TreeItem<NavItem> secAyuda = seccion("❓", "Ayuda");
        secAyuda.getChildren().addAll(
                hoja("📬", "Informacion", "panelContacto")
        );

        root.getChildren().addAll(secPerfil, secAlmacenamiento, secAyuda);
        menuTree.setRoot(root);

        // Personalizar celdas del árbol
        menuTree.setCellFactory(tv -> new TreeCell<>() {
            @Override
            protected void updateItem(NavItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Ícono + texto juntos en la celda
                    setText(item.icono() + "  " + item.nombre());
                }
            }
        });
    }

    /**
     * Configura el listener para mostrar el panel correspondiente al seleccionar.
     */
    private void configurarSeleccion() {
        menuTree.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nuevo) -> {
            if (nuevo == null) return;
            NavItem item = nuevo.getValue();
            // Solo los ítems hoja tienen panelId
            if (item.panelId() != null && !item.panelId().isBlank()) {
                mostrarPanel(item.panelId());
            }
        });
    }

    /**
     * Oculta todos los paneles y muestra solo el solicitado.
     */

    private void mostrarPanel(String panelId) {
        panelMap.forEach((id, nodo) -> nodo.setVisible(false));
        Node target = panelMap.get(panelId);
        if (target != null) {
            target.setVisible(true);

            mostrarPanelConfiguracion(panelId);


        } else {
            panelBienvenida.setVisible(true);
        }
    }


    // HELPERS PARA CREAR ÍTEMS DEL ÁRBOL
    private TreeItem<NavItem> seccion(String icono, String nombre) {
        TreeItem<NavItem> item = new TreeItem<>(new NavItem(icono, nombre, null));
        item.setExpanded(true);
        return item;
    }

    private TreeItem<NavItem> hoja(String icono, String nombre, String panelId) {
        return new TreeItem<>(new NavItem(icono, nombre, panelId));
    }

    private void mostrarPanelConfiguracion(String panelId) {

        if (panelId.equals("panelVerPerfil")) {
            verPerfil();
            panelVerPerfil.setVisible(true);
        } else if (panelId.equals("panelCambiarContrasena")) {
            panelCambiarContrasena.setVisible(true);


        } else if (panelId.equals("panelFechaRespaldo")) {
            panelFechaRespaldo.setVisible(true);
            mostrarUltimoRespaldo();
        } else if (panelId.equals("panelCambiarNombreUsuario")) {
            panelCambiarNombreUsuario.setVisible(true);
            lblNombreUsuarioActual.setText(userSession.getUsername());
        } else if (panelId.equals("panelCambiarFoto")) {
            panelCambiarFoto.setVisible(true);
            mostarPanelCambiarFoto();
        }
    }//mostrarPanel

    // HANDLERS DE ACCIONES (FXML onAction)

    @FXML
    private void onCambiarContrasena() {

        //Compara la contraseña ingresada con la existente en BD
        boolean contrasenaValida = securityPassword.verificarPasswordActual(pfContrasenaActual, lblContrasenaError, userSession);
        boolean nuevaContrasenaValida = securityPassword.verificarNewPassword(pfNuevaContrasena, pfConfirmarContrasena, txtConfirmarContrasenaVisible, lblContrasenaError2);


        if (contrasenaValida && nuevaContrasenaValida) {

            taskService.runTask(
                    loadingComponentController,
                    () -> {
                        supabaseAuthService.actualizarPassword(userSession.getAuthId(), pfNuevaContrasena.getText().trim());
                        supabaseService.actualizarPassword(userSession.getAuthId(), userSession, pfNuevaContrasena.getText().trim());

                        User supabaseUser = supabaseService.findUsuarioById(userSession.getAuthId(), SessionManager.getAccessToken());

                        User localUser = userService.findUserById(userSession.getUsuarioId());

                        localUser.setAuthId(supabaseUser.getAuthId());
                        localUser.setUsername(supabaseUser.getUsername());
                        localUser.setCorreo(supabaseUser.getCorreo());
                        localUser.setPassword(passwordEncoder.encode(supabaseUser.getPassword()));
                        localUser.setNombre(supabaseUser.getNombre());
                        localUser.setApellidos(supabaseUser.getApellidos());
                        localUser.setFotoPerfil(supabaseUser.getFotoPerfil());
                        localUser.setActive(supabaseUser.getActive());
                        localUser.setRole(supabaseUser.getRole());
                        localUser.setStatusLicencia(supabaseUser.getStatusLicencia());

                        userService.guardarUsuario(localUser);
                        userService.updateNextCheck(userSession.getUsuarioId(),LocalDateTime.now().toString());




                        return null;
                    },(resultado) -> {

                        mostrarInformacion("Contraseña actualizada", "", "La contraseña se actualizó exitosamente");
                        pfContrasenaActual.clear();
                        txtContrasenaActualVisible.clear();
                        pfNuevaContrasena.clear();
                        txtNuevaContrasenaVisible.clear();
                        pfConfirmarContrasena.clear();
                        txtConfirmarContrasenaVisible.clear();
                        lblContrasenaError2.setText("");
                        lblContrasenaError.setText("");

                        

                    },(ex)->{

                        if (ex instanceof InterruptedException || ex instanceof java.util.concurrent.CancellationException) {
                            mostrarError("Accion cancelada", "", "Accion cancelada por el usuario");
                        } else if (ex instanceof UserException) {
                          mostrarError("No se pudo guardar","Ocurrio un problema al guardar los cambios.",""+ex.getMessage());
                        } else  {
                            mostrarError("Error interno","","Ocurrio un error inesperado al guardar los cambios."+ex.getMessage());
                            ex.printStackTrace();
                        }

                    },null
            );

            securityPassword.cambiarPassword(userSession, pfNuevaContrasena.getText());
        }
    }//onCambiarContrasena


    @FXML
    private void onCambiarNombreUsuario() {

      boolean confirmar =
              mostrarConfirmacion("Actualizar usuario","Se cambiara el nombre de usuario a '"+txtNuevoNombreUsuario.getText().trim()+"'.",
                      "¿Desea continuar?",
                      "Aceptar",
                      "Cancelar");

      if (confirmar){
          taskService.runTask(
                  loadingComponentController,
                  () -> {

                      userSession.setUsername(txtNuevoNombreUsuario.getText().trim());

                      supabaseService.actualizarUsername(userSession.getAuthId(),userSession);



                      User supabaseUser = supabaseService.findUsuarioById(userSession.getAuthId(), SessionManager.getAccessToken());

                      User localUser = userService.findUserById(userSession.getUsuarioId());

                      localUser.setAuthId(supabaseUser.getAuthId());
                      localUser.setUsername(supabaseUser.getUsername());
                      localUser.setCorreo(supabaseUser.getCorreo());
                      localUser.setPassword(passwordEncoder.encode(supabaseUser.getPassword()));
                      localUser.setNombre(supabaseUser.getNombre());
                      localUser.setApellidos(supabaseUser.getApellidos());
                      localUser.setFotoPerfil(supabaseUser.getFotoPerfil());
                      localUser.setActive(supabaseUser.getActive());
                      localUser.setRole(supabaseUser.getRole());
                      localUser.setStatusLicencia(supabaseUser.getStatusLicencia());

                      userService.guardarUsuario(localUser);
                      userService.updateNextCheck(userSession.getUsuarioId(),LocalDateTime.now().toString());

                      return null;

                  },(resultado) ->{

                      lblNombreUsuarioActual.setText(userSession.getUsername());
                      String texto = userSession.getUsername();
                      ventanaPrincipalController.setCambiarPaginaEtiqueta(new Label(texto));


                      mostrarInformacion("Usuario actualizado", "", "El nombre de usuario se actualizó exitosamente.");
                  },(ex) ->{

                      if (ex instanceof InterruptedException || ex instanceof java.util.concurrent.CancellationException) {
                          mostrarError("Accion cancelada", "", "Accion cancelada por el usuario");
                      } else if (ex instanceof UserException) {
                          mostrarError("No se pudo guardar","Ocurrio un problema al guardar los cambios.",""+ex.getMessage());
                      } else if (ex instanceof IOException || ex instanceof NullPointerException) {
                          mostrarWarning("Usuario actualizado","","El nombre de usuario se actualizó correctamente, pero no se pudo cargar en la interfaz. Reinicie la aplicación para ver los cambios.");
                      } else  {
                          mostrarError("Error interno","","Ocurrio un error inesperado al guardar los cambios."+ex.getMessage());
                          ex.printStackTrace();
                      }


                  },null
          );
      }


    }//onCambiarNombreUsuario

    @FXML
    private void onSeleccionarFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imagenes", "*.png", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) rootPane.getScene().getWindow();

        File file = fileChooser.showOpenDialog(stage);

        if (file != null && file.exists()) {

            Image image = new Image(file.toURI().toString());
            avatarPreview.setImage(image);
            txtRutaImg.setText(file.getAbsolutePath());

        } else {
            Image image = new Image("/icons/user.png");
            avatarPreview.setImage(image);
           txtRutaImg.setText("");
        }

    }

    @FXML
    private void onGuardarFoto() {
        taskService.runTask(
                loadingComponentController,
                ()->{
                    userSession.setFotoPerfil(txtRutaImg.getText().trim());

                    supabaseService.actualizarFotoPerfil(userSession.getAuthId(),userSession);
                    User supabaseUser =  supabaseService.findUsuarioById(userSession.getAuthId(),SessionManager.getAccessToken());
                    User localUser = userService.findUserById(userSession.getUsuarioId());

                    localUser.setAuthId(supabaseUser.getAuthId());
                    localUser.setUsername(supabaseUser.getUsername());
                    localUser.setCorreo(supabaseUser.getCorreo());
                    localUser.setPassword(passwordEncoder.encode(supabaseUser.getPassword()));
                    localUser.setNombre(supabaseUser.getNombre());
                    localUser.setApellidos(supabaseUser.getApellidos());
                    localUser.setFotoPerfil(supabaseUser.getFotoPerfil());
                    localUser.setUpdatedAt(LocalDateTime.now().toString());
                    localUser.setActive(supabaseUser.getActive());
                    localUser.setRole(supabaseUser.getRole());
                    localUser.setStatusLicencia(supabaseUser.getStatusLicencia());

                    userService.guardarUsuario(localUser);
                    userService.updateNextCheck(userSession.getUsuarioId(),LocalDateTime.now().toString());


                    return null;
                },(resultado)->{

                    mostrarInformacion("Foto actualizada","","La foto se actualizó exitosamente.");


                },(ex)->{

                    if (ex instanceof InterruptedException || ex instanceof java.util.concurrent.CancellationException) {
                        mostrarError("Accion cancelada", "", "Accion cancelada por el usuario");
                    } else if (ex instanceof UserException) {
                        mostrarError("No se pudo guardar","Ocurrio un problema al guardar los cambios.",""+ex.getMessage());
                    } else  {
                        mostrarError("Error interno","","Ocurrio un error inesperado al guardar los cambios."+ex.getMessage());
                        ex.printStackTrace();
                    }

                },null
        );

    }//onGuardarFoto

    @FXML
    private void onEliminarFoto(Event event){

        taskService.runTask(
                loadingComponentController,
                ()->{
                    userSession.setFotoPerfil(null);

                    supabaseService.actualizarFotoPerfil(userSession.getAuthId(),userSession);
                    User supabaseUser =  supabaseService.findUsuarioById(userSession.getAuthId(),SessionManager.getAccessToken());
                    User localUser = userService.findUserById(userSession.getUsuarioId());

                    localUser.setAuthId(supabaseUser.getAuthId());
                    localUser.setUsername(supabaseUser.getUsername());
                    localUser.setCorreo(supabaseUser.getCorreo());
                    localUser.setPassword(passwordEncoder.encode(supabaseUser.getPassword()));
                    localUser.setNombre(supabaseUser.getNombre());
                    localUser.setApellidos(supabaseUser.getApellidos());
                    localUser.setFotoPerfil(supabaseUser.getFotoPerfil());
                    localUser.setActive(supabaseUser.getActive());
                    localUser.setRole(supabaseUser.getRole());
                    localUser.setStatusLicencia(supabaseUser.getStatusLicencia());

                    userService.guardarUsuario(localUser);
                    userService.updateNextCheck(userSession.getUsuarioId(),LocalDateTime.now().toString());


                    return null;
                },(resultado)->{

                    Image image = new Image("/icons/user.png");
                    avatarPreview.setImage(image);
                    txtRutaImg.setText("");

                    mostrarInformacion("Foto eliminada","","La foto se elimino exitosamente.");


                },(ex)->{

                    if (ex instanceof InterruptedException || ex instanceof java.util.concurrent.CancellationException) {
                        mostrarError("Accion cancelada", "", "Accion cancelada por el usuario");
                    } else if (ex instanceof UserException) {
                        mostrarError("No se pudo guardar","Ocurrio un problema al guardar los cambios.",""+ex.getMessage());
                    } else  {
                        mostrarError("Error interno","","Ocurrio un error inesperado al guardar los cambios."+ex.getMessage());
                        ex.printStackTrace();
                    }

                },null
        );


    }//onEliminar


    // ──────────────────────────────────────
    // MODELO INTERNO
    // ──────────────────────────────────────

    /**
     * Representa un ítem del menú de navegación.
     *
     * @param icono   Emoji o texto corto usado como ícono
     * @param nombre  Texto visible en el árbol
     * @param panelId fx:id del panel FXML a mostrar (null para secciones padre)
     */
    public record NavItem(String icono, String nombre, String panelId) {
        @Override
        public String toString() {
            return icono + "  " + nombre;
        }
    }

    private LoadingComponentController loadingComponentController;
    private VentanaPrincipalController ventanaPrincipalController;
    private BooleanProperty boolPassword = new SimpleBooleanProperty(false);


    @Override
    public void configuraciones() {
        MenuContextSetting.disableMenu(rootPane);
        txtContrasenaActualVisible.textProperty().bindBidirectional(pfContrasenaActual.textProperty());
        txtNuevaContrasenaVisible.textProperty().bindBidirectional(pfNuevaContrasena.textProperty());
        txtConfirmarContrasenaVisible.textProperty().bindBidirectional(pfConfirmarContrasena.textProperty());


        btnCambiarContrasena.disableProperty().
                bind(pfContrasenaActual.textProperty().isEmpty()
                        .or(pfContrasenaActual.textProperty().isNull())
                        .or(pfNuevaContrasena.textProperty().isEmpty()
                                .or(pfNuevaContrasena.textProperty().isNull()))
                        .or(pfConfirmarContrasena.textProperty().isEmpty())
                        .or(pfConfirmarContrasena.textProperty().isNull())
                        .or(boolPassword.not())
                );

        btnGuardarFoto.disableProperty().bind(txtRutaImg.textProperty().isEmpty());

        btnCambiarNombreUsuario.disableProperty().bind(txtNuevoNombreUsuario.textProperty().isEmpty());


        txtNuevoNombreUsuario.textProperty().addListener((observable, oldValue, newValue) -> {
           if (txtNuevoNombreUsuario.getText().length() > 40){
               txtNuevoNombreUsuario.setText(oldValue);
           }
        });

    }

    @Override
    public void listeners() {

        pfConfirmarContrasena.setOnKeyPressed(event -> {
            validarPassword();

        });

        txtConfirmarContrasenaVisible.textProperty().addListener(event -> {
            validarPassword();

        });

        btnTogglePassword.setOnAction(event -> togglePassword(pfContrasenaActual, txtContrasenaActualVisible, btnTogglePassword));

        btnTogglePassword2.setOnAction(event -> togglePassword(pfNuevaContrasena, txtNuevaContrasenaVisible, btnTogglePassword2));

        btnTogglePassword3.setOnAction(event -> togglePassword(pfConfirmarContrasena, txtConfirmarContrasenaVisible, btnTogglePassword3));

    }

    public void setUser(User user) {
        this.userSession = user;
    }

    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingComponentController = loading;
    }

    @Override
    public void setVentanaPrincipalController(VentanaPrincipalController controller) {
        this.ventanaPrincipalController = controller;
    }

    @FXML
    private void crearRespaldo(ActionEvent actionEvent) {

        boolean confirmacion = mostrarConfirmacion("", "Se creara una copia de seguridad local y se guardara una extra en la nube."
                , "¿Desea continuar?", "Continuar", "Cancelar");

        if (!confirmacion)
            return;

        taskService.runTask(
                loadingComponentController,
                () -> {
                    respaldoService.generarRespaldo();
                    return null;
                }, (resultado) -> {
                    mostrarInformacion("Respaldo creado", "", "El respaldo se creo exitosamente.");
                }, (ex) -> {
                    if (ex instanceof RespaldoException) {
                        mostrarError("No se pudo crear respaldo",
                                "Ocurrio un problema al crear el respaldo",
                                "" + ex.getMessage());
                    } else if (ex instanceof InterruptedException || ex instanceof java.util.concurrent.CancellationException) {
                        mostrarError("Accion cancelada", "", "Accion cancelada por el usuario");
                    } else {
                        mostrarError("Error interno", "", "Ocurrio un error inesperado al crear el respaldo. Vuelva a intentarlo mas tarde.");
                    }
                }, null

        );
    }//crearRespaldo

    private void mostrarUltimoRespaldo() {

        taskService.runTask(
                loadingComponentController,
                () -> {
                    Respaldo ultimoRespaldo = respaldoService.ObtenerUltimoRespaldo();

                    return ultimoRespaldo;

                }, (ultimoRespaldo) -> {

                    if (ultimoRespaldo != null) {

                        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

                        LocalDateTime dateTime = LocalDateTime.parse(ultimoRespaldo.getCreatedAt(), inputFormatter);

                        String fecha = dateTime.format(outputFormatter);

                        lblFechaUltimoRespaldo.setText(fecha);

                        long bytes = ultimoRespaldo.getTamanioBytes();
                        String texto;


                        if (bytes < 1024) {
                            texto = bytes + " B";

                        } else if (bytes < 1024 * 1024) {

                            texto = String.format("%.2f KB", bytes / 1024.0);

                        } else if (bytes < 1024L * 1024L * 1024L) {
                            texto = String.format("%.2f MB", bytes / (1024.0 * 1024.0));
                        } else {
                            texto = String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
                        }

                        lblTamanoRespaldo.setText(texto);
                    }

                }, (ex) -> {

                    if (ex instanceof RespaldoException) {
                        mostrarError("Error al mostrar datos", "", "" + ex.getMessage());
                    } else {
                        mostrarError("Error interno", "", "Ocurrio un error inesperado al mostrar los datos. Vuelva a intentarlo mas tarde.");
                    }

                }, null
        );

    }//mostrarUltimoRespaldo

    private void verPerfil() {

        lblNombreCompleto.setText(userSession.getNombre() + " " + userSession.getApellidos());
        lblCorreo.setText(userSession.getCorreo());
        lblRol.setText(userSession.getRole());
        lblUsernameInfo.setText(userSession.getUsername());
        lblCorreoInfo.setText(userSession.getCorreo());

        String rutaImagen = userSession.getFotoPerfil();
        Image image;

        if (rutaImagen != null && !rutaImagen.isBlank()) {

            File file = new File(rutaImagen);
            if (file.exists()) {
                image = new Image(file.toURI().toString());
            } else {
                image = new Image(getClass().getResource("/icons/user.png").toExternalForm());
            }

        } else {
            image = new Image(getClass().getResource("/icons/user.png").toExternalForm());
        }

        DateTimeFormatter formatterIn = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatterOut = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            LocalDateTime fecha = LocalDateTime.parse(userSession.getCreatedAt(), formatterIn);
            String fechaStr = fecha.format(formatterOut);

            lblFechaRegistro.setText(fechaStr);

        } catch (DateTimeParseException e) {
            lblFechaRegistro.setText("N/D");
            e.printStackTrace();
        }


    }//verPerfil

    private void mostarPanelCambiarFoto(){
        User user =  userService.findUserById(userSession.getUsuarioId());



        String imgRuta = user.getFotoPerfil();

        if (imgRuta != null){

            File file = new File(imgRuta);

            if (file != null && file.exists()){
                Image image = new Image(file.toURI().toString());
                avatarPreview.setImage(image);
            }else {
                Image image = new Image("/icons/user.png");
                avatarPreview.setImage(image);
            }

        }else {
            Image image = new Image("/icons/user.png");
            avatarPreview.setImage(image);
        }




    }//mostarPanelCambiarFoto

    private void togglePassword(PasswordField pf, TextField tf, Button btnEye) {

        boolean mostrar = pf.isVisible();

        pf.setVisible(!mostrar);
        pf.setManaged(!mostrar);

        tf.setVisible(mostrar);
        tf.setManaged(mostrar);

        if (mostrar) {
            ImageView ojoInvisible = new ImageView(
                    new Image(getClass().getResourceAsStream("/icons/visibility_off.png")));
            btnEye.setGraphic(ojoInvisible);
        } else {
            ImageView ojoVisible = new ImageView(
                    new Image(getClass().getResourceAsStream("/icons/visibility.png")));
            btnEye.setGraphic(ojoVisible);
        }
    }

    private void validarPassword() {
        String contrasena;

        if (pfConfirmarContrasena.isVisible())
            contrasena = pfConfirmarContrasena.getText().trim();
        else
            contrasena = txtConfirmarContrasenaVisible.getText().trim();


        if (!contrasena.matches("^(?=.*[A-Z])(?=.*\\d).{8,}$")) {
            lblContrasenaError2.setVisible(true);
            lblContrasenaError2.setStyle("-fx-text-fill: red; -fx-font-size:8px;");
            lblContrasenaError2.setText("La contraseña debe contener almenos 8 caracteres, una letra Mayuscula y un numero.");
            boolPassword.set(false);
        } else {
            boolPassword.set(true);
            lblContrasenaError2.setVisible(false);
            lblContrasenaError2.setText("");
            txtConfirmarContrasenaVisible.setStyle("");
            pfConfirmarContrasena.setStyle("");

        }
    }//validarPassword


}//class
