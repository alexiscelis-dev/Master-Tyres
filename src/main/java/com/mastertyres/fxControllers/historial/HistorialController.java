package com.mastertyres.fxControllers.historial;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

@Component
public class HistorialController {
    @FXML
    private VBox contenedorNota;
    @FXML
    private VBox detallePromocion;
    @FXML
    private ImageView zoom;

    @FXML
    private void initialize() {

        configuraciones();


    }//initialize

    private void configuraciones() {



    }//configuraciones





}//class
