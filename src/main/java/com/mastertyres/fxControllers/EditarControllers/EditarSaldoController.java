package com.mastertyres.fxControllers.EditarControllers;

import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.utils.MenuContextSetting;
import com.mastertyres.common.utils.RegexTools;
import com.mastertyres.nota.model.NotaDTO;
import com.mastertyres.nota.model.StatusNota;
import com.mastertyres.nota.service.NotaService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.mastertyres.common.utils.MensajesAlert.mostrarError;
import static com.mastertyres.common.utils.MensajesAlert.mostrarInformacion;

@Component
public class EditarSaldoController implements IFxController {
    @FXML
    private AnchorPane root;
    @FXML
    private TextField txtSaldo;
    @FXML
    private CheckBox cbLiquidar;
    @FXML
    private Label lblSaldo;
    @FXML
    private Button btnActualizar;

    @Autowired
    private NotaService notaService;

    private NotaDTO notaSaldo;



    @FXML
    private void initialize(){

       configuraciones();
       listeners();

    }//initialize

    @Override
    public void configuraciones(){

        MenuContextSetting.disableMenu(root);
        RegexTools.aplicarNumerosDecimal(txtSaldo);

        btnActualizar.disableProperty().bind(
                txtSaldo.textProperty().isNull()
                        .or(txtSaldo.textProperty().isEmpty())
        );

    }//configuraciones

    @Override
    public void listeners() {

        cbLiquidar.selectedProperty().addListener((observable, oldValue, newValue) -> {
            revisarCheckBox();
        });

    }//listeners

    private void revisarCheckBox() {
        if (cbLiquidar.isSelected())
            txtSaldo.setText( "0");

        else
            txtSaldo.setText("");
    }

    public void setSaldoFavor(NotaDTO notaSaldo){
        this.notaSaldo = notaSaldo;
        revisarCheckBox();
        lblSaldo.setText("Saldo disponible: $" + notaSaldo.getSaldoFavor());
    }

    @FXML
    private void actualizar(ActionEvent event){
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        try {

            if (Float.parseFloat(txtSaldo.getText()) == 0){
                notaService.actualizarStatus(StatusNota.PAGADO.toString(),notaSaldo.getNotaId());
            }else {
                notaService.actualizarStatus(StatusNota.A_FAVOR.toString(),notaSaldo.getNotaId());
            }

            notaService.actualizarSaldo(Float.parseFloat(txtSaldo.getText()),notaSaldo.getNotaId());
            notaService.actualizarUpdatedAtNota(notaSaldo.getNotaId(),fecha);

            Stage stage = (Stage) txtSaldo.getScene().getWindow();
            stage.close();
            mostrarInformacion("Saldo Actualizado", "", "El saldo se actualizo correctamente. Puede consultar mas detalles en  nota '+' Detalles cliente.");

        }catch (Exception e){
            e.printStackTrace();
            Stage stage = (Stage) txtSaldo.getScene().getWindow();
            stage.close();
            mostrarError("Error inesperado", "", "Ocurrió un problema al realizar la operación.");
        }

    }//actualizar


    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) txtSaldo.getScene().getWindow();
        stage.close();
    }//cancelar

    public NotaDTO getNotaSaldo() {
        return this.notaSaldo;
    }

    public void setNotaSaldo(final NotaDTO notaSaldo) {
        this.notaSaldo = notaSaldo;
    }
}//class
