package com.mastertyres.fxControllers.nota;

import com.mastertyres.nota.model.NotaDTO;
import javafx.fxml.FXML;
import org.springframework.stereotype.Component;

@Component
public class NotaDetallesController {
    @FXML
    private NotaDTO notaDetalles;
    @FXML
    private void initialize(){

    }//initialize

    public void setNotaDetalles(NotaDTO notaDetalles){
        this.notaDetalles = notaDetalles;

    }//setNotaDetalles
}//class
