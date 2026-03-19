package com.example.escrirapmp1;

import javafx.application.Application;
import javafx.stage.Stage;

import com.example.escrirapmp1.vista.GameStage;

/**
 * Main - Punto de entrada simple. Delega gestión de Stage a GameStage.
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GameStage.getInstance().init(primaryStage);
        GameStage.getInstance().showStartScene();
    }
}

