package com.mastertyres.fxControllers.nota;

import com.mastertyres.cliente.service.ClienteService;
import com.mastertyres.common.ApplicationContextProvider;
import com.mastertyres.common.GenerarPDF;
import com.mastertyres.common.NotaUtils;
import com.mastertyres.fxControllers.imprimirNota.ImprimirNotaController;
import com.mastertyres.fxControllers.ventanaPrincipal.VentanaPrincipalController;
import com.mastertyres.nota.model.Nota;
import com.mastertyres.nota.model.NotaDTO;
import com.mastertyres.nota.model.StatusNota;
import com.mastertyres.nota.service.NotaService;
import com.mastertyres.notaClienteDetalle.model.NotaClienteDetalle;
import com.mastertyres.notaClienteDetalle.service.NotaClienteDetService;
import com.mastertyres.notaDetalle.model.NotaDetalle;
import com.mastertyres.notaDetalle.service.NotaDetalleService;
import com.mastertyres.vehiculo.service.VehiculoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import static com.mastertyres.common.FechaUtils.getFechaFormateada;
import static com.mastertyres.common.FechaUtils.getFechaFormateadaSegundos;
import static com.mastertyres.common.MensajesAlert.*;

@Component
public class NotaController {
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
    private Button btnHistorial;
    @FXML
    private TextField txtBuscar;

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


    private VentanaPrincipalController ventanaPrincipalController;


    private NotaDTO notaSeleccionada;

    public void setVentanaPrincipalController(VentanaPrincipalController controller) {
        this.ventanaPrincipalController = controller;
    }

    private static final int NOTAS_POR_PAGINA = 20;

    private int tamañoPagina = 20;

    @FXML private Pagination PaginadorNotas;

