package com.mastertyres.fxControllers.agregarNota;

import com.mastertyres.common.MenuContextSetting;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class AgregarNotaController {

    @FXML private AnchorPane rootPane;
    @FXML private GridPane gridPaneIcons;
    @FXML private StackPane btnShowIcons;
    @FXML private TextField txtDia;
    @FXML private TextField txtMes;
    @FXML private TextField txtAnio;
    @FXML private StackPane spPorcentajeGas;
    @FXML private TextField txtHoraEntrega;
    @FXML private TextField txtNumNota;
    private Arc arcoGas;
    @FXML private Canvas canvasGas;
    @FXML private Label lblGas;





    @FXML
    private void initialize(){
        MenuContextSetting.disableMenu(rootPane);
        btnShowIcons.setOnMouseClicked(event -> showIcons());
        mostrarFechayHora();
       // spPorcentajeGas.setOnMouseClicked( );
        txtNumNota.setTextFormatter(new TextFormatter<>( change -> {
            if (change.getControlNewText().matches("\\d{0,6}")){
                return change;
            }
            return null;
        }) );
        txtDia.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d{0,2}")){
                return change;
            }
            return null;
        }));
        txtMes.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d{0,2}")){
                return change;
            }
            return null;
        }));
        txtAnio.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d{0,4}")){
                return change;
            }
            return null;
        }));
        spPorcentajeGas.setOnMouseClicked(event -> mostrarSlider(spPorcentajeGas.getScene().getWindow()));
        dibujarGasolina(50);





    }//initialize


    private void showIcons(){
        final Duration TIEMPO = Duration.millis(500);

        if (!gridPaneIcons.isVisible()){
            gridPaneIcons.setVisible(true);
            TranslateTransition slideIN = new TranslateTransition(TIEMPO,gridPaneIcons);
            slideIN.setFromY(-50); //Mueve nodo
            slideIN.setToY(0); // Termina en su posición normal

            FadeTransition fadeIn = new FadeTransition(TIEMPO,gridPaneIcons);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            ParallelTransition showTransition = new ParallelTransition(slideIN,fadeIn);
            showTransition.play();

        }
        else{

            TranslateTransition slideOut = new TranslateTransition(TIEMPO,gridPaneIcons);
            slideOut.setFromY(0); //Mueve nodo
            slideOut.setToY(-50); // Termina en su posición normal

            FadeTransition fadeOut = new FadeTransition(TIEMPO,gridPaneIcons);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);

            ParallelTransition showTransition = new ParallelTransition(slideOut,fadeOut);
            showTransition.setOnFinished(event -> gridPaneIcons.setVisible(false));
            showTransition.play();


        }




    }//showIcons

    private void mostrarFechayHora(){
        txtDia.setText(LocalDate.now().getDayOfMonth() +"");
        txtMes.setText(LocalDate.now().getMonthValue() + "");
        txtAnio.setText(LocalDate.now().getYear() + "");

        LocalDateTime fechayHora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String hora = fechayHora.format(formatter);
        txtHoraEntrega.setText(hora);

    }//mostrarFecha

    private void mostrarSlider(Window owner){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(owner);

        VBox vBox = new VBox(10);
        vBox.setPadding(new javafx.geometry.Insets(15));
        Label label = new Label("Selecciona el porcentaje de gasolina");

        Slider slider = new Slider(0,100,50);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(10);
        slider.setMinorTickCount(4);

        Button btnAceptar = new Button("Aceptar");

        btnAceptar.setOnAction(event -> {
            double porcentaje = slider.getValue();
            dibujarGasolina(porcentaje);
            dialog.close();
        });
        vBox.getChildren().add(label);
        vBox.getChildren().add(slider);
        vBox.getChildren().add(btnAceptar);
        Scene scene = new Scene(vBox,300,150);
        dialog.setScene(scene);
        dialog.setTitle("Porcentaje de gas");
        dialog.showAndWait();

    }//mostrarSlider



    private void dibujarGasolina(double porcentaje) {
        GraphicsContext gc = canvasGas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvasGas.getWidth(), canvasGas.getHeight());

        double centerX = canvasGas.getWidth() / 2;
        double centerY = canvasGas.getHeight() / 2;
        double radius = 60;
        double startAngle = 180; // arco empieza desde izquierda
        double length = 180 * porcentaje / 100.0;

        // Fondo gris
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(8);
        gc.strokeArc(centerX - radius, centerY - radius, radius * 2, radius * 2,
                startAngle, 180, ArcType.OPEN);

        lblGas.setText(String.format("%d %%",(int)porcentaje,"%"));

        // Arco de porcentaje
        if (porcentaje > 60)
            gc.setStroke(Color.GREEN);
        else if (porcentaje > 30)
            gc.setStroke(Color.ORANGE);
        else
            gc.setStroke(Color.RED);

        gc.strokeArc(centerX - radius, centerY - radius, radius * 2, radius * 2,
                startAngle, length, ArcType.OPEN);
    }




}//class
