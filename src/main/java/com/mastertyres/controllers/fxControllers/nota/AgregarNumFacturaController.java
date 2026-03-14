package com.mastertyres.controllers.fxControllers.nota;

import com.mastertyres.common.interfaces.IFxController;
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

import static com.mastertyres.common.utils.MensajesAlert.mostrarExcepcion;
import static com.mastertyres.common.utils.MensajesAlert.mostrarInformacion;
import static com.mastertyres.common.utils.MenuContextSetting.disableMenu;
import static com.mastertyres.common.utils.RegexTools.aplicarNumFactura;

@Component
public class AgregarNumFacturaController implements IFxController {
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
        listeners();

    }//initialize

    @Override
    public void configuraciones(){

        disableMenu(root);

        aplicarNumFactura(txtNumFactura);
        btnAgregar.disableProperty().bind(
                txtNumFactura.textProperty().isEmpty()
                        .or(txtNumFactura.textProperty().isNull())
                        .or(txtNumFactura.textProperty().length().lessThan(3))
        );

    }//configuraciones

    @Override
    public void listeners() {

    }//listeners

    public void setNumFactura(Integer notaId, String numFactura){
        this.notaId = notaId;
        txtNumFactura.setText(numFactura);


    }//setNumFactura

    @FXML
    private void agregar(ActionEvent event){
      try {
          notaService.actualizarNumFactura(txtNumFactura.getText(),getNotaId());

          notaService.actualizarUpdatedAtNota(getNotaId(),LocalDateTime.now().toString());
          mostrarInformacion(
                  "Operación completada",
                  "Número de factura registrado",
                  "El número de factura ha sido agregado y los cambios se guardaron correctamente."
          );
          cancelar(null);
      }catch (Exception e){
          e.printStackTrace();

          mostrarExcepcion(
                  "Error inesperado",
                  "Se produjo una excepción durante la operación",
                  "Ocurrió un problema al intentar realizar la operación solicitada.",
                  e
          );
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

