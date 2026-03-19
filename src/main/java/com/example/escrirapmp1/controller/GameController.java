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
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import com.example.escrirapmp1.model.GameModel;
import com.example.escrirapmp1.model.GameStatus;
import com.example.escrirapmp1.model.WordProvider;
import com.example.escrirapmp1.vista.GameStage;

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
    private boolean isValidating = false;

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
    private Circle timerCircle;

    @FXML
    private Label levelLabel;
    
    @FXML 
    private Label streakTopLabel;
    
    private int maxTimeForLevel = 0;
    
    // Summary panel elements (legacy, not used)

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
        isValidating = false;
        timeoutTriggered = false;
        inputField.setDisable(false);

        // Verificar que el estado no sea FINISHED ni FAILED
        if (GameStatus.FINISHED.equals(gameModel.getGameStatus()) || GameStatus.FAILED.equals(gameModel.getGameStatus())) {
            return;
        }

        // Obtener palabra del proveedor
        gameModel.setCurrentWord(wordProvider.getRandomWord(gameModel.getCurrentLevel()));

        // Cambiar estado a PLAYING
        gameModel.setGameStatus(GameStatus.PLAYING);

        // Inicializar tiempo para el nivel
        gameModel.initializeTimeForLevel();
        maxTimeForLevel = gameModel.getRemainingTime();
        if (timerCircle != null) {
            double circum = 2 * Math.PI * 27;
            timerCircle.setRotate(-90);
            timerCircle.getStrokeDashArray().setAll(circum);
            timerCircle.setStrokeDashOffset(0);  // full visible
        }
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
        
        updateTopBar();
    }

    private void updateTopBar() {
        if (levelLabel != null) {
            levelLabel.setText("Nivel " + gameModel.getCurrentLevel());
        }
        if (streakTopLabel != null) {
            streakTopLabel.setText("🔥 " + gameModel.getStreak());
        }
        if (timerLabel != null) {
            timerLabel.setStyle("-fx-font-size:18px; -fx-font-weight:bold; -fx-font-family: 'Courier New', monospace;");
        }
    }

    /**
     * Inicia el temporizador de cuenta regresiva.
     */
    private void startTimer() {
        // Si ya existe un timeline, detenerlo primero
        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
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
        timeoutTriggered = false;
        inputField.setDisable(false);
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
        if (timerCircle != null && maxTimeForLevel > 0) {
            double progress = (double) remaining / maxTimeForLevel;
            double circum = 2 * Math.PI * 27;
            timerCircle.getStrokeDashArray().setAll(circum);
            timerCircle.setStrokeDashOffset(circum * (1 - progress));

            double depletionRatio = 1.0 - progress;

            Color start = Color.web("#7b3fe4");
            Color end = Color.web("#e74c3c");
            Color color = start.interpolate(end, depletionRatio);

            timerCircle.setStroke(color);
        }
    }


    /**
     * Valida la entrada del usuario contra la palabra actual.
     */
    private void validateInput() {
        if (isValidating) {
            return;
        }
        isValidating = true;
        inputField.setDisable(true);
        // Detener timer
        stopTimer();
        
        // Limpiar mensaje de feedback del nivel anterior
        feedbackLabel.setText("");

        if (!GameStatus.PLAYING.equals(gameModel.getGameStatus())) {
            inputField.setDisable(false);
            isValidating = false;
            return;
        }

        String userInput = inputField.getText().trim();
        String targetWord = gameModel.getCurrentWord();

        if (targetWord != null && userInput.equals(targetWord)) {
            // Correcto
            gameModel.incrementLevel();
            gameModel.incrementStreak();
            
            if (gameModel.isGameFinished()) {
                gameModel.setGameStatus(GameStatus.FINISHED);
                showGameSummary(gameModel.getCurrentLevel(), gameModel.getMaxStreak(), gameModel.getRemainingTime());
                return;
            }
            
            feedbackLabel.setText("¡Correcto!");
            feedbackLabel.setStyle("-fx-text-fill: green;");
            startNewLevel();
        } else {
            gameModel.resetStreak();
            
            if (timeoutTriggered) {
                // Timeout: game over
                gameModel.setGameStatus(GameStatus.FAILED);
                String message = "¡Tiempo agotado!";
                feedbackLabel.setText(message);
                feedbackLabel.setStyle("-fx-text-fill: red;");
                
                int lastCompletedLevel = Math.max(1, gameModel.getCurrentLevel() - 1);
                showGameSummary(lastCompletedLevel, gameModel.getMaxStreak(), 0);
            } else {
                // Error escritura: retry mismo nivel
                String message = "¡Incorrecto! Era: " + targetWord;
                feedbackLabel.setText(message);
                feedbackLabel.setStyle("-fx-text-fill: red;");
                startNewLevel(); // Mantiene level actual
            }
        }
        isValidating = false;
    }
    
    /**
     * Muestra el resumen final del juego cuando se completa.
     */
    private void showGameSummary(int finalLevel, int maxStreak, int timeLeft) {
        stopTimer();
        isValidating = false;
        GameStage.getInstance().showSummaryScene(finalLevel, maxStreak, timeLeft);
    }
    
    /**
     * Muestra el panel de juego (oculta el resumen).
     */
    // Legacy showGameplay (panels removed)
    private void showGameplay() {
        // No longer needed with separate scenes
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
        stopTimer();
        timeoutTriggered = true;
        validateInput();
    }

    @Override
    public void onGameStart() {
        startNewLevel();
    }
}
