package com.mastertyres.fxControllers.agregarNota;

import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.common.ApplicationContextProvider;
import com.mastertyres.common.MenuContextSetting;
import com.mastertyres.fxControllers.nota.BuscarClienteController;
import com.mastertyres.fxControllers.nota.BuscarLlantaController;
import com.mastertyres.inventario.model.Inventario;
import com.mastertyres.vehiculo.model.VehiculoDTO;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.mastertyres.common.MensajesAlert.mostrarError;

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

    //Datos Clientes
    @FXML private TextField txtNombre;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtDireccion2;
    @FXML private TextField txtRfc;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtMarca;
    @FXML private TextField txtModelo;
    @FXML private TextField txtCategoria;
    @FXML private TextField txtAnioVehiculo;
    @FXML private TextField txtKms;
    @FXML private TextField txtPlacas;

    @FXML private TextField txtObservaciones;
    @FXML private TextField getTxtObservaciones2;

    //Datos nota
    @FXML private TextField txtLlantas;





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
        txtAnioVehiculo.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d{0,4}")){
                return change;
            }
            return null;
        }));
        txtKms.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")){
                return change;
            }
            return null;
        } ));
        spPorcentajeGas.setOnMouseClicked(event -> mostrarSlider(spPorcentajeGas.getScene().getWindow()));
        dibujarGasolina(50);


    }//initialize

//Seccion precargar en nota
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

    //Seccion dibujar arco gasolina

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

    @FXML
    private void buscarCliente(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/nota/BuscarCliente.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext() ::getBean);
            Parent root = loader.load();

            Stage stage = new Stage(StageStyle.UTILITY);
            BuscarClienteController controller = loader.getController();


            stage.setTitle("Buscar cliente");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            stage.showAndWait();

            Cliente cliente = controller.getClienteSeleccionado();
            llenarNota(cliente);

            VehiculoDTO vehiculos = controller.getVehiculoSeleccionado();
            llenarNota(vehiculos);




        }catch (Exception e){
            mostrarError("Error","","Error inesperado.");
            e.printStackTrace();
        }

    }//buscarCliente

    @FXML
    private void buscarLlanta(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/nota/BuscarLlanta.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext() ::getBean);
            Parent root = loader.load();

            Stage stage = new Stage(StageStyle.UTILITY);
            BuscarLlantaController controller = loader.getController();

            stage.setTitle("Buscar llantas");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            stage.showAndWait();
            Inventario inventario = controller.getLlantaSeleccionada();
            llenarNota(inventario);



        }catch (Exception e){
            mostrarError("Error","","Error inesperado.");
            e.printStackTrace();
        }

    }//buscarCliente





    private void llenarNota(Cliente cliente){
        if (cliente != null){
            System.out.println(cliente.getClienteId());
            txtNombre.setText(cliente.getNombre() + " " +
                    (cliente.getApellido() != null ? cliente.getApellido() : "") + " "  +
                    (cliente.getSegundoApellido() != null ? cliente.getSegundoApellido() : "")
            );

            txtDireccion.setText(cliente.getDomicilio() != null ? cliente.getDomicilio() : "");
            txtRfc.setText(cliente.getRfc() != null ? cliente.getRfc() :"");
            txtCorreo.setText(cliente.getCorreo() != null ? cliente.getCorreo() : "");

        }

    }//llenarNota

    private void llenarNota(VehiculoDTO vehiculos){
        if (vehiculos != null){
            txtMarca.setText(vehiculos.getNombreMarca());
            txtModelo.setText(vehiculos.getNombreModelo() + " " + vehiculos.getNombreCategoria());
            txtAnioVehiculo.setText(vehiculos.getAnio() +"");
            txtKms.setText(vehiculos.getKilometros() + "");
            txtPlacas.setText(vehiculos.getPlacas());
        }

    }//llenarNota

    private void llenarNota(Inventario inventario){

        txtLlantas.setText(inventario.getMarca() + " " + inventario.getModelo() + " " + " " + inventario.getMedida());

    }//llenarNota


}//class
