package com.example.escrirapmp1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import com.example.escrirapmp1.vista.GameStage;

/**
 * StartController - Controlador mínimo para iniciar el menú.
 */
public class StartController {

    @FXML
    private Button startButton;

    /**
     * Handle Start Game button click.
     * Switches to the game scene via GameStage.
     */
    @FXML
    private void startGame() {
        GameStage.getInstance().switchToGameScene();
    }
}
