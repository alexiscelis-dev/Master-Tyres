package com.mastertyres.controllers.fxControllers.inventario;

import com.mastertyres.common.exeptions.InventarioException;
import com.mastertyres.common.interfaces.ICleanable;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.MensajesAlert;
import com.mastertyres.common.utils.MenuContextSetting;
import com.mastertyres.components.fxComponents.loader.LoadingComponentController;
import com.mastertyres.common.interfaces.ILoader;
import com.mastertyres.inventario.model.Inventario;
import com.mastertyres.inventario.model.StatusInventario;
import com.mastertyres.inventario.service.InventarioService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;

import static com.mastertyres.common.utils.InventarioUtils.generarIdentificador;
import static com.mastertyres.common.utils.InventarioUtils.indicesChoiceBox;

@Component
public class EditarInventarioController implements IFxController, ILoader {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField txtCodBarras;
    @FXML
    private TextField txtDot;
    @FXML
    private TextField txtMarca;
    @FXML
    private TextField txtModelo;
    @FXML
    private ChoiceBox<String> cbIndiceCarga;
    @FXML
    private ChoiceBox<String> cbIndiceVelocidad;
    @FXML
    private ChoiceBox<String> choiceAncho;
    @FXML
    private ChoiceBox<String> choicePerfil;
    @FXML
    private ChoiceBox<String> choiceRin;
    @FXML
    private TextField txtObservaciones;
    @FXML
    private TextField txtPrecioV;
    @FXML
    private TextField txtPrecioC;
    @FXML
    private TextField txtStock;
    @FXML
    private TextField txtImg;
    @FXML
    private VBox vBoxImg;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnImagen;

    @Autowired
    private InventarioService inventarioService;
    @Autowired
    private TaskService taskService;


    private Inventario inventario;
    private BooleanProperty marcaValido = new SimpleBooleanProperty(true);
    private BooleanProperty medidaValido = new SimpleBooleanProperty(true);
    private BooleanProperty stockValido = new SimpleBooleanProperty(true);
    private BooleanProperty precioCompraValido = new SimpleBooleanProperty(true);
    private BooleanProperty precioVentaValido = new SimpleBooleanProperty(true);
    private BooleanProperty codBarrasValido = new SimpleBooleanProperty(true);
    private BooleanProperty dotValido = new SimpleBooleanProperty(true);
    private LoadingComponentController loadingOverlayController;


