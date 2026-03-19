package com.example.escrirapmp1.vista;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import com.example.escrirapmp1.controller.SummaryController;

import java.io.IOException;

/**
 * GameStage - Centraliza la gestión del Stage y navegación de escenas.
 * Singleton para acceso simple desde controladores.
 */
public class GameStage {
    private static GameStage instance;
    private Stage primaryStage;

    private GameStage() {}

    public static GameStage getInstance() {
        if (instance == null) {
            instance = new GameStage();
        }
        return instance;
    }

    /**
     * Inicializa con el Stage principal.
     */
    public void init(Stage stage) {
        this.primaryStage = stage;
        this.primaryStage.setTitle("Escritura rapida - MP#1");
    }

    /**
     * Carga y muestra una escena FXML.
     */
    public void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Pane root = loader.load();
            Scene newScene = new Scene(root, 800, 600);
            primaryStage.setScene(newScene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra escena de inicio.
     */
    public void showStartScene() {
        loadScene("/com/example/escrirapmp1/start-view.fxml");
    }

    /**
     * Cambia a escena del juego.
     */
    public void switchToGameScene() {
        loadScene("/com/example/escrirapmp1/escri-view.fxml");
    }

    /**
     * Muestra escena summary con datos del juego.
     */
    public void showSummaryScene(int finalLevel, int maxStreak, int remainingTime) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/escrirapmp1/summary-view.fxml"));
            Pane root = loader.load();
            SummaryController controller = loader.getController();
            controller.setData(finalLevel, maxStreak, remainingTime);
            Scene newScene = new Scene(root, 800, 600);
            primaryStage.setScene(newScene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
