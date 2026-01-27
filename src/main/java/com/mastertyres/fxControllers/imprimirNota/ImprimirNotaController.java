package com.mastertyres.fxControllers.imprimirNota;

import com.mastertyres.cliente.service.ClienteService;
import com.mastertyres.common.utils.RegexTools;
import com.mastertyres.fxComponents.LoadingComponentController;
import com.mastertyres.fxComponents.interfaces.ILoading;
import com.mastertyres.inventario.service.InventarioService;
import com.mastertyres.nota.model.BaseNota;
import com.mastertyres.nota.model.NotaDTO;
import com.mastertyres.nota.model.StatusNota;
import com.mastertyres.nota.service.NotaService;
import com.mastertyres.notaDetalle.service.NotaDetalleService;
import com.mastertyres.vehiculo.service.VehiculoService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

//el metododo agregarNota() recibe de parametro true si quiere la nota original
@Component
public class ImprimirNotaController extends BaseNota implements ILoading {

    @FXML
    private VBox contenedorTipoNota;
    @FXML
    private Label lbltipoNota;
    @FXML
    private Label lblNumFactura;

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


    private NotaDTO notaImprimir;
    private String numNota;
    private LoadingComponentController loadingOverlayController;

    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingOverlayController = loading;
    }

    @FXML
    private void initialize() {
        configuraciones();
        operacionesCampos();

    }//initialize

    public void agregarNota(String numNota, boolean tipoNota) {
        llenarNota(numNota,tipoNota);
    }

    private void llenarNota(String numNota, boolean tipoNota) {
        notaImprimir = notaService.buscarPorNumNota(StatusNota.ACTIVE.toString(), numNota);


        if (notaImprimir != null) {

            txtNumNota.setText(notaImprimir.getNumNota());
            lblNumFactura.setText("Número de factura: " + (notaImprimir.getNumFactura() != null ? notaImprimir.getNumFactura() : "N/D"));

            //codigo para mostrar fecha, la fecha viene en una misma columna en la BD y semuestra en diferentes campos en la Nota

            String strFecha = "", strHoraEntrega = "", strHoraFormateada = "";
            String arrayFecha[];


            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter outPutFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter horaInputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            DateTimeFormatter horaOutputFormatter = DateTimeFormatter.ofPattern("HH:mm");
            strFecha = notaImprimir.getFechaYHora();

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
            txtNombre.setText(notaImprimir.getNombreClienteNota());

            txtDireccion.setText(notaImprimir.getDireccion1Nota() != null ? notaImprimir.getDireccion1Nota() : "");
            txtDireccion2.setText(notaImprimir.getDireccion2Nota() != null ? notaImprimir.getDireccion2Nota() : "");
            txtRfc.setText(notaImprimir.getRfcNota() != null ? notaImprimir.getRfcNota() : "");
            txtCorreo.setText(notaImprimir.getCorreoNota() != null ? notaImprimir.getCorreoNota() : "");

            //datos vehiculo
            txtMarca.setText(notaImprimir.getMarcaNota());
            txtModelo.setText(notaImprimir.getModeloNota());
            txtAnioVehiculo.setText(notaImprimir.getAnioNota() + "");
            txtKms.setText((notaImprimir.getKilometrosNota() != null ? notaImprimir.getKilometrosNota() : "") + "");
            txtPlacas.setText(notaImprimir.getPlacasNota() != null ? notaImprimir.getPlacasNota() : "");
            dibujarGasolina(notaImprimir.getPorcentajeGas());

            //checkBox
            revisarCheckBoxes(notaImprimir);

            txtObservaciones.setText(notaImprimir.getObservaciones() != null ? notaImprimir.getObservaciones() : "");
            txtObservaciones2.setText(notaImprimir.getObservaciones2() != null ? notaImprimir.getObservaciones2() : "");

            //Nota
            txtAlineacion.setText(notaImprimir.getAlineacion());
            txtAlineacionCantidad.setText(notaUtils.eliminarCero(notaImprimir.getAlineacionCantidad()));
            txtAlineacionUnitario.setText(notaUtils.eliminarCero(notaImprimir.getAlineacionUnitario()));
            txtAlineacionTotal.setText(notaUtils.eliminarCero(notaImprimir.getAlineacionTotal()));

            txtBalanceo.setText(notaImprimir.getBalanceo());
            txtBalanceoCantidad.setText(notaUtils.eliminarCero(notaImprimir.getBalanceoCantidad()));
            txtBalanceoUnitario.setText(notaUtils.eliminarCero(notaImprimir.getBalanceoUnitario()));
            txtBalanceoTotal.setText(notaUtils.eliminarCero(notaImprimir.getBalanceoTotal()));

            txtLlantas.setText(notaImprimir.getLlantaCampo());
            txtLlantasCantidad.setText(notaUtils.eliminarCero(notaImprimir.getLlantaCantidad()));
            txtLlantasUnitario.setText(notaUtils.eliminarCero(notaImprimir.getLlantaUnitario()));
            txtLlantasTotal.setText(notaUtils.eliminarCero(notaImprimir.getLlantaTotal()));

            txtAmorDelanteros.setText(notaImprimir.getAmorDelanteros());
            txtAmorDelCantidad.setText(notaUtils.eliminarCero(notaImprimir.getAmorDelCantidad()));
            txtAmorDelUnitario.setText(notaUtils.eliminarCero(notaImprimir.getAmorDelUnitario()));
            txtAmorDelTotal.setText(notaUtils.eliminarCero(notaImprimir.getAmorDelTotal()));

            txtAmorTraseros.setText(notaImprimir.getAmorTraseros());
            txtAmorTrasCantidad.setText(notaUtils.eliminarCero(notaImprimir.getAmorTrasCantidad()));
            txtAmorTrasUnitario.setText(notaUtils.eliminarCero(notaImprimir.getAmorTrasUnitario()));
            txtAmorTrasTotal.setText(notaUtils.eliminarCero(notaImprimir.getAmorTrasTotal()));

            txtSuspension.setText(notaImprimir.getSuspension());
            txtSuspensionCantidad.setText(notaUtils.eliminarCero(notaImprimir.getSuspensionCantidad()));
            txtSuspensionUnitario.setText(notaUtils.eliminarCero(notaImprimir.getSuspensionUnitario()));
            txtSuspensionTotal.setText(notaUtils.eliminarCero(notaImprimir.getSuspensionTotal()));

            txtSuspension2.setText(notaImprimir.getSuspension2());
            txtSuspensionCantidad2.setText(notaUtils.eliminarCero(notaImprimir.getSuspensionCantidad2()));
            txtSuspensionUnitario2.setText(notaUtils.eliminarCero(notaImprimir.getSuspensionUnitario2()));
            txtSuspensionTotal2.setText(notaUtils.eliminarCero(notaImprimir.getSuspensionTotal2()));

            txtMecanica.setText(notaImprimir.getMecanica());
            txtMecanicaCantidad.setText(notaUtils.eliminarCero(notaImprimir.getMecanicaCantidad()));
            txtMecanicaUnitario.setText(notaUtils.eliminarCero(notaImprimir.getMecanicaUnitario()));
            txtMecanicaTotal.setText(notaUtils.eliminarCero(notaImprimir.getMecanicaTotal()));

            txtMecanica2.setText(notaImprimir.getMecanica2());
            txtMecanicaCantidad2.setText(notaUtils.eliminarCero(notaImprimir.getMecanicaCantidad2()));
            txtMecanicaUnitario2.setText(notaUtils.eliminarCero(notaImprimir.getMecanicaUnitario2()));
            txtMecanicaTotal2.setText(notaUtils.eliminarCero(notaImprimir.getMecanicaTotal2()));

            txtFrenos.setText(notaImprimir.getFrenos());
            txtFrenosCantidad.setText(notaUtils.eliminarCero(notaImprimir.getFrenosCantidad()));
            txtFrenosUnitario.setText(notaUtils.eliminarCero(notaImprimir.getFrenosUnitario()));
            txtFrenosTotal.setText(notaUtils.eliminarCero(notaImprimir.getFrenosTotal()));

            txtFrenos2.setText(notaImprimir.getFrenos2());
            txtFrenosCantidad2.setText(notaUtils.eliminarCero(notaImprimir.getFrenosCantidad2()));
            txtFrenosUnitario2.setText(notaUtils.eliminarCero(notaImprimir.getFrenosUnitario2()));
            txtFrenosTotal2.setText(notaUtils.eliminarCero(notaImprimir.getFrenosTotal2()));

            txtOtros.setText(notaImprimir.getOtros());
            txtOtrosCantidad.setText(notaUtils.eliminarCero(notaImprimir.getOtrosCantidad()));
            txtOtrosUnitario.setText(notaUtils.eliminarCero(notaImprimir.getOtrosUnitario()));
            txtOtrosTotal.setText(notaUtils.eliminarCero(notaImprimir.getOtrosTotal()));

            txtOtros2.setText(notaImprimir.getOtros2());
            txtOtrosCantidad2.setText(notaUtils.eliminarCero(notaImprimir.getOtrosCantidad2()));
            txtOtrosUnitario2.setText(notaUtils.eliminarCero(notaImprimir.getOtrosUnitario2()));
            txtOtrosTotal2.setText(notaUtils.eliminarCero(notaImprimir.getOtrosTotal2()));

            txtSubTotalFrenos.setText(notaUtils.eliminarCero(notaImprimir.getSubTotalFrenos()));
            txtSubTotalMecanica.setText(notaUtils.eliminarCero(notaImprimir.getSubTotalMecanica()));
            txtSubTotalOtros.setText(notaUtils.eliminarCero(notaImprimir.getSubTotalOtros()));
            txtTotal.setText(notaUtils.eliminarCero(notaImprimir.getTotal()));

            setNotaImprimir(notaImprimir);

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

    private void configuraciones() {

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

    public void setNotaImprimir(final NotaDTO notaImprimir) {
        this.notaImprimir = notaImprimir;
    }



}//class