    @FXML
    private void initialize() {

        configuraciones();
        listeners();

    }//initialize

    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingOverlayController = loading;

    }

    @Override
    public void configuraciones() {


        txtStock.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*(\\.\\d{0,2})?")) {
                return change;
            }
            return null;
        }));

        txtPrecioC.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*(\\.\\d{0,2})?")) {
                return change;
            }
            return null;
        }));

        txtPrecioV.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*(\\.\\d{0,2})?")) {
                return change;
            }
            return null;
        }));


        MenuContextSetting.disableMenu(rootPane); //Desabilita el menu en los componentes
        indicesChoiceBox(cbIndiceVelocidad, cbIndiceCarga, choiceAncho, choicePerfil, choiceRin); //Agrega la lista de indices de carga y velocidad



    }//configuraciones

    @Override
    public void listeners() {

        configurarValidaciones(); //Verifica validaciones de sintaxis

        //Evitan que la lista del choiceBox se muestre en otro lado que no se abajo del mismo ChoiceBox

        choiceAncho.setOnMousePressed(event -> {
            if (!choiceAncho.isShowing()) {
                choiceAncho.show();
                choiceAncho.hide();
            }
        });

        choicePerfil.setOnMousePressed(event -> {
            if (!choicePerfil.isShowing()) {
                choicePerfil.show();
                choicePerfil.hide();
            }
        });

        choiceRin.setOnMousePressed(event -> {
            if (!choiceRin.isShowing()){
                choiceRin.show();
                choiceRin.hide();
            }
        });


        cbIndiceVelocidad.setOnMousePressed(event -> {
            if (!cbIndiceVelocidad.isShowing()) {
                cbIndiceVelocidad.show();
                cbIndiceVelocidad.hide();
            }

        });
        cbIndiceCarga.setOnMousePressed(event -> {
            if (!cbIndiceCarga.isShowing()) {
                cbIndiceCarga.show();
                cbIndiceCarga.hide();
            }
        });

        btnActualizar.setOnAction(event -> {

            actualizarInventario();
        });

        btnImagen.setOnAction(event -> seleccionarImg());

    }//liisteners


    public void editarInventario(Inventario inventario) {
        this.inventario = inventario;
        char medida[] = inventario.getMedida().toCharArray();

        String ancho = "";
        String perfil = "";
        String rin = "";

        for (int i = 0; i < 3; i++)
            ancho += medida[i];

        for (int i = 4; i < 6; i++)
            perfil += medida[i];

        for (int i = 7; i < 9; i++)
            rin += medida[i];


        txtCodBarras.setText(inventario.getCodigoBarras() != null ? inventario.getCodigoBarras() : "");
        txtDot.setText(inventario.getDot() != null ? inventario.getDot() : "");
        txtMarca.setText(inventario.getMarca());
        txtModelo.setText(inventario.getModelo());
        cbIndiceCarga.setValue(inventario.getIndiceCarga());
        cbIndiceVelocidad.setValue(inventario.getIndiceVelocidad());
        txtStock.setText(inventario.getStock() + "");
        txtPrecioC.setText(inventario.getPrecioCompra() + "");
        txtPrecioV.setText(inventario.getPrecioVenta() + "");
        txtObservaciones.setText(inventario.getObservaciones());
        choiceAncho.setValue(ancho);
        choicePerfil.setValue(perfil);
        choiceRin.setValue(rin);
        vBoxImg.setVisible(true);
        //txtImg.setText(inventario.getImagen());
        String imagenPath = inventario.getImagen() != null ? inventario.getImagen() : "";
        txtImg.setText(imagenPath);

        File file = new File(txtImg.getText());

//        if (file.exists()) {
//            Image image = new Image(file.toURI().toString());
//            ImageView imageView = new ImageView(image);
//            imageView.setFitHeight(200);
//            imageView.setFitWidth(200);
//            imageView.setPreserveRatio(true);
//            vBoxImg.getChildren().clear();
//            vBoxImg.getChildren().add(imageView);
        Image image;
        if (!imagenPath.isBlank() && new File(imagenPath).exists()) {
            image = new Image(new File(imagenPath).toURI().toString());
        } else {
            image = new Image(getClass().getResource("/icons/imagenPorDefecto.jpg").toExternalForm());
//            Image image = new Image(getClass().getResource("/icons/imagenPorDefecto.jpg").toExternalForm());
//            ImageView imageView = new ImageView(image);
//            imageView.setFitHeight(200);
//            imageView.setFitWidth(200);
//            imageView.setPreserveRatio(true);
//            vBoxImg.getChildren().clear();
//            vBoxImg.getChildren().add(imageView);
        }

        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);
        vBoxImg.getChildren().clear();
        vBoxImg.getChildren().add(imageView);

    }//editarInventario


    private void configurarValidaciones() {

        txtCodBarras.textProperty().addListener(((observable, oldText, newText) -> {
            if (!txtCodBarras.getText().matches("(\\d{8,13})?")) {
                codBarrasValido.set(false);
                txtCodBarras.setStyle("-fx-border-color: red;");
            } else {
                codBarrasValido.set(true);
                txtCodBarras.setStyle("");
            }

        }));

        txtDot.textProperty().addListener(((observable, oldtext, newTex) -> {
            if (txtDot.getText() != null)
                txtDot.setText(txtDot.getText().toUpperCase());

            if (!txtDot.getText().matches("^$|[A-Za-z0-9]{6,9}[0-9]{4}")) {
                dotValido.set(false);
                txtDot.setStyle("-fx-border-color: red;");
            } else {
                txtDot.setStyle("");
                dotValido.set(true);
            }

        }));

        txtMarca.textProperty().addListener(((observable, oldtext, newTex) -> {
            if (txtMarca.getText().isBlank()) {
                txtMarca.setStyle("-fx-border-color: red;");
                marcaValido.set(false);
            } else {
                marcaValido.set(true);
                txtMarca.setStyle("");
            }

        }));


        txtStock.textProperty().addListener(((observable, oldtext, newText) -> {
            if (txtStock.getText().isBlank() || !txtStock.getText().matches("\\d*")) {
                txtStock.setStyle("-fx-border-color: red;");
                stockValido.set(false);
                return;
            }

            try {
                long valor = Long.parseLong(newText);
                if (valor > Integer.MAX_VALUE) {
                    txtStock.setStyle("-fx-border-color: red;");
                    stockValido.set(false);
                } else {
                    txtStock.setStyle("");
                    stockValido.set(true);
                }

            } catch (NumberFormatException e) {
                txtStock.setStyle("-fx-border-color: red;");
                stockValido.set(false);
            }

        }));


        txtPrecioC.textProperty().addListener(((observable, oldtext, newTex) -> {
            if (txtPrecioC.getText().isBlank() || !txtPrecioC.getText().matches("\\d*(\\.\\d{0,2})?")) {
                txtPrecioC.setStyle("-fx-border-color: red;");
                precioCompraValido.set(false);
            } else {
                precioCompraValido.set(true);
                txtPrecioC.setStyle("");
            }

        }));

        txtPrecioV.textProperty().addListener(((observable, oldtext, newTex) -> {
            if (txtPrecioV.getText().isBlank() || !txtPrecioV.getText().matches("\\d*(\\.\\d{0,2})?")) {
                txtPrecioV.setStyle("-fx-border-color: red;");
                precioVentaValido.set(false);
            } else {
                txtPrecioV.setStyle("");
                precioVentaValido.set(true);
            }
        }));


        btnActualizar.disableProperty().bind(
                (txtMarca.textProperty().isEmpty())
                        .or(codBarrasValido.not())
                        .or(dotValido.not())
                        .or(medidaValido.not())
                        .or(cbIndiceCarga.valueProperty().isNull())
                        .or(cbIndiceVelocidad.valueProperty().isNull())
                        .or(txtStock.textProperty().isEmpty())
                        .or(stockValido.not())
                        .or(txtPrecioC.textProperty().isEmpty())
                        .or(precioCompraValido.not())
                        .or(txtPrecioV.textProperty().isEmpty())
                        .or(precioVentaValido.not())

        );

    }//configurarValidaciones

    private void actualizarInventario() {

        boolean confirmar = MensajesAlert.mostrarConfirmacion(
                "Confirmar actualización",
                "Guardar cambios",
                "¿Está seguro de que desea guardar los cambios seleccionados? Se actualizarán los datos del registro de inventario.",
                "Sí, guardar",
                "Cancelar"
        );

        if (!confirmar) return; //Evita que siga si el usuario cancela

        //cerrarVentana(); // cierra la ventana emergente para que al momento de actualizar el loading pueda bloaquear toda la pantalla
        String codigoBarras = (txtCodBarras.getText() != null && !txtCodBarras.getText().isBlank())
                ? txtCodBarras.getText()
                : null;

        String dot = (txtDot.getText() != null && !txtDot.getText().isBlank())
                ? txtDot.getText()
                : null;


        inventario.setIdentificadorLlanta(generarIdentificador(txtMarca, txtModelo, choiceAncho, choicePerfil, choiceRin, cbIndiceCarga, cbIndiceVelocidad));
        inventario.setCodigoBarras(codigoBarras);
        inventario.setDot(dot);
        inventario.setMarca(txtMarca.getText().trim());
        inventario.setModelo(txtModelo.getText().trim());
        inventario.setIndiceCarga(cbIndiceCarga.getValue());
        inventario.setIndiceVelocidad(cbIndiceVelocidad.getValue());
        inventario.setStock(Integer.parseInt(txtStock.getText()));
        inventario.setPrecioCompra(Float.parseFloat(txtPrecioC.getText()));
        inventario.setPrecioVenta(Float.parseFloat(txtPrecioV.getText()));
        inventario.setObservaciones(txtObservaciones.getText());
        inventario.setImagen(txtImg.getText());


        if (Integer.parseInt(txtStock.getText()) == 0)
            inventario.setActive(StatusInventario.SIN_STOCK.toString());

        else if (Integer.parseInt(txtStock.getText()) > 0)
            inventario.setActive(StatusInventario.ACTIVE.toString());


        taskService.runTask(
                loadingOverlayController,
                () ->{

                    inventarioService.actualizarUptatedAt(LocalDateTime.now().toString(), inventario.getInventarioId());
                    inventarioService.actualizarInventario(inventario);

                    return null;
                },
                (resultado) -> {


                    MensajesAlert.mostrarInformacion(
                            "Operación completada",
                            "Inventario actualizado",
                            "Los datos del inventario se han actualizado correctamente en el sistema."
                    );
                    cerrarVentana();


                },
                (ex) -> {
                    if (ex.getCause() instanceof InventarioException) {
                        MensajesAlert.mostrarExcepcionThrowable(
                                "Error al actualizar",
                                "Problema con el registro del inventario",
                                "Ocurrió un problema al intentar actualizar los datos. ",
                                ex
                        );
                    } else if (ex.getCause() instanceof InterruptedException || ex.getCause() instanceof java.util.concurrent.CancellationException) {
                        MensajesAlert.mostrarWarning(
                                "Operación cancelada",
                                "Acción interrumpida",
                                "La acción fue cancelada por el usuario."
                        );
                    } else {
                        MensajesAlert.mostrarExcepcionThrowable(
                                "Error inesperado",
                                "Fallo en el sistema",
                                "Ocurrió un error inesperado y no fue posible actualizar el inventario. Por favor, inténtelo de nuevo más tarde.",
                                ex
                        );
                    }
                },null
        );
        


    }//actualizarInventario

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) txtCodBarras.getScene().getWindow();
        stage.close();

    }//cerrarVentana

    private void seleccionarImg() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(
                "Archivos de imagen", "*.png", "*.jpg", "*.jpeg"
        ));

        Stage stage = (Stage) btnImagen.getScene().getWindow();
        File archivo = fileChooser.showOpenDialog(stage);

        if (archivo != null) {
            String url = archivo.getAbsolutePath();
            txtImg.setText(url);

            File file = new File(archivo.getAbsolutePath());
            Image image = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(200);
            imageView.setFitHeight(200);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);
            vBoxImg.getChildren().clear();
            vBoxImg.getChildren().add(imageView);
        }

    } //seleccionarImg

}//EditarInventario
