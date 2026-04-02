package com.mastertyres.controllers.fxControllers.historial;

import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.service.NotaUtils;
import com.mastertyres.common.utils.MenuContextSetting;
import com.mastertyres.nota.entity.BaseNota;
import com.mastertyres.nota.DTOs.NotaDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class NotaPreviewController extends BaseNota implements IFxController {
    @FXML
    private Label lblNumFactura;

    @Autowired
    private NotaUtils notaUtils;



    @FXML
    private void initialize(){
        configuraciones();
        listeners();

    }//initialize

    public void agregarNota(NotaDTO notaVentana) {
        llenarNota(notaVentana);
    }

    private void llenarNota(NotaDTO notaVentana) {

        if (notaVentana != null) {

            lblNumFactura.setText("Número de factura: " + (notaVentana.getNumFactura() != null ? notaVentana.getNumFactura() : "N/D"));
            txtNumNota.setText(notaVentana.getNumNota());

            //codigo para mostrar fecha, la fecha viene en una misma columna en la BD y semuestra en diferentes campos en la Nota

            String strFecha = "", strHoraEntrega = "", strHoraFormateada = "";
            String arrayFecha[];


            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter outPutFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter horaInputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            DateTimeFormatter horaOutputFormatter = DateTimeFormatter.ofPattern("HH:mm");
            strFecha = notaVentana.getFechaYHora();

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
            txtNombre.setText(notaVentana.getNombreClienteNota());
            txtDireccion.setText(notaVentana.getDireccion1Nota() != null ? notaVentana.getDireccion1Nota() : "");
            txtDireccion2.setText(notaVentana.getDireccion2Nota() != null ? notaVentana.getDireccion2Nota() : "");
            txtRfc.setText(notaVentana.getRfcNota() != null ? notaVentana.getRfcNota() : "");
            txtCorreo.setText(notaVentana.getCorreoNota() != null ? notaVentana.getCorreoNota() : "");

            //datos vehiculo
            txtMarca.setText(notaVentana.getMarcaNota());
            txtModelo.setText(notaVentana.getModeloNota());
            txtAnioVehiculo.setText(notaVentana.getAnioNota() + "");
            txtKms.setText((notaVentana.getKilometrosNota() != null ? notaVentana.getKilometrosNota() : "") + "");
            txtPlacas.setText(notaVentana.getPlacasNota() != null ? notaVentana.getPlacasNota() : "");
            dibujarGasolina(notaVentana.getPorcentajeGas());

            //checkBox
            //este metodo revisa cuales estan seleccionados  y
            revisarCheckBoxes(notaVentana);

            txtObservaciones.setText(notaVentana.getObservaciones() != null ? notaVentana.getObservaciones() : "");
            txtObservaciones2.setText(notaVentana.getObservaciones2() != null ? notaVentana.getObservaciones2() : "");

            //Nota
            txtAlineacion.setText(notaVentana.getAlineacion());
            txtAlineacionCantidad.setText(notaUtils.eliminarCero(notaVentana.getAlineacionCantidad()));
            txtAlineacionUnitario.setText(notaUtils.eliminarCero(notaVentana.getAlineacionUnitario()));
            txtAlineacionTotal.setText(notaUtils.eliminarCero(notaVentana.getAlineacionTotal()));

            txtBalanceo.setText(notaVentana.getBalanceo());
            txtBalanceoCantidad.setText(notaUtils.eliminarCero(notaVentana.getBalanceoCantidad()));
            txtBalanceoUnitario.setText(notaUtils.eliminarCero(notaVentana.getBalanceoUnitario()));
            txtBalanceoTotal.setText(notaUtils.eliminarCero(notaVentana.getBalanceoTotal()));

            txtLlantas.setText(notaVentana.getLlantaCampo());
            txtLlantasCantidad.setText(notaUtils.eliminarCero(notaVentana.getLlantaCantidad()));
            txtLlantasUnitario.setText(notaUtils.eliminarCero(notaVentana.getLlantaUnitario()));
            txtLlantasTotal.setText(notaUtils.eliminarCero(notaVentana.getLlantaTotal()));

            txtAmorDelanteros.setText(notaVentana.getAmorDelanteros());
            txtAmorDelCantidad.setText(notaUtils.eliminarCero(notaVentana.getAmorDelCantidad()));
            txtAmorDelUnitario.setText(notaUtils.eliminarCero(notaVentana.getAmorDelUnitario()));
            txtAmorDelTotal.setText(notaUtils.eliminarCero(notaVentana.getAmorDelTotal()));

            txtAmorTraseros.setText(notaVentana.getAmorTraseros());
            txtAmorTrasCantidad.setText(notaUtils.eliminarCero(notaVentana.getAmorTrasCantidad()));
            txtAmorTrasUnitario.setText(notaUtils.eliminarCero(notaVentana.getAmorTrasUnitario()));
            txtAmorTrasTotal.setText(notaUtils.eliminarCero(notaVentana.getAmorTrasTotal()));

            txtSuspension.setText(notaVentana.getSuspension());
            txtSuspensionCantidad.setText(notaUtils.eliminarCero(notaVentana.getSuspensionCantidad()));
            txtSuspensionUnitario.setText(notaUtils.eliminarCero(notaVentana.getSuspensionUnitario()));
            txtSuspensionTotal.setText(notaUtils.eliminarCero(notaVentana.getSuspensionTotal()));

            txtSuspension2.setText(notaVentana.getSuspension2());
            txtSuspensionCantidad2.setText(notaUtils.eliminarCero(notaVentana.getSuspensionCantidad2()));
            txtSuspensionUnitario2.setText(notaUtils.eliminarCero(notaVentana.getSuspensionUnitario2()));
            txtSuspensionTotal2.setText(notaUtils.eliminarCero(notaVentana.getSuspensionTotal2()));

            txtMecanica.setText(notaVentana.getMecanica());
            txtMecanicaCantidad.setText(notaUtils.eliminarCero(notaVentana.getMecanicaCantidad()));
            txtMecanicaUnitario.setText(notaUtils.eliminarCero(notaVentana.getMecanicaUnitario()));
            txtMecanicaTotal.setText(notaUtils.eliminarCero(notaVentana.getMecanicaTotal()));

            txtMecanica2.setText(notaVentana.getMecanica2());
            txtMecanicaCantidad2.setText(notaUtils.eliminarCero(notaVentana.getMecanicaCantidad2()));
            txtMecanicaUnitario2.setText(notaUtils.eliminarCero(notaVentana.getMecanicaUnitario2()));
            txtMecanicaTotal2.setText(notaUtils.eliminarCero(notaVentana.getMecanicaTotal2()));

            txtFrenos.setText(notaVentana.getFrenos());
            txtFrenosCantidad.setText(notaUtils.eliminarCero(notaVentana.getFrenosCantidad()));
            txtFrenosUnitario.setText(notaUtils.eliminarCero(notaVentana.getFrenosUnitario()));
            txtFrenosTotal.setText(notaUtils.eliminarCero(notaVentana.getFrenosTotal()));

            txtFrenos2.setText(notaVentana.getFrenos2());
            txtFrenosCantidad2.setText(notaUtils.eliminarCero(notaVentana.getFrenosCantidad2()));
            txtFrenosUnitario2.setText(notaUtils.eliminarCero(notaVentana.getFrenosUnitario2()));
            txtFrenosTotal2.setText(notaUtils.eliminarCero(notaVentana.getFrenosTotal2()));

            txtOtros.setText(notaVentana.getOtros());
            txtOtrosCantidad.setText(notaUtils.eliminarCero(notaVentana.getOtrosCantidad()));
            txtOtrosUnitario.setText(notaUtils.eliminarCero(notaVentana.getOtrosUnitario()));
            txtOtrosTotal.setText(notaUtils.eliminarCero(notaVentana.getOtrosTotal()));

            txtOtros2.setText(notaVentana.getOtros2());
            txtOtrosCantidad2.setText(notaUtils.eliminarCero(notaVentana.getOtrosCantidad2()));
            txtOtrosUnitario2.setText(notaUtils.eliminarCero(notaVentana.getOtrosUnitario2()));
            txtOtrosTotal2.setText(notaUtils.eliminarCero(notaVentana.getOtrosTotal2()));

            txtSubTotalFrenos.setText(notaUtils.eliminarCero(notaVentana.getSubTotalFrenos()));
            txtSubTotalMecanica.setText(notaUtils.eliminarCero(notaVentana.getSubTotalMecanica()));
            txtSubTotalOtros.setText(notaUtils.eliminarCero(notaVentana.getSubTotalOtros()));
            txtTotal.setText(notaUtils.eliminarCero(notaVentana.getTotal()));




        }//if nota != null


    }//llenarNota

    @Override
    public void configuraciones() {
        MenuContextSetting.disableMenu(rootPane);
    }

    @Override
    public void listeners() {

    }
}//class
