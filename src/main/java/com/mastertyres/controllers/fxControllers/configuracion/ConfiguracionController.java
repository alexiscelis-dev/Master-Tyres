package com.mastertyres.controllers.fxControllers.configuracion;

import com.mastertyres.auth.SupabaseAuthService;
import com.mastertyres.auth.SupabaseService;
import com.mastertyres.common.exeptions.RespaldoException;
import com.mastertyres.common.exeptions.UserException;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.interfaces.ILoader;
import com.mastertyres.common.interfaces.IVentanaPrincipal;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.GenerarLogs;
import com.mastertyres.common.utils.MenuContextSetting;
import com.mastertyres.components.fxComponents.loader.LoadingComponentController;
import com.mastertyres.controllers.fxControllers.ventanaPrincipal.VentanaPrincipalController;
import com.mastertyres.respaldo.entity.Respaldo;
import com.mastertyres.respaldo.service.RespaldoService;
import com.mastertyres.security.SecurityPassword;
import com.mastertyres.user.entity.RolUser;
import com.mastertyres.user.entity.User;
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
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static com.mastertyres.common.utils.MensajesAlert.*;

@Component
public class ConfiguracionController implements IVentanaPrincipal, Initializable, IFxController, ILoader {

    @Value("${app.name}")
    private String appName;
    @Value("${app.company}")
    private String appCompany;
    @Value("${app.version}")
    private String appVersion;
    @Value("${app.build}")
    private String appBuild;
    @Value("${app.ultima.actualizacion}")
    private String appUltimaActualizacion;

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
    private VBox panelLogs;
    @FXML
    private VBox eliminarUsuariosLocales;
    @FXML
    private Label lblNombreCompleto;
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
    @FXML
    private Label lblNombreApp;
    @FXML
    private Label lblNombreEmpresa;
    @FXML
    private Label lblVersionApp;
    @FXML
    private Label lblBuild;
    @FXML
    private Label lblUltimaActualizacion;

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

    //Cambiar foto de perfil
    @FXML
    private Button btnSeleccionarFoto;
    @FXML
    private Button btnGuardarFoto;
    @FXML
    private TextField txtRutaImg;
    @FXML
    private ImageView avatarPreview;
    @FXML
    private StackPane profileContainer;
    @FXML
    private Circle profileCircle;


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
    @FXML
    private Label lblDerechosReservados;

    // Ayuda
    @FXML
    private VBox panelContacto;

    //Administrador
    @FXML
    private DatePicker dpFechaInicio;
    @FXML
    private DatePicker dpFechaFin;
    @FXML
    private Button btnGenerarLogs;
    @FXML
    private ListView<User> listaUsuarios;
    @FXML
    private TextField txtUsuarioEliminar;
    @FXML
    private Button EliminarUsuarioLocal;


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
        panelMap.put("logsRespaldos", panelLogs);
        panelMap.put("eliminarUsuariosLocales", eliminarUsuariosLocales);
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

        // ── Admin ──


        if (esAdmin()) {
            TreeItem<NavItem> secAdmin = seccion("👑", "Administrador");

            secAdmin.getChildren().addAll(
                    hoja("❌", "Eliminar usuarios locales", "eliminarUsuariosLocales"),
                    hoja("📊", "Logs respaldos", "logsRespaldos")
            );

            root.getChildren().add(secAdmin);
        }


        root.getChildren().addAll(secPerfil, secAlmacenamiento, secAyuda);


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
        menuTree.setRoot(root);
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


        if (panelId.equals("logsRespaldos") && !esAdmin()) {
            mostrarError("Acceso denegado", "", "No tienes permisos para acceder");
            return;
        } else if (panelId.equals("eliminarUsuariosLocales") && !esAdmin()) {
            mostrarError("Acceso denegado", "", "No tienes permisos para acceder");
            return;
        }


