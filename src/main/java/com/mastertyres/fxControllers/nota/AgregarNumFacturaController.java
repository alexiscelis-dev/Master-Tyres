package com.mastertyres.fxControllers.nota;

import com.mastertyres.common.MenuContextSetting;
import com.mastertyres.common.RegexTools;
import com.mastertyres.nota.service.NotaService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.mastertyres.common.MensajesAlert.mostrarError;
import static com.mastertyres.common.MensajesAlert.mostrarInformacion;

@Component
public class AgregarNumFacturaController {
    @FXML
    private AnchorPane root;
    private Integer notaId;
    @FXML
    private TextField txtNumFactura;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnCancelar;

    @Autowired
    private NotaService notaService;


    @FXML
    private void initialize(){
        configuraciones();

    }//initialize

    private void configuraciones(){

        MenuContextSetting.disableMenu(root);

        RegexTools.aplicarNumFactura(txtNumFactura);
        btnAgregar.disableProperty().bind(
                txtNumFactura.textProperty().isEmpty()
                        .or(txtNumFactura.textProperty().isNull())
                        .or(txtNumFactura.textProperty().length().lessThan(3))
        );

    }

    public void setNumFactura(Integer notaId, String numFactura){
        this.notaId = notaId;
        txtNumFactura.setText(numFactura);


    }//setNumFactura

    @FXML
    private void agregar(ActionEvent event){
      try {
          notaService.actualizarNumFactura(txtNumFactura.getText(),getNotaId());

          notaService.actualizarUpdatedAtNota(getNotaId(),LocalDateTime.now().toString());
          mostrarInformacion("Numero de factura agregado", "", "Los cambios se guardaron correctamente.");
          cancelar(null);
      }catch (Exception e){
          e.printStackTrace();

          mostrarError("Error inesperado", "", "Ocurrió un problema al realizar la operación.");
          cancelar(null);

      }
    }//agregar

    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }


    public Integer getNotaId() {
        return this.notaId;
    }

}//class

