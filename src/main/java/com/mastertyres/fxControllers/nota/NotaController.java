package com.mastertyres.fxControllers.nota;

import com.mastertyres.cliente.service.ClienteService;
import com.mastertyres.common.exeptions.NotaException;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.interfaces.ILoader;
import com.mastertyres.common.interfaces.IVentanaPrincipal;
import com.mastertyres.common.service.NotaUtils;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.ApplicationContextProvider;
import com.mastertyres.common.utils.MenuContextSetting;
import com.mastertyres.fxComponents.LoadingComponentController;
import com.mastertyres.fxControllers.historial.HistorialController;
import com.mastertyres.fxControllers.imprimirNota.ImprimirNotaController;
import com.mastertyres.fxControllers.ventanaPrincipal.VentanaPrincipalController;
import com.mastertyres.nota.model.NotaDTO;
import com.mastertyres.nota.model.StatusNota;
import com.mastertyres.nota.service.NotaService;
import com.mastertyres.notaClienteDetalle.service.NotaClienteDetService;
import com.mastertyres.notaDetalle.service.NotaDetalleService;
import com.mastertyres.vehiculo.service.VehiculoService;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CancellationException;

import static com.mastertyres.common.utils.FechaUtils.getFechaFormateada;
import static com.mastertyres.common.utils.FechaUtils.getFechaFormateadaSegundos;
import static com.mastertyres.common.utils.GenerarPDF.generarPDF;
import static com.mastertyres.common.utils.MensajesAlert.*;

@Component
public class NotaController implements IVentanaPrincipal, IFxController, ILoader {
    @FXML
    private AnchorPane ventanaNotas;
    @FXML
    private TilePane contenedorNotas;
    @FXML
    private Label lblStatus;
    @FXML
    private Label lblNumNota;
    @FXML
    private Label lblNumFactura;
    @FXML
    private Label lblCliente;
    @FXML
    private Label lblVehiculo;
    @FXML
    private Label lblFechaEmicion;
    @FXML
    private Label lblFechaLimite;
    @FXML
    private Label lblSaldoFavor;
    @FXML
    private Label lblAdeudo;
    @FXML
    private Label lblTotal;
    @FXML
    private Button btnNuevaNota;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnImprimir;
    @FXML
    private Button btnDarPlazo;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnRefrescar;
    @FXML
    private Button btnHistorial;
    @FXML
    private Button btnBuscar;
    @FXML
    private TextField txtBuscar;
    @FXML
    private DatePicker dpBuscar;
    @FXML
    private CheckBox chkRangoNota;
    @FXML
    private DatePicker dpBuscarFin;
    @FXML
    private Label lblHasta;
    private boolean esRangoActual = false; // Nueva variable de clase
    @FXML
    private Pagination PaginadorNotas;

    @FXML
    private ChoiceBox<String> atributoBusquedaNota;

    @Autowired
    private NotaService notaService;
    @Autowired
    ClienteService clienteService;
    @Autowired
    VehiculoService vehiculoService;
    @Autowired
    NotaClienteDetService notaClienteDetService;
    @Autowired
    private NotaDetalleService notaDetalleService;
    @Autowired
    NotaUtils notaUtils;
    @Autowired
    private TaskService taskService;

    private String filtroActual = "sin filtro";
    private String textoBusquedaActual = "";
    private String textoBusquedaActual2 = "";
    private boolean modoBusqueda = false;
    private boolean esFecha = false;


    //Cambios despues del fork


    private VentanaPrincipalController ventanaPrincipalController;
    private LoadingComponentController loadingOverlayController;
    private NotaDTO notaSeleccionada;
    private VBox cardSeleccionada = null;

