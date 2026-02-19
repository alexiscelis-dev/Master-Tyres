package com.mastertyres.controllers.fxControllers.Configuracion;

import com.mastertyres.common.exeptions.RespaldoException;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.interfaces.ILoader;
import com.mastertyres.common.interfaces.IVentanaPrincipal;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.components.fxComponents.LoadingComponentController;
import com.mastertyres.controllers.fxControllers.ventanaPrincipal.VentanaPrincipalController;
import com.mastertyres.respaldo.service.RespaldoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
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

    // Almacenamiento
    @FXML
    private VBox panelCrearRespaldo;

    @FXML
    private VBox panelFechaRespaldo;
    @FXML
    private Button btnRespaldo;

    // Ayuda
    @FXML
    private VBox panelAtajos;
    @FXML
    private VBox panelContacto;


    // FXML — TreeView
    @FXML
    private TreeView<NavItem> menuTree;

    // Mapa: id de panel → nodo FXML
    private final Map<String, Node> panelMap = new HashMap<>();

    // INICIALIZACIÓN
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registrarPaneles();
        construirMenu();
        configurarSeleccion();
    }

    /**
     * Registra todos los paneles en el mapa para acceso rápido por clave.
     */
    private void registrarPaneles() {
        panelMap.put("panelBienvenida", panelBienvenida);
        panelMap.put("panelVerPerfil", panelVerPerfil);
        panelMap.put("panelCambiarContrasena", panelCambiarContrasena);
        panelMap.put("panelEditarCorreo", panelEditarCorreo);
        panelMap.put("panelCambiarNombreUsuario", panelCambiarNombreUsuario);
        panelMap.put("panelCambiarFoto", panelCambiarFoto);
        panelMap.put("panelCrearRespaldo", panelCrearRespaldo);
        panelMap.put("panelFechaRespaldo", panelFechaRespaldo);
        panelMap.put("panelAtajos", panelAtajos);
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
                hoja("✉", "Editar correo", "panelEditarCorreo"),
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
                hoja("⌨", "Atajos", "panelAtajos"),
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

    // HANDLERS DE ACCIONES (FXML onAction)

    @FXML
    private void onCambiarContrasena() {
        System.out.println("Cambiar contraseña");
    }

    @FXML
    private void onGuardarCorreo() {
        System.out.println("Guardar correo");
    }

    @FXML
    private void onCambiarNombreUsuario() {
        System.out.println("Cambiar nombre de usuario");
    }

    @FXML
    private void onSeleccionarFoto() {
        // TODO: abrir FileChooser
        System.out.println("Seleccionar foto");
    }

    @FXML
    private void onGuardarFoto() {
        System.out.println("Guardar foto");
    }

    @FXML
    private void onCrearRespaldoLocal() {

        System.out.println("Crear respaldo");
    }

    @FXML
    private void onElegirRutaRespaldo() {
        System.out.println("Elegir ruta de respaldo");
    }

    @FXML
    private void onCargarRespaldo() {

    }

    @FXML
    private void onElegirArchivoRespaldo() {
        // TODO: abrir FileChooser
        System.out.println("Elegir archivo de respaldo");
    }

    @FXML
    private void onEnviarContacto() {
        System.out.println("Enviar mensaje de contacto");
    }

    @FXML
    private void onAceptar() {
        // TODO: guardar cambios pendientes y cerrar
        System.out.println("Aceptar");
    }

    @FXML
    private void onCancelar() {
        // TODO: descartar cambios y cerrar
        System.out.println("Cancelar");
    }

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
        //   btnRespaldo.setOnAction(event -> crearRespaldo());

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

       boolean confirmacion = mostrarConfirmacion("","Se creara una copia de seguridad local y se guardara una extra en la nube."
                ,"¿Desea continuar?","Continuar","Cancelar");

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




}//class
