package com.mastertyres.fxControllers.nota;

import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.utils.MenuContextSetting;
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

import static com.mastertyres.common.utils.MensajesAlert.mostrarError;
import static com.mastertyres.common.utils.MensajesAlert.mostrarInformacion;

@Component
public class DarPlazoController implements IFxController {
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
    public String status = StatusNota.VENCIDO.toString(); // No actualiza solo le da un valor a la ventana de notas a la etiqueta status para actualizar
                                                          // el que actualiza el status es el service


    @FXML
    private void initialize(){

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

    }//configuraciones

    @Override
    public void listeners() {

    }//listeners


    public void setNota(Integer notaId){
        btnActualizar.disableProperty().bind(dpFecha.valueProperty().isNull());

        btnActualizar.setOnAction(event -> actualizar(notaId));

    }

    @FXML
    private void cancelar(ActionEvent event){
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();

    }



    private void actualizar(Integer notaId){

        try {
            String fechaVencimiento = dpFecha.getValue() != null ? dpFecha.getValue().toString() : "";
            fecha = fechaVencimiento;
            notaService.actualilzarFechaVencimiento(fechaVencimiento,notaId, StatusNota.ACTIVE.toString());
            notaService.actualizarStatus(StatusNota.POR_PAGAR.toString(),notaId);
            status = StatusNota.POR_PAGAR.toString();
            mostrarInformacion("Fecha limite de pago actualizada","","Los cambios se guardaron correctamente.");


            cancelar(null);


        }catch (Exception e) {
             String status = StatusNota.VENCIDO.toString();

            mostrarError("Error inesperado", "", "Ocurrió un problema al actualizar la fecha de vencimiento.");
            e.printStackTrace();

        }
    }//actualizar
}//class
