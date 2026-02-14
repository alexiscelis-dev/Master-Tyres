package com.mastertyres.common.utils;

import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.io.File;

import static com.mastertyres.common.utils.MensajesAlert.mostrarError;

public class ClipboardUtil {

    public static void copiarImagenURL(String imgUrl){

        try {

            File file = new File(imgUrl);
            if (!file.exists()){
                Clipboard  clipboard = Clipboard.getSystemClipboard();
                clipboard.clear();

                return;
            }

            Image imagePromocion = new Image(file.toURI().toString());

            ClipboardContent content = new ClipboardContent();
            content.putImage(imagePromocion);
            Clipboard.getSystemClipboard().setContent(content);

        }catch (Exception e){
            e.printStackTrace();
            mostrarError("Error al copiar imagen",
                    "",
                    "No se pudo copiar la imagen de la promocion al portapapeles.");
        }

    }//copiarImagenURL

}//class