    @Override
    public void setVentanaPrincipalController(VentanaPrincipalController controller) {
        this.ventanaPrincipalController = controller;

    }

    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingOverlayController = loading;
    }

    private static final int NOTAS_POR_PAGINA = 20;
    private int tamañoPagina = 20;
    private PauseTransition delayQuery = new PauseTransition(Duration.millis(300));

    @FXML
    private void initialize() {

        configuraciones();
        listeners();
        cargarNota();

    }//initialize

    @Override
    public void configuraciones() {

        MenuContextSetting.disableMenu(ventanaNotas);

        atributoBusquedaNota.setValue("Sin Filtro");

    }//configuraciones

    @Override
    public void listeners() {

        atributoBusquedaNota.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue == null) {
                return;
            }

            switch (newValue.toString()) {

                case "Adeudo" -> filtroActual = "Adeudo";
                case "Saldo a favor" -> filtroActual = "Saldo a favor";

            }//switch

            txtBuscar.clear();
            modoBusqueda = false;

            if (PaginadorNotas.getCurrentPageIndex() == 0) {
                cargarPagina(0);
            } else {
                PaginadorNotas.setCurrentPageIndex(0);
            }

        });


        atributoBusquedaNota.setOnMousePressed(event -> {
                    if (!atributoBusquedaNota.isShowing()) {
                        atributoBusquedaNota.show();
                        atributoBusquedaNota.hide();
                    }
                }
        );

        // Listener para detectar cambios en el ChoiceBox
        atributoBusquedaNota.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            configurarBuscador(newVal);
        });

        // Listener para el CheckBox de rango
        chkRangoNota.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            lblHasta.setVisible(isSelected);
            lblHasta.setManaged(isSelected);
            dpBuscarFin.setVisible(isSelected);
            dpBuscarFin.setManaged(isSelected);
        });

        txtBuscar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                accionBuscar(null);
            }
        });

        btnEditar.setOnAction(event ->
                editarNota(notaSeleccionada.getNumNota()));

        btnImprimir.setOnAction(event ->
                imprimir(notaSeleccionada.getNumNota()));

        btnEliminar.setOnAction(event ->
                eliminarNota(
                        notaSeleccionada.getNotaId(),
                        notaSeleccionada.getNumNota()
                ));

        btnDarPlazo.setOnAction(event ->
                darPlazo(notaSeleccionada.getNotaId()));

        btnRefrescar.setOnAction(event -> {
            resetBusqueda();
            cargarNota();
        });


    }//listeners

    private void configurarBuscador(String criterio) {
        if (criterio == null) return;

        boolean esFecha = criterio.equals("Fecha de emicion") || criterio.equals("Fecha de vencimiento");

        // Toggle de visibilidad para Texto
        txtBuscar.setVisible(!esFecha);
        txtBuscar.setManaged(!esFecha);

        // Toggle de visibilidad para Fecha y Checkbox
        dpBuscar.setVisible(esFecha);
        dpBuscar.setManaged(esFecha);
        chkRangoNota.setVisible(esFecha);
        chkRangoNota.setManaged(esFecha);

        if (!esFecha) {
            chkRangoNota.setSelected(false);
            dpBuscar.setValue(null);
            dpBuscarFin.setValue(null);
        }
    }//configurarBuscador

    private void cargarPagina(int indicePagina) {


        taskService.runTask(
                loadingOverlayController,
                () -> {
                    Page<NotaDTO> pagina = null;

                    if (modoBusqueda) {
                        // Usamos la variable de estado 'esRangoActual' para que el paginado sea consistente
                        if (esRangoActual) {
                            pagina = notaService.buscadorRangos(filtroActual, textoBusquedaActual, textoBusquedaActual2, indicePagina, tamañoPagina);
                        } else {
                            pagina = notaService.buscador(filtroActual, textoBusquedaActual, indicePagina, tamañoPagina);
                        }
                    } else {

                        if ("Adeudo".equals(filtroActual)) {
                            pagina = notaService.buscarPorAdeudo(StatusNota.ACTIVE.toString(), indicePagina, tamañoPagina);
                        } else if ("Saldo a favor".equals(filtroActual)) {
                            pagina = notaService.buscarPorSaldoFavor(StatusNota.ACTIVE.toString(), indicePagina, tamañoPagina);
                        } else {
                            pagina = notaService.listarNotasPaginado(StatusNota.ACTIVE.toString(), indicePagina, tamañoPagina);
                        }

                    }

                    return pagina;
                }, (pagina) -> {

                    mostrarNotas(pagina.getContent());
                    PaginadorNotas.setPageCount(Math.max(pagina.getTotalPages(), 1));

                }, (ex) -> {

                    if (ex instanceof NotaException) {
                        mostrarWarning("Error de busqueda", "Ocurrio un problema al mostrar las notas", "" + ex.getMessage());
                    } else {
                        ex.printStackTrace();
                        ex.getMessage();
                        mostrarError("Error interno", "", "Ocurrio un problema al mostrar las notas");
                    }

                }, null
        );


    }//cargarPagina

    private void ConfigurarNuevoPaginadorBusqueda(String filtro, String busqueda, String busqueda2) {
        modoBusqueda = true;
        filtroActual = filtro;
        textoBusquedaActual = busqueda;
        textoBusquedaActual2 = busqueda2;

        // Guardamos si esta búsqueda fue de rango o no
        esRangoActual = chkRangoNota.isSelected();

        PaginadorNotas.setCurrentPageIndex(0);
        cargarPagina(0);
    }

    private void configurarPaginador() {

        PaginadorNotas.currentPageIndexProperty().addListener(
                (obs, oldIndex, newIndex) -> cargarPagina(newIndex.intValue())
        );

        PaginadorNotas.setCurrentPageIndex(0);
        cargarPagina(0);
    }

    private void cargarNota() {
        contenedorNotas.setVgap(20);
        configurarPaginador();
    }//cargarNota

    private void mostrarNotas(List<NotaDTO> notas) {
        contenedorNotas.getChildren().clear();
        cardSeleccionada = null;

        for (NotaDTO nota : notas) {
            VBox card = crearCardNota(nota);
            contenedorNotas.getChildren().add(card);
        }

    }//mostrarNotas

    private VBox crearCardNota(NotaDTO nota) {

        VBox card = new VBox();
        String estiloVerde = "-fx-background-color: #1A1A1A; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;";
        String estiloAzul = "-fx-background-color: #1A1A1A; -fx-padding: 10; -fx-border-color: #87CEEB; -fx-border-radius: 10; -fx-background-radius: 10;";
        String estiloRojo = "-fx-background-color: #1A1A1A; -fx-padding: 10; -fx-border-color: #FC0000; -fx-border-radius: 10; -fx-background-radius: 10;";
        String estiloAmarillo = "-fx-background-color: #1A1A1A; -fx-padding: 10; -fx-border-color: #FFDF00; -fx-border-radius: 10; -fx-background-radius: 10;";
        String estiloAnaranjado = "-fx-background-color: #1A1A1A; -fx-padding: 10; -fx-border-color: #FF4000; -fx-border-radius: 10; -fx-background-radius: 10;";
        String estiloSeleccionado = "-fx-background-color: #8EB83D; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10; -fx-text-fill: #1A1A1A;";

        String estiloPorDefecto = "";

        switch (nota.getStatusNota()) {

            case "VENCIDO", "POR_PAGAR" -> {

                LocalDate fechaVencimiento = LocalDate.parse(nota.getFechaVencimiento());
                LocalDate hoy = LocalDate.now();

                long diasRestantes = ChronoUnit.DAYS.between(hoy, fechaVencimiento);

                if (diasRestantes < 0) {
                    // Si no estaba marcado como vencido, lo actualizamos
                    if (!nota.getStatusNota().equals("VENCIDO")) {
                        notaService.actualizarStatus(StatusNota.VENCIDO.toString(), nota.getNotaId());
                    }
                    estiloPorDefecto = estiloRojo;
                } else if (diasRestantes <= 5) {
                    // Si estaba vencido, lo regresamos a POR_PAGAR
                    if (nota.getStatusNota().equals("VENCIDO")) {
                        notaService.actualizarStatus(StatusNota.POR_PAGAR.toString(), nota.getNotaId());
                    }
                    estiloPorDefecto = estiloAnaranjado;
                } else {
                    // Si estaba vencido, lo regresamos a POR_PAGAR
                    if (nota.getStatusNota().equals("VENCIDO")) {
                        notaService.actualizarStatus(StatusNota.POR_PAGAR.toString(), nota.getNotaId());
                    }
                    estiloPorDefecto = estiloAmarillo;
                }
            }
            case "A_FAVOR" -> estiloPorDefecto = estiloAzul;
            case "PAGADO" -> estiloPorDefecto = estiloVerde;
            default -> estiloPorDefecto = estiloVerde;
        }


        card.setStyle(estiloPorDefecto);
        card.setUserData(estiloPorDefecto); //se guarda el estilo original
        card.setPrefSize(500, 100);


        Label numeroNota = new Label(nota.getNumNota());
        numeroNota.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: white;");


        String nombreCliente = nota.getNombreClienteNota() != null
                ? notaUtils.eliminarPuntos(nota.getNombreClienteNota())
                : "SIN CLIENTE";

        Label lblCliente = new Label(nombreCliente);

        lblCliente.setStyle("-fx-text-fill: white;");

        String marca = nota.getMarcaNota() != null
                ? notaUtils.eliminarPuntos(nota.getMarcaNota())
                : "";

        String modelo = nota.getModeloNota() != null
                ? notaUtils.eliminarPuntos(nota.getModeloNota())
                : "";

        String anio = nota.getAnioNota() != null
                ? nota.getAnioNota().toString()
                : "";

        Label lblVehiculo = new Label((marca + " " + modelo + " " + anio).trim());

        lblVehiculo.setStyle("-fx-text-fill: white;");

        Label total = new Label("Total: $" + nota.getTotal());

        total.setStyle("-fx-text-fill: white;");

        VBox textBox = new VBox(5, numeroNota, lblCliente, lblVehiculo, total);
        HBox contenBox = new HBox(10, textBox);

        card.getChildren().add(contenBox);

        card.setOnMouseEntered(e -> {
            if (card != cardSeleccionada) {
                card.setStyle(estiloSeleccionado);
            }
        });

        card.setOnMouseExited(e -> {
            if (card != cardSeleccionada) {
                card.setStyle((String) card.getUserData());
            }
        });

        card.setOnMouseClicked(e -> {
            if (cardSeleccionada != null && cardSeleccionada != card) {
                cardSeleccionada.setStyle((String) cardSeleccionada.getUserData());
            }
            cardSeleccionada = card;
            card.setStyle(estiloSeleccionado);
            mostrarDetalleNota(nota);
        });


        return card;
    }//crearCardNota

    private void mostrarDetalleNota(NotaDTO nota) {

        notaSeleccionada = nota;
        btnEditar.setDisable(false);
        btnImprimir.setDisable(false);
        btnEliminar.setDisable(false);


        switch (nota.getStatusNota()) {

            case "PAGADO" -> {
                lblStatus.setText("PAGADA");
                btnDarPlazo.setDisable(true);

            }
            case "POR_PAGAR" -> {
                lblStatus.setText("POR PAGAR");
                btnDarPlazo.setDisable(false);
            }
            case "VENCIDO" -> {
                lblStatus.setText("VENCIDO");
                btnDarPlazo.setDisable(false);
            }
            case "A_FAVOR" -> {
                lblStatus.setText("PAGADA, CON SALDO A FAVOR");
                btnDarPlazo.setDisable(true);
            }

        }


        String fechaStr = nota.getCreatedAt();
        String fechaStr2 = nota.getFechaVencimiento();

        String fechaFormateada = "N/D";
        String fechaFormateada2 = "N/D";


        if (fechaStr != null && !fechaStr.trim().isEmpty()) {

            fechaFormateada = getFechaFormateadaSegundos(fechaStr);
        }


        if (fechaStr2 != null && !fechaStr2.trim().isEmpty()) {

            fechaFormateada2 = getFechaFormateada(fechaStr2);

        }


        lblNumNota.setText(nota.getNumNota());


        lblNumFactura.setText(nota.getNumFactura() != null ? nota.getNumFactura() : "Sin facturar");


        //lblCliente.setText(nota.getNombreClienteNota());
        //lblVehiculo.setText(nota.getMarcaNota() + " " + nota.getModeloNota() + " " + nota.getAnioNota());
        String nombreCliente = nota.getNombreClienteNota() != null
                ? notaUtils.eliminarPuntos(nota.getNombreClienteNota())
                : "SIN CLIENTE";

        String marca = nota.getMarcaNota() != null
                ? notaUtils.eliminarPuntos(nota.getMarcaNota())
                : "";

        String modelo = nota.getModeloNota() != null
                ? notaUtils.eliminarPuntos(nota.getModeloNota())
                : "";

        String anio = nota.getAnioNota() != null
                ? nota.getAnioNota().toString()
                : "";

        lblCliente.setText(nombreCliente);
        lblVehiculo.setText((marca + " " + modelo + " " + anio).trim());


        lblFechaEmicion.setText(fechaFormateada);
        lblFechaLimite.setText(fechaFormateada2);

        lblAdeudo.setText("$" + nota.getAdeudo());
        lblTotal.setText("$" + nota.getTotal());


        lblSaldoFavor.setText("$" + nota.getSaldoFavor());


    }//mostrarDetalleNota

    @FXML
    private void agregarNotas(ActionEvent actionEvent) {

        ventanaPrincipalController.viewContent(
                null,
                "/fxmlViews/nota/NotaFormulario.fxml",
                "Agregar Nota");
        ventanaPrincipalController.cambiarPaginaEtiqueta.setText("AGREGAR NOTA");


    }//agregarNotas

    private void editarNota(String numNota) {

        Object controllerObj = ventanaPrincipalController.viewContent(
                null,
                "/fxmlViews/nota/EditarNota.fxml",
                "Editar Nota");
        EditarNotaController controller = (EditarNotaController) controllerObj;
        controller.agregarNota(numNota);
        ventanaPrincipalController.cambiarPaginaEtiqueta.setText("EDITAR NOTA");


    }//editarNota

    private void eliminarNota(Integer notaId, String numNota) {

        boolean eliminar = mostrarConfirmacion("Eliminar nota",
                "¿Estas apunto de eliminar la nota " + numNota + " esta accion no se podra deshacer",
                "¿Desea continuar?", "Eliminar", "Cancelar");

        if (eliminar) {

            taskService.runTask(
                    loadingOverlayController,
                    () -> {
                        notaService.eliminarNota(StatusNota.INACTIVE.toString(), notaId);
                        notaService.actualizarUpdatedAtNota(notaId, LocalDateTime.now().toString());
                        return null;
                    }, (resultado) -> {
                        cargarNota();
                        mostrarInformacion("Nota eliminada", "", "La nota se elimino correctamente");
                    }, (excepcion) -> {
                        excepcion.printStackTrace();
                        mostrarError("Error al eliminar", "", "No fue posible eliminar la nota. Intente de nuevo mas tarde.");
                    }, null
            );

        }//eliminar


    }//eliminarNota

    private void imprimir(String numNota) {
        try {
            final double NOTA_WITDTH = 960;
            final double NOTA_HEIGHT = 1590;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/nota/ImprimirNota.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);

            Parent root = loader.load();
            ImprimirNotaController controller = loader.getController();
            controller.agregarNota(numNota, true);


            AnchorPane contenedorImprimir = controller.getRootPane();

            double scaleX = 3.0;
            double scaleY = 2.5;

            Scale scale = new Scale(scaleX, scaleY);
            scale.setPivotY(0);
            scale.setPivotX(0);

            contenedorImprimir.getTransforms().add(scale);


            Scene tempScene = new Scene(contenedorImprimir);
            contenedorImprimir.applyCss();
            contenedorImprimir.layout();


            double ancho = contenedorImprimir.prefWidth(-1) * scaleX;
            double alto = contenedorImprimir.prefHeight(-1) * scaleY;

            WritableImage nota1 = new WritableImage(
                    (int) ancho,
                    (int) alto
            );

            if (this.ventanaPrincipalController != null) {
                this.ventanaPrincipalController.configurarControlador(controller);
            }


            contenedorImprimir.snapshot(null, nota1);

            controller.agregarNota(numNota, false);

            WritableImage nota2 = new WritableImage(
                    (int) ancho,
                    (int) alto
            );
            contenedorImprimir.snapshot(null, nota2);

            contenedorImprimir.getTransforms().remove(scale);

            FileChooser chooser = new FileChooser();
            chooser.setTitle("Guardar Nota en PDF");
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF", "*.pdf")
            );
            chooser.setInitialFileName("Nota_" + numNota + ".pdf");

            File archivo = chooser.showSaveDialog(null);
            if (archivo == null) return;

            taskService.runTask(
                    loadingOverlayController,
                    () -> {
                        generarPDF(nota1, nota2, archivo.getAbsolutePath());
                        return null;
                    },
                    (resultado) -> {
                        mostrarInformacion("Nota creada", "", "Se generó el documento exitosamente");
                    },
                    (excepcion) -> {
                        if (excepcion instanceof IOException) {
                            mostrarError("Error al generar archivo",
                                    "El archivo no pudo crearse o está siendo usado por otro programa.",
                                    "Cierra otros programas e inténtalo de nuevo.");
                        } else if (excepcion instanceof InterruptedException || excepcion instanceof CancellationException) {
                            mostrarWarning("Operación cancelada",
                                    "",
                                    "La impresión del documento fue cancelada por el usuario.");
                        } else {
                            excepcion.printStackTrace();
                            mostrarWarning("Operacion cancelada", "", "La impresion del documento fue cancelada");
                        }

                    }, null
            );


        } catch (Exception e) {
            e.printStackTrace();
            mostrarError(
                    "Error inesperado",
                    "",
                    "Ocurrió un problema al preparar la nota."
            );

        }

    }//imprimir

    private void darPlazo(Integer notaId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/nota/DarPlazo.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent root = loader.load();

            DarPlazoController controller = loader.getController();
            controller.setNota(notaId);

            Stage stage = new Stage(StageStyle.UTILITY);
            stage.setTitle("Dar plazo a nota");
            stage.setScene(new Scene(root));
            stage.setResizable(false);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();


            notaSeleccionada.setFechaVencimiento(controller.fecha); //actualiza la fecha antes de crear la nueva card
            notaSeleccionada.setStatusNota(controller.status);


            cargarNota();
            mostrarDetalleNota(notaSeleccionada);


        } catch (Exception e) {
            mostrarError("Error inesperado", "", "Ocurrió un problema al realizar la operación.");
            e.printStackTrace();

        }


    }//darPlazo

    @FXML
    private void historial(ActionEvent even) {
        try {
            Object controllerObj = ventanaPrincipalController.viewContent(
                    null,
                    "/fxmlViews/historial/Historial.fxml",
                    "Historial Notas"
            );
            HistorialController controller = (HistorialController) controllerObj;
            ventanaPrincipalController.cambiarPaginaEtiqueta.setText("HISTORIAL DE NOTAS");


        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al cargar la vista", "", "Ocurrio un problema al cargar la vista. Vuelve a intentarlo mas tarde.");
        }

    }

    private void resetBusqueda() {

        modoBusqueda = false;
        filtroActual = "sin filtro";
        textoBusquedaActual = "";

        txtBuscar.clear();
        atributoBusquedaNota.setValue("Sin Filtro");

        PaginadorNotas.setCurrentPageIndex(0);
        cargarPagina(0);
    }

    @FXML
    public void accionBuscar(ActionEvent actionEvent) {

        String seleccion = atributoBusquedaNota.getValue();
        if (seleccion == null) {
            atributoBusquedaNota.setValue("Sin Filtro");
            return;
        }


        if (seleccion != null && atributoBusquedaNota.equals("Total")) {
            String arrayTotal[] = txtBuscar.getText().trim().split(",");

            if (!txtBuscar.getText().isEmpty()) {

                if (arrayTotal.length == 1) {
                    arrayTotal = new String[]{arrayTotal[0], arrayTotal[0]};
                }

                if (arrayTotal.length > 2) {
                    mostrarWarning("Valores invalidos.", "", "Solo se permiten dos valores separados por una coma.");
                    return;
                }

                try {

                    String valor1 = arrayTotal[0].trim();
                    String valor2 = (arrayTotal.length == 2)
                            ? arrayTotal[1].trim()
                            : valor1;

                    Double total1 = Double.parseDouble(arrayTotal[0].trim());
                    Double total2 = Double.parseDouble(arrayTotal[1].trim());

                    Page<NotaDTO> notaTotal = notaService.buscarPorTotal(total1, total2, StatusNota.ACTIVE.toString(), 0, 20);
                    mostrarNotas(notaTotal.getContent());


                } catch (NumberFormatException e) {
                    mostrarWarning("Valores invalidos", "", "Ingrese un rango de valores numericos separados por una coma (,) ej. 0,0.");
                    return;
                }
            }


        }//if-total



        String busqueda = "";
        String busqueda2 = "";
        boolean esFecha = seleccion.equals("Fecha de emicion") || seleccion.equals("Fecha de vencimiento");

        if (esFecha) {
            // VALIDACIÓN 1: Fecha de inicio nula
            if (dpBuscar.getValue() == null) {
                mostrarWarning("Fecha requerida", null, "Por favor seleccione la fecha inicial.");
                return;
            }

            busqueda = dpBuscar.getValue().toString();

            if (chkRangoNota.isSelected()) {
                // VALIDACIÓN 2: Fecha final nula en rango
                if (dpBuscarFin.getValue() == null) {
                    mostrarWarning("Rango incompleto", null, "Ha activado búsqueda por rango, seleccione la fecha final.");
                    return;
                }

                // VALIDACIÓN 3: Orden de fechas (Inicio no puede ser mayor a Fin)
                if (dpBuscar.getValue().isAfter(dpBuscarFin.getValue())) {
                    // Invertimos o avisamos. Aquí avisaremos:
                    mostrarWarning("Rango inválido", "Fecha incoherente", "La fecha de inicio no puede ser posterior a la fecha final.");
                    return;
                }

                busqueda2 = dpBuscarFin.getValue().toString();
            }
        } else {
            busqueda = txtBuscar.getText();
            if (busqueda == null || busqueda.isBlank()) {
                resetBusqueda();
                return;
            }
        }

        // Si pasó las validaciones, configuramos el paginador
        ConfigurarNuevoPaginadorBusqueda(seleccion.toLowerCase(), busqueda, busqueda2);

    }//accionBuscar

}//class
