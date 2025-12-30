package com.mastertyres.fxControllers.nota;

import com.mastertyres.common.MenuContextSetting;
import com.mastertyres.nota.model.StatusNota;
import com.mastertyres.nota.service.NotaService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.mastertyres.common.MensajesAlert.mostrarError;
import static com.mastertyres.common.MensajesAlert.mostrarInformacion;

@Component
public class DarPlazoController {
    @FXML
    private AnchorPane root;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnActualizar;
    @FXML
    private DatePicker dpFecha;
    @Autowired
    private NotaService notaService;

    public String fecha;

    @FXML
    private void initialize(){
        configuraciones();


    }//initialize

    public void setNota(Integer notaId){
        btnActualizar.disableProperty().bind(dpFecha.valueProperty().isNull());

        btnActualizar.setOnAction(event -> actualizar(notaId));

    }

    @FXML
    private void cancelar(ActionEvent event){
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();

    }


    private void configuraciones() {
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

    }

    private void actualizar(Integer notaId){

        try {
            String fechaVencimiento = dpFecha.getValue() != null ? dpFecha.getValue().toString() : "";
            fecha = fechaVencimiento;
            notaService.actualilzarFechaVencimiento(fechaVencimiento,notaId, StatusNota.ACTIVE.toString());
            mostrarInformacion("Fecha limite de pago actualizada","","Los cambios se guardaron correctamente.");

            cancelar(null);


        }catch (Exception e) {
            mostrarError("Error inesperado", "", "Ocurrió un problema al actualizar la fecha de vencimiento.");
            e.printStackTrace();

        }
    }//actualizar
}//class
