package com.mastertyres.fxControllers.nota;

import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.cliente.model.StatusCliente;
import com.mastertyres.cliente.service.ClienteService;
import com.mastertyres.nota.model.NotaDTO;
import com.mastertyres.nota.model.StatusNota;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.mastertyres.common.FechaUtils.getFechaFormateada;

@Component
public class NotaDetallesController {

    @FXML
    private NotaDTO notaDetalles;
    @FXML
    private Label lblNumNota;
    @FXML
    private Label lblNumFactura;
    @FXML
    private Label lblFechaVencimiento;
    @FXML
    private Label lblAdeudo;
    @FXML
    private Label lblSaldo;
    @FXML
    private Label lblStatus;
    @FXML
    private Label lblFechaCreacion;
    @FXML
    private Label lblGenero;
    @FXML
    private Label lblTipoCliente;
    @FXML
    private Button btnCancelar;

    @Autowired
    ClienteService clienteService;

    @FXML
    private void initialize() {

    }//initialize

    public void setNotaDetalles(NotaDTO notaDetalles) {
        this.notaDetalles = notaDetalles;
        llenarNota();


    }//setNotaDetalles

    private void llenarNota() {
        String strAdeudo = "N/A", strSaldo = "N/A", strStatus = "",
                strNumFactura = "", strGenero = "", strTipoCliente = "",
                generoComparar = "", fechaFormateadaVencimiento = "N/A";

        Cliente cliente = clienteService.buscarClientePorId(notaDetalles.getClienteId(), StatusCliente.ACTIVE.toString());


        lblNumNota.setText("Numero de nota:  " + notaDetalles.getNumNota());

        strNumFactura = notaDetalles.getNumFactura() != null ? notaDetalles.getNumFactura() : "Sin facturar";
        lblNumFactura.setText("Numero de factura:  " + strNumFactura);


        if (notaDetalles.getFechaVencimiento() != null) {

            fechaFormateadaVencimiento = getFechaFormateada(notaDetalles.getFechaVencimiento());

        }


        lblFechaVencimiento.setText("Fecha de vencimiento:  " + fechaFormateadaVencimiento);


        if (notaDetalles.getAdeudo() != 0)
            strAdeudo = "Adeudo: $" + notaDetalles.getAdeudo();

        lblAdeudo.setText("Adeudo:  " + strAdeudo);

        if (notaDetalles.getSaldoFavor() != 0)
            strSaldo = "$" + notaDetalles.getSaldoFavor();

        lblSaldo.setText("Saldo a favor:  " + strSaldo);

        if (notaDetalles.getStatusNota().equals(StatusNota.PAGADO.toString()))
            strStatus += "Pagada";
        else if (notaDetalles.getStatusNota().equals(StatusNota.POR_PAGAR.toString()))
            strStatus += "Con adeudo";
        else if (notaDetalles.getStatusNota().equals(StatusNota.A_FAVOR.toString()))
            strStatus += "Con saldo a favor";

        lblStatus.setText("Estado:  " + strStatus);

        String fechaBd = notaDetalles.getCreatedAt();

        DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatterOutput = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        String fechaFormateada = LocalDateTime.parse(fechaBd, formatterInput).format(formatterOutput);


        lblFechaCreacion.setText("Fecha de creacion:  " + fechaFormateada);

        generoComparar = cliente.getGenero();

        if (generoComparar != null) {
            switch (generoComparar) {
                case "M" -> strGenero = "Masculino";
                case "F" -> strGenero = "Femenino";
                case "O" -> strGenero = "Otro";
                case "X" -> strGenero = "";
                default -> strGenero = "";
            }
        }

        lblGenero.setText("Genero:  " + strGenero);

        switch (cliente.getTipoCliente()) {
            case "INDIVIDUAL" -> strTipoCliente = "INDIVIDUAL";
            case "EMPRESA" -> strTipoCliente = "EMPRESA";
        }


        lblTipoCliente.setText("Tipo de cliente : " + strTipoCliente);


    }//llenarNota

    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }


}//class
