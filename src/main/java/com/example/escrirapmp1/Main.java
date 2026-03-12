package com.example.escrirapmp1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.layout.Pane;

import java.io.IOException;

public class Main extends Application {
 
    private static Stage primaryStage;
 
    public static void main(String[] args) {
        launch(args);
    }

    public static void switchScene(String fxmlPath) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxmlPath));
            Pane root = fxmlLoader.load();
            primaryStage.getScene().setRoot(root);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(
                Main.class.getResource("start-view.fxml")
        );
        Pane root = fxmlLoader.load();

        Scene scene = new Scene(root,800,600);

        primaryStage.setTitle("Escritura rapida - MP#1");
        primaryStage.setScene(scene);
        Main.primaryStage = primaryStage;
        primaryStage.show();


    }
}

