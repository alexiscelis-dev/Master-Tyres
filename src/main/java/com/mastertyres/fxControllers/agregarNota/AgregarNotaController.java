package com.mastertyres.fxControllers.agregarNota;

import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.common.ApplicationContextProvider;
import com.mastertyres.common.MenuContextSetting;
import com.mastertyres.common.RegexTools;
import com.mastertyres.fxControllers.nota.BuscarClienteController;
import com.mastertyres.fxControllers.nota.BuscarLlantaController;
import com.mastertyres.fxControllers.nota.RegistrarNotaController;
import com.mastertyres.inventario.model.Inventario;
import com.mastertyres.nota.model.NotaDTO;
import com.mastertyres.nota.model.StatatusSiNo;
import com.mastertyres.vehiculo.model.VehiculoDTO;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.mastertyres.common.MensajesAlert.*;
import static com.mastertyres.common.NotaUtils.*;

@Component
public class AgregarNotaController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button btnBorrarCliente;
    @FXML
    private Button btnBorrarInventario;
    @FXML
    private GridPane gridPaneIcons;
    @FXML
    private StackPane btnShowIcons;
    @FXML
    private TextField txtDia;
    @FXML
    private TextField txtMes;
    @FXML
    private TextField txtAnio;
    @FXML
    private StackPane spPorcentajeGas;
    @FXML
    private TextField txtHoraEntrega;
    @FXML
    private TextField txtNumNota;
    private Arc arcoGas;
    @FXML
    private Canvas canvasGas;
    @FXML
    private Label lblGas;

    //Datos Clientes
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtDireccion2;
    @FXML
    private TextField txtRfc;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtMarca;
    @FXML
    private TextField txtModelo;
    @FXML
    private TextField txtCategoria;
    @FXML
    private TextField txtAnioVehiculo;
    @FXML
    private TextField txtKms;
    @FXML
    private TextField txtPlacas;

    @FXML
    private TextField txtObservaciones;
    @FXML
    private TextField txtObservaciones2;

    //seccion cuando se busca a la llanta
    //Datos nota detalle

    @FXML
    private CheckBox cbRayonesSi;
    @FXML
    private CheckBox cbRayonesNo;
    @FXML
    private CheckBox cbGolpesSi;
    @FXML
    private CheckBox cbGolpesNo;
    @FXML
    private CheckBox cbTaponesSi;
    @FXML
    private CheckBox cbTaponesNo;
    @FXML
    private CheckBox cbTapetesSi;
    @FXML
    private CheckBox cbTapetesNo;
    @FXML
    private CheckBox cbRadioSi;
    @FXML
    private CheckBox cbRadioNo;
    @FXML
    private CheckBox cbGatoSi;
    @FXML
    private CheckBox cbGatoNo;
    @FXML
    private CheckBox cbLlaveSi;
    @FXML
    private CheckBox cbLlaveNo;
    @FXML
    private CheckBox cbLlantaSi;
    @FXML
    private CheckBox cbLlantaNo;
    @FXML
    private CheckBox cbCbRadioSi;
    @FXML
    private CheckBox cbCbRadioNo;
    @FXML
    private TextField txtAlineacion;
    @FXML
    private TextField txtAlineacionCantidad;
    @FXML
    private TextField txtAlineacionUnitario;
    @FXML
    private TextField txtAlineacionTotal;
    @FXML
    private TextField txtBalanceo;
    @FXML
    private TextField txtBalanceoCantidad;
    @FXML
    private TextField txtBalanceoUnitario;
    @FXML
    private TextField txtBalanceoTotal;
    @FXML
    private TextField txtLlantas;
    @FXML
    private TextField txtLlantasCantidad;
    @FXML
    private TextField txtLlantasUnitario;
    @FXML
    private TextField txtLlantasTotal;
    @FXML
    private TextField txtAmorDelanteros;
    @FXML
    private TextField txtAmorDelCantidad;
    @FXML
    private TextField txtAmorDelUnitario;
    @FXML
    private TextField txtAmorDelTotal;
    @FXML
    private TextField txtAmorTraseros;
    @FXML
    private TextField txtAmorTrasCantidad;
    @FXML
    private TextField txtAmorTrasUnitario;
    @FXML
    private TextField txtAmorTrasTotal;
    @FXML
    private TextField txtSuspension;
    @FXML
    private TextField txtSuspensionCantidad;
    @FXML
    private TextField txtSuspensionUnitario;
    @FXML
    private TextField txtSuspensionTotal;
    @FXML
    private TextField txtSuspension2;
    @FXML
    private TextField txtSuspensionCantidad2;
    @FXML
    private TextField txtSuspensionUnitario2;
    @FXML
    private TextField txtSuspensionTotal2;
    @FXML
    private TextField txtMecanica;
    @FXML
    private TextField txtMecanicaCantidad;
    @FXML
    private TextField txtMecanicaUnitario;
    @FXML
    private TextField txtMecanicaTotal;
    @FXML
    private TextField txtMecanica2;
    @FXML
    private TextField txtMecanicaCantidad2;
    @FXML
    private TextField txtMecanicaUnitario2;
    @FXML
    private TextField txtMecanicaTotal2;
    @FXML
    private TextField txtFrenos;
    @FXML
    private TextField txtFrenosCantidad;
    @FXML
    private TextField txtFrenosUnitario;
    @FXML
    private TextField txtFrenosTotal;
    @FXML
    private TextField txtFrenos2;
    @FXML
    private TextField txtFrenosCantidad2;
    @FXML
    private TextField txtFrenosUnitario2;
    @FXML
    private TextField txtFrenosTotal2;
    @FXML
    private TextField txtSubTotalMecanica;  //subtotal mecanica
    @FXML
    private TextField txtSubTotalFrenos;  //subtotal frenos
    @FXML
    private TextField txtSubTotalOtros;  //subtotal otros
    @FXML
    private TextField txtOtros;
    @FXML
    private TextField txtOtrosCantidad;
    @FXML
    private TextField txtOtrosUnitario;
    @FXML
    private TextField txtOtros2;
    @FXML
    private TextField txtOtrosCantidad2;
    @FXML
    private TextField txtOtrosUnitario2;
    @FXML
    private TextField txtOtrosTotal2;
    @FXML
    private TextField txtOtrosTotal;
    @FXML
    private TextField txtTotal; //total de la nota


    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnRefrescar;

    private Cliente clienteNota;
    private Inventario inventarioNota;
    private VehiculoDTO vehiculosNota;
    private int porcentajeGasNota;
    private String rayones;
    private String golpes;
    private String tapones;
    private String tapetes;
    private String radio;
    private String gato;
    private String llave;
    private String llanta;
    private BooleanProperty boolTotal = new SimpleBooleanProperty(true);



    @FXML
    private void initialize() {

        mostrarPopupHora(txtHoraEntrega);

        configuraciones();
        operacionesCampos();

        btnShowIcons.setOnMouseClicked(event -> showIcons(gridPaneIcons));

        spPorcentajeGas.setOnMouseClicked(event -> mostrarSlider(spPorcentajeGas.getScene().getWindow()));
        dibujarGasolina(50);


        btnGuardar.setOnAction(event -> registrar(clienteNota, vehiculosNota, inventarioNota));


    }//initialize

    private void operacionesCampos() {
//Alineacion
        txtAlineacionCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = toFloatSafe(txtAlineacionCantidad.getText()) * toFloatSafe(txtAlineacionUnitario.getText());

            txtAlineacionTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtAlineacionUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = toFloatSafe(txtAlineacionCantidad.getText()) * toFloatSafe(newValue.toString());
            txtAlineacionTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //Balanceo

        txtBalanceoCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = toFloatSafe(txtBalanceoCantidad.getText()) * toFloatSafe(txtBalanceoUnitario.getText());

            txtBalanceoTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtBalanceoUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = toFloatSafe(txtBalanceoCantidad.getText()) * toFloatSafe(newValue.toString());

            txtBalanceoTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //llantas
        txtLlantasCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = toFloatSafe(txtLlantasCantidad.getText()) * toFloatSafe(txtLlantasUnitario.getText());

            txtLlantasTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtLlantasUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = toFloatSafe(txtLlantasCantidad.getText()) * toFloatSafe(newValue.toString());

            txtLlantasTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //amortiguadores delanteros
        txtAmorDelCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = toFloatSafe(txtAmorDelCantidad.getText()) * toFloatSafe(txtAmorDelUnitario.getText());

            txtAmorDelTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtAmorDelUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = toFloatSafe(txtAmorDelCantidad.getText()) * toFloatSafe(newValue.toString());

            txtAmorDelTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //amortiguadores traseros

        txtAmorTrasCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = toFloatSafe(txtAmorTrasCantidad.getText()) * toFloatSafe(txtAmorTrasUnitario.getText());

            txtAmorTrasTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtAmorTrasUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = toFloatSafe(txtAmorTrasCantidad.getText()) * toFloatSafe(newValue.toString());

            txtAmorTrasTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //suspension

        txtSuspensionCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = toFloatSafe(txtSuspensionCantidad.getText()) * toFloatSafe(txtSuspensionUnitario.getText());

            txtSuspensionTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtSuspensionUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = toFloatSafe(txtSuspensionCantidad.getText()) * toFloatSafe(newValue.toString());

            txtSuspensionTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //suspension 2

        txtSuspensionCantidad2.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = toFloatSafe(txtSuspensionCantidad2.getText()) * toFloatSafe(txtSuspensionUnitario2.getText());

            txtSuspensionTotal2.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtSuspensionUnitario2.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = toFloatSafe(txtSuspensionCantidad2.getText()) * toFloatSafe(newValue.toString());

            txtSuspensionTotal2.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //Mecanica

        txtMecanicaCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = toFloatSafe(txtMecanicaCantidad.getText()) * toFloatSafe(txtMecanicaUnitario.getText());

            txtMecanicaTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtMecanicaUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = toFloatSafe(txtMecanicaCantidad.getText()) * toFloatSafe(newValue.toString());

            txtMecanicaTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //mecanica2

        txtMecanicaCantidad2.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = toFloatSafe(txtMecanicaCantidad2.getText()) * toFloatSafe(txtMecanicaUnitario2.getText());

            txtMecanicaTotal2.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtMecanicaUnitario2.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = toFloatSafe(txtMecanicaCantidad2.getText()) * toFloatSafe(newValue.toString());

            txtMecanicaTotal2.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //frenos

        txtFrenosCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = toFloatSafe(txtFrenosCantidad.getText()) * toFloatSafe(txtFrenosUnitario.getText());

            txtFrenosTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtFrenosUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = toFloatSafe(txtFrenosCantidad.getText()) * toFloatSafe(newValue.toString());

            txtFrenosTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtFrenosCantidad2.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = toFloatSafe(txtFrenosCantidad2.getText()) * toFloatSafe(txtFrenosUnitario2.getText());

            txtFrenosTotal2.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtFrenosUnitario2.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = toFloatSafe(txtFrenosCantidad2.getText()) * toFloatSafe(newValue.toString());

            txtFrenosTotal2.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });
        //otros
        txtOtrosCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = toFloatSafe(txtOtrosCantidad.getText()) * toFloatSafe(txtOtrosUnitario.getText());

            txtOtrosTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtOtrosUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = toFloatSafe(txtOtrosCantidad.getText()) * toFloatSafe(newValue.toString());

            txtOtrosTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });
        //otros2
        txtOtrosCantidad2.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = toFloatSafe(txtOtrosCantidad2.getText()) * toFloatSafe(txtOtrosUnitario2.getText());

            txtOtrosTotal2.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtOtrosUnitario2.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = toFloatSafe(txtOtrosCantidad2.getText()) * toFloatSafe(newValue.toString());

            txtOtrosTotal2.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });


        //al escribir directamente el el total de cada uno
        txtAlineacionTotal.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtBalanceoTotal.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtLlantasTotal.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtAmorDelTotal.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtAmorTrasTotal.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtSuspensionTotal.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtSuspensionTotal2.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtMecanicaTotal.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtMecanicaTotal2.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtFrenosTotal.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtFrenosTotal2.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtOtrosTotal.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtOtrosTotal2.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtSubTotalMecanica.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtSubTotalFrenos.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtSubTotalOtros.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });


    }//operacionesCampos

    private float sumaTotal() {
        float suma = 0;

        suma = toFloatSafe(txtAlineacionTotal.getText()) +
                toFloatSafe(txtBalanceoTotal.getText()) +
                toFloatSafe(txtLlantasTotal.getText()) +
                toFloatSafe(txtAmorDelTotal.getText()) +
                toFloatSafe(txtAmorTrasTotal.getText()) +
                toFloatSafe(txtSuspensionTotal.getText()) +
                toFloatSafe(txtSuspensionTotal2.getText()) +
                toFloatSafe(txtMecanicaTotal.getText()) +
                toFloatSafe(txtMecanicaTotal2.getText()) +
                toFloatSafe(txtFrenosTotal.getText()) +
                toFloatSafe(txtFrenosTotal2.getText()) +
                toFloatSafe(txtOtrosTotal.getText()) +
                toFloatSafe(txtOtrosTotal2.getText()) +
                toFloatSafe(txtSubTotalMecanica.getText()) +
                toFloatSafe(txtSubTotalFrenos.getText()) +
                toFloatSafe(txtSubTotalOtros.getText()); //16


        return suma;
    }//sumaTotal

    private void configuraciones() {

        //configuracion checkBox

        cbRayonesSi.setOnAction(event -> {
            if (cbRayonesSi.isSelected()) {
                cbRayonesNo.setSelected(false);
            }
        });
        cbRayonesNo.setOnAction(event -> {
            if (cbRayonesNo.isSelected()) {
                cbRayonesSi.setSelected(false);
            }
        });
        cbGolpesSi.setOnAction(event -> {
            if (cbGolpesSi.isSelected()) {
                cbGolpesNo.setSelected(false);
            }
        });
        cbGolpesNo.setOnAction(event -> {
            if (cbGolpesNo.isSelected()) {
                cbGolpesSi.setSelected(false);
            }
        });
        cbTaponesSi.setOnAction(event -> {
            if (cbTaponesSi.isSelected()) {
                cbTaponesNo.setSelected(false);
            }
        });
        cbTaponesNo.setOnAction(event -> {
            if (cbTaponesNo.isSelected()) {
                cbTaponesSi.setSelected(false);
            }
        });
        cbTapetesSi.setOnAction(event -> {
            if (cbTapetesSi.isSelected()) {
                cbTapetesNo.setSelected(false);
            }
        });
        cbTapetesNo.setOnAction(event -> {
            if (cbTapetesNo.isSelected()) {
                cbTapetesSi.setSelected(false);
            }
        });
        cbRadioSi.setOnAction(event -> {
            if (cbRadioSi.isSelected()) {
                cbRadioNo.setSelected(false);
            }
        });
        cbRadioNo.setOnAction(event -> {
            if (cbRadioNo.isSelected()) {
                cbRadioSi.setSelected(false);
            }
        });
        cbGatoSi.setOnAction(event -> {
            if (cbGatoSi.isSelected()) {
                cbGatoNo.setSelected(false);
            }
        });
        cbGatoNo.setOnAction(event -> {
            if (cbGatoNo.isSelected()) {
                cbGatoSi.setSelected(false);
            }
        });
        cbLlaveSi.setOnAction(event -> {
            if (cbLlaveSi.isSelected()) {
                cbLlaveNo.setSelected(false);
            }
        });
        cbLlaveNo.setOnAction(event -> {
            if (cbLlaveNo.isSelected()) {
                cbLlaveSi.setSelected(false);
            }
        });
        cbLlantaSi.setOnAction(event -> {
            if (cbLlantaSi.isSelected()) {
                cbLlantaNo.setSelected(false);
            }
        });
        cbLlantaNo.setOnAction(event -> {
            if (cbLlantaNo.isSelected()) {
                cbLlantaSi.setSelected(false);
            }
        });


        //Iniciliza la fecha y hora del dia y sistema
        mostrarFechayHora();

        //deshabilita los menu del clic derecho en los campos de texto
        MenuContextSetting.disableMenu(rootPane);

        //validaciones Regex
        RegexTools.aplicarNumerosDecimalNota(txtTotal);
        RegexTools.aplicar24Horas(txtHoraEntrega);
        RegexTools.aplicar6Enteros(txtNumNota);
        RegexTools.aplicar2Enteros(txtDia);
        RegexTools.aplicar2Enteros(txtMes);
        RegexTools.aplicar4Enteros(txtAnio);
        RegexTools.aplicar4Enteros(txtAnioVehiculo);
        RegexTools.aplicarNumeroEntero(txtKms);

        RegexTools.aplicarNumeroEntero(txtAlineacionCantidad);
        RegexTools.aplicarNumeroEntero(txtBalanceoCantidad);
        RegexTools.aplicarNumeroEntero(txtLlantasCantidad);
        RegexTools.aplicarNumeroEntero(txtAmorDelCantidad);
        RegexTools.aplicarNumeroEntero(txtAmorTrasCantidad);
        RegexTools.aplicarNumeroEntero(txtSuspensionCantidad);
        RegexTools.aplicarNumeroEntero(txtSuspensionCantidad2);
        RegexTools.aplicarNumeroEntero(txtMecanicaCantidad);
        RegexTools.aplicarNumeroEntero(txtMecanicaCantidad2);
        RegexTools.aplicarNumeroEntero(txtFrenosCantidad);
        RegexTools.aplicarNumeroEntero(txtFrenosCantidad2);
        RegexTools.aplicarNumeroEntero(txtOtrosCantidad);
        RegexTools.aplicarNumeroEntero(txtOtrosCantidad2);

        RegexTools.aplicarNumerosDecimalNota(txtAlineacionUnitario);
        RegexTools.aplicarNumerosDecimalNota(txtBalanceoUnitario);
        RegexTools.aplicarNumerosDecimalNota(txtLlantasUnitario);
        RegexTools.aplicarNumerosDecimalNota(txtAmorDelUnitario);
        RegexTools.aplicarNumerosDecimalNota(txtAmorTrasUnitario);
        RegexTools.aplicarNumerosDecimalNota(txtSuspensionUnitario);
        RegexTools.aplicarNumerosDecimalNota(txtSuspensionUnitario2);
        RegexTools.aplicarNumerosDecimalNota(txtMecanicaUnitario);
        RegexTools.aplicarNumerosDecimalNota(txtMecanicaUnitario);
        RegexTools.aplicarNumerosDecimalNota(txtMecanicaUnitario2);
        RegexTools.aplicarNumerosDecimalNota(txtFrenosUnitario);
        RegexTools.aplicarNumerosDecimalNota(txtFrenosUnitario2);
        RegexTools.aplicarNumerosDecimalNota(txtOtrosUnitario);
        RegexTools.aplicarNumerosDecimalNota(txtOtrosUnitario2);
        RegexTools.aplicarNumerosDecimalNota(txtAlineacionTotal);
        RegexTools.aplicarNumerosDecimalNota(txtBalanceoTotal);
        RegexTools.aplicarNumerosDecimalNota(txtLlantasTotal);
        RegexTools.aplicarNumerosDecimalNota(txtAmorDelTotal);
        RegexTools.aplicarNumerosDecimalNota(txtAmorTrasTotal);
        RegexTools.aplicarNumerosDecimalNota(txtSuspensionTotal);
        RegexTools.aplicarNumerosDecimalNota(txtSuspensionTotal2);
        RegexTools.aplicarNumerosDecimalNota(txtSubTotalFrenos);
        RegexTools.aplicarNumerosDecimalNota(txtSubTotalMecanica);
        RegexTools.aplicarNumerosDecimalNota(txtSubTotalOtros);
        RegexTools.aplicarNumerosDecimalNota(txtMecanicaTotal);
        RegexTools.aplicarNumerosDecimalNota(txtMecanicaTotal2);
        RegexTools.aplicarNumerosDecimalNota(txtFrenosTotal);
        RegexTools.aplicarNumerosDecimalNota(txtFrenosTotal2);
        RegexTools.aplicarNumerosDecimalNota(txtOtrosTotal);
        RegexTools.aplicarNumerosDecimalNota(txtOtrosTotal2);

    }//configuraciones


    //Seccion precargar en nota



    private void mostrarFechayHora() {
        LocalDate hoy = LocalDate.now();
        txtDia.setText(String.format("%02d", hoy.getDayOfMonth()));
        txtMes.setText(String.format("%02d", hoy.getMonthValue()));
        txtAnio.setText(String.valueOf(hoy.getYear()));

        LocalDateTime fechayHora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String hora = fechayHora.format(formatter);
        txtHoraEntrega.setText(hora);

    }//mostrarFecha

    //Seccion dibujar arco gasolina

    private void mostrarSlider(Window owner) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(owner);

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(15));
        Label label = new Label("Selecciona el porcentaje de gasolina");

        Slider slider = new Slider(0, 100, 50);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(10);
        slider.setMinorTickCount(4);

        Button btnAceptar = new Button("Aceptar");

        btnAceptar.setOnAction(event -> {
            double porcentaje = slider.getValue();
            dibujarGasolina(porcentaje);
            dialog.close();
        });
        vBox.getChildren().add(label);
        vBox.getChildren().add(slider);
        vBox.getChildren().add(btnAceptar);
        Scene scene = new Scene(vBox, 300, 150);
        dialog.setScene(scene);
        dialog.setTitle("Porcentaje de gas");
        dialog.showAndWait();

    }//mostrarSlider

    private void dibujarGasolina(double porcentaje) {
        GraphicsContext gc = canvasGas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvasGas.getWidth(), canvasGas.getHeight());

        double centerX = canvasGas.getWidth() / 2;
        double centerY = canvasGas.getHeight() / 2;
        double radius = 60;
        double startAngle = 180; // arco empieza desde izquierda
        double length = 180 * porcentaje / 100.0;

        // Fondo gris
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(8);
        gc.strokeArc(centerX - radius, centerY - radius, radius * 2, radius * 2,
                startAngle, 180, ArcType.OPEN);

        setPorcentajeGasNota((int) porcentaje);

        lblGas.setText(String.format("%d %%", (int) porcentaje, "%"));

        // Arco de porcentaje
        if (porcentaje > 60)
            gc.setStroke(Color.GREEN);
        else if (porcentaje > 30)
            gc.setStroke(Color.ORANGE);
        else
            gc.setStroke(Color.RED);

        gc.strokeArc(centerX - radius, centerY - radius, radius * 2, radius * 2,
                startAngle, length, ArcType.OPEN);
    }

    @FXML
    private void buscarCliente(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/nota/BuscarCliente.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent root = loader.load();

            Stage stage = new Stage(StageStyle.UTILITY);
            BuscarClienteController controller = loader.getController();


            stage.setTitle("Buscar cliente");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            stage.showAndWait();

            Cliente cliente = controller.getClienteSeleccionado();
            llenarNota(cliente);

            VehiculoDTO vehiculos = controller.getVehiculoSeleccionado();
            llenarNota(vehiculos);


        } catch (Exception e) {
            mostrarError("Error", "", "Error inesperado.");
            e.printStackTrace();
        }

    }//buscarCliente

    @FXML
    private void buscarLlanta(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/nota/BuscarLlanta.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent root = loader.load();

            Stage stage = new Stage(StageStyle.UTILITY);
            BuscarLlantaController controller = loader.getController();

            stage.setTitle("Buscar llantas");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            stage.showAndWait();
            Inventario inventario = controller.getLlantaSeleccionada();
            llenarNota(inventario);


        } catch (Exception e) {
            mostrarError("Error inesperado", "", "Ocurrió un problema al realizar la operación.");

            e.printStackTrace();
        }

    }//buscarCliente

    //metodo que separa la direccion en los dos campos en caso de ser necesario(direccion1 y 2)
    private void  direccionFormatter(String direccionTxt) {

        String arrayDireccion [] = direccionTxt.split(" ");


        String direccion1 = "", direccion2 = "";

        for (int i = 0; i <arrayDireccion.length ; i++) {

           if (direccion1.length() < 59){
               direccion1 += arrayDireccion[i] + " ";
           }else {
               if (direccion2.length() < 69)
               direccion2 += arrayDireccion[i] + " ";
               else{
                   direccion2 += "...";
                   break;
               }

           }

        }

        txtDireccion.setText(direccion1);
        txtDireccion2.setText(direccion2);

    }//direccionFormatter


    private void llenarNota(Cliente cliente) {
        if (cliente != null) {

            txtNombre.setText(cliente.getNombre() + " " +
                    (cliente.getApellido() != null ? cliente.getApellido() : "") + " " +
                    (cliente.getSegundoApellido() != null ? cliente.getSegundoApellido() : "")
            );

         //   txtDireccion.setText(cliente.getDomicilio() != null ? cliente.getDomicilio() : "");
            direccionFormatter(cliente.getDomicilio() != null ? cliente.getDomicilio() : "");
            txtRfc.setText(cliente.getRfc() != null ? cliente.getRfc() : "");
            txtCorreo.setText(cliente.getCorreo() != null ? cliente.getCorreo() : "");

        }
        setClienteNota(cliente);
        btnBorrarCliente.setDisable(false);

    }//llenarNota

    private void llenarNota(VehiculoDTO vehiculos) {
        if (vehiculos != null) {
            txtMarca.setText(vehiculos.getNombreMarca());
            txtModelo.setText(vehiculos.getNombreModelo() + " " + vehiculos.getNombreCategoria());
            txtAnioVehiculo.setText(vehiculos.getAnio() + "");
            txtKms.setText(vehiculos.getKilometros() + "");
            txtPlacas.setText(vehiculos.getPlacas());
            habilitar(false, "cliente");

            setVehiculoNota(vehiculos);

        }


    }//llenarNota

    private void llenarNota(Inventario inventario) {
        if (inventario != null) {
            txtLlantas.setText(inventario.getMarca() + " " + inventario.getModelo() + " " + " " + inventario.getMedida());

            if (inventario.getStock() == 0) {
                txtLlantasCantidad.setText("1");
                txtLlantasUnitario.setText(inventario.getPrecioVenta() + "");
            } else {
                txtLlantasCantidad.setText(inventario.getStock() + "");
                txtLlantasUnitario.setText(inventario.getPrecioVenta() + "");

            }


            habilitar(false, "inventario");

        }

        setInventarioNota(inventario);
        btnBorrarInventario.setDisable(false);


    }//llenarNota


    private void habilitar(boolean habilitar, String opcion) {

        switch (opcion) {
            case "cliente" -> {
                txtMarca.setEditable(habilitar);
                txtModelo.setEditable(habilitar);
                txtAnioVehiculo.setEditable(habilitar);
                txtKms.setEditable(habilitar);
                txtPlacas.setEditable(habilitar);
                txtRfc.setEditable(habilitar);
                txtNombre.setEditable(habilitar);
                txtDireccion.setEditable(habilitar);
                txtDireccion2.setEditable(habilitar);
                txtCorreo.setEditable(habilitar);

            }
            case "inventario" -> {
                txtLlantas.setEditable(habilitar);
                txtLlantasCantidad.setEditable(habilitar);
            }

        }//switch

    }//habilitar

    private void registrar(Cliente cRegistro, VehiculoDTO vRegistro, Inventario iRegistro) {
        boolean error = false;

        if (cRegistro == null) {
            mostrarWarning(
                    "Campos obligatorio",
                    "Cliente y Vehículo Faltantes ",
                    "Asocie un cliente y un vehículo antes de guardar la nota.");

            error = true;
            return;
        }

        if (txtNumNota.getText().trim().isEmpty()) {
            mostrarWarning("Campo obligatorio",
                    "Numero de nota ",
                    "El numero de nota es obligatorio. Por favor, ingrese un valor antes de continuar.");

            error = true;
            return;

        }

        if (txtTotal.getText().trim().isEmpty()) {
            mostrarWarning("Campo obligatorio",
                    "Total de la nota ",
                    "El total de la nota es obligatorio. Por favor, ingrese un valor antes de continuar.");

            error = true;
            return;
        }


        if (!error) {

            checkCheckBoxes();

            try {


                String fechaYhora = txtAnio.getText() + "-" + txtMes.getText() + "-" + txtDia.getText() + " " + txtHoraEntrega.getText() + ":00";
                String nombre =  cRegistro.getNombre() + " " + ( cRegistro.getApellido() != null ? cRegistro.getApellido() : "") + " " +
                (cRegistro.getSegundoApellido() != null ? cRegistro.getSegundoApellido() : "");

                NotaDTO.NotaDTOBuilder nuevaNotaBuilder = NotaDTO.builder()



                        .clienteId(cRegistro.getClienteId())
                        .vehiculoId(vRegistro.getId())
                        .nombreClienteNota(nombre)
                        .direccion1Nota(txtDireccion.getText())
                        .direccion2Nota(txtDireccion2.getText())
                        .rfcNota(cRegistro.getRfc())
                        .correoNota(cRegistro.getCorreo())
                        .marcaNota(vRegistro.getNombreMarca())
                        .modeloNota(vRegistro.getNombreModelo())
                        .categoriaNota(vRegistro.getNombreCategoria())
                        .anioNota(vRegistro.getAnio())
                        .kilometrosNota(vRegistro.getKilometros())
                        .placasNota(vRegistro.getPlacas())

                        .numNota(txtNumNota.getText())
                        .numFactura("")
                        .fechaYHora(fechaYhora)
                        .fechaVencimiento("")
                        .total(toFloatSafe(txtTotal.getText())) // datos nota

                        //obserbaciones
                        .observaciones(txtObservaciones.getText())
                        .observaciones2(txtObservaciones2.getText())

                        //porcentaje gasolina
                        .porcentajeGas(getPorcentajeGasNota())
                        //checkBox
                        .rayones(getRayones())
                        .golpes(getGolpes())
                        .tapones(getTapones())
                        .tapetes(getTapetes())
                        .radio(getRadio())
                        .gato(getGato())
                        .llave(getLlave())
                        .llanta(getLlanta())
                        //nota
                        .alineacion(txtAlineacion.getText())
                        .alineacionCantidad(toIntSafe(txtAlineacionCantidad.getText()))
                        .alineacionUnitario(toFloatSafe(txtAlineacionUnitario.getText()))
                        .alineacionTotal(toFloatSafe(txtAlineacionTotal.getText()))
                        .balanceo(txtBalanceo.getText())
                        .balanceoCantidad(toIntSafe(txtBalanceoCantidad.getText()))
                        .balanceoUnitario(toFloatSafe(txtBalanceoUnitario.getText()))
                        .balanceoTotal(toFloatSafe(txtBalanceoTotal.getText()))
                        //inventario o llantas
                        .llantaCampo(txtLlantas.getText())
                        .llantaCantidad(toIntSafe(txtLlantasCantidad.getText()))
                        .llantaUnitario(toFloatSafe(txtLlantasUnitario.getText()))
                        .llantaTotal(toFloatSafe(txtLlantasTotal.getText()))
                        //
                        .amorDelanteros(txtAmorDelanteros.getText())
                        .amorDelCantidad(toIntSafe(txtAmorDelCantidad.getText()))
                        .amorDelUnitario(toFloatSafe(txtAmorDelUnitario.getText()))
                        .amorDelTotal(toFloatSafe(txtAmorDelTotal.getText()))

                        .amorTraseros(txtAmorTraseros.getText())
                        .amorTrasCantidad(toIntSafe(txtAmorDelCantidad.getText()))
                        .amorTrasUnitario(toFloatSafe(txtAmorTrasUnitario.getText()))
                        .amorTrasTotal(toFloatSafe(txtAmorTrasTotal.getText()))

                        .suspension(txtSuspension.getText())
                        .suspensionCantidad(toIntSafe(txtSuspensionCantidad.getText()))
                        .suspensionUnitario(toFloatSafe(txtSuspensionUnitario.getText()))
                        .suspensionTotal(toFloatSafe(txtSuspensionTotal.getText()))

                        .suspension2(txtSuspension2.getText())
                        .suspensionCantidad2(toIntSafe(txtSuspensionCantidad2.getText()))
                        .suspensionUnitario2(toFloatSafe(txtSuspensionUnitario2.getText()))
                        .suspensionTotal2(toFloatSafe(txtSuspensionTotal2.getText()))

                        .mecanica(txtMecanica.getText())
                        .mecanicaCantidad(toIntSafe(txtMecanicaCantidad.getText()))
                        .mecanicaUnitario(toFloatSafe(txtMecanicaUnitario.getText()))
                        .mecanicaTotal(toFloatSafe(txtMecanicaTotal.getText()))

                        .mecanica2(txtMecanica2.getText())
                        .mecanicaCantidad2(toIntSafe(txtMecanicaCantidad2.getText()))
                        .mecanicaUnitario2(toFloatSafe(txtMecanicaUnitario2.getText()))
                        .mecanicaTotal2(toFloatSafe(txtMecanicaTotal2.getText()))

                        .frenos(txtFrenos.getText())
                        .frenosCantidad(toIntSafe(txtFrenosCantidad.getText()))
                        .frenosUnitario(toFloatSafe(txtFrenosUnitario.getText()))
                        .frenosTotal(toFloatSafe(txtFrenosTotal.getText()))

                        .frenos2(txtFrenos2.getText())
                        .frenosCantidad2(toIntSafe(txtFrenosCantidad2.getText()))
                        .frenosUnitario2(toFloatSafe(txtFrenosUnitario2.getText()))
                        .frenosTotal2(toFloatSafe(txtFrenosTotal2.getText()))

                        .otros(txtOtros.getText())
                        .otrosCantidad(toIntSafe(txtOtrosCantidad.getText()))
                        .otrosUnitario(toFloatSafe(txtOtrosUnitario.getText()))
                        .otrosTotal(toFloatSafe(txtOtrosTotal.getText()))

                        .otros2(txtOtros2.getText())
                        .otrosCantidad2(toIntSafe(txtOtrosCantidad2.getText()))
                        .otrosUnitario2(toFloatSafe(txtOtrosUnitario2.getText()))
                        .otrosTotal2(toFloatSafe(txtOtrosTotal2.getText()))

                        //subtotales de la parte de la derecha
                        .subTotalFrenos(toFloatSafe(txtSubTotalFrenos.getText()))
                        .subTotalMecanica(toFloatSafe(txtSubTotalMecanica.getText()))
                        .subTotalOtros(toFloatSafe(txtSubTotalOtros.getText()));

                if (iRegistro != null) {
                    nuevaNotaBuilder.inventarioId(iRegistro.getInventarioId());
                } else {
                    nuevaNotaBuilder.inventarioId(null);
                }

                NotaDTO nuevaNota = nuevaNotaBuilder.build();


                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/nota/RegistrarNota.fxml"));
                loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
                Parent root = loader.load();

                RegistrarNotaController controller = loader.getController();
                controller.agregarNota(nuevaNota);
                controller.setOnRegistroCompleto(() -> refrescar(null));


                Stage stage = new Stage(StageStyle.UTILITY);


                stage.setTitle("Registrar Nota");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.showAndWait();

            } catch (Exception e) {
                mostrarError("Error inesperado", "", "Ocurrió un problema al realizar la operación.");

                e.printStackTrace();
            }

        }


    }//registrar



    private void checkCheckBoxes() {

        //rayones
        if (cbRayonesSi.isSelected() && !cbRayonesNo.isSelected())
            setRayones(StatatusSiNo.SI.toString());
        else if (!cbRayonesSi.isSelected() && !cbRayonesNo.isSelected())
            setRayones(StatatusSiNo.DESELECCIONADO.toString());
        else
            setRayones(StatatusSiNo.NO.toString());

        //golpes
        if (cbGolpesSi.isSelected() && !cbGolpesNo.isSelected())
            setGolpes(StatatusSiNo.SI.toString());
        else if (!cbGolpesSi.isSelected() && !cbGolpesNo.isSelected())
            setGolpes(StatatusSiNo.DESELECCIONADO.toString());
        else
            setGolpes(StatatusSiNo.NO.toString());

        //tapones
        if (cbTaponesSi.isSelected() && !cbTaponesNo.isSelected())
            setTapones(StatatusSiNo.SI.toString());
        else if (!cbTaponesSi.isSelected() && !cbTaponesNo.isSelected())
            setTapones(StatatusSiNo.DESELECCIONADO.toString());
        else
            setTapones(StatatusSiNo.NO.toString());


        //tapetes
        if (cbTapetesSi.isSelected() && !cbTapetesNo.isSelected())
            setTapetes(StatatusSiNo.SI.toString());
        else if (!cbTapetesSi.isSelected() && !cbTapetesNo.isSelected())
            setTapetes(StatatusSiNo.DESELECCIONADO.toString());
        else
            setTapetes(StatatusSiNo.NO.toString());

        //radio
        if (cbRadioSi.isSelected() && !cbRadioNo.isSelected())
            setRadio(StatatusSiNo.SI.toString());
        else if (!cbRadioSi.isSelected() && !cbRadioNo.isSelected())
            setRadio(StatatusSiNo.DESELECCIONADO.toString());
        else
            setRadio(StatatusSiNo.NO.toString());


        //gato
        if (cbGatoSi.isSelected() && !cbGatoNo.isSelected())
            setGato(StatatusSiNo.SI.toString());
        else if (!cbGatoSi.isSelected() && !cbGatoNo.isSelected())
            setGato(StatatusSiNo.DESELECCIONADO.toString());
        else
            setGato(StatatusSiNo.NO.toString());

        //llave
        if (cbLlaveSi.isSelected() && !cbLlaveNo.isSelected())
            setLlave(StatatusSiNo.SI.toString());
        else if (!cbLlaveSi.isSelected() && !cbLlaveNo.isSelected())
            setLlave(StatatusSiNo.DESELECCIONADO.toString());
        else
            setLlave(StatatusSiNo.NO.toString());

        //llanta
        if (cbLlantaSi.isSelected() && !cbLlantaNo.isSelected())
            setLlanta(StatatusSiNo.SI.toString());
        else if (!cbLlantaSi.isSelected() && !cbLlantaNo.isSelected())
            setLlanta(StatatusSiNo.DESELECCIONADO.toString());
        else
            setLlanta(StatatusSiNo.NO.toString());


    }//checkCheckBoxes

    @FXML
    private void refrescar(ActionEvent event) {
        clienteNota = null;
        vehiculosNota = null;
        inventarioNota = null;
        btnBorrarInventario.setDisable(true);
        btnBorrarCliente.setDisable(true);

        txtNumNota.setText("");
        mostrarFechayHora();
        txtNombre.setText("");
        txtDireccion.setText("");
        txtDireccion2.setText("");
        txtRfc.setText("");
        txtCorreo.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtAnioVehiculo.setText("");
        txtKms.setText("");
        txtPlacas.setText("");
        cbRayonesSi.setSelected(false);
        cbRayonesNo.setSelected(false);
        cbGolpesSi.setSelected(false);
        cbGolpesNo.setSelected(false);
        cbTaponesSi.setSelected(false);
        cbTaponesNo.setSelected(false);
        cbTapetesSi.setSelected(false);
        cbTapetesNo.setSelected(false);
        cbRadioSi.setSelected(false);
        cbRadioNo.setSelected(false);
        cbGatoSi.setSelected(false);
        cbGatoNo.setSelected(false);
        cbLlaveSi.setSelected(false);
        cbLlaveNo.setSelected(false);
        cbLlantaSi.setSelected(false);
        cbLlantaNo.setSelected(false);
        txtObservaciones.setText("");
        txtObservaciones2.setText("");
        dibujarGasolina(50);
        txtAlineacion.setText("");
        txtAlineacionCantidad.setText("");
        txtAlineacionUnitario.setText("");
        txtAlineacionTotal.setText("");
        txtBalanceo.setText("");
        txtBalanceoCantidad.setText("");
        txtBalanceoUnitario.setText("");
        txtBalanceoTotal.setText("");
        txtLlantas.setText("");
        txtLlantasCantidad.setText("");
        txtLlantasUnitario.setText("");
        txtLlantasTotal.setText("");
        txtAmorDelanteros.setText("");
        txtAmorDelCantidad.setText("");
        txtAmorDelUnitario.setText("");
        txtAmorDelTotal.setText("");
        txtAmorTraseros.setText("");
        txtAmorTrasCantidad.setText("");
        txtAmorTrasUnitario.setText("");
        txtAmorTrasTotal.setText("");
        txtSuspension.setText("");
        txtSuspensionCantidad.setText("");
        txtSuspensionUnitario.setText("");
        txtSuspensionTotal.setText("");
        txtSuspension2.setText("");
        txtSuspensionCantidad2.setText("");
        txtSuspensionUnitario2.setText("");
        txtSuspensionTotal2.setText("");
        txtMecanica.setText("");
        txtMecanicaCantidad.setText("");
        txtMecanicaUnitario.setText("");
        txtMecanicaTotal.setText("");
        txtMecanica2.setText("");
        txtMecanicaCantidad2.setText("");
        txtMecanicaUnitario2.setText("");
        txtMecanicaTotal2.setText("");
        txtFrenos.setText("");
        txtFrenosCantidad.setText("");
        txtFrenosUnitario.setText("");
        txtFrenosTotal.setText("");
        txtFrenos2.setText("");
        txtFrenosCantidad2.setText("");
        txtFrenosUnitario2.setText("");
        txtFrenosTotal2.setText("");
        txtOtros.setText("");
        txtOtrosCantidad.setText("");
        txtOtrosUnitario.setText("");
        txtOtrosTotal.setText("");
        txtOtros2.setText("");
        txtOtrosCantidad2.setText("");
        txtOtrosUnitario2.setText("");
        txtOtrosTotal2.setText("");
        txtSubTotalFrenos.setText("");
        txtSubTotalMecanica.setText("");
        txtSubTotalOtros.setText("");
        txtTotal.setText("");


        habilitar(true, "inventario");
        habilitar(true, "cliente");

    }//refrescar

    //pone en null stock de inventario para desasociar la relacion Entidad nota-inventario
    @FXML
    private void eliminarInventario() {


        boolean confirmar = mostrarConfirmacion(
                "Eliminar llanta de Nota",
                "La llanta seleccionada dejará de estar asociada a la nota.\n",
                "Esto no eliminará la llanta del inventario, solo la relación con la nota.\n" +
                        "¿Deseas continuar?",
                "Continuar",
                "Cancelar"
        );

        if (confirmar) {
            setInventarioNota(null);
            txtLlantas.setText("");
            txtLlantasCantidad.setText("");
            txtLlantasUnitario.setText("");
            txtLlantasTotal.setText("");
            btnBorrarInventario.setDisable(true);

        }

    }//eliminarInventario

    //Pone en null cliente y vehiculo para desasocial las relaciones de entidades Nota - vehiculo- Cliente
    @FXML
    private void eliminarCliente() {


        boolean confirmar = mostrarConfirmacion(
                "Eliminar cliente de Nota",
                "El cliente y el vehículo dejarán de estar asociados a la nota.\n",
                "Esto no eliminará al cliente ni el vehículo del sistema, solo la relación con la nota.\n" +
                        "¿Deseas continuar?\n\n",
                "Continuar",
                "Cancelar"
        );


        if (confirmar) {
            setClienteNota(null);
            setVehiculoNota(null);

            txtNombre.setText("");
            txtDireccion.setText("");
            txtDireccion2.setText("");
            txtRfc.setText("");
            txtCorreo.setText("");
            mostrarFechayHora();
            txtMarca.setText("");
            txtModelo.setText("");
            txtAnioVehiculo.setText("");
            txtKms.setText("");
            txtPlacas.setText("");
            btnBorrarCliente.setDisable(true);
        }
    }//eliminarCliente


    //getters y setters


    private void setClienteNota(Cliente clienteNota) {
        this.clienteNota = clienteNota;
    }

    private void setInventarioNota(Inventario inventarioNota) {
        this.inventarioNota = inventarioNota;
    }

    private void setVehiculoNota(VehiculoDTO vehiculos) {
        this.vehiculosNota = vehiculos;
    }




    //getters y setters para checkBox
    public String getRayones() {
        return this.rayones;
    }

    public void setRayones(final String rayones) {
        this.rayones = rayones;
    }

    public String getGolpes() {
        return this.golpes;
    }

    public void setGolpes(final String golpes) {
        this.golpes = golpes;
    }

    public String getTapones() {
        return this.tapones;
    }

    public void setTapones(final String tapons) {
        this.tapones = tapones;
    }

    public String getTapetes() {
        return this.tapetes;
    }

    public void setTapetes(final String tapetes) {
        this.tapetes = tapetes;
    }

    public String getRadio() {
        return this.radio;
    }

    public void setRadio(final String radio) {
        this.radio = radio;
    }

    public String getGato() {
        return this.gato;
    }

    public void setGato(final String gato) {
        this.gato = gato;
    }

    public String getLlave() {
        return this.llave;
    }

    public void setLlave(final String llave) {
        this.llave = llave;
    }

    public String getLlanta() {
        return this.llanta;
    }

    public void setLlanta(final String llanta) {
        this.llanta = llanta;
    }

    public int getPorcentajeGasNota() {
        return this.porcentajeGasNota;
    }

    public void setPorcentajeGasNota(final int porcentajeGasNota) {
        this.porcentajeGasNota = porcentajeGasNota;
    }
}//class
