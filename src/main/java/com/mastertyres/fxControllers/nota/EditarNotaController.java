package com.mastertyres.fxControllers.nota;

import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.cliente.model.StatusCliente;
import com.mastertyres.cliente.service.ClienteService;
import com.mastertyres.common.ApplicationContextProvider;
import com.mastertyres.common.MenuContextSetting;
import com.mastertyres.common.NotaUtils;
import com.mastertyres.common.RegexTools;
import com.mastertyres.fxControllers.EditarControllers.EditarAdeudoController;
import com.mastertyres.fxControllers.EditarControllers.EditarSaldoController;
import com.mastertyres.inventario.model.Inventario;
import com.mastertyres.inventario.service.InventarioService;
import com.mastertyres.nota.model.*;
import com.mastertyres.nota.service.NotaService;
import com.mastertyres.notaClienteDetalle.model.NotaClienteDetalle;
import com.mastertyres.notaClienteDetalle.service.NotaClienteDetService;
import com.mastertyres.notaDetalle.model.NotaDetalle;
import com.mastertyres.notaDetalle.service.NotaDetalleService;
import com.mastertyres.vehiculo.model.StatusVehiculo;
import com.mastertyres.vehiculo.model.Vehiculo;
import com.mastertyres.vehiculo.service.VehiculoService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.mastertyres.common.MensajesAlert.*;

@Component
public class EditarNotaController {


    @FXML
    private AnchorPane rootPane;
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

    private String rayones;
    private String golpes;
    private String tapones;
    private String tapetes;
    private String radio;
    private String gato;
    private String llave;
    private String llanta;

    @Autowired
    private NotaService notaService;
    @Autowired
    private VehiculoService vehiculoService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private NotaDetalleService notaDetalleService;
    @Autowired
    private NotaClienteDetService notaClienteDetService;
    @Autowired
    private InventarioService inventarioService;
    @Autowired
    private NotaUtils notaUtils;


    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnActualizarDatos;
    @FXML
    private Button btnActualizarAdeudo;
    @FXML
    private Button btnClienteDetalles;
    @FXML
    private Button btnActualizarSaldoFavor;
    @FXML
    private Button btnNotaDetalles;
    @FXML
    private Button btnAgregarNumFactura;


    private PopupControl popup;
    private ComboBox<Integer> cmbHora;
    private ComboBox<Integer> cmbMinuto;


    private int porcentajeGasNota;

    private NotaDTO notaEditar;
    private String numNota;


    @FXML
    private void initialize() {
        configuraciones();
        operacionesCampos();

        notaUtils.mostrarPopupHora(txtHoraEntrega);

        btnShowIcons.setOnMouseClicked(event -> notaUtils.showIcons(gridPaneIcons));

        spPorcentajeGas.setOnMouseClicked(event -> mostrarSlider(spPorcentajeGas.getScene().getWindow()));

        btnActualizarDatos.setOnAction(event -> actualizarDatosCliente(notaEditar.getClienteId(), notaEditar.getVehiculoId()));

        btnActualizarAdeudo.setOnAction(event -> actualizarAdeudo(getNotaEditar()));

        btnActualizarSaldoFavor.setOnAction(event -> actualizarSaldo(getNotaEditar()));

        btnNotaDetalles.setOnAction(event -> notaDetalles(getNotaEditar()));

        btnGuardar.setOnAction(event -> actualizarNota(notaEditar));

        btnAgregarNumFactura.setOnAction(event -> agregarNumFactura(notaEditar));


    }//initialize


    public void agregarNota(String numNota) {
        llenarNota(numNota);
    }

