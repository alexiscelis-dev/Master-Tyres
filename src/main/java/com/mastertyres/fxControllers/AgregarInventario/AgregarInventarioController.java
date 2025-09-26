package com.mastertyres.fxControllers.AgregarInventario;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;


@Component
public class AgregarInventarioController {
    @FXML
    private TextField txtCodBarras;
    @FXML
    private TextField txtDot;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtMarca;
    @FXML
    private TextField txtModelo;
    @FXML
    private TextField txtMedida1;
    @FXML
    private TextField txtMedida2;
    @FXML
    private ChoiceBox<String> cbIndiceCarga;
    @FXML
    private ChoiceBox<String> cbIndiceVelocidad;

    @FXML
    private TextField txtStock;
    @FXML
    private TextField txtPrecioC;
    @FXML
    private TextField txtprecioV;
    @FXML
    private TextField txtImg;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnRegistrar;


    @FXML
    private void initialize(){
        indicesChoiceBox();
        btnLimpiar.setOnAction(event -> clean());

        //Abre y cierra el popup del choiceBox para que aparezca en la posicion correcta y no debajo de la tabla
        cbIndiceCarga.setOnMousePressed(event -> {
            if (!cbIndiceCarga.isShowing()){
                cbIndiceCarga.show();
                cbIndiceCarga.hide();
            }
        });
        cbIndiceVelocidad.setOnMousePressed( event -> {
            if (!cbIndiceVelocidad.isShowing()){
                cbIndiceVelocidad.show();
                cbIndiceVelocidad.hide();
            }
        });

    }//ininitialize

    private void indicesChoiceBox(){
        String[] indicesVelocidad = {
                "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8",
                "B", "C", "D", "E", "F", "G",
                "J", "K", "L", "M", "N",
                "P", "Q", "R", "S", "T",
                "U", "H", "V", "W", "Y",
                "(Y)", "ZR"
        };
        for (String indice: indicesVelocidad)
            cbIndiceVelocidad.getItems().addAll(indice);


        for (int i = 60; i <=138 ; i++)
            cbIndiceCarga.getItems().addAll(i +"");


    }//indicesChoiceBox

    private void clean(){
        txtCodBarras.setText("");
        txtDot.setText("");
        txtNombre.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtMedida1.setText("");
        txtMedida2.setText("");
        cbIndiceCarga.setValue(null);
        cbIndiceVelocidad.setValue(null);
        txtStock.setText("");
        txtPrecioC.setText("");
        txtprecioV.setText("");
        txtImg.setText("");

    }//clean

}//class
