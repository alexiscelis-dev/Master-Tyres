package com.mastertyres.fxControllers.nota;

import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.utils.MenuContextSetting;
import com.mastertyres.common.utils.RegexTools;
import com.mastertyres.nota.model.NotaDTO;
import com.mastertyres.nota.model.StatusNota;
import com.mastertyres.nota.service.NotaService;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.mastertyres.common.utils.MensajesAlert.mostrarError;
import static com.mastertyres.common.utils.MensajesAlert.mostrarInformacion;

@Component
public class EditarAdeudoController implements IFxController {

    @FXML
    private AnchorPane root;
    @FXML
    private TextField txtAdeudo;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private CheckBox cbLiquidar;
    @FXML
    private Label lblAdeudo;
    @FXML
    private Button btnActualizar;

    @Autowired
    NotaService notaService;


    private NotaDTO notaAdeudo;

    private BooleanProperty boolActualizarNota = new SimpleBooleanProperty(true);

    @FXML
    private void initialize() {

        configuraciones();
        listeners();

    }//initialize

    @Override
    public void configuraciones() {

        MenuContextSetting.disableMenuDatePicker(root);

        dpFecha.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #eeeeee;");

                }

            }
        });


        MenuContextSetting.disableMenu(root);
        MenuContextSetting.disableMenuDatePicker(dpFecha);
        RegexTools.aplicarNumerosDecimal(txtAdeudo);

        BooleanBinding fechaInvalida = dpFecha.valueProperty().isNull().and(cbLiquidar.selectedProperty().not());

        btnActualizar.disableProperty().bind(
                txtAdeudo.textProperty().isNull()
                        .or(txtAdeudo.textProperty().isEmpty())
                        .or(boolActualizarNota.not())
                        .or(fechaInvalida)

        );


    }//configuraciones

    @Override
    public void listeners() {

        cbLiquidar.selectedProperty().addListener((observable, oldValue, newValue) -> {
            revisarCheckBox(notaAdeudo.getAdeudo());
        });

        btnActualizar.setOnAction(event -> actualizar(notaAdeudo));

    }//listeners

    private void revisarCheckBox(float conAdeudo) {
        if (cbLiquidar.isSelected()) {
            txtAdeudo.setText("0");
            dpFecha.setValue(null);
            dpFecha.setDisable(true);
        } else {
            txtAdeudo.setText("");
            dpFecha.setDisable(false);
        }


    }

    public void setAdeudo(NotaDTO notaAdeudo) {
        this.notaAdeudo = notaAdeudo;

        String strFecha = notaAdeudo.getFechaVencimiento();

        if (strFecha != null && !strFecha.trim().isEmpty())
            dpFecha.setValue(LocalDate.parse(strFecha));
        else
            dpFecha.setValue(null);


        lblAdeudo.setText("Adeudo: $" + notaAdeudo.getAdeudo());
        revisarCheckBox(notaAdeudo.getAdeudo());


    }//setAdeudo


    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) txtAdeudo.getScene().getWindow();
        stage.close();
    }//cancelar

    private void actualizar(NotaDTO notaAdeudo) {

        if (Float.parseFloat(txtAdeudo.getText()) == 0) {
            notaService.actualizarStatus(StatusNota.PAGADO.toString(), notaAdeudo.getNotaId());
            dpFecha.setValue(null);
        } else {
            notaService.actualizarStatus(StatusNota.POR_PAGAR.toString(), notaAdeudo.getNotaId());
        }

        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String fechaStr = null;

        //if asiga null si el datePicker no tiene nada seleccionado
        if (dpFecha.getValue() != null)
            fechaStr = dpFecha.getValue().toString();


        try {
            notaService.actualizarAdeudo(Float.parseFloat(txtAdeudo.getText()), fechaStr, notaAdeudo.getNotaId());
            notaService.actualizarUpdatedAtNota(notaAdeudo.getNotaId(), fecha);


            Stage stage = (Stage) txtAdeudo.getScene().getWindow();
            stage.close();
            mostrarInformacion("Adeudo actualizado", "", "El adeudo se actualizo correctamente. Puede consultar mas detalles en  nota '+' Detalles cliente.");


        } catch (Exception e) {
            e.printStackTrace();
            Stage stage = (Stage) txtAdeudo.getScene().getWindow();
            stage.close();
            mostrarError("Error inesperado", "", "No se pudo actualizar el lemento seleccionado, vuelva a intentarlo más tarde.");
        }


    }//actualizar


}//class
