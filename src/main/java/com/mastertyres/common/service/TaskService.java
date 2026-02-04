package com.mastertyres.common.service;

import com.mastertyres.common.exeptions.AppException;
import com.mastertyres.fxComponents.LoadingComponentController;
import javafx.concurrent.Task;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

import static com.mastertyres.common.utils.MensajesAlert.mostrarError;

@Component
public class TaskService {

    public <T> Task<T> runTask(LoadingComponentController loadingController,
                               Callable<T> actionBackground,
                               Consumer<T> toFinish,
                               Consumer<Throwable>onFailed,
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

            if (loadingController != null){
                loadingController.hide();
            }

            Throwable rawError = task.getException();
            Throwable trueError = desempaquetarExcepcion(rawError);

            if (onFailed != null){
                onFailed.accept(trueError);
            }else {
                mostrarError("Error interno", "","Ocurrió un problema inesperado. Vuelva a intentarlo mas tarde.");

            }


            /*
            if (loadingController != null) {

                loadingController.hide();
                Throwable error = task.getException();
                error.printStackTrace();

                if (onFailed != null) {
                    onFailed.accept(error);
                }else {
                    mostrarError("Error interno", "","Ocurrió un problema inesperado. Vuelva a intentarlo mas tarde.");
                }

            }

             */

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
        return runTask(loadingController, actionBackground, toFinish, null,null);
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
                },null,
                onCancelled
        );
    }//runTask

    public Task<Void> runTask(LoadingComponentController loadingController,
                              Runnable actionBackground,
                              Runnable toFinish) {
        return runTask(loadingController, actionBackground, toFinish, null);
    }

    private Throwable desempaquetarExcepcion(Throwable ex){
        Throwable causa = ex;

        while(causa != null){
            if (causa instanceof AppException){
                return causa;
            }
            if (causa.getCause() == null){
                return  causa;
            }
            causa = causa.getCause();

        }

        return ex;
    }


}//class
