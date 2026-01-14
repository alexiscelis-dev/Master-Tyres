package com.mastertyres.fxControllers.AgregarInventario;

import com.mastertyres.common.exeptions.InventarioException;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.MenuContextSetting;
import com.mastertyres.fxComponents.LoadingComponentController;
import com.mastertyres.fxComponents.interfaces.ILoading;
import com.mastertyres.fxControllers.ventanaPrincipal.VentanaPrincipalController;
import com.mastertyres.inventario.model.Inventario;
import com.mastertyres.inventario.service.InventarioService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

import static com.mastertyres.common.utils.InventarioUtils.generarIdentificador;
import static com.mastertyres.common.utils.InventarioUtils.indicesChoiceBox;
import static com.mastertyres.common.utils.MensajesAlert.*;


@Component
public class AgregarInventarioController implements ILoading {
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
    private TextField txtObservaciones;
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
    private TextField txtStock;
    @FXML
    private TextField txtPrecioC;
    @FXML
    private TextField txtPrecioV;
    @FXML
    private TextField txtImg;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnRegistrar;
    @FXML
    private Button btnImagen;
    @FXML
    private Button btnRefrescar;

    @Autowired
    private InventarioService inventarioService;
    @Autowired
    private TaskService taskService;

    private BooleanProperty codBarrasValido = new SimpleBooleanProperty(true);
    private BooleanProperty dotValido = new SimpleBooleanProperty(true);
    private BooleanProperty marcaValido = new SimpleBooleanProperty(true);
    private BooleanProperty medida1Valido = new SimpleBooleanProperty(true);
    private BooleanProperty medida2Valido = new SimpleBooleanProperty(true);
    private BooleanProperty indiceCargaValido = new SimpleBooleanProperty(true);
    private BooleanProperty indiceVelocidadValido = new SimpleBooleanProperty(true);
    private BooleanProperty stockValido = new SimpleBooleanProperty(true);
    private BooleanProperty precioCompraValido = new SimpleBooleanProperty(true);
    private BooleanProperty precioventaValido = new SimpleBooleanProperty(true);

    private VentanaPrincipalController ventanaPrincipalController;
    private LoadingComponentController loadingOverlayController;


