package com.mastertyres.common;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Stack;


public class NavigatorManager {

    private static Stage mainSatage;
    private static final Stack<Parent> history = new Stack<>();

    public static void setStage(Stage stage){
        mainSatage = stage;
    }

    // ir a una nueva vista y guardar la vista actual
    public static void navigateTo(Parent root){
        if (mainSatage.getScene() != null){
            history.push(mainSatage.getScene().getRoot());

        }
        mainSatage.setScene(new Scene(root));
        mainSatage.show();
    }

    public static void goBack(){
        if (!history.isEmpty())
        {
            Parent previusRoot = history.pop();
            mainSatage.setScene(new Scene(previusRoot));
            mainSatage.show();

        }

    }

    public static void pushHistory(Parent root){
        history.push(root);
    }


}//clase
