package com.example.escrirapmp1.model;

/**
 * GameModel - Manages the state of the typing game.
 * Handles level progression, timer, streak, and game status.
 * 
 * @author Vidali
 * @version 1.0
 */
public class GameModel {

    // Constantes del temporizador - No se modifican externamente
    /** Base time for level 1 in seconds. */
    private static final int BASE_TIME = 20;
    /** Minimum time per level in seconds. */
    private static final int MIN_TIME = 2;
    /** Maximum achievable level. */
    private static final int MAX_LEVEL = 45;

    // Atributos del estado del juego
    /** Current game level (starts at 1). */
    private int currentLevel;
    /** Current word to type. */
    private String currentWord;
    /** Remaining time for current level in seconds. */
    private int remainingTime;
    /** Current game status (READY, PLAYING, FAILED, FINISHED). */
    private String gameStatus;
    /** Current consecutive correct answers streak. */
    private int streak;
    /** Maximum streak achieved in the game. */
    private int maxStreak;

    /**
     * Constructor básico con valores iniciales.
     */
    public GameModel() {
        this.currentLevel = 1;
        this.currentWord = "";
        this.remainingTime = 0;
        this.gameStatus = GameStatus.READY;
        this.streak = 0;
        this.maxStreak = 0;
    }

    /**
     * Initializes the time limit for the current level.
     * Formula: max(BASE_TIME - ((currentLevel - 1) / 5) * 2, MIN_TIME)
     */
    public void initializeTimeForLevel() {
        // Fórmula de dificultad progresiva cada 5 niveles
        int time = Math.max(BASE_TIME - ((currentLevel - 1) / 5) * 2, MIN_TIME);
        this.remainingTime = time;
    }

    /**
     * Decrements the remaining time by 1 second.
     */
    public void decrementTime() {
        if (remainingTime > 0) {
            remainingTime--;
        }
    }

    /**
     * Checks if the time has run out.
     * @return true if remainingTime is less than or equal to 0
     */
    public boolean isTimeOver() {
        return remainingTime <= 0;
    }

    // Getters y Setters

    public int getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Increments the current level by 1.
     * Encapsulates the logic for modifying the level.
     * Checks if MAX_LEVEL is reached and sets game to FINISHED.
     */
    public void incrementLevel() {
        if (currentLevel < MAX_LEVEL) {
            currentLevel++;
        } else {
            gameStatus = GameStatus.FINISHED;
        }
    }

    /**
     * Checks if the game is finished.
     * @return true if game status is FINISHED
     */
    public boolean isGameFinished() {
        return GameStatus.FINISHED.equals(gameStatus);
    }

    /**
     * Resets the game to initial state.
     * Resets level to 1, streak to 0, time, and sets status to READY.
     */
    public void resetGame() {
        this.currentLevel = 1;
        this.streak = 0;
        this.maxStreak = 0;
        this.remainingTime = 0;
        this.gameStatus = GameStatus.READY;
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    /**
     * Increments the streak counter by 1.
     */
    public void incrementStreak() {
        streak++;
        if (streak > maxStreak) {
            maxStreak = streak;
        }
    }

    /**
     * Resets the streak counter to 0.
     */
    public void resetStreak() {
        streak = 0;
    }

    /**
     * Returns the current streak value.
     * @return current streak
     */
    public int getStreak() {
        return streak;
    }

    /**
     * Returns the maximum streak achieved.
     */
    public int getMaxStreak() {
        return maxStreak;
    }


}
