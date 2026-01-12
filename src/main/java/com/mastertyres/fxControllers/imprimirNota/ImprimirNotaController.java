package com.mastertyres.fxControllers.imprimirNota;

import com.mastertyres.cliente.service.ClienteService;
import com.mastertyres.common.RegexTools;
import com.mastertyres.common.utils.NotaUtils;
import com.mastertyres.inventario.service.InventarioService;
import com.mastertyres.nota.model.NotaDTO;
import com.mastertyres.nota.model.StatatusSiNo;
import com.mastertyres.nota.model.StatusNota;
import com.mastertyres.nota.service.NotaService;
import com.mastertyres.notaDetalle.service.NotaDetalleService;
import com.mastertyres.vehiculo.service.VehiculoService;
import javafx.fxml.FXML;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

//el metododo agregarNota() recibe de parametro true si quiere la nota original
@Component
public class ImprimirNotaController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private AnchorPane contentNota;
    @FXML
    private ScrollPane scrollPaneNota;
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
    private VBox contenedorTipoNota;
    @FXML
    private TextField txtNumNota;
    @FXML
    private Label lblNumFactura;
    @FXML
    private Label lbltipoNota;

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
    private  Button btnAgregarNumFactura;


    private int porcentajeGasNota;
    private NotaDTO notaEditar;
    private String numNota;


    @FXML
    private void initialize() {
        configuraciones();
        operacionesCampos();

    }//initialize

    public void agregarNota(String numNota, boolean tipoNota) {
        llenarNota(numNota,tipoNota);
    }

    private void llenarNota(String numNota, boolean tipoNota) {
        notaEditar = notaService.buscarPorNumNota(StatusNota.ACTIVE.toString(), numNota);


        if (notaEditar != null) {

            txtNumNota.setText(notaEditar.getNumNota());
            lblNumFactura.setText("Número de factura: " + (notaEditar.getNumFactura() != null ? notaEditar.getNumFactura() : "N/D"));

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

            setNotaEditar(notaEditar);

            //Convierte el mismp FXML a dos Notas Original y Copia
            if (tipoNota){
                lbltipoNota.setText("ORIGINAL");
                lblNumFactura.setVisible(true);
            }else {
                lbltipoNota.setText("COPIA");
                lblNumFactura.setVisible(false);
            }




        }//if nota != null


    }//llenarNota

    private void revisarCheckBoxes(String[] status) {

        fillCheckBox(cbRayonesSi, cbRayonesNo, status[0]);
        fillCheckBox(cbGolpesSi, cbGolpesNo, status[1]);
        fillCheckBox(cbTaponesSi, cbTaponesNo, status[2]);
        fillCheckBox(cbTapetesSi, cbTapetesSi, status[3]);
        fillCheckBox(cbRadioSi, cbRadioSi, status[4]);
        fillCheckBox(cbGatoSi, cbGatoSi, status[5]);
        fillCheckBox(cbLlaveSi, cbLlaveSi, status[6]);
        fillCheckBox(cbLlantaSi, cbLlantaSi, status[7]);


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
            float num = notaUtils.toFloatSafe(txtOtrosCantidad2.getText()) * notaUtils. toFloatSafe(newValue.toString());

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
      //  MenuContextSetting.disableMenu(rootPane);

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


    public NotaDTO getNotaEditar() {
        return this.notaEditar;
    }

    public void setNotaEditar(final NotaDTO notaEditar) {
        this.notaEditar = notaEditar;
    }

    public int getPorcentajeGasNota() {
        return this.porcentajeGasNota;
    }

    public void setPorcentajeGasNota(final int porcentajeGasNota) {
        this.porcentajeGasNota = porcentajeGasNota;
    }

    public AnchorPane getRootPane() {
        return this.rootPane;
    }
}//class
