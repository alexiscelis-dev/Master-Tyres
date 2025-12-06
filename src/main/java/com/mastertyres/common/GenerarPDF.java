package com.mastertyres.common;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;

import static com.mastertyres.common.MensajesAlert.mostrarError;
import static com.mastertyres.common.MensajesAlert.mostrarInformacion;

public class GenerarPDF {

    public static void generarPDF(WritableImage img, String ruta){
        try {
            // Convertir WritableImage a PNG bytes
            ByteArrayOutputStream imageByte = new ByteArrayOutputStream();
            ImageIO.write(SwingFXUtils.fromFXImage(img,null),"png",imageByte);

            //Crear PDF
            PdfWriter writer = new PdfWriter(ruta);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            //se crea imagen para iText
            ImageData imageData = ImageDataFactory.create(imageByte.toByteArray());
            com.itextpdf.layout.element.Image image = new com.itextpdf.layout.element.Image(imageData);

            image.setAutoScale(true); //ajusta imagen al tamaño del PDF
            image.scaleToFit(595, 842);
            document.add(image);
            document.close();

            mostrarInformacion("Nota creada","","Se genero el documento exitosamente");



        }catch (Exception e){
            e.printStackTrace();
            mostrarError("Error inesperado","","Ocurrio un error al generar el documento PDF");
        }

    }

}//class
