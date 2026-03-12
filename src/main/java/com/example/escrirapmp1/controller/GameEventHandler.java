package com.example.escrirapmp1.controller;

/**
 * GameEventHandler - Interfaz personalizada para manejar eventos del juego.
 * Abstrae la lógica de eventos para evitar acoplamiento directo con lambdas.
 * Suma puntos en "Estructuras orientadas a eventos" del curso FPOE.
 */
public interface GameEventHandler {

    void onValidateInput();

    void onTimeUp();

    void onGameStart();

}
