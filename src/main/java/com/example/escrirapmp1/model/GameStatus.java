package com.example.escrirapmp1.model;

/**
 * Class representing the possible states of the game.
 * Replaces the previous enum with String constants.
 */
public final class GameStatus {

    public static final String READY = "READY";
    public static final String PLAYING = "PLAYING";
    public static final String FAILED = "FAILED";
    public static final String FINISHED = "FINISHED";

    private GameStatus() {
        // Prevent instantiation
    }
}
