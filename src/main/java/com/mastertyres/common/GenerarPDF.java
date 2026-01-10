package com.mastertyres.common;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;

import static com.mastertyres.common.MensajesAlert.mostrarError;
import static com.mastertyres.common.MensajesAlert.mostrarInformacion;

public class GenerarPDF {

    public static void generarPDF(WritableImage img1, WritableImage img2, String ruta) {
        try {
            // Convertir WritableImage a PNG bytes
            ByteArrayOutputStream imageByte1 = new ByteArrayOutputStream();
            ByteArrayOutputStream imageByte2 = new ByteArrayOutputStream();

            ImageIO.write(SwingFXUtils.fromFXImage(img1, null), "png", imageByte1);
            ImageIO.write(SwingFXUtils.fromFXImage(img2, null), "png", imageByte2);

            //Crear PDF
            PdfWriter writer = new PdfWriter(ruta);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4.rotate());
            document.setMargins(0, 0, 0, 0);

            //se crea imagen para iText
            ImageData imageData1 = ImageDataFactory.create(imageByte1.toByteArray());
            ImageData imageData2 = ImageDataFactory.create(imageByte2.toByteArray());
            com.itextpdf.layout.element.Image image1 = new com.itextpdf.layout.element.Image(imageData1);
            com.itextpdf.layout.element.Image image2 = new com.itextpdf.layout.element.Image(imageData2);

            image1.setAutoScale(true);
            image2.setAutoScale(true);

            float pageWidth = PageSize.A4.rotate().getWidth();
            float pageHeight = PageSize.A4.rotate().getHeight();

            Table table = new Table(new float[] {pageWidth / 2, pageWidth / 2});
            table.setWidth(UnitValue.createPointValue(pageWidth));
            table.setFixedLayout();
            table.setKeepTogether(true);

            Cell cell1 = new Cell()
                    .setHeight(pageHeight)
                    .add(image1)
                    .setPadding(0)
                    .setBorder(Border.NO_BORDER);


            Cell cell2 = new Cell()
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setHeight(pageHeight)
                    .add(image2)
                    .setPadding(0)
                    .setBorder(Border.NO_BORDER);

                 table.addCell(cell1);
                 table.addCell(cell2);
                 document.add(table);
                 document.close();



            mostrarInformacion("Nota creada", "", "Se genero el documento exitosamente");


        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error inesperado", "", "Ocurrio un error al generar el documento PDF");
        }

    }// generarPDF

}//class