    private void llenarNota(String numNota) {
        notaEditar = notaService.buscarPorNumNota(StatusNota.ACTIVE.toString(), numNota);


        if (notaEditar != null) {

            txtNumNota.setText(notaEditar.getNumNota());

            //codigo para mostrar fecha, la fecha viene en una misma columna en la BD y semuestra en diferentes campos en la Nota

            String strFecha = "", strHoraEntrega = "", strHoraFormateada = "";
            String arrayFecha[];


            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter outPutFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter horaInputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            DateTimeFormatter horaOutputFormatter = DateTimeFormatter.ofPattern("HH:mm");
            strFecha = notaEditar.getFechaYHora();

            String fechaFormateada
                    = LocalDateTime.parse(strFecha, inputFormatter)
                    .format(outPutFormatter);

            arrayFecha = fechaFormateada.split("-");

            txtDia.setText(arrayFecha[0]);
            txtMes.setText(arrayFecha[1]);
            txtAnio.setText(arrayFecha[2]);

            arrayFecha = strFecha.split(" ");

            strHoraEntrega = arrayFecha[1];
            LocalTime hora = LocalTime.parse(strHoraEntrega, horaInputFormatter);
            strHoraFormateada = hora.format(horaOutputFormatter);


            txtHoraEntrega.setText(strHoraFormateada);

            //datos cliente
            txtNombre.setText(notaEditar.getNombreClienteNota());
            txtDireccion.setText(notaEditar.getDireccion1Nota() != null ? notaEditar.getDireccion1Nota() : "");
            txtDireccion2.setText(notaEditar.getDireccion2Nota() != null ? notaEditar.getDireccion2Nota() : "");
            txtRfc.setText(notaEditar.getRfcNota() != null ? notaEditar.getRfcNota() : "");
            txtCorreo.setText(notaEditar.getCorreoNota() != null ? notaEditar.getCorreoNota() : "");

            //datos vehiculo
            txtMarca.setText(notaEditar.getMarcaNota());
            txtModelo.setText(notaEditar.getModeloNota());
            txtAnioVehiculo.setText(notaEditar.getAnioNota() + "");
            txtKms.setText((notaEditar.getKilometrosNota() != null ? notaEditar.getKilometrosNota() : "") + "");
            txtPlacas.setText(notaEditar.getPlacasNota() != null ? notaEditar.getPlacasNota() : "");
            dibujarGasolina(notaEditar.getPorcentajeGas());

            //checkBox
            String checkBoxArray[] = new String[8];

            checkBoxArray[0] = notaEditar.getRayones();
            checkBoxArray[1] = notaEditar.getGolpes();
            checkBoxArray[2] = notaEditar.getTapones();
            checkBoxArray[3] = notaEditar.getTapetes();
            checkBoxArray[4] = notaEditar.getRadio();
            checkBoxArray[5] = notaEditar.getGato();
            checkBoxArray[6] = notaEditar.getLlave();
            checkBoxArray[7] = notaEditar.getLlanta();

            revisarCheckBoxes(checkBoxArray);

            txtObservaciones.setText(notaEditar.getObservaciones() != null ? notaEditar.getObservaciones() : "");
            txtObservaciones2.setText(notaEditar.getObservaciones2() != null ? notaEditar.getObservaciones2() : "");

            //Nota
            txtAlineacion.setText(notaEditar.getAlineacion());
            txtAlineacionCantidad.setText(notaUtils.eliminarCero(notaEditar.getAlineacionCantidad()));
            txtAlineacionUnitario.setText(notaUtils.eliminarCero(notaEditar.getAlineacionUnitario()));
            txtAlineacionTotal.setText(notaUtils.eliminarCero(notaEditar.getAlineacionTotal()));

            txtBalanceo.setText(notaEditar.getBalanceo());
            txtBalanceoCantidad.setText(notaUtils.eliminarCero(notaEditar.getBalanceoCantidad()));
            txtBalanceoUnitario.setText(notaUtils.eliminarCero(notaEditar.getBalanceoUnitario()));
            txtBalanceoTotal.setText(notaUtils.eliminarCero(notaEditar.getBalanceoTotal()));

            txtLlantas.setText(notaEditar.getLlantaCampo());
            txtLlantasCantidad.setText(notaUtils.eliminarCero(notaEditar.getLlantaCantidad()));
            txtLlantasUnitario.setText(notaUtils.eliminarCero(notaEditar.getLlantaUnitario()));
            txtLlantasTotal.setText(notaUtils.eliminarCero(notaEditar.getLlantaTotal()));

            txtAmorDelanteros.setText(notaEditar.getAmorDelanteros());
            txtAmorDelCantidad.setText(notaUtils.eliminarCero(notaEditar.getAmorDelCantidad()));
            txtAmorDelUnitario.setText(notaUtils.eliminarCero(notaEditar.getAmorDelUnitario()));
            txtAmorDelTotal.setText(notaUtils.eliminarCero(notaEditar.getAmorDelTotal()));

            txtAmorTraseros.setText(notaEditar.getAmorTraseros());
            txtAmorTrasCantidad.setText(notaUtils.eliminarCero(notaEditar.getAmorTrasCantidad()));
            txtAmorTrasUnitario.setText(notaUtils.eliminarCero(notaEditar.getAmorTrasUnitario()));
            txtAmorTrasTotal.setText(notaUtils.eliminarCero(notaEditar.getAmorTrasTotal()));

            txtSuspension.setText(notaEditar.getSuspension());
            txtSuspensionCantidad.setText(notaUtils.eliminarCero(notaEditar.getSuspensionCantidad()));
            txtSuspensionUnitario.setText(notaUtils.eliminarCero(notaEditar.getSuspensionUnitario()));
            txtSuspensionTotal.setText(notaUtils.eliminarCero(notaEditar.getSuspensionTotal()));

            txtSuspension2.setText(notaEditar.getSuspension2());
            txtSuspensionCantidad2.setText(notaUtils.eliminarCero(notaEditar.getSuspensionCantidad2()));
            txtSuspensionUnitario2.setText(notaUtils.eliminarCero(notaEditar.getSuspensionUnitario2()));
            txtSuspensionTotal2.setText(notaUtils.eliminarCero(notaEditar.getSuspensionTotal2()));

            txtMecanica.setText(notaEditar.getMecanica());
            txtMecanicaCantidad.setText(notaUtils.eliminarCero(notaEditar.getMecanicaCantidad()));
            txtMecanicaUnitario.setText(notaUtils.eliminarCero(notaEditar.getMecanicaUnitario()));
            txtMecanicaTotal.setText(notaUtils.eliminarCero(notaEditar.getMecanicaTotal()));

            txtMecanica2.setText(notaEditar.getMecanica2());
            txtMecanicaCantidad2.setText(notaUtils.eliminarCero(notaEditar.getMecanicaCantidad2()));
            txtMecanicaUnitario2.setText(notaUtils.eliminarCero(notaEditar.getMecanicaUnitario2()));
            txtMecanicaTotal2.setText(notaUtils.eliminarCero(notaEditar.getMecanicaTotal2()));

            txtFrenos.setText(notaEditar.getFrenos());
            txtFrenosCantidad.setText(notaUtils.eliminarCero(notaEditar.getFrenosCantidad()));
            txtFrenosUnitario.setText(notaUtils.eliminarCero(notaEditar.getFrenosUnitario()));
            txtFrenosTotal.setText(notaUtils.eliminarCero(notaEditar.getFrenosTotal()));

            txtFrenos2.setText(notaEditar.getFrenos2());
            txtFrenosCantidad2.setText(notaUtils.eliminarCero(notaEditar.getFrenosCantidad2()));
            txtFrenosUnitario2.setText(notaUtils.eliminarCero(notaEditar.getFrenosUnitario2()));
            txtFrenosTotal2.setText(notaUtils.eliminarCero(notaEditar.getFrenosTotal2()));

            txtOtros.setText(notaEditar.getOtros());
            txtOtrosCantidad.setText(notaUtils.eliminarCero(notaEditar.getOtrosCantidad()));
            txtOtrosUnitario.setText(notaUtils.eliminarCero(notaEditar.getOtrosUnitario()));
            txtOtrosTotal.setText(notaUtils.eliminarCero(notaEditar.getOtrosTotal()));

            txtOtros2.setText(notaEditar.getOtros2());
            txtOtrosCantidad2.setText(notaUtils.eliminarCero(notaEditar.getOtrosCantidad2()));
            txtOtrosUnitario2.setText(notaUtils.eliminarCero(notaEditar.getOtrosUnitario2()));
            txtOtrosTotal2.setText(notaUtils.eliminarCero(notaEditar.getOtrosTotal2()));

            txtSubTotalFrenos.setText(notaUtils.eliminarCero(notaEditar.getSubTotalFrenos()));
            txtSubTotalMecanica.setText(notaUtils.eliminarCero(notaEditar.getSubTotalMecanica()));
            txtSubTotalOtros.setText(notaUtils.eliminarCero(notaEditar.getSubTotalOtros()));
            txtTotal.setText(notaUtils.eliminarCero(notaEditar.getTotal()));

            if (notaEditar.getStatusNota().equals(StatusNota.PAGADO.toString())){
                btnActualizarAdeudo.setDisable(true);
                btnActualizarSaldoFavor.setDisable(true);
            } else if (notaEditar.getStatusNota().equals(StatusNota.A_FAVOR.toString())) {
                btnActualizarAdeudo.setDisable(true);
            } else if (notaEditar.getStatusNota().equals(StatusNota.POR_PAGAR.toString()) || notaEditar.getStatusNota().equals(StatusNota.VENCIDO.toString())) {
                btnActualizarSaldoFavor.setDisable(true);
            }

            setNotaEditar(notaEditar);


        }//if nota != null


    }//llenarNota


