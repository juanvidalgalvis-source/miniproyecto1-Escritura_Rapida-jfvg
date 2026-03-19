package com.example.escrirapmp1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import com.example.escrirapmp1.vista.GameStage;

/**
 * SummaryController - Muestra resumen del juego y maneja reinicio.
 */
public class SummaryController {

    @FXML
    private Label finalLevelLabel;

    @FXML
    private Label streakLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Button restartButton;

    /**
     * Establece los datos del resumen del juego.
     */
    public void setData(int finalLevel, int maxStreak, int remainingTime) {
        if (finalLevelLabel != null) {
            finalLevelLabel.setText("Nivel alcanzado: " + finalLevel);
        }
        if (streakLabel != null) {
            streakLabel.setText("Mejor racha: " + maxStreak);
        }
        if (timeLabel != null) {
            timeLabel.setText("Tiempo restante: " + remainingTime + "s");
        }
    }

    /**
     * Reinicia el juego volviendo a la escena principal.
     */
    @FXML
    private void restartGame() {
        GameStage.getInstance().switchToGameScene();
    }
}

