package com.mastertyres.common;

import javafx.event.Event;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;

//Esta clase permite quitarle las opciones del clic derecho a todos los textField de los formularios
public class TextFieldSetting {


    public static void disableMenuDatePicker(Parent root) {
        root.getChildrenUnmodifiable().forEach(node -> {
            if (node instanceof DatePicker) {
                DatePicker dp = (DatePicker) node;

                dp.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);
            } else if (node instanceof Parent) {
                disableMenuDatePicker((Parent) node);
            }
        });
    }

    public static void disableMenuTextField(Parent root) {
        root.getChildrenUnmodifiable().forEach(node -> {
            if (node instanceof TextField) {

                ((TextField) node).setContextMenu(new ContextMenu());
            } else if (node instanceof Parent) {
                disableMenuTextField((Parent) node);
            }
        });
    }

}//clase