    private void revisarCheckBoxes(String[] status) {

        fillCheckBox(cbRayonesSi, cbRayonesNo, status[0]);
        fillCheckBox(cbGolpesSi, cbGolpesNo, status[1]);
        fillCheckBox(cbTaponesSi, cbTaponesNo, status[2]);
        fillCheckBox(cbTapetesSi, cbTapetesNo, status[3]);
        fillCheckBox(cbRadioSi, cbRadioNo, status[4]);
        fillCheckBox(cbGatoSi, cbGatoNo, status[5]);
        fillCheckBox(cbLlaveSi, cbLlaveNo, status[6]);
        fillCheckBox(cbLlantaSi, cbLlantaNo, status[7]);


    }//revisarCheckBoxes

    private CheckBox fillCheckBox(CheckBox checkBoxSi, CheckBox checkBoxNo, String status) {

        if (status == null) {
            status = "";
        }


        if (status.equals(StatatusSiNo.SI.toString())) {
            checkBoxSi.setSelected(true);
            return checkBoxSi;
        } else if (status.equals(StatatusSiNo.NO.toString())) {
            checkBoxNo.setSelected(true);
            return checkBoxNo;
        } else {
            checkBoxSi.setSelected(false);
            checkBoxNo.setSelected(false);
            return null;
        }


    }//fillCheckBox

