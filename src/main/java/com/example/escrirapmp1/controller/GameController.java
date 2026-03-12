package com.example.escrirapmp1.controller;

import javafx.fxml.FXML;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Arc;
import javafx.scene.paint.Color;
import com.example.escrirapmp1.model.GameModel;
import com.example.escrirapmp1.model.GameStatus;
import com.example.escrirapmp1.model.WordProvider;

/**
 * GameController - Controlador principal del juego.
 * Coordina las interacciones entre Model y View.
 */
public class GameController implements GameEventHandler {

    // Referencia al modelo del juego
    private GameModel gameModel;

    // Referencia al proveedor de palabras
    private WordProvider wordProvider;

    // Timeline para el temporizador
    private Timeline timeline;

    // Bandera para indicar si el tiempo se agotó
    private boolean timeoutTriggered = false;

    // Referencias a elementos de la vista (FXML)
    @FXML
    private TextField inputField;

    @FXML
    private Label wordLabel;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button validateButton;

@FXML
    private Label timerLabel;

    @FXML
    private Arc timerArc;
    
    private int maxTimeForLevel = 0;
    
    // Summary panel elements
    @FXML
    private StackPane gameplayPanel;
    
    @FXML
    private StackPane summaryPanel;
    
    @FXML
    private Label finalLevelLabel;
    
    @FXML
    private Label streakLabel;
    
    @FXML
    private Label remainingTimeLabel;
    
    @FXML
    private Button restartButton;

    /**
     * Constructor por defecto para FXMLLoader.
     */
    public GameController() {
        this.gameModel = new GameModel();
        this.wordProvider = new WordProvider();
    }

    /**
     * Inicializa el controlador despues de que se carga el FXML.
     * Registra los manejadores de eventos.
     */
    @FXML
    public void initialize() {
        // Registrar handler de teclado para ENTER
        KeyboardAdapter keyboardAdapter = new KeyboardAdapter(this);
        inputField.setOnKeyPressed(keyboardAdapter);

        // Registrar handler de mouse para el boton
        MouseAdapter mouseAdapter = new MouseAdapter(this);
        validateButton.setOnMouseClicked(mouseAdapter);

        // Automatically start first level
        onGameStart();
    }

    /**
     * Inicia un nuevo nivel en el juego.
     */
    public void startNewLevel() {
        // Detener timer existente
        stopTimer();

        // Verificar que el estado no sea FINISHED
        if (GameStatus.FINISHED.equals(gameModel.getGameStatus())) {
            return;
        }

        // Obtener palabra del proveedor
        gameModel.setCurrentWord(wordProvider.getRandomWord(gameModel.getCurrentLevel()));

        // Cambiar estado a PLAYING
        gameModel.setGameStatus(GameStatus.PLAYING);

        // Inicializar tiempo para el nivel
        gameModel.initializeTimeForLevel();
        maxTimeForLevel = gameModel.getRemainingTime();
        updateTimerLabel();

        // Iniciar timer
        startTimer();

        // Limpiar TextField
        if (inputField != null) {
            inputField.clear();
            inputField.setDisable(false);
        }

        // Mostrar palabra en UI
        if (wordLabel != null) {
            wordLabel.setText(gameModel.getCurrentWord());
        }
        
        // Mostrar panel de juego
        showGameplay();
    }