    @FXML
    private void initialize() {
        cargarNota();

        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                cargarNota();
            } else {
                cargarNotasFiltradas(newValue);
            }
        });

        btnEditar.setOnAction(event -> editarNota(notaSeleccionada.getNumNota()));

        btnImprimir.setOnAction(event -> imprimir(notaSeleccionada.getNumNota()));

        btnEliminar.setOnAction(event -> eliminarNota(notaSeleccionada.getNotaId(),notaSeleccionada.getNumNota()));

    }//initialize

    private void configurarPaginador() {
        Page<NotaDTO> paginaInicial =
                notaService.listarNotasPaginado("ACTIVE", 0, tamañoPagina);

        mostrarNotas(paginaInicial.getContent());

        PaginadorNotas.setPageCount(paginaInicial.getTotalPages());
        PaginadorNotas.setCurrentPageIndex(0);

        PaginadorNotas.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            Page<NotaDTO> nuevaPagina =
                    notaService.listarNotasPaginado("ACTIVE", newIndex.intValue(), tamañoPagina);

            mostrarNotas(nuevaPagina.getContent());
        });
    }

    private void configurarPaginadorFiltradas(String filtro) {
        Page<NotaDTO> paginaFiltrada = notaService.buscarNotas(filtro,0, tamañoPagina);
        mostrarNotas(paginaFiltrada.getContent());
        PaginadorNotas.setPageCount(paginaFiltrada.getTotalPages());
        PaginadorNotas.setCurrentPageIndex(0);
        PaginadorNotas.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            Page<NotaDTO> nuevaPagina = notaService.buscarNotas(filtro, newIndex.intValue(), tamañoPagina);
            mostrarNotas(nuevaPagina.getContent());
        });
    }

    private void cargarNota() {
       configurarPaginador();
    }//cargarNota

    private void mostrarNotas(List<NotaDTO> notas) {
        contenedorNotas.getChildren().clear();

        for (NotaDTO nota : notas) {
            VBox card = crearCardNota(nota);
            contenedorNotas.getChildren().add(card);
        }

    }//mostrarNotas

    private VBox crearCardNota(NotaDTO nota) {

      Nota notaBuscar = notaService.buscarPorId(nota.getNotaId());
      NotaDetalle notaDetalle = notaDetalleService.buscarNotaDetalle(notaBuscar);

        NotaClienteDetalle notaClienteDetalle = notaClienteDetService.buscarclienteDetalle(notaBuscar);



        VBox card = new VBox();
        card.setStyle("-fx-background-color: #1A1A1A; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;");
        card.setPrefSize(500, 100);


        Label numeroNota = new Label(nota.getNumNota());
        numeroNota.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: white;");
        Label lblCliente = new Label(
                notaUtils.eliminarPuntos( notaClienteDetalle.getNombreClienteNota())

        );
        lblCliente.setStyle("-fx-text-fill: white;");
        Label lblVehiculo = new Label(
                notaUtils.eliminarPuntos(notaClienteDetalle.getMarcaNota()) + " " +
                     notaUtils.eliminarPuntos(notaClienteDetalle.getModeloNota()) + " " +
                     notaClienteDetalle.getAnioNota()

        );
        lblVehiculo.setStyle("-fx-text-fill: white;");
        Label total = new Label("Total: $" + notaBuscar.getTotal());
        total.setStyle("-fx-text-fill: white;");

        VBox textBox = new VBox(5, numeroNota, lblCliente, lblVehiculo, total);
        HBox contenBox = new HBox(10, textBox);
        card.getChildren().add(contenBox);


        // ========= ESTILOS INTERACTIVOS =========
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #8EB83D; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #1A1A1A; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;"));

        // Al hacer clic
        card.setOnMouseClicked(event -> {
            card.setStyle("-fx-background-color: #8EB83D; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;");
            mostrarDetalleNota(nota);
        });

        return card;
    }//crearCardNota

    private void mostrarDetalleNota(NotaDTO nota) {
        notaSeleccionada = nota;

        btnEditar.setDisable(false);
        btnImprimir.setDisable(false);
        btnDarPlazo.setDisable(false);
        btnEliminar.setDisable(false);
        btnHistorial.setDisable(false);

        switch (nota.getStatusNota()) {

            case "PAGADO" -> lblStatus.setText("PAGADA");
            case "POR_PAGAR" -> lblStatus.setText("POR PAGAR");
            case "VENCIDO" -> lblStatus.setText("VENCIDO");
            case "A_FAVOR" -> lblStatus.setText("PAGADA, CON SALDO A FAVOR");

        }



        String fechaStr = nota.getCreatedAt();
        String fechaStr2 = nota.getFechaVencimiento();

        String fechaFormateada = "N/A";
        String fechaFormateada2 = "N/A";


        if (fechaStr != null && !fechaStr.trim().isEmpty()) {

          fechaFormateada =  getFechaFormateadaSegundos(fechaStr);
        }


        if (fechaStr2 != null && !fechaStr2.trim().isEmpty()) {

         fechaFormateada2 =  getFechaFormateada(fechaStr2);

        }


        lblNumNota.setText(nota.getNumNota());


        lblNumFactura.setText(nota.getNumFactura() != null ? nota.getNumFactura() : "Sin facturar");


        lblCliente.setText(nota.getNombreClienteNota());
        lblVehiculo.setText(nota.getMarcaNota() + " " + nota.getModeloNota() + " " + nota.getAnioNota());


        lblFechaEmicion.setText(fechaFormateada);
        lblFechaLimite.setText(fechaFormateada2);

        lblAdeudo.setText("$" + nota.getAdeudo());
        lblTotal.setText("$" + nota.getTotal());


        lblSaldoFavor.setText("$" + nota.getSaldoFavor());


    }//mostrarDetalleNota

    private void cargarNotasFiltradas(String filtro) {
        configurarPaginadorFiltradas(filtro);
    }//cargarNotasFiltradas

    @FXML
    private void agregarNotas(ActionEvent actionEvent) {

        ventanaPrincipalController.viewContent(
                null,
                "/fxmlViews/nota/NotaFormulario.fxml",
                "Agregar Nota");
        ventanaPrincipalController.cambiarPaginaEtiqueta.setText("Agregar Nota");


    }//agregarNotas

    private void editarNota(String numNota) {

        Object controllerObj = ventanaPrincipalController.viewContent(
                null,
                "/fxmlViews/nota/EditarNota.fxml",
                "Editar Nota");
        EditarNotaController controller = (EditarNotaController) controllerObj;
        controller.agregarNota(numNota);
        ventanaPrincipalController.cambiarPaginaEtiqueta.setText("Editar Nota");


    }//editarNota

    private void eliminarNota(Integer notaId, String numNota) {

        boolean eliminar = mostrarConfirmacion("Eliminar nota",
                "¿Estas apunto de eliminar la nota " + numNota + " esta accion no se podra deshacer",
                "¿Desea continuar?","Eliminar","Cancelar");
        if (eliminar){
            try {
                notaService.eliminarNota(StatusNota.INACTIVE.toString(), notaId);
                notaService.actualizarUpdatedAtNota(notaId, LocalDateTime.now().toString());
                cargarNota();
                mostrarInformacion("Nota eliminada", "", "La nota se elimino correctamente");
            }catch (Exception e){
                e.printStackTrace();
                mostrarError("No fue posible eliminar la nota","", "Ocurrio un error al eliminar la nota seleccionada " + e.getMessage());

            }

        }//


    }//eliminarNota

    private void imprimir(String numNota) {
        try {
            // 1. Cargar FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/nota/ImprimirNota.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent root = loader.load();

            // 2. Ejecutar tu lógica
            ImprimirNotaController controller = loader.getController();
            controller.agregarNota(numNota);

            // 3. Crear una escena temporal para que JavaFX renderice bien los nodos
            Scene tempScene = new Scene(root);

            // 4. Forzar layout y CSS para que calcule tamaños
            root.applyCss();
            root.layout();

            double escala = 0.9;
            root.setScaleX(escala);
            root.setScaleY(escala);

            //  Aquí sí se renderiza bien, no como antes
            WritableImage snapshot = new WritableImage(
                    (int) root.prefWidth(-1),
                    (int) root.prefHeight(-1)
            );

            root.snapshot(null, snapshot);

            // 5. FileChooser para guardar
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Guardar Nota en PDF");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
            chooser.setInitialFileName("Nota_" + numNota + ".pdf");

            File archivo = chooser.showSaveDialog(root.getScene().getWindow());
            if (archivo == null) return;

            // 6. Generar PDF
            GenerarPDF.generarPDF(snapshot, archivo.getAbsolutePath());




        } catch (Exception e) {
            mostrarError("Error inesperado", "", "Ocurrió un problema al realizar la operación.");
            e.printStackTrace();

        }

    }//imprimir

    public NotaDTO getNotaSeleccionada() {
        return this.notaSeleccionada;
    }

    public void setNotaSeleccionada(final NotaDTO notaSeleccionada) {
        this.notaSeleccionada = notaSeleccionada;
    }
}//class