    private void operacionesCampos() {
//Alineacion
        txtAlineacionCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtAlineacionCantidad.getText()) * notaUtils.toFloatSafe(txtAlineacionUnitario.getText());

            txtAlineacionTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtAlineacionUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtAlineacionCantidad.getText()) * notaUtils.toFloatSafe(newValue.toString());
            txtAlineacionTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //Balanceo

        txtBalanceoCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtBalanceoCantidad.getText()) * notaUtils.toFloatSafe(txtBalanceoUnitario.getText());

            txtBalanceoTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtBalanceoUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtBalanceoCantidad.getText()) * notaUtils.toFloatSafe(newValue.toString());

            txtBalanceoTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //llantas
        txtLlantasCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtLlantasCantidad.getText()) * notaUtils.toFloatSafe(txtLlantasUnitario.getText());

            txtLlantasTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtLlantasUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtLlantasCantidad.getText()) * notaUtils.toFloatSafe(newValue.toString());

            txtLlantasTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //amortiguadores delanteros
        txtAmorDelCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtAmorDelCantidad.getText()) * notaUtils.toFloatSafe(txtAmorDelUnitario.getText());

            txtAmorDelTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtAmorDelUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtAmorDelCantidad.getText()) * notaUtils.toFloatSafe(newValue.toString());

            txtAmorDelTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //amortiguadores traseros

        txtAmorTrasCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtAmorTrasCantidad.getText()) * notaUtils.toFloatSafe(txtAmorTrasUnitario.getText());

            txtAmorTrasTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtAmorTrasUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtAmorTrasCantidad.getText()) * notaUtils.toFloatSafe(newValue.toString());

            txtAmorTrasTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //suspension

        txtSuspensionCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtSuspensionCantidad.getText()) * notaUtils.toFloatSafe(txtSuspensionUnitario.getText());

            txtSuspensionTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtSuspensionUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtSuspensionCantidad.getText()) * notaUtils.toFloatSafe(newValue.toString());

            txtSuspensionTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //suspension 2

        txtSuspensionCantidad2.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtSuspensionCantidad2.getText()) * notaUtils.toFloatSafe(txtSuspensionUnitario2.getText());

            txtSuspensionTotal2.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtSuspensionUnitario2.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtSuspensionCantidad2.getText()) * notaUtils.toFloatSafe(newValue.toString());

            txtSuspensionTotal2.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //Mecanica

        txtMecanicaCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtMecanicaCantidad.getText()) * notaUtils.toFloatSafe(txtMecanicaUnitario.getText());

            txtMecanicaTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtMecanicaUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtMecanicaCantidad.getText()) * notaUtils.toFloatSafe(newValue.toString());

            txtMecanicaTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //mecanica2

        txtMecanicaCantidad2.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtMecanicaCantidad2.getText()) * notaUtils.toFloatSafe(txtMecanicaUnitario2.getText());

            txtMecanicaTotal2.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtMecanicaUnitario2.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtMecanicaCantidad2.getText()) * notaUtils.toFloatSafe(newValue.toString());

            txtMecanicaTotal2.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //frenos

        txtFrenosCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtFrenosCantidad.getText()) * notaUtils.toFloatSafe(txtFrenosUnitario.getText());

            txtFrenosTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtFrenosUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtFrenosCantidad.getText()) * notaUtils.toFloatSafe(newValue.toString());

            txtFrenosTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtFrenosCantidad2.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtFrenosCantidad2.getText()) * notaUtils.toFloatSafe(txtFrenosUnitario2.getText());

            txtFrenosTotal2.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtFrenosUnitario2.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtFrenosCantidad2.getText()) * notaUtils.toFloatSafe(newValue.toString());

            txtFrenosTotal2.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });
        //otros
        txtOtrosCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtOtrosCantidad.getText()) * notaUtils.toFloatSafe(txtOtrosUnitario.getText());

            txtOtrosTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtOtrosUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtOtrosCantidad.getText()) * notaUtils.toFloatSafe(newValue.toString());

            txtOtrosTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });
        //otros2
        txtOtrosCantidad2.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtOtrosCantidad2.getText()) * notaUtils.toFloatSafe(txtOtrosUnitario2.getText());

            txtOtrosTotal2.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtOtrosUnitario2.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtOtrosCantidad2.getText()) * notaUtils.toFloatSafe(newValue.toString());

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

        suma =  notaUtils.toFloatSafe(txtAlineacionTotal.getText()) +
                notaUtils.toFloatSafe(txtBalanceoTotal.getText()) +
                notaUtils.toFloatSafe(txtLlantasTotal.getText()) +
                notaUtils.toFloatSafe(txtAmorDelTotal.getText()) +
                notaUtils.toFloatSafe(txtAmorTrasTotal.getText()) +
                notaUtils.toFloatSafe(txtSuspensionTotal.getText()) +
                notaUtils.toFloatSafe(txtSuspensionTotal2.getText()) +
                notaUtils.toFloatSafe(txtMecanicaTotal.getText()) +
                notaUtils.toFloatSafe(txtMecanicaTotal2.getText()) +
                notaUtils.toFloatSafe(txtFrenosTotal.getText()) +
                notaUtils.toFloatSafe(txtFrenosTotal2.getText()) +
                notaUtils.toFloatSafe(txtOtrosTotal.getText()) +
                notaUtils.toFloatSafe(txtOtrosTotal2.getText()) +
                notaUtils.toFloatSafe(txtSubTotalMecanica.getText()) +
                notaUtils.toFloatSafe(txtSubTotalFrenos.getText()) +
                notaUtils.toFloatSafe(txtSubTotalOtros.getText()); //16


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

        //deshabilitar botones de actualizar saldo a favor y adeudo si estos son 0


    }//configuraciones

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


    //Seccion precargar en nota


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

    public int getPorcentajeGasNota() {
        return this.porcentajeGasNota;
    }

    public void setPorcentajeGasNota(final int porcentajeGasNota) {
        this.porcentajeGasNota = porcentajeGasNota;
    }


    @FXML
    private void actualizarAdeudo(NotaDTO notaAdeudo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/nota/EditarAdeudo.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent root = loader.load();
            EditarAdeudoController controller = loader.getController();
            controller.setAdeudo(notaAdeudo);

            Stage stage = new Stage(StageStyle.UTILITY);
            stage.setTitle("Actualizar Adeudo");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

            actualizarDatosCliente(notaAdeudo);

        } catch (Exception e) {
            mostrarError("Error inesperado", "", "Ocurrió un problema al realizar la operación.");
            e.printStackTrace();

        }


    }//actualizarAdeudo

    private void actualizarDatosCliente(Integer clienteId, Integer vehiculoId) {


        if (clienteId != null) {

            try {
                Cliente cliente = clienteService.buscarClientePorId(clienteId, StatusCliente.ACTIVE.toString());
                Vehiculo vehiculo = vehiculoService.buscarVehiculoPorId(vehiculoId, StatusVehiculo.ACTIVE.toString());

                notaUtils.campoFormatter(
                        cliente.getNombre() + " " + (cliente.getApellido() != null ? cliente.getApellido() : "") + " " +
                                (cliente.getSegundoApellido() != null ? cliente.getSegundoApellido() : ""),
                        txtNombre,
                        CampoNota.NOMBRE
                );

                notaUtils.campoFormatter(cliente.getDomicilio() != null ? cliente.getDomicilio() : "", txtDireccion, txtDireccion2);


                notaUtils.campoFormatter(
                        cliente.getRfc() != null ? cliente.getRfc() : "",
                        txtRfc,
                        CampoNota.RFC
                );

                notaUtils.campoFormatter(
                        cliente.getCorreo() != null ? cliente.getCorreo() : "",
                        txtCorreo
                );
                notaUtils.campoFormatter(
                        vehiculo.getMarca().getNombreMarca(),
                        txtMarca,
                        CampoNota.MARCA
                );
                notaUtils.campoFormatter(
                        vehiculo.getModelo().getNombreModelo(),
                        txtModelo,
                        CampoNota.MODELO
                );

                txtAnioVehiculo.setText(vehiculo.getAnio() + "");

                notaUtils.campoFormatter(
                        vehiculo.getKilometros() + "",
                        txtKms,
                        CampoNota.KILOMETROS
                );

                txtPlacas.setText(vehiculo.getPlacas() != null ? vehiculo.getPlacas() : "");


                mostrarInformacion("Informacion actualizada", "", "Los campos se actualizaron correctamente.");
            } catch (Exception e) {
                e.printStackTrace();
                mostrarError("Error inesperado", "", "Ocurrió un problema al realizar la operación.");
            }

        }


    }//actualizarDatosCliente

    private void actualizarDatosCliente(NotaDTO nota) {

        if (nota != null)
            llenarNota(nota.getNumNota());

    }//actualizarDatosCliente


    private void actualizarSaldo(NotaDTO notaClienteDet) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/nota/EditarSaldo.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);

            Parent root = loader.load();
            EditarSaldoController controller = loader.getController();
            controller.setSaldoFavor(notaClienteDet);

            Stage stage = new Stage(StageStyle.UTILITY);
            stage.setTitle("Actualizar Saldo");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

            actualizarDatosCliente(notaClienteDet);


        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error inesperado", "", "Ocurrió un problema al realizar la operación.");
        }


    }//actualizarSaldo

    private void notaDetalles(NotaDTO notaDetalles) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/nota/NotaDetalles.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);

            Parent root = loader.load();
            NotaDetallesController controller = loader.getController();
            controller.setNotaDetalles(notaDetalles);

            Stage stage = new Stage(StageStyle.UTILITY);
            stage.setTitle("Nota Detalles");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

            actualizarDatosCliente(notaDetalles);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error inesperado", "", "Ocurrió un problema al realizar la operación.");
        }

    }//notaDetalles


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


    private void actualizarNota(NotaDTO notaActualizar) {

        boolean guardar = mostrarConfirmacion("Guardar cambios", "", "¿Desea guardar los cambios realizados?", "Guardar", "Cancelar");

        checkCheckBoxes();

        if (guardar) {
            Nota notaRegistrar = notaService.buscarPorId(notaActualizar.getNotaId());
            NotaDetalle ndRegistrar = notaDetalleService.buscarNotaDetalle(notaRegistrar);
            NotaClienteDetalle detalleCliente = notaClienteDetService.buscarclienteDetalle(notaRegistrar);


            Cliente cliente = clienteService.buscarClientePorId(notaActualizar.getClienteId(), StatusNota.ACTIVE.toString());
            Vehiculo vehiculo = vehiculoService.buscarVehiculoPorId(notaActualizar.getVehiculoId(), StatusVehiculo.ACTIVE.toString());

            Inventario inventario = null;
            if (notaActualizar.getInventarioId() != null)
                inventario = inventarioService.buscarLlantaPorId(notaActualizar.getInventarioId());


            //actualizar campos nota
            String fechaYHora = txtAnio.getText() + "-" + "-" + txtMes.getText() + "-" + txtDia.getText() + " " + txtHoraEntrega.getText() + ":00";

            notaRegistrar.setNumFactura(notaActualizar.getNumFactura());
            notaRegistrar.setTotal(notaUtils.toFloatSafe(txtTotal.getText()));
            notaRegistrar.setFechaYhora(fechaYHora); //pendiente
            notaRegistrar.setFechaVencimiento(notaActualizar.getFechaVencimiento());
            notaRegistrar.setAdeudo(notaActualizar.getAdeudo());
            notaRegistrar.setSaldoFavor(notaActualizar.getSaldoFavor());
            notaRegistrar.setStatusNota(notaActualizar.getStatusNota());
            notaRegistrar.setCliente(cliente);
            notaRegistrar.setVehiculo(vehiculo);
            notaRegistrar.setCreatedAt(LocalDateTime.now().toString());


            //datos cliente y vehiculo
            //en el caso de la entidad cliente no se setean ya que en caso de haber cambios el usuario los hace con el UPDATE y
            //estos se traen directo de la BD no de los campos de texto. El mismo caso con vehiculo


            //actualizar nota detalle
            ndRegistrar.setObservaciones(txtObservaciones.getText());
            ndRegistrar.setObservaciones2(txtObservaciones2.getText());
            ndRegistrar.setPorcentajeGas(getPorcentajeGasNota());
            ndRegistrar.setRayones(getRayones());//comienzan checkBox
            ndRegistrar.setGolpes(getGolpes());
            ndRegistrar.setTapones(getTapones());
            ndRegistrar.setTapetes(getTapetes());
            ndRegistrar.setRadio(getRadio());
            ndRegistrar.setGato(getGato());
            ndRegistrar.setLlave(getLlave());
            ndRegistrar.setLlanta(getLlanta()); //terminan CkeckBox
            ndRegistrar.setAlineacion(txtAlineacion.getText()); //comienzan campos de la nota
            ndRegistrar.setAlineacionCantidad(notaUtils.toIntSafe(txtAlineacionCantidad.getText()));
            ndRegistrar.setAlineacionUnitario(notaUtils.toFloatSafe(txtAlineacionUnitario.getText()));
            ndRegistrar.setAlineacionTotal(notaUtils.toFloatSafe(txtAlineacionTotal.getText()));
            ndRegistrar.setBalanceo(txtBalanceo.getText());
            ndRegistrar.setBalanceoCantidad(notaUtils.toIntSafe(txtBalanceoCantidad.getText()));
            ndRegistrar.setBalanceoUnitario(notaUtils.toFloatSafe(txtBalanceoUnitario.getText()));
            ndRegistrar.setBalanceoTotal(notaUtils.toFloatSafe(txtBalanceoTotal.getText()));
            ndRegistrar.setAmorDelanteros(txtAmorDelanteros.getText());
            ndRegistrar.setAmorDelCantidad(notaUtils.toIntSafe(txtAmorDelCantidad.getText()));
            ndRegistrar.setAmorDelUnitario(notaUtils.toFloatSafe(txtAmorDelUnitario.getText()));
            ndRegistrar.setAmorDelTotal(notaUtils.toFloatSafe(txtAmorDelTotal.getText()));
            ndRegistrar.setAmorTraseros(txtAmorTraseros.getText());
            ndRegistrar.setAmorTrasCantidad(notaUtils.toIntSafe(txtAmorTrasCantidad.getText()));
            ndRegistrar.setAmorTrasUnitario(notaUtils.toFloatSafe(txtAmorTrasUnitario.getText()));
            ndRegistrar.setAmorTrasTotal(notaUtils.toFloatSafe(txtAmorTrasTotal.getText()));
            ndRegistrar.setSuspension(txtSuspension.getText());
            ndRegistrar.setSuspensionCantidad(notaUtils.toIntSafe(txtSuspensionCantidad.getText()));
            ndRegistrar.setSuspensionUnitario(notaUtils.toFloatSafe(txtSuspensionUnitario.getText()));
            ndRegistrar.setSuspensionTotal(notaUtils.toFloatSafe(txtSuspensionTotal.getText()));
            ndRegistrar.setSuspension2(txtSuspension2.getText());
            ndRegistrar.setSuspensionCantidad2(notaUtils.toIntSafe(txtSuspensionCantidad2.getText()));
            ndRegistrar.setSuspensionUnitario2(notaUtils.toFloatSafe(txtSuspensionUnitario2.getText()));
            ndRegistrar.setMecanica(txtMecanica.getText());
            ndRegistrar.setMecanicaCantidad(notaUtils.toIntSafe(txtMecanicaCantidad.getText()));
            ndRegistrar.setMecanicaUnitario(notaUtils.toFloatSafe(txtMecanicaUnitario.getText()));
            ndRegistrar.setMecanicaTotal(notaUtils.toFloatSafe(txtMecanicaTotal.getText()));
            ndRegistrar.setMecanica2(txtMecanica2.getText());
            ndRegistrar.setMecanicaCantidad2(notaUtils.toIntSafe(txtMecanicaCantidad2.getText()));
            ndRegistrar.setMecanicaUnitario2(notaUtils.toFloatSafe(txtMecanicaUnitario2.getText()));
            ndRegistrar.setMecanicaTotal2(notaUtils.toFloatSafe(txtMecanicaTotal2.getText()));
            ndRegistrar.setFrenos(txtFrenos.getText());
            ndRegistrar.setFrenosCantidad(notaUtils.toIntSafe(txtFrenosCantidad.getText()));
            ndRegistrar.setFrenosUnitario(notaUtils.toFloatSafe(txtFrenosUnitario.getText()));
            ndRegistrar.setFrenosTotal(notaUtils.toFloatSafe(txtFrenosTotal.getText()));
            ndRegistrar.setFrenos2(txtFrenos2.getText());
            ndRegistrar.setFrenosCantidad2(notaUtils.toIntSafe(txtFrenosCantidad2.getText()));
            ndRegistrar.setFrenosUnitario2(notaUtils.toFloatSafe(txtFrenosUnitario2.getText()));
            ndRegistrar.setFrenosTotal2(notaUtils.toFloatSafe(txtFrenosTotal2.getText()));
            ndRegistrar.setOtros(txtOtros.getText());
            ndRegistrar.setOtrosCantidad(notaUtils.toIntSafe(txtOtrosCantidad.getText()));
            ndRegistrar.setOtrosUnitario(notaUtils.toFloatSafe(txtOtrosUnitario.getText()));
            ndRegistrar.setOtrosTotal(notaUtils.toFloatSafe(txtOtrosTotal.getText()));
            ndRegistrar.setOtros2(txtOtros2.getText());
            ndRegistrar.setOtrosCantidad2(notaUtils.toIntSafe(txtOtrosCantidad2.getText()));
            ndRegistrar.setOtrosUnitario2(notaUtils.toFloatSafe(txtOtrosUnitario2.getText()));
            ndRegistrar.setOtrosTotal2(notaUtils.toFloatSafe(txtOtrosTotal2.getText()));
            ndRegistrar.setSubTotalFrenos(notaUtils.toFloatSafe(txtSubTotalOtros.getText()));
            ndRegistrar.setSubTotalMecanica(notaUtils.toFloatSafe(txtSubTotalMecanica.getText()));
            ndRegistrar.setSubTotalOtros(notaUtils.toFloatSafe(txtSubTotalOtros.getText()));
            ndRegistrar.setLlantaCampo(txtLlantas.getText());
            ndRegistrar.setLlantaCantidad(notaUtils.toIntSafe(txtLlantasCantidad.getText()));
            ndRegistrar.setLlantaUnitario(notaUtils.toFloatSafe(txtLlantasUnitario.getText()));
            ndRegistrar.setLlantaTotal(notaUtils.toFloatSafe(txtLlantasTotal.getText()));

            //actuallizar Nota Cliente Detalle
            detalleCliente.setNombreClienteNota(txtNombre.getText());
            detalleCliente.setDireccion1Nota(txtDireccion.getText());
            detalleCliente.setDireccion2Nota(txtDireccion2.getText());
            detalleCliente.setRfcNota(txtRfc.getText());
            detalleCliente.setCorreoNota(txtCorreo.getText());
            detalleCliente.setMarcaNota(txtMarca.getText());
            detalleCliente.setModeloNota(txtModelo.getText());
            detalleCliente.setCategoriaNota(detalleCliente.getCategoriaNota());
            detalleCliente.setAnioNota(notaUtils.toIntSafe(txtAnioVehiculo.getText()));
            detalleCliente.setKilometrosNota(notaUtils.toIntSafe(txtKms.getText()));
            detalleCliente.setPlacasNota(txtPlacas.getText());


            try {

                notaService.actualizarNota(notaRegistrar, ndRegistrar, detalleCliente);


                mostrarInformacion("Nota Actualizada", "", "Los cambios se guardaron correctamente.");


            } catch (Exception e) {
                e.printStackTrace();
                mostrarError("Error inesperado", "", "Ocurrió un problema al realizar la operación.");
            }
        }//if guardar

    }//actualizarNota

    private void agregarNumFactura(NotaDTO notaEditar) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/nota/AgregarNumFactura.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);

            Parent root = loader.load();
            AgregarNumFacturaController controller = loader.getController();
            controller.setNumFactura(notaEditar.getNotaId(),notaEditar.getNumFactura());

            Stage stage = new Stage(StageStyle.UTILITY);
            stage.setTitle("Agregar Numero de Factura");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

            actualizarDatosCliente(notaEditar);

        } catch (Exception e) {
            mostrarError("Error inesperado", "", "Ocurrió un problema al realizar la operación.");
            e.printStackTrace();

        }


    }//agregarNumFactura


//getters y setters

    public String getNumNota() {
        return this.numNota;
    }

    public void setNumNota(final String numNota) {
        this.numNota = numNota;
    }

    public NotaDTO getNotaEditar() {
        return this.notaEditar;
    }

    public void setNotaEditar(final NotaDTO notaEditar) {
        this.notaEditar = notaEditar;
    }

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

    public void setTapones(final String tapones) {
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
}//class
