package com.mastertyres.fxControllers.historial;

import com.mastertyres.common.exeptions.NotaException;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.service.NotaUtils;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.ApplicationContextProvider;
import com.mastertyres.common.utils.MenuContextSetting;
import com.mastertyres.fxComponents.LoadingComponentController;
import com.mastertyres.common.interfaces.ILoader;
import com.mastertyres.fxControllers.nota.NotaPreviewController;
import com.mastertyres.nota.model.BaseNota;
import com.mastertyres.nota.model.NotaDTO;
import com.mastertyres.nota.model.StatusNota;
import com.mastertyres.nota.service.NotaService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.mastertyres.common.utils.MensajesAlert.mostrarError;

@Component
public class HistorialController extends BaseNota implements IFxController, ILoader {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TilePane contenedorHistorial;
    @FXML
    private VBox detallePromocion;
    @FXML
    private ImageView ivZoom;
    @FXML
    private ChoiceBox<String> choiceLimite;
    @FXML
    private TextField txtBuscar;
    @FXML
    private Button btnBuscar;
    @FXML
    private Label lblNumFactura;
    @FXML
    private Label lblDia;
    @FXML
    private Label lblMes;
    @FXML
    private Label lblAnio;

    @Autowired
    private NotaService notaService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private NotaUtils notaUtils;

