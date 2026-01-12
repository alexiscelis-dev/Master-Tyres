package com.mastertyres.fxComponents;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Component;


@Component
public class LoadingComponentController {
    @FXML
    private StackPane rootPane;



    public void show() {
        rootPane.setVisible(true);
        rootPane.setMouseTransparent(false);
        rootPane.toFront();
    }

    public void hide() {
        rootPane.setVisible(false);
        rootPane.setMouseTransparent(true);
    }

    public boolean isVisible() {
        return rootPane.isVisible();
    }



}//class
