package com.mastertyres.fxControllers.nota;

import com.mastertyres.common.exeptions.NotaException;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.interfaces.ILoading;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.MenuContextSetting;
import com.mastertyres.common.utils.RegexTools;
import com.mastertyres.fxComponents.LoadingComponentController;
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

import static com.mastertyres.common.utils.MensajesAlert.mostrarError;
import static com.mastertyres.common.utils.MensajesAlert.mostrarInformacion;

@Component
public class EditarSaldoController implements IFxController, ILoading {
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
    @Autowired
    private TaskService taskService;

    private NotaDTO notaSaldo;

    private LoadingComponentController loadingOverlayController;



    @FXML
    private void initialize(){

       configuraciones();
       listeners();

    }//initialize

    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingOverlayController = loading;
    }

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
        if (cbLiquidar.isSelected()){
            txtSaldo.setText( "0");
            txtSaldo.setEditable(false);
        }
        else{
            txtSaldo.setText("");
            txtSaldo.setEditable(true);
        }
    }//revisarCheckBox

    public void setSaldoFavor(NotaDTO notaSaldo){
        this.notaSaldo = notaSaldo;
        revisarCheckBox();
        lblSaldo.setText("Saldo disponible: $" + notaSaldo.getSaldoFavor());
    }

    @FXML
    private void actualizar(ActionEvent event){

        taskService.runTask(
                loadingOverlayController,
                () ->{

                    if (Float.parseFloat(txtSaldo.getText()) == 0){
                        notaService.actualizarStatus(StatusNota.PAGADO.toString(),notaSaldo.getNotaId());
                    }else {
                        notaService.actualizarStatus(StatusNota.A_FAVOR.toString(),notaSaldo.getNotaId());
                    }

                    notaService.actualizarSaldo(Float.parseFloat(txtSaldo.getText()),notaSaldo.getNotaId());
                    notaService.actualizarUpdatedAtNota(notaSaldo.getNotaId(),LocalDateTime.now().toString());
                    return null;

                },(resultado) ->{

                    mostrarInformacion("Saldo Actualizado",
                            "",
                            "El saldo se actualizo correctamente. Puede consultar mas detalles en  nota '+' Detalles cliente.");
                    cancelar(null);

                },(ex) ->{

                    if (ex instanceof NotaException){
                        mostrarError("Error al actualizar","Ocurrio un problema al guardar los cambios",""+ex.getMessage());
                    }else {
                        mostrarError("Error interno", "","Ocurrio un error inesperado al intentar guardar los cambios. Vuelva a intentarlo mas tarde.");
                        cancelar(null);
                    }

                }, null

        );


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