    public void setVentanaPrincipalController(VentanaPrincipalController controller) {
        this.ventanaPrincipalController = controller;
    }
    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingOverlayController = loading;
    }

    @FXML
    private void initialize() {

        configuraciones();

        btnRegistrar.setOnAction(event -> registrar());

        btnImagen.setOnAction(event -> seleccionarImg());


    }//ininitialize

    private void configuraciones(){
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

        choicePerfil.setOnMousePressed(event -> {
            if (!choicePerfil.isShowing()){
                choicePerfil.show();
                choicePerfil.hide();

            }
        });

        MenuContextSetting.disableMenu(rootPane); //Quita el menu contextual del clic derecho

        indicesChoiceBox(cbIndiceVelocidad,cbIndiceCarga,choiceAncho,choicePerfil,choiceRin);
        btnLimpiar.setOnAction(event -> clean());

        //Abre y cierra el popup del choiceBox para que aparezca en la posicion correcta y no debajo de la tabla
        cbIndiceCarga.setOnMousePressed(event -> {
            if (!cbIndiceCarga.isShowing()) {
                cbIndiceCarga.show();
                cbIndiceCarga.hide();
            }
        });

        cbIndiceVelocidad.setOnMousePressed(event -> {
            if (!cbIndiceVelocidad.isShowing()) {
                cbIndiceVelocidad.show();
                cbIndiceVelocidad.hide();
            }
        });

        choiceAncho.setOnMousePressed(event -> {
            if (!choiceAncho.isShowing()){
                choiceAncho.show();
                choiceAncho.hide();


            }
        });

        choiceRin.setOnMousePressed(event -> {
            if (!choiceRin.isShowing()){
                choiceRin.show();
                choiceRin.hide();
            }
        });
    }

    private void clean() {
        txtCodBarras.setText("");
        txtDot.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        cbIndiceCarga.setValue(null);
        cbIndiceVelocidad.setValue(null);
        choiceAncho.setValue(null);
        choicePerfil.setValue(null);
        choiceRin.setValue(null);
        txtStock.setText("");
        txtPrecioC.setText("");
        txtPrecioV.setText("");
        txtImg.setText("");
        txtObservaciones.setText("");
        txtCodBarras.setStyle("");
        txtDot.setStyle("");
        txtMarca.setStyle("");
        txtPrecioC.setStyle("");
        txtPrecioV.setStyle("");
        txtStock.setStyle("");


    }//clean

    private void registrar() {



        if (!empty()) {
            String medida = choiceAncho.getValue() + "/" + choicePerfil.getValue() + "R" + choiceRin.getValue();
            String identificadorLlanta = generarIdentificador(txtMarca,txtModelo,choiceAncho,choicePerfil,choiceRin,cbIndiceCarga,cbIndiceVelocidad);

            String codigoBarras = (txtCodBarras.getText() != null && !txtCodBarras.getText().isBlank())
                    ? txtCodBarras.getText()
                    : null;

            String dot = (txtDot.getText() != null && !txtDot.getText().isBlank())
                    ? txtDot.getText()
                    : null;




            Inventario inventario = Inventario.builder()
                    .identificadorLlanta(identificadorLlanta)
                    .codigoBarras(codigoBarras)
                    .dot(dot)
                    .marca(txtMarca.getText())
                    .modelo(txtModelo.getText())
                    .medida(medida)
                    .indiceCarga(cbIndiceCarga.getValue() != null ? cbIndiceCarga.getValue() : "")
                    .indiceVelocidad(cbIndiceVelocidad.getValue() != null ? cbIndiceVelocidad.getValue() : "")
                    .stock(Integer.parseInt(txtStock.getText()))
                    .precioCompra(Float.parseFloat(txtPrecioC.getText()))
                    .precioVenta(Float.parseFloat(txtPrecioV.getText()))
                    .observaciones(txtObservaciones.getText())
                    .imagen(txtImg.getText() != null ? txtImg.getText() : "")
                    .build();


                taskService.runTask(

                        loadingOverlayController,
                        () -> {
                            Thread.sleep(5000);
                            inventarioService.guardarInventario(inventario);
                            return null;
                        },
                        (resultado) ->{
                            mostrarInformacion("Elemento agregado", "", "Elemento agregado al inventario correctamente.");
                            clean();

                        },(ex) ->{
                            if (ex.getCause() instanceof InterruptedException || ex.getCause() instanceof java.util.concurrent.CancellationException){
                                ex.printStackTrace();
                                mostrarError("Operacion cancelada", "", "La acción fue cancelada por el usuario. ");
                            } else if (ex.getCause() instanceof InventarioException) {
                                ex.printStackTrace();
                                mostrarError("Error al agregar al inventario","","" + ex.getCause().getMessage());
                            }else {
                                ex.printStackTrace();
                                mostrarError("Error inesperado", "", "No se pudo guardar el inventario, vuelva a intentarlo más tarde.");
                            }
                        },null
                );



        } else {
            mostrarWarning("Campos oblicatorios", "", "los campos marcados con '*' son obligatorios");
        }

    }//registrar


    // solo verifica si los campos necesarios estan vacios (marca,medida,stock,precios)

    private boolean empty() {
        boolean empty = false;


        if (txtMarca.getText().isEmpty()) {
            txtMarca.setStyle("-fx-border-color:red; -fx-border-width:2px;");
            empty = true;
        }
        if (txtMarca.getText().isEmpty()) {
            txtMarca.setStyle("-fx-border-color:red; -fx-border-width:2px;");
            empty = true;
        }


        if (txtPrecioC.getText().isEmpty()) {
            txtPrecioC.setStyle("-fx-border-color:red; -fx-border-width:2px;");
            empty = true;
        }
        if (txtPrecioV.getText().isEmpty()) {
            txtPrecioV.setStyle("-fx-border-color:red; -fx-border-width:2px;");
            empty = true;
        }

        return empty;
    }// empty

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
        }

    } //seleccionarImg


    private void configurarValidaciones() {
        //

        txtCodBarras.textProperty().addListener((observable, oldText, newText) -> {

            if (!txtCodBarras.getText().matches("(\\d{8,13})?")) {
                codBarrasValido.set(false);
                txtCodBarras.setStyle("-fx-border-color: red;");
            } else {
                codBarrasValido.set(true);
                txtCodBarras.setStyle("");
            }
        });

        txtDot.textProperty().addListener(((observable, oldtext, newText) -> {
            txtDot.setText(txtDot.getText().toUpperCase());
            if (!txtDot.getText().matches("^$|[A-Za-z0-9]{6,9}[0-9]{4}")) {
                dotValido.set(false);
                txtDot.setStyle("-fx-border-color: red;");
            } else {
                txtDot.setStyle("");
                dotValido.set(true);
            }
        }));

        txtMarca.textProperty().addListener(((observable, oldtext, newText) -> {
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



        txtPrecioC.textProperty().addListener(((observable, oldtext, newText) -> {
            boolean valido = true;

            if (txtPrecioC.getText().isBlank() || !txtPrecioC.getText().matches("\\d*(\\.\\d{0,2})?")) {
                valido = false;
            } else {
                try {
                    float valor = Float.parseFloat(newText);
                    if (valor > Float.MAX_VALUE) {
                        valido = false;
                    }
                } catch (NumberFormatException e) {
                    valido = false;
                }
            }
            if (!valido) {
                txtPrecioC.setStyle("-fx-border-color: red;");
                precioCompraValido.set(false);
            } else {
                txtPrecioC.setStyle("");
                precioCompraValido.set(true);
            }
        }));

        txtPrecioV.textProperty().addListener(((observable, oldtext, newText) -> {
            boolean valido = true;

            if (txtPrecioV.getText().isBlank() || !txtPrecioV.getText().matches("\\d*(\\.\\d{0,2})?")) {
                valido = false;
            } else {
                try {
                    float valor = Float.parseFloat(newText);
                    if (valor > Float.MAX_VALUE) {
                        valido = false;
                    }
                } catch (NumberFormatException e) {
                    valido = false;  // si no se puede convertir, también invalido
                }
            }
            if (!valido) {
                txtPrecioV.setStyle("-fx-border-color: red;");
                precioventaValido.set(false);
            } else {
                txtPrecioV.setStyle("");
                precioventaValido.set(true);
            }

        }));

        btnRegistrar.disableProperty().bind(
                (txtMarca.textProperty().isEmpty())
                        .or(txtStock.textProperty().isEmpty())
                        .or(txtPrecioC.textProperty().isEmpty())
                        .or(txtPrecioV.textProperty().isEmpty())
                        .or(stockValido.not())
                        .or(choiceAncho.valueProperty().isNull())
                        .or(choicePerfil.valueProperty().isNull())
                        .or(choiceRin.valueProperty().isNull())
                        .or(precioventaValido.not())
                        .or(precioCompraValido.not())
                        .or(dotValido.not())
                        .or(codBarrasValido.not())


        );

    }//configurarValidaciones


}//class
