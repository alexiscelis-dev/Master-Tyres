package com.mastertyres.fxControllers.EditarControllers;

import com.mastertyres.common.MenuContextSetting;
import com.mastertyres.common.RegexTools;
import com.mastertyres.nota.model.NotaDTO;
import com.mastertyres.nota.service.NotaService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.mastertyres.common.MensajesAlert.mostrarError;
import static com.mastertyres.common.MensajesAlert.mostrarInformacion;

@Component
public class EditarAdeudoController {

    @FXML
    private AnchorPane root;
    @FXML
    private TextField txtAdeudo;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private CheckBox cbLiquidar;
    @FXML
    private Button btnActualizar;

    @Autowired
    NotaService notaService;


    private NotaDTO notaAdeudo;

    private BooleanProperty boolActualizarNota = new SimpleBooleanProperty(false);

    @FXML
    private void initialize() {
        configuraciones();
        cbLiquidar.selectedProperty().addListener((observable, oldValue, newValue) -> {
            revisarCheckBox();
        });

        btnActualizar.setOnAction(event -> actualizar(notaAdeudo));


    }//initialize


    private void configuraciones() {
        MenuContextSetting.disableMenu(root);
        MenuContextSetting.disableMenuDatePicker(dpFecha);
        RegexTools.aplicarNumerosDecimal(txtAdeudo);

        btnActualizar.disableProperty().bind(
                txtAdeudo.textProperty().isNull()
                        .or(txtAdeudo.textProperty().isEmpty())
                        .or(dpFecha.valueProperty().isNull())

        );

    }

    private void revisarCheckBox() {
        if (cbLiquidar.isSelected())
            txtAdeudo.setText(notaAdeudo.getAdeudo() + "");
        else
            txtAdeudo.setText("");
    }

    public void setAdeudo(NotaDTO notaAdeudo) {
        this.notaAdeudo = notaAdeudo;
        LocalDate fecha = LocalDate.parse(notaAdeudo.getFechaVencimiento());
        dpFecha.setValue(fecha);
        revisarCheckBox();


    }//setAdeudo


    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) txtAdeudo.getScene().getWindow();
        stage.close();
    }//cancelar

    private void actualizar(NotaDTO notaAdeudo){
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

       try {
           notaService.actualizarAdeudo(Float.parseFloat(txtAdeudo.getText()),dpFecha.getValue().toString(),notaAdeudo.getNotaId());
           notaService.actualizarUpdatedAtNota(notaAdeudo.getNotaId(),fecha);
           mostrarInformacion("Adeudo actualizado", "", "El adeudo se actualizo correctamente. Puede consultar mas detalles en  nota +.");
       }catch (Exception e){
           e.printStackTrace();
           mostrarError("Error inesperado", "", "No se pudo actualizar el lemento seleccionado, vuelva a intentarlo más tarde.");
       }



    }//actualizar

}//class