    private LoadingComponentController loadingOverlayController;
    private VBox cardSeleccionada = null;
    private NotaDTO notaVentana = null;


    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingOverlayController = loading;

    }

    @FXML
    private void initialize() {

        configuraciones();
        listeners();

    }//initialize

    @Override
    public void configuraciones() {

        MenuContextSetting.disableMenu(rootPane);
        choiceLimite.setValue("Sin Filtro");
        ivZoom.setVisible(false);

    }//configuraciones

    @Override
    public void listeners() {

        btnBuscar.setOnAction(event -> cargarNota());

        ivZoom.setOnMouseClicked(event -> {
            if (notaVentana != null)
                zoomDetalles(notaVentana);

        });

        btnRefrescar.setOnAction(event -> modoReset());

    }//listeners

    private void zoom() {

        detallePromocion.setOnMouseEntered(event -> {
            ivZoom.setVisible(true);
        });

        detallePromocion.setOnMouseExited(event -> {
            ivZoom.setVisible(false);
        });
    }//zoom

    private void zoomDetalles(NotaDTO notaVentana) {

        taskService.runTask(
                loadingOverlayController,
                () -> {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/nota/NotaPreview.fxml"));
                    loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);

                    Parent root = loader.load();
                    Object[] resultado = {root, loader};
                    return resultado;

                }, (resultado) -> {
                    Parent root = (Parent) resultado[0];
                    FXMLLoader loader = (FXMLLoader) resultado[1];

                    Stage stage = new Stage(StageStyle.UTILITY);
                    stage.setTitle("Nota " + notaVentana.getNumNota());
                    stage.setScene(new Scene(root));
                    NotaPreviewController controller = loader.getController();

                    controller.agregarNota(notaVentana);
                    stage.setResizable(false);
                    stage.setMaximized(false);

                    stage.show();
                }, (ex) -> {

                    if (ex.getCause() instanceof Exception) {
                        mostrarError("Error al cargar vista", "", "Ocurrio un error al cargar la vista. Vuelva a intentarlo mas tarde.");
                    }

                }, null
        );


    }//zoomDetalles

    private void cargarNota() {
        contenedorHistorial.setVgap(80);
        contenedorHistorial.setPadding(new Insets(40, 10, 40, 10));
        configurarChoiceBox();
    }//cargarNota

    private void configurarChoiceBox() {
        final String limite; //durante un lamba  no debe cambiar


        if (choiceLimite.getValue() == null || choiceLimite.getValue().equals("") || choiceLimite.getValue().equals("Sin Filtro")) {
            limite = "5";
        } else {
            limite = choiceLimite.getValue().toString();
        }

        if (txtBuscar.getText() == null || txtBuscar.getText().isBlank())
            return;


        taskService.runTask(
                loadingOverlayController,
                () -> {
                    List<NotaDTO> historial = notaService.buscarHistorial(Integer.parseInt(limite), txtBuscar.getText());
                    return historial;
                },
                (resultado) -> {
                    List<NotaDTO> historial = (List<NotaDTO>) resultado;
                    mostrarNotas(historial);
                }, (ex) -> {
                    if (ex instanceof NotaException) {
                        mostrarError("Error al cargar historial", "", "" + ex);
                    } else if (ex instanceof Exception) {
                        mostrarError("Error inesperado", "", "Ocurrio un problema al cargar los datos del historial. Vuelve a intentarlo mas tarde.");

                    }

                    ex.printStackTrace();
                }, null
        );


    }//configurarChoiceBox

    private void mostrarNotas(List<NotaDTO> historial) {
        contenedorHistorial.getChildren().clear();
        cardSeleccionada = null;

        for (NotaDTO nota : historial) {
            VBox card = crearCardNota(nota);
            contenedorHistorial.getChildren().add(card);
        }

    }//mostrarNotas

    private VBox crearCardNota(NotaDTO cardHistorial) {

        VBox card = new VBox();
        String estiloVerde = "-fx-background-color: #1A1A1A; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10";
        String estiloSeleccionado = "-fx-background-color: #8EB83D; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10; -fx-text-fill: #1A1A1A;";
        String estiloPorDefecto = "";
        card.setStyle(estiloVerde);
        card.setUserData(estiloVerde);

        card.setPrefSize(500, 100);

        Label numeroNota = new Label(cardHistorial.getNumNota());
        numeroNota.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: white;");

        Label cliente = new Label(cardHistorial.getNombreClienteNota());
        cliente.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: white;");

        Label fechaYHora = new Label(cardHistorial.getFechaYHora());
        fechaYHora.setStyle("-fx-text-fill: white;");

        Label total = new Label("Total: $" + cardHistorial.getTotal());

        total.setStyle("-fx-text-fill: white;");

        VBox textBox = new VBox(5, numeroNota, cliente, fechaYHora, total);
        HBox contenBox = new HBox(5, textBox);

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

            llenarNota(cardHistorial.getNumNota());
            notaVentana = cardHistorial;
            zoom();

        });

        return card;
    }//crearCardNota

    private void llenarNota(String numNota) {

        taskService.runTask(
                loadingOverlayController,
                () -> {
                    NotaDTO notaPreView = notaService.buscarPorNumNota(StatusNota.ACTIVE.toString(), numNota);
                    return notaPreView;
                }, (notaPreview) -> {
                    llenarNota((NotaDTO) notaPreview);
                }, (ex) -> {
                    if (ex instanceof NotaException) {
                        mostrarError("Error de carga", "", "" + ex);
                    } else if (ex instanceof Exception) {
                        mostrarError("Error al cargar nota", "",
                                "Ocurrio un error inesperado al cargar los detalles de la nota. Vuelve a intentarlo mas tarde.");
                    }
                }, null
        );


    }//llenarNota

    private void llenarNota(NotaDTO notaPreView) {
        //llenar campos

        //fecha
        //codigo para mostrar fecha, la fecha viene en una misma columna en la BD y semuestra en diferentes campos en la Nota

        String strFecha = "", strHoraEntrega = "", strHoraFormateada = "";
        String arrayFecha[];


        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter outPutFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter horaInputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter horaOutputFormatter = DateTimeFormatter.ofPattern("HH:mm");
        strFecha = notaPreView.getFechaYHora();

        String fechaFormateada
                = LocalDateTime.parse(strFecha, inputFormatter)
                .format(outPutFormatter);

        arrayFecha = fechaFormateada.split("-");

        lblDia.setText(arrayFecha[0]);
        lblMes.setText(arrayFecha[1]);
        lblAnio.setText(arrayFecha[2]);

        arrayFecha = strFecha.split(" ");

        strHoraEntrega = arrayFecha[1];
        LocalTime hora = LocalTime.parse(strHoraEntrega, horaInputFormatter);
        strHoraFormateada = hora.format(horaOutputFormatter);


        txtHoraEntrega.setText(strHoraFormateada);


        txtNumNota.setText(notaPreView.getNumNota());

        //datos cliente
        txtNombre.setText(notaPreView.getNombreClienteNota());
        txtDireccion.setText(notaPreView.getDireccion1Nota() != null ? notaPreView.getDireccion1Nota() : "");
        txtDireccion2.setText(notaPreView.getDireccion2Nota() != null ? notaPreView.getDireccion2Nota() : "");
        txtRfc.setText(notaPreView.getRfcNota() != null ? notaPreView.getRfcNota() : "");
        txtCorreo.setText(notaPreView.getCorreoNota() != null ? notaPreView.getCorreoNota() : "");

        //datos vehiculo
        txtMarca.setText(notaPreView.getMarcaNota());
        txtModelo.setText(notaPreView.getModeloNota());
        txtAnioVehiculo.setText(notaPreView.getAnioNota() + "");
        txtKms.setText((notaPreView.getKilometrosNota() != null ? notaPreView.getKilometrosNota() : "") + "");
        txtPlacas.setText(notaPreView.getPlacasNota() != null ? notaPreView.getPlacasNota() : "");


        lblNumFactura.setText("Número de factura: " + (notaPreView.getNumFactura() != null ? notaPreView.getNumFactura() : "N/D"));


    }//llenarNota

    private void modoReset() {

        choiceLimite.setValue("Sin Filtro");
        txtBuscar.setText("");
        lblDia.setText("");
        lblMes.setText("");
        lblAnio.setText("");
        txtNumNota.setText("");
        lblNumFactura.setText("Número de factura: N/D");
        txtNombre.setText("");
        txtDireccion.setText("");
        txtDireccion2.setText("");
        txtRfc.setText("");
        txtCorreo.setText("");
        txtHoraEntrega.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtAnioVehiculo.setText("");
        txtKms.setText("");
        txtPlacas.setText("");
        contenedorHistorial.getChildren().clear();

    }//modoReset


}//class
