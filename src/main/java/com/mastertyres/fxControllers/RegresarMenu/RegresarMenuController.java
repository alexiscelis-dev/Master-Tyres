package com.mastertyres.fxControllers.RegresarMenu;

import javafx.fxml.FXML;
import org.springframework.stereotype.Component;

@Component
public class RegresarMenuController {

/*Esta clase solamente es necesaria para hacer que se regrese al menu al momento de precionar a la imagen
debido a que el table view y los componentes se agregan sobre el pane no se puede solo regresar se necesita mandar
como parametro otro archivo FXML para cambiar de ventana (Una ventana vacia) es lo que es esta clase.
    */

    @FXML
    public void initialize(){

    }
}