        panelMap.forEach((id, nodo) -> nodo.setVisible(false));
        Node target = panelMap.get(panelId);
        if (target != null) {
            target.setVisible(true);

            mostrarPanelConfiguracion(panelId);


        } else {
            panelBienvenida.setVisible(true);
        }
    }//mostrarPanel


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
            //   panelVerPerfil.setVisible(true);
        } else if (panelId.equals("panelCambiarContrasena")) {
            panelCambiarContrasena.setVisible(true);

        } else if (panelId.equals("panelFechaRespaldo")) {
            //   panelFechaRespaldo.setVisible(true);
            mostrarUltimoRespaldo();
        } else if (panelId.equals("panelCambiarNombreUsuario")) {
            panelCambiarNombreUsuario.setVisible(true);
            lblNombreUsuarioActual.setText(userSession.getUsername());
        } else if (panelId.equals("panelCambiarFoto")) {
            //  panelCambiarFoto.setVisible(true);
            mostarPanelCambiarFoto();
        } else if (panelId.equals("panelContacto")) {
            lblNombreApp.setText(appName);
            lblNombreEmpresa.setText(appCompany);
            lblVersionApp.setText(appVersion);
            lblBuild.setText(appBuild);
            lblUltimaActualizacion.setText(appUltimaActualizacion);
            lblDerechosReservados.setText(appCompany + "© 2026 - Todos los derechos reservados");

            panelContacto.setVisible(true);

        } else if (panelId.equals("logsRespaldos")) {
            if (esAdmin()) {
                panelLogs.setVisible(true);

            }

        } else if (panelId.equals("eliminarUsuariosLocales")) {

            if (esAdmin()) {
                eliminarUsuariosLocales.setVisible(true);
                mostrarUsuarios();

            }
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

                        User supabaseUser = supabaseService.findUsuarioById(userSession.getAuthId());

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
                        userService.updateNextCheck(userSession.getUsuarioId(), LocalDateTime.now().toString());


                        return null;
                    }, (resultado) -> {

                        mostrarInformacion("Contraseña actualizada", "", "La contraseña se actualizó exitosamente");
                        pfContrasenaActual.clear();
                        txtContrasenaActualVisible.clear();
                        pfNuevaContrasena.clear();
                        txtNuevaContrasenaVisible.clear();
                        pfConfirmarContrasena.clear();
                        txtConfirmarContrasenaVisible.clear();
                        lblContrasenaError2.setText("");
                        lblContrasenaError.setText("");


                    }, (ex) -> {

                        if (ex instanceof InterruptedException || ex instanceof java.util.concurrent.CancellationException) {
                            mostrarError("Accion cancelada", "", "Accion cancelada por el usuario");
                        } else if (ex instanceof UserException) {
                            mostrarError("No se pudo guardar", "Ocurrio un problema al guardar los cambios.", "" + ex.getMessage());
                        } else {
                            mostrarError("Error interno", "", "Ocurrio un error inesperado al guardar los cambios." + ex.getMessage());
                            ex.printStackTrace();
                        }

                    }, null
            );

            securityPassword.cambiarPassword(userSession, pfNuevaContrasena.getText());
        }
    }//onCambiarContrasena

    @FXML
    private void onCambiarNombreUsuario() {

        boolean confirmar =
                mostrarConfirmacion("Actualizar usuario", "Se cambiara el nombre de usuario a '" + txtNuevoNombreUsuario.getText().trim() + "'.",
                        "¿Desea continuar?",
                        "Aceptar",
                        "Cancelar");

        if (confirmar) {
            taskService.runTask(
                    loadingComponentController,
                    () -> {

                        userSession.setUsername(txtNuevoNombreUsuario.getText().trim());

                        supabaseService.actualizarUsername(userSession.getAuthId(), userSession);


                        User supabaseUser = supabaseService.findUsuarioById(userSession.getAuthId());

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
                        userService.updateNextCheck(userSession.getUsuarioId(), LocalDateTime.now().toString());

                        return null;

                    }, (resultado) -> {

                        lblNombreUsuarioActual.setText(userSession.getUsername());
                        ventanaPrincipalController.refreshUserInfo(userSession);
//                      String texto = userSession.getUsername();
//                      ventanaPrincipalController.setCambiarPaginaEtiqueta(new Label(texto));


                        mostrarInformacion("Usuario actualizado", "", "El nombre de usuario se actualizó exitosamente.");
                    }, (ex) -> {

                        if (ex instanceof InterruptedException || ex instanceof java.util.concurrent.CancellationException) {
                            mostrarError("Accion cancelada", "", "Accion cancelada por el usuario");
                        } else if (ex instanceof UserException) {
                            mostrarError("No se pudo guardar", "Ocurrio un problema al guardar los cambios.", "" + ex.getMessage());
                        } else if (ex instanceof IOException || ex instanceof NullPointerException) {
                            mostrarWarning("Usuario actualizado", "", "El nombre de usuario se actualizó correctamente, pero no se pudo cargar en la interfaz. Reinicie la aplicación para ver los cambios.");
                        } else {
                            mostrarError("Error interno", "", "Ocurrio un error inesperado al guardar los cambios." + ex.getMessage());
                            ex.printStackTrace();
                        }


                    }, null
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
                () -> {
                    userSession.setFotoPerfil(txtRutaImg.getText().trim());

                    supabaseService.actualizarFotoPerfil(userSession.getAuthId(), userSession);
                    User supabaseUser = supabaseService.findUsuarioById(userSession.getAuthId());
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
                    userService.updateNextCheck(userSession.getUsuarioId(), LocalDateTime.now().toString());


                    return null;
                }, (resultado) -> {

                    mostrarInformacion("Foto actualizada", "", "La foto se actualizó exitosamente.");
                    ventanaPrincipalController.refreshUserInfo(userSession);

                }, (ex) -> {

                    if (ex instanceof InterruptedException || ex instanceof java.util.concurrent.CancellationException) {
                        mostrarError("Accion cancelada", "", "Accion cancelada por el usuario");
                    } else if (ex instanceof UserException) {
                        mostrarError("No se pudo guardar", "Ocurrio un problema al guardar los cambios.", "" + ex.getMessage());
                    } else {
                        mostrarError("Error interno", "", "Ocurrio un error inesperado al guardar los cambios." + ex.getMessage());
                        ex.printStackTrace();
                    }

                }, null
        );

    }//onGuardarFoto

    @FXML
    private void onEliminarFoto(Event event) {

        taskService.runTask(
                loadingComponentController,
                () -> {
                    userSession.setFotoPerfil(null);

                    supabaseService.actualizarFotoPerfil(userSession.getAuthId(), userSession);
                    User supabaseUser = supabaseService.findUsuarioById(userSession.getAuthId());
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
                    userService.updateNextCheck(userSession.getUsuarioId(), LocalDateTime.now().toString());


                    return null;
                }, (resultado) -> {

                    Image image = new Image("/icons/user.png");
                    avatarPreview.setImage(image);
                    txtRutaImg.setText("");

                    mostrarInformacion("Foto eliminada", "", "La foto se elimino exitosamente.");
                    ventanaPrincipalController.refreshUserInfo(userSession);

                }, (ex) -> {

                    if (ex instanceof InterruptedException || ex instanceof java.util.concurrent.CancellationException) {
                        mostrarError("Accion cancelada", "", "Accion cancelada por el usuario");
                    } else if (ex instanceof UserException) {
                        mostrarError("No se pudo guardar", "Ocurrio un problema al guardar los cambios.", "" + ex.getMessage());
                    } else {
                        mostrarError("Error interno", "", "Ocurrio un error inesperado al guardar los cambios." + ex.getMessage());
                        ex.printStackTrace();
                    }

                }, null
        );


    }//onEliminar

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
            if (txtNuevoNombreUsuario.getText().length() > 40) {
                txtNuevoNombreUsuario.setText(oldValue);
            }
        });

        btnGenerarLogs.disableProperty().bind(dpFechaInicio.valueProperty().isNull()
                .or(dpFechaFin.valueProperty().isNull()));


    }//configuraciones

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
        construirMenu();
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
                        mostrarExcepcionThrowable("Error interno",
                                "",
                                "Ocurrio un error inesperado al crear el respaldo. Vuelva a intentarlo mas tarde.",
                                ex);
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

                        lblFechaUltimoRespaldo.setText(fecha + " h");

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
                        mostrarExcepcionThrowable("Error al mostrar datos", "", "" + ex.getMessage(), ex);
                    } else {
                        mostrarExcepcionThrowable("Error interno",
                                "",
                                "Ocurrio un error inesperado al mostrar los datos. Vuelva a intentarlo mas tarde.",
                                ex);
                    }

                }, null
        );

    }//mostrarUltimoRespaldo

    private void verPerfil() {

        if (userSession != null && userSession.getRole() != null && userSession.getRole().equals(RolUser.ADMIN.toString())) {
            lblRol.setText("Administrador");

        } else {
            lblRol.setText("Usuario");
        }


        lblNombreCompleto.setText(userSession.getNombre() + " " + userSession.getApellidos());
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
        avatarImageView.setImage(image);


        double radio = 45;

        Circle circle = new Circle(radio, radio, radio);
        avatarImageView.setClip(circle);


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


    private void mostarPanelCambiarFoto() {
        User user = userService.findUserById(userSession.getUsuarioId());


        String imgRuta = user.getFotoPerfil();

        if (imgRuta != null) {

            File file = new File(imgRuta);

            if (file != null && file.exists()) {
                Image image = new Image(file.toURI().toString());
                avatarPreview.setImage(image);
            } else {
                Image image = new Image("/icons/user.png");
                avatarPreview.setImage(image);
            }

        } else {
            Image image = new Image("/icons/user.png");
            avatarPreview.setImage(image);
        }

        double radio = 55;

        Circle circle = new Circle(radio, radio, radio);
        avatarPreview.setClip(circle);


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

    @FXML
    private void generarLogs(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar logs");
        fileChooser.setInitialFileName("Logs_respaldos");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivo de texto", "*.txt")
        );

        File file = fileChooser.showSaveDialog(null);

        if (file == null)
            return;


        String fechaInicio = dpFechaInicio.getValue() != null ? dpFechaInicio.getValue().toString() : null;
        String fechaFin = dpFechaFin.getValue() != null ? dpFechaFin.getValue().toString() : null;


        taskService.runTask(
                loadingComponentController,
                () -> {

                    List<Respaldo> respaldos = respaldoService.listarRespaldos(fechaInicio, fechaFin);
                    StringBuilder logContenido = new StringBuilder();

                    for (Respaldo respaldo : respaldos) {
                        logContenido.append(respaldo.toString()).append("\n");
                    }
                    GenerarLogs.generarLogRespaldo(file, fechaInicio, fechaFin, logContenido.toString());

                    return null;

                }, (resultado) -> {

                    mostrarInformacion(
                            "Operación completada",
                            "Logs generados",
                            "Los logs se generaron correctamente."
                    );

                }, (ex) -> {

                    if (ex instanceof InterruptedException || ex instanceof java.util.concurrent.CancellationException) {
                        mostrarError("Operación cancelada",
                                "",
                                "La operación fue cancelada por el usuario.");
                    } else if (ex instanceof RespaldoException) {
                        mostrarExcepcionThrowable(
                                "Error al generar logs",
                                "No se pudieron generar los logs.",
                                "Ocurrió un problema al crear el archivo de logs de respaldos.",
                                ex
                        );
                    } else {
                        mostrarExcepcionThrowable(
                                "Error interno",
                                "Error inesperado en el sistema",
                                "Ocurrió un error inesperado al intentar guardar los cambios. Por favor, inténtelo de nuevo más tarde.",
                                ex
                        );
                    }

                }, null
        );

    }//generarLogs

    @FXML
    private void mostrarUsuarios() {
        List<User> usuarios = userService.listarUsuarios();

        listaUsuarios.getItems().setAll(usuarios);

    }//mostrarUsuarios

    @FXML
    private void eliminarUsuarioLocal(ActionEvent actionEvent) {

        User usuarioSeleccionado = listaUsuarios.getSelectionModel().getSelectedItem();

        int index = usuarioSeleccionado.getUsuarioId();

        boolean eliminar = mostrarConfirmacion("Eliminar usuario ",
                "El usuario con el identificador " + index + " sera eliminado de la base de datos local.",
                "¿Desea continuar?",
                "Elimiinar",
                "Cancelar");

        if (!eliminar)
            return;

        taskService.disable(rootPane);

        taskService.runTask(
                loadingComponentController,
                () -> {
                    userService.eliminarUsuarioLocal(index);




                    return null;
                }, (resultado) -> {
                    taskService.enable(rootPane);
                    listaUsuarios.getItems().remove(usuarioSeleccionado);

                    mostrarInformacion(
                            "Operación completada",
                            "Nota usuario eliminado",
                            "El usuario ha sido eliminado del sistema correctamente."
                    );


                }, (ex) -> {
                    taskService.enable(rootPane);

                    if (ex instanceof InterruptedException || ex instanceof java.util.concurrent.CancellationException) {
                        mostrarError("Operación cancelada",
                                "",
                                "La operación fue cancelada por el usuario.");
                    } else if (ex instanceof UserException) {
                        mostrarExcepcionThrowable(
                                "Error interno",
                                "Ocurrio un error al eliminar el usuario.",
                                "Ocurrió un error inesperado al intentar eliminar el usuario seleccionado. Por favor, inténtelo de nuevo más tarde.",
                                ex
                        );
                    } else {
                        mostrarExcepcionThrowable(
                                "Error interno",
                                "Error inesperado en el sistema",
                                "Ocurrió un error inesperado al intentar guardar los cambios. Por favor, inténtelo de nuevo más tarde.",
                                ex
                        );
                    }

                }, null
        );

    }//eliminarUsuarioLocal


    private boolean esAdmin() {
        boolean esAdmin = userSession != null &&
                userSession.getRole() != null &&
                userSession.getRole().equals(RolUser.ADMIN.toString());

        return esAdmin;
    }


}//class