    /**
     * Inicia el temporizador de cuenta regresiva.
     */
    private void startTimer() {
        // Si ya existe un timeline, detenerlo primero
        if (timeline != null) {
            timeline.stop();
        }

        // Crear nuevo Timeline con KeyFrame de 1 segundo
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            // Decrementar tiempo
            gameModel.decrementTime();

            // Actualizar label
            updateTimerLabel();

            // Verificar si el tiempo se agotó
            if (gameModel.isTimeOver()) {
                stopTimer();
                onTimeUp();
            }
        }));

        // Ejecutar indefinidamente
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Detiene el temporizador.
     */
    private void stopTimer() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    /**
     * Reinicia el juego.
     */
    public void restartGame() {
        gameModel.resetGame();
        startNewLevel();
    }

    /**
     * Actualiza el label del temporizador.
     */
    private void updateTimerLabel() {
        int remaining = gameModel.getRemainingTime();
        if (timerLabel != null) {
            timerLabel.setText(String.valueOf(remaining));
        }
        if (timerArc != null && maxTimeForLevel > 0) {
            double progress = (double) remaining / maxTimeForLevel;
            timerArc.setLength(progress * 360.0);
            double depletionRatio = 1.0 - progress;

            Color start = Color.web("#7b3fe4");
            Color end = Color.web("#e74c3c");
            Color color = start.interpolate(end, depletionRatio);

            timerArc.setStroke(color);
        }
    }


    /**
     * Valida la entrada del usuario contra la palabra actual.
     */
    private void validateInput() {
        // Detener timer
        stopTimer();
        
        // Limpiar mensaje de feedback del nivel anterior
        feedbackLabel.setText("");

        // Verificar que el estado sea PLAYING
        if (!GameStatus.PLAYING.equals(gameModel.getGameStatus())) {
            return;
        }

        // Obtener texto del input
        if (inputField == null) {
            return;
        }
        String userInput = inputField.getText();
        if (userInput == null) {
            return;
        }
        userInput = userInput.trim();
        String targetWord = gameModel.getCurrentWord();

        // Comparar palabras
        if (targetWord != null && userInput.equals(targetWord)) {
            // Correcto: incrementar nivel y racha
            gameModel.incrementLevel();
            gameModel.incrementStreak();
            
            // Verificar si el juego termino
            if (gameModel.isGameFinished()) {
                showGameSummary();
                return;
            }
            
            feedbackLabel.setText("¡Correcto!");
            feedbackLabel.setStyle("-fx-text-fill: green;");
            startNewLevel();
        } else {
            // Incorrecto: cambiar estado a FAILED y resetear racha
            gameModel.setGameStatus(GameStatus.FAILED);
            gameModel.resetStreak();
            
            // Mostrar mensaje segun si fue por timeout o no
            if (timeoutTriggered) {
                feedbackLabel.setText("¡¡¡Tiempo!!!");
            } else {
                feedbackLabel.setText("Incorrecto!!. Era: " + targetWord);
            }
            feedbackLabel.setStyle("-fx-text-fill: red;");
            
            // Mostrar resumen del juego después de un delay (para que se vea el mensaje)
            Timeline delayTimeline = new Timeline(new KeyFrame(Duration.seconds(1.5), event -> {
                showGameSummary();
            }));
            delayTimeline.setCycleCount(1);
            delayTimeline.play();
        }

        // Resetear bandera de timeout
        timeoutTriggered = false;
    }
    
    /**
     * Muestra el resumen final del juego cuando se completa.
     */
    private void showGameSummary() {
        // Detener el temporizador
        stopTimer();
        
        // Obtener informacion del juego
        int finalLevel = gameModel.getCurrentLevel();
        int totalStreak = gameModel.getStreak();
        int timeLeft = gameModel.getRemainingTime();
        
        // Actualizar labels del panel de resumen
        if (finalLevelLabel != null) {
            finalLevelLabel.setText("Nivel Alcanzado: " + finalLevel);
        }
        if (streakLabel != null) {
            streakLabel.setText("Puntaje Total: " + totalStreak);
        }
        if (remainingTimeLabel != null) {
            remainingTimeLabel.setText("Tiempo Restante: " + timeLeft + " seconds");
        }
        
        // Ocultar panel de juego y mostrar panel de resumen
        if (gameplayPanel != null) {
            gameplayPanel.setVisible(false);
        }
        if (summaryPanel != null) {
            summaryPanel.setVisible(true);
        }
    }
    
    /**
     * Muestra el panel de juego (oculta el resumen).
     */
    private void showGameplay() {
        // Ocultar panel de resumen y mostrar panel de juego
        if (summaryPanel != null) {
            summaryPanel.setVisible(false);
        }
        if (gameplayPanel != null) {
            gameplayPanel.setVisible(true);
        }
    }

    // ========== CLASES ADAPTADORAS ==========

    /**
     * Clase adaptadora para eventos de teclado.
     */
    public class KeyboardAdapter implements EventHandler<KeyEvent> {
        
        private GameEventHandler handler;
        
        public KeyboardAdapter(GameEventHandler handler) {
            this.handler = handler;
        }
        
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.ENTER) {
                handler.onValidateInput();
            }
        }
    }

    /**
     * Clase adaptadora para eventos de mouse.
     */
    public class MouseAdapter implements EventHandler<MouseEvent> {
        
        private GameEventHandler handler;
        
        public MouseAdapter(GameEventHandler handler) {
            this.handler = handler;
        }
        
        @Override
        public void handle(MouseEvent event) {
            handler.onValidateInput();
        }
    }
    
    // ========== GameEventHandler Implementation ==========

    @Override
    public void onValidateInput() {
        validateInput();
    }

    @Override
    public void onTimeUp() {
        timeoutTriggered = true;
        validateInput();
    }

    @Override
    public void onGameStart() {
        startNewLevel();
    }
}
