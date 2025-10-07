package com.mastertyres.common;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;

//Esta clase permite quitarle las opciones del clic derecho a todos los textField de los formularios
public class MenuContextSetting {


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

    public static void disableMenu(Node node) {
        if (node instanceof TextField tf) {
            tf.setContextMenu(new ContextMenu()); // deshabilita menú contextual
        } else if (node instanceof ScrollPane sp) {
            // recorrer el contenido del ScrollPane
            Node content = sp.getContent();
            if (content != null) disableMenu(content);
        } else if (node instanceof Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                disableMenu(child); // recursión normal
            }
        }
    }


}//clase
