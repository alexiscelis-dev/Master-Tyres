package com.mastertyres.common.service;

import com.mastertyres.common.exeptions.AppException;
import com.mastertyres.components.fxComponents.loader.LoadingComponentController;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

import static com.mastertyres.common.utils.MensajesAlert.mostrarError;

@Component
public class TaskService {

    private Label cargando;

    public void disable(AnchorPane rootPane) {

        if (cargando == null){ // evita crear muchos Label
            cargando = new Label("Cargando...");
        }

        final String STYLE_CARGAR = "-fx-text-fill: white;" +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 6,0,0,1);";

        cargando.setStyle(STYLE_CARGAR);
        // Centrar el label
        AnchorPane.setTopAnchor(cargando, 0.0);
        AnchorPane.setBottomAnchor(cargando, 0.0);
        AnchorPane.setLeftAnchor(cargando, 0.0);
        AnchorPane.setRightAnchor(cargando, 0.0);

        cargando.setAlignment(Pos.CENTER);
        cargando.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        rootPane.getChildren().add(cargando);

        rootPane.setDisable(true);

    }

    public void enable(AnchorPane rootPane) {
        rootPane.setDisable(false);
        if (cargando != null){
            rootPane.getChildren().remove(cargando);
        }

    }


    public <T> Task<T> runTask(LoadingComponentController loadingController,
                               Callable<T> actionBackground,
                               Consumer<T> toFinish,
                               Consumer<Throwable> onFailed,
                               Runnable onCancelled) {

        if (loadingController != null) {
            loadingController.show();
        }

        Task<T> task = new Task<T>() {
            @Override
            protected T call() throws Exception {
                return actionBackground.call();

            }
        };

        task.setOnSucceeded(e -> {
            if (loadingController != null) {
                loadingController.hide();
            }
            if (toFinish != null) {
                toFinish.accept(task.getValue());
            }

        });
        task.setOnFailed(e -> {

            if (loadingController != null) {
                loadingController.hide();
            }

            Throwable rawError = task.getException();
            Throwable trueError = desempaquetarExcepcion(rawError);

            if (onFailed != null) {
                onFailed.accept(trueError);
            } else {
                mostrarError("Error interno", "", "Ocurrió un problema inesperado. Vuelva a intentarlo mas tarde.");

            }
        });

        task.setOnCancelled(e -> {
            if (loadingController != null) loadingController.hide();
            if (onCancelled != null) onCancelled.run();
        });

        new Thread(task).start();
        return task;


    }//runTask

    public <T> Task<T> runTask(LoadingComponentController loadingController,
                               Callable<T> actionBackground,
                               Consumer<T> toFinish) {
        return runTask(loadingController, actionBackground, toFinish, null, null);
    }

    public Task<Void> runTask(LoadingComponentController loadingController,
                              Runnable actionBackground,
                              Runnable toFinish,
                              Runnable onCancelled) {
        return runTask(
                loadingController,
                () -> {
                    actionBackground.run();
                    return null;
                },
                (resultado) -> {
                    if (toFinish != null) toFinish.run();
                }, null,
                onCancelled
        );
    }//runTask

    public Task<Void> runTask(LoadingComponentController loadingController,
                              Runnable actionBackground,
                              Runnable toFinish) {
        return runTask(loadingController, actionBackground, toFinish, null);
    }

    private Throwable desempaquetarExcepcion(Throwable ex) {
        Throwable causa = ex;

        while (causa != null) {
            if (causa instanceof AppException) {
                return causa;
            }
            if (causa.getCause() == null) {
                return causa;
            }
            causa = causa.getCause();

        }

        return ex;
    }


}//class
