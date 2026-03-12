package com.example.escrirapmp1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import com.example.escrirapmp1.Main;

/**
 * StartController - Minimal controller for the start menu.
 */
public class StartController {

    @FXML
    private Button startButton;

    /**
     * Handle Start Game button click.
     * Switches to the game scene.
     */
    @FXML
    private void startGame() {
        Main.switchScene("escri-view.fxml");
    }
}